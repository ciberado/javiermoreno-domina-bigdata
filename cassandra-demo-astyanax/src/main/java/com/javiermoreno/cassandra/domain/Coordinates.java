package com.javiermoreno.cassandra.domain;

import java.io.Serializable;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Coordinates implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty("type")
	private String type;
	@JsonProperty("coordinates")
	private double[] coordinates = new double[2];

	public Coordinates() {

	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double[] getCoordinates() {
		return coordinates;
	}

	public double getLon() {
		return coordinates[0];
	}
	
	public double getLat() {
		return coordinates[1];
	}
	
	public void setCoordinates(double[] coordinates) {
		this.coordinates = coordinates;
	}

	@Override
	public String toString() {
		return "Coordinates [type=" + type + ", lon="
				+ getLon() + ", lat=" + getLat() + "]";
	}

	
	
}
