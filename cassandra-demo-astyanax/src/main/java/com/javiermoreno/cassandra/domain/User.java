package com.javiermoreno.cassandra.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class User {

	@JsonProperty("id_str")
	private String id;
	@JsonProperty("created_at")
	private Date createdAt;
	@JsonProperty("name")
	private String name;
	@JsonProperty("screen_name")
	private String screenName;
	@JsonProperty("followers_count")
	private int followersCount;
	@JsonProperty("friends_count")
	private int followingCount;
	@JsonProperty("geo_enabled")
	private boolean geoEnabled;
	@JsonProperty("lang")
	private String lang;
	@JsonProperty("location")
	private String location;
	@JsonProperty("description")
	private String description; 
	@JsonProperty("profile_image_url")
	private String profileImageUrl;
	@JsonProperty("statuses_count")
	private int statusesCount;
	@JsonProperty("url")
	private String url;
	@JsonProperty("verified")
	private boolean verified;
	
	public User() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public int getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}

	public int getFollowingCount() {
		return followingCount;
	}

	public void setFollowingCount(int followingCount) {
		this.followingCount = followingCount;
	}

	public boolean isGeoEnabled() {
		return geoEnabled;
	}

	public void setGeoEnabled(boolean geoEnabled) {
		this.geoEnabled = geoEnabled;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String descrition) {
		this.description = descrition;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public int getStatusesCount() {
		return statusesCount;
	}

	public void setStatusesCount(int statusesCount) {
		this.statusesCount = statusesCount;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
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

	@Override
	public String toString() {
		return "User [name=" + name + ", screenName=" + screenName
				+ ", followersCount=" + followersCount + ", followingCount="
				+ followingCount + ", location=" + location
				+ ", statusesCount=" + statusesCount + ", id=" + id + ", lang="
				+ lang + ", descrition=" + description + ", verified="
				+ verified + ", createdAt=" + createdAt + ", geoEnabled="
				+ geoEnabled + ", profileImageUrl=" + profileImageUrl
				+ ", url=" + url + "]";
	}

	
	
}
