package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.restclienttemplate.ComposeActivity.RESULT_TWEET_KEY;

public class TimelineActivity extends AppCompatActivity {
    public static final int COMPOSE_TWEET_REQUEST_CODE = 100; //get request code back for new tweet
    private TwitterClient client;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    private SwipeRefreshLayout swipeContainer;
    MenuItem miActionProgressItem;
    public boolean ReplyYes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                swipeContainer.setRefreshing(false);
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        client = TwitterApp.getRestClient(this);
        //steps: find recycler view
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        //initiate array list (data source)
        tweets = new ArrayList<>();
        //construct adapter from data source
        tweetAdapter = new TweetAdapter(tweets); // pass array list to adapter
        //recyclerView setup (layout manager, use adapter
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        // set adapter
        rvTweets.setAdapter(tweetAdapter);

        getSupportActionBar().setTitle("Twitter");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1da1f2")));
        getSupportActionBar().setLogo(getDrawable(R.drawable.ic_launcher_twitter_round));
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            public void onSuccess(JSONArray json) {
                // Remember to CLEAR OUT old items before appending in the new ones
                tweetAdapter.clear();
                // ...the data has come back, add new items to your adapter...
                tweetAdapter.addAll(tweets);
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
            }

            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) { //if item in switch equal to case
            case R.id.miCompose:
                composeMessage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if(requestCode == COMPOSE_TWEET_REQUEST_CODE && resultCode == RESULT_OK){
            //mock code use Parcelable
            Tweet resultTweet = Parcels.unwrap(getIntent().getParcelableExtra(RESULT_TWEET_KEY));
            tweets.add(0, resultTweet);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);
            Toast.makeText(this, "tweet post successful", Toast.LENGTH_LONG).show();
        }
    }

    private void composeMessage() {
        //open ComposeActivity to create new tweet
        Intent composeTweet = new Intent(this, ComposeActivity.class);
        startActivityForResult(composeTweet, COMPOSE_TWEET_REQUEST_CODE);
    }

    private void replyMessage() { //TODO: maybe delete/change
        //open ComposeActivity to reply with new tweet
        Intent composeTweet = new Intent(this, ComposeActivity.class);
        startActivityForResult(composeTweet, COMPOSE_TWEET_REQUEST_CODE);
        ReplyYes = true;
        composeTweet.putExtra("replyYes", ReplyYes);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        //populate timeline while preparing menu
        populateTimeline();
        // Return to finish
        return super.onPrepareOptionsMenu(menu);
    }

    public void showProgressBar() {
        // Show progress item
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }

    //populate timeline on Twitter Feed
    private void populateTimeline(){
        showProgressBar();
        client.getHomeTimeline(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                Log.d("TwitterClient", response.toString()); //logs data to twitter client
                //iterate through JSON array
                //for each entry, deserialize JSON object
                for(int i = 0; i <response.length(); i++){
                    //convert each object to Tweet model
                    //add tweet model to data source
                    //notify adapter we've added an item (notify what to change)
                    try{
                        Tweet tweet = Tweet.fromJson(response.getJSONObject(i));
                        tweets.add(tweet);
                        tweetAdapter.notifyItemInserted(tweets.size() - 1); //significance -1?
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            hideProgressBar();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
                hideProgressBar();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();            }

        });

    }
}
