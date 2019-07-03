package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {
    public static final String RESULT_TWEET_KEY = "result tweet";
    EditText etTweetInput;
    Button btnSend;
    TwitterClient client;
    TextView tvCharCount;

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            tvCharCount.setText(String.valueOf(280-s.length()));
        }

        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        boolean replyYes;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        etTweetInput = findViewById(R.id.etTweetInput);
        tvCharCount = findViewById(R.id.tvCharCount);
        btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sendTweet();
            }
        });
        etTweetInput.addTextChangedListener(mTextEditorWatcher);

        client = TwitterApp.getRestClient(this);

    }

    private void sendTweet() {
        client.sendTweet(etTweetInput.getText().toString(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    try{
                        JSONObject responseJson = new JSONObject(new String(responseBody));
                        Tweet resultTweet = Tweet.fromJson(responseJson);

                        //return result to calling activity
                        Intent resultData = new Intent(); //returns back to whoever called
                        //use parcel
                        resultData.putExtra("RESULT_TWEET_KEY", Parcels.wrap(resultTweet)); //key and value for intent
                        setResult(RESULT_OK, resultData);
                        finish();
                    } catch(JSONException e){
                        Log.e("ComposeActivity", "Error parsing response", e);
                    }

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }
}
