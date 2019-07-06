package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{
    private List<Tweet> mTweets;
    public static Tweet currTweet;
    public static Context context;
    //pass Tweet Array into constructor
    public TweetAdapter(List<Tweet> tweets){
        mTweets =tweets;
    }
    //for each row, inflate layout and cache reference into ViewHolder Cache
    //invoked when need new row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //get data according to position (which tweet)
        Tweet tweet = mTweets.get(position);
        currTweet = tweet;
        //populate view according to data
        holder.tvUsername.setText(tweet.user.name);
        holder.tvBody.setText(tweet.body);
        holder.tvDate.setText(tweet.createdAt);
        Glide.with(context).load(tweet.user.profileImageUrl).into(holder.ivProfileImage);
        if(tweet.hasEntities==true){
            String entityUrl = tweet.entity.loadUrl;
            Glide.with(context).load(entityUrl).into(holder.entityTweet);
            holder.entityTweet.setVisibility(View.VISIBLE);
        }
        else{
            holder.entityTweet.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    //bind values based on position of element
    //create ViewHolder class
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvDate;
        public ImageButton btnReply;
        public ImageView entityTweet;
        public FloatingActionButton fabButton;


        public ViewHolder(View itemView){
            super(itemView);

            //perform findViewById lookups
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUserName);
            tvDate = (TextView)itemView.findViewById(R.id.tvDate);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            btnReply = (ImageButton) itemView.findViewById(R.id.btnReply);
            entityTweet = (ImageView) itemView.findViewById(R.id.ivPic);
            fabButton = (FloatingActionButton) itemView.findViewById(R.id.fab);


            btnReply.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("username", currTweet.tweetAuthor);
                    context.startActivity(intent);
                }
            });

        }
    }

    // Clean all elements of the recycler
    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }
}
