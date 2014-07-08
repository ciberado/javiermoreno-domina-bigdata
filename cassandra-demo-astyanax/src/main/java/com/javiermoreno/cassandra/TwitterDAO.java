package com.javiermoreno.cassandra;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.javiermoreno.cassandra.domain.ClassificationProperties.SentimentType;
import com.javiermoreno.cassandra.domain.Tweet;
import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.exceptions.BadRequestException;
import com.netflix.astyanax.connectionpool.exceptions.ConnectionException;
import com.netflix.astyanax.connectionpool.exceptions.NotFoundException;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnFamily;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.query.RowQuery;
import com.netflix.astyanax.serializers.StringSerializer;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;
import com.netflix.astyanax.util.RangeBuilder;

public class TwitterDAO {

    private static final Logger log = LoggerFactory.getLogger(TwitterDAO.class);

    private static final String CLUSTER = "Javivi Cluster";
    private static final String KEYSPACE_NAME = "DemoKeyspace";
    private static final String POS_NEG_TWEETS_CFNAME = "posNegTweets";
    private static final String CONTADORES_CFNAME = "contadores";

    private ObjectMapper mapper;
    private AstyanaxContext<Keyspace> ctx;
    private Keyspace keyspace;
    private ColumnFamily<String, String> posNegTweetsCF;
    private ColumnFamily<String, String> contadoresCF;

    public void inicializar() throws ConnectionException {
        mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZ yyyy", Locale.ENGLISH));

        this.ctx = new AstyanaxContext.Builder()
                .forCluster(CLUSTER)
                .forKeyspace(KEYSPACE_NAME)
                .withConnectionPoolConfiguration(
                        new ConnectionPoolConfigurationImpl("MyConnectionPool").setPort(9160)
                                .setMaxConnsPerHost(1).setSeeds("127.0.0.1:9160"))
                .withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
                .withAstyanaxConfiguration(
                        new AstyanaxConfigurationImpl().setCqlVersion("3.0.0")
                                .setTargetCassandraVersion("1.2"))
                .buildKeyspace(ThriftFamilyFactory.getInstance());
        this.ctx.start();
        try {
            keyspace = ctx.getClient();
            keyspace.describeKeyspace();
        } catch (BadRequestException exc) {
            log.info("Creo que no existía el keyspace. Inicializando ahora.");
            keyspace.createKeyspace(ImmutableMap
                    .<String, Object> builder()
                    .put("strategy_options",
                            ImmutableMap.<String, Object> builder().put("replication_factor", "1")
                                    .build()).put("strategy_class", "SimpleStrategy").build());
            log.debug(keyspace.describeKeyspace().getName() + " creado.");
        }
        try {
            posNegTweetsCF = new ColumnFamily<String, String>(
                    POS_NEG_TWEETS_CFNAME,
                    StringSerializer.get(), StringSerializer.get());
            keyspace.getColumnFamilyProperties(POS_NEG_TWEETS_CFNAME);
        } catch (NotFoundException | BadRequestException exc) {
            log.info("Creo que no existía la columnFamily. Inicializando ahora.");
            keyspace.createColumnFamily(posNegTweetsCF, 
                    ImmutableMap.<String, Object> builder()
                    .put("key_validation_class", "UTF8Type") // validador row
                                                             // key
                    .put("comparator_type", "UTF8Type") // validador column name
                    .put("default_validation_class", "UTF8Type") // validador
                                                                 // column value
                                                                 // (json)
                    // Alternativamente (permite marcar rangos de tipo long como
                    // segundo criterio
                    // del slice a cambio de tener que trabajar con
                    // AnnotatedCompositeSerializer)
                    // .put("comparator_type",
                    // "CompositeType(UTF8Type, LongType)")
                    .build());

        }
        try {
            contadoresCF = new ColumnFamily<String, String>(CONTADORES_CFNAME,
                    StringSerializer.get(), StringSerializer.get());
            keyspace.getColumnFamilyProperties(CONTADORES_CFNAME);
        } catch (NotFoundException | BadRequestException exc) {
            log.info("Creo que no existía la columnFamily. Inicializando ahora.");
            keyspace.createColumnFamily(
                    contadoresCF,
                    ImmutableMap.<String, Object> builder()
                            .put("default_validation_class", "CounterColumnType")
                            .put("replicate_on_write", true).build());

        }
    }

    public void guardarTweet(Tweet tweet) throws ConnectionException, JsonProcessingException {
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String rowKey = sdf.format(tweet.getCreatedAt()) + 
                        ":" + tweet.getClassificationProperties().getSentiment() +
                        ":" + Math.abs(tweet.getIdStr().hashCode() % 10);
        String json = mapper.writeValueAsString(tweet);
        String columnKey = tweet.getClassificationProperties().getCountryCode()
                       + ":" + tweet.getIdStr();
        
        
        this.guardarTweetImpl(rowKey, columnKey, json);
        
    }
    
    
    public void guardarTweetImpl(String rowKey, String columnKey, String jsonTweet) 
    throws ConnectionException {
            keyspace.prepareColumnMutation(this.posNegTweetsCF, rowKey, columnKey)
                    .putValue(jsonTweet, /* ttl */ null)
                    .execute();
    }
 

    public List<Tweet> recuperarTweetsImpl(String rowKey, String start, 
                                           String end, int limit) 
    throws ConnectionException {
        try {
            RangeBuilder rangeBuilder = 
                        new RangeBuilder()./*setReversed()*/setStart(start);
            if (end != null) rangeBuilder.setEnd(end);  
            rangeBuilder.setLimit(limit);
            RowQuery<String, String> query = keyspace
                    .prepareQuery(this.posNegTweetsCF)
                    .getKey(rowKey)
                    .autoPaginate(true)
                    .withColumnRange(rangeBuilder.build());
            ColumnList<String> results = query.execute().getResult();
            List<Tweet> tweets= new ArrayList<Tweet>();         
            if (results.isEmpty() == false) {
                for (Column<String> c : results) {
                    Tweet tweet = mapper.readValue(c.getStringValue(), Tweet.class);
                    tweets.add(tweet);
                }
            }
            return tweets;
        } catch (IOException exc) {
            throw new RuntimeException(exc);
        }
    }
    //  dao.recuperarTweetsImpl("20130423:P:4", 
    //                          "Br", "ZZZZZZZZZZZZz", 5));

}














