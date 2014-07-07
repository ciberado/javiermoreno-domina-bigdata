package com.javiermoreno.hadoop.mapreduce;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class User {

    @JsonProperty("id")
    private BigInteger id; 
	@JsonProperty("name")
	private String name;
	@JsonProperty("screen_name")
	private String screenName;
	
	public User() {
	}

	public void setId(BigInteger id) {
        this.id = id;
    }
	
	public BigInteger getId() {
        return id;
    }

	
	public void setName(String name) {
        this.name = name;
    }
	
	public String getName() {
        return name;
    }
	
	public void setScreenName(String screenName) {
        this.screenName = screenName;
    }
	
	public String getScreenName() {
        return screenName;
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
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
}
