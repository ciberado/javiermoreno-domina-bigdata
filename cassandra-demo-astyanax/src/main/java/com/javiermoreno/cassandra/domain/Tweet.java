package com.javiermoreno.cassandra.domain;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Tweet implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("created_at")
	private Date createdAt;
	@JsonProperty("id")
	private BigInteger id; 
	@JsonProperty("id_str")
	private String idStr; // twitter no usa signo, por lo que tenemos que utilizar string o biginteger
	                // Ojo tambi�n con los n�meros de m�s de 53 bits: javascript tiene problemas
 	@JsonIgnore
	private String username;
	@JsonProperty("text")
	private String text;
	@JsonProperty("source")
	private String source;
	@JsonProperty("favorite_count")
	private int favorites;
	@JsonProperty("retweet_count")
	private int retweets;
	@JsonProperty("lang")
	private String language;
	@JsonProperty("place")
	private Place place;
	@JsonProperty("possibly_sensitive")
	private boolean nsfw;
	@JsonProperty("user")
	private User user;
	@JsonProperty("coordinates")
	private Coordinates coordinates;
	@JsonProperty
	private ClassificationProperties classificationProperties =
				new ClassificationProperties();
	
	public Tweet() {

	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
 
	public BigInteger getId() {
		return id;
	}
	
	public void setId(BigInteger id) {
		this.id = id;
	}
	public String getIdStr() {
		return idStr;
	}
	
	public void setIdStr(String idStr) {
		this.idStr = idStr;
	}
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	 

	public int getFavorites() {
		return favorites;
	}

	public void setFavorites(int favorites) {
		this.favorites = favorites;
	}

	public int getRetweets() {
		return retweets;
	}

	public void setRetweets(int retweets) {
		this.retweets = retweets;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public boolean isNsfw() {
		return nsfw;
	}

	public void setNsfw(boolean nsfw) {
		this.nsfw = nsfw;
	}

	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}
	
	public Coordinates getCoordinates() {
		return coordinates;
	}
	
	public ClassificationProperties getClassificationProperties() {
		return classificationProperties;
	}
	
	public void setClassificationProperties(
			ClassificationProperties classificationProperties) {
		this.classificationProperties = classificationProperties;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tweet other = (Tweet) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
	@Override
	public String toString() {
		return "Tweet [screenNames=" + user.getScreenName() + ", text=" + text
				+ ", favorites=" + favorites + ", retweets=" + retweets
				+ ", createdAt=" + createdAt + ", place=" + place + ", user="
				+ user + ", id=" + id + ", source=" + source + ", language="
				+ language + ", nsfw=" + nsfw + ", coordinates=" + coordinates + "]";
	}

 

	 
	
	
}
