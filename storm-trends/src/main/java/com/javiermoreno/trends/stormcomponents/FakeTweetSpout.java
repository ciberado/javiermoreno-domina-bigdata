package com.javiermoreno.trends.stormcomponents;

import static backtype.storm.utils.Utils.sleep;
import static com.javiermoreno.trends.DemoHelpers.crearTweet;
import static com.javiermoreno.trends.DemoHelpers.instanceName;
import static java.text.MessageFormat.format;

import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import com.javiermoreno.trends.domain.TweetDTOThrift;
import com.javiermoreno.trends.geo.CoordinateLatLon;
import com.javiermoreno.trends.geo.CoordinateUTM;
import com.javiermoreno.trends.geo.Datum;


public class FakeTweetSpout extends BaseRichSpout {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(FakeTweetSpout.class);

    private static final int PAUSA_MEDIA_ENTRE_TWEETS = 25;
    private static final Random rnd = new Random();

    private SpoutOutputCollector collector;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        log.info("Abriendo Spout.");
        this.collector = collector;
    }

    @Override
    public void nextTuple() {
        sleep(rnd.nextInt(PAUSA_MEDIA_ENTRE_TWEETS));
        TweetDTOThrift tweet = crearTweet();
        CoordinateLatLon latLon = new CoordinateLatLon(tweet.getLat(), tweet.getLon());
        CoordinateUTM utm = Datum.WGS84.latLonToUTM(latLon, -1);
        utm.setAccuracy(1000.0);
        String shortFormCoord = utm.getShortForm();
        log.info(format("{0}*E {1}.", shortFormCoord, instanceName(this)));
        collector.emit(new Values(tweet.getTimestamp(), tweet.getMessage(), shortFormCoord));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // Una vez estamos emitiendo el stream deberíamos reducir al mínimo la información enviada
        declarer.declare(new Fields("timestamp", "message", "utmSquare"));
    }



}
