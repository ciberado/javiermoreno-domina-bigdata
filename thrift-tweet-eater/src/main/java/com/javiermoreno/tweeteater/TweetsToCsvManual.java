package com.javiermoreno.tweeteater;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.zip.GZIPInputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.javiermoreno.tweeteater.domain.Tweet;

public class TweetsToCsvManual {

	public static void main(String[] args) throws Exception {
		String path = "C:/temp/tweets_json/tweets.json.gz";
		String pathOut = "C:/temp/tweets-out";
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDateFormat(new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZ yyyy", Locale.ENGLISH));
		ObjectReader reader = mapper.reader(Tweet.class);

		System.out.println("Procesando.");
		CharsetDecoder utf8Decoder = Charset.forName("UTF-8").newDecoder();
		utf8Decoder.onMalformedInput(CodingErrorAction.IGNORE);
		utf8Decoder.onUnmappableCharacter(CodingErrorAction.IGNORE);
		try (Reader in = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(path)),utf8Decoder)); 
			 PrintWriter outCSV = new PrintWriter(new FileWriter(pathOut+ ".csv"))) {
			Iterator<Tweet> tweetsIter = reader.readValues(in);
			int count = 0, countLocated = 0;
			MessageFormat mf = new MessageFormat("\"{0}\",{1},{2},{3},\"{4}\",\"{5}\"", Locale.ENGLISH);
			while (tweetsIter.hasNext() == true) {
				Tweet tweet = tweetsIter.next();
				count = count + 1;
				if ((tweet.getCoordinates() != null) || (tweet.getPlace() != null)) {
					String screenName = tweet.getUser().getScreenName().replaceAll("[\"\\s]", " ");
					String place = tweet.getPlace() == null ? "" : tweet.getPlace().getName();
					String text = tweet.getText().replaceAll("[\"\\s]", " ");
					
					outCSV.println(mf.format(new Object[] {
							screenName, tweet.getCreatedAt().getTime(),
							tweet.getCoordinates() == null ? "" : tweet.getCoordinates().getLon(), 
							tweet.getCoordinates() == null ? "" : tweet.getCoordinates().getLat(), 
							place, text}));
					
					countLocated = countLocated + 1;
				}
			}
			System.out.println(countLocated);
			System.out.println(count);
		} 
		System.out.println("Fin.");
		
	}
	
}
