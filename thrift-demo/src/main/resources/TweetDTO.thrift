namespace java com.javiermoreno.thriftdemo

struct TweetDTOThrift {
	 05: optional string id,
     10: required string screenName,
     20: required string message,
     30: optional i64 timestamp,
     40: optional double lon,
     50: optional double lat,
     60: optional string place
}

service TweetService {

	TweetDTOThrift tweetById(10: string id),
	
	void saveTweet(10: TweetDTOThrift tweet),
	
	oneway void reset()
}