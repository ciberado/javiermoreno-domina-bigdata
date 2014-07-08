package com.javiermoreno.cassandra.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ClassificationProperties {
	
	public enum SentimentType { P, N }
	
	@JsonProperty
	private SentimentType sentiment;
	@JsonProperty
	private String countryCode;
	
	public ClassificationProperties() {
		// TODO Auto-generated constructor stub
	}
	
	public SentimentType getSentiment() {
		return sentiment;
	}
	
	public void setSentiment(SentimentType sentiment) {
		this.sentiment = sentiment;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	
	public String getCountryCode() {
		return countryCode;
	}
	
}
