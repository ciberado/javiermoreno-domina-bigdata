package com.javiermoreno.tweeteater;

import static java.util.Arrays.asList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.zip.GZIPOutputStream;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.javiermoreno.tweeteater.domain.Tweet;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Constants.FilterLevel;
import com.twitter.hbc.core.endpoint.Location;
import com.twitter.hbc.core.endpoint.Location.Coordinate;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;


public class TweetStreamClient {

	public static void main(String[] args) throws ParseException {
		BlockingQueue<String> processorQueue = new LinkedBlockingQueue<String>(10000);
		BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>(10000);
		// ESTOS DATOS NO DEBERÍAN HARDCODEARSE!!!
		Authentication hosebirdAuth = new OAuth1("yyc3Ppu7EtgdGlLQO1hELg", 
				                                 "3gScwZQdx0Wr27K9TkNwGzKABz3JECm0TopwPLxpW4",
		                                         "132851687-YwcpaWaRsjY7UqyeGGAzu26v9v2LL9CMNPV8ekJe", 
		                                         "KiTBN70lUKKQXvK9lKXkfJI3em2HeFGlrhvgIz8w");		
		//Authentication hosebirdAuth = new BasicAuth("ciberado", "");

		StatusesFilterEndpoint endPoint = new StatusesFilterEndpoint()
		        .trackTerms(asList(new String[] {"Barcelona", "Catalunya"}))
		        .locations(asList(new Location[] {
		        		new Location(new Coordinate(0.13, 40.49), new Coordinate(3.49, 43.0)) // SWLon, SWLat, NELon, NELat
		        }));
        endPoint.filterLevel(FilterLevel.None);  // obtener tweets relevantes y no relevantes
		endPoint.stallWarnings(true); // Recibir notificaciones avisando sobre una posible sanci�n
		// endPoint.followings(asList(new Long[] {})); // Seguir los ids indicados
		// endPoint.languages(asList(new String[] {"ca", "en", "es"})); // Filtrado por idiomas
		
		ClientBuilder builder = new ClientBuilder()
		    .hosts(Constants.STREAM_HOST)
		    .authentication(hosebirdAuth)
//		    .endpoint(new StatusesSampleEndpoint())
		    .endpoint(endPoint)
		    .gzipEnabled(true)
		    .processor(new StringDelimitedProcessor(processorQueue))
		    .eventMessageQueue(eventQueue);
		Client hosebirdClient = builder.build();
		hosebirdClient.connect();
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZ yyyy", Locale.ENGLISH));
		ObjectReader reader = mapper.reader(Tweet.class);
		String message = null;
		String path = "c:/temp/tweets_{0}.json.gz";
		int rollingCount = 0;
		File file = new File(MessageFormat.format(path, rollingCount));
		long t0 = System.currentTimeMillis();
		long lastReceived = 1;
		BufferedWriter out = null;							
		try {
			//out = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(file))));
			out = new BufferedWriter(new FileWriter(file));
			while (hosebirdClient.isDone() == false) {
				try {
					message = processorQueue.take();
					long currentReceived = System.currentTimeMillis();
					if (lastReceived != 0) {
						long diffTime = currentReceived - lastReceived;
						double mps = 1000.0 / diffTime;
						System.out.println("mps: " + MessageFormat.format("{0,number,#.##} mps", mps));
					}
					lastReceived = currentReceived;
					if (message.startsWith("{\"created_at\"") == true) {
						Tweet tweet = reader.readValue(message);		
						System.out.println(tweet);
						out.write(message);
						out.newLine();
						if (file.length() > 1024*1024*96) {
							rollingCount = rollingCount + 1;
							file = new File(MessageFormat.format(path, rollingCount));
							if (out != null) out.close();
							out = new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(new FileOutputStream(file))));							
						}
					} else {
						System.err.println(message);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (JsonProcessingException e) {
			System.err.println(message);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
