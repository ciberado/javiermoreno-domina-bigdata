struct TweetDTOThrift {
     10: required string screenName,
     20: required string message,
     30: optional i64 timestamp,
     40: optional double lon,
     50: optional double lat,
     60: optional string place
}