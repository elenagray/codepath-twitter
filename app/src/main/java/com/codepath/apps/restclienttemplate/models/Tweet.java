package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Parcel
public class Tweet {

    //listing attributes we want
    public String body;
    public long uid; // database ID for tweet
    public User user;
    public String createdAt;
    public Integer favoriteCount;
    public Integer rtCount;
    public String tweetAuthor;
    public Entity entity;
    public boolean hasEntities;
    public Tweet(){

    }

    //deserialization of JSON
    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        //extract values from JSON
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = getRelativeTimeAgo(jsonObject.getString("created_at"));
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        tweet.favoriteCount =  jsonObject.getInt("favorite_count");
        tweet.rtCount = jsonObject.getInt("retweet_count");
        tweet.tweetAuthor = jsonObject.getString("in_reply_to_screen_name");

        JSONObject entityObject = jsonObject.getJSONObject("entities");
        if(entityObject.has("media")){ // checking if object has media
            JSONArray mediaEndpoint = entityObject.getJSONArray("media");
            if(mediaEndpoint!=null && mediaEndpoint.length()!= 0){
                tweet.entity = Entity.fromJSON(jsonObject.getJSONObject("entities"));
                tweet.hasEntities = true;
            }else{
                tweet.hasEntities = false;
            }
        }



        return tweet;
    }

    public static String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return relativeDate;
    }
}
