package com.codepath.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.codepath.flixster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {

    // add api ky
    private static final String YOUTUBE_API_KEY = "AIzaSyDe7LqleRJQoIrtZnfxkmNGxjB2nmTehdg";
    public static String NOW_PLAYING = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    TextView tvTitle;
    TextView tvOverview;
    RatingBar ratingBar;
    YouTubePlayerView youTubePlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvTitle = findViewById(R.id.tvTitile);
        tvOverview = findViewById(R.id.tvOverview);
        ratingBar = findViewById(R.id.ratingBar);
        final YouTubePlayerView youTubePlayerView =
                (YouTubePlayerView) findViewById(R.id.player);

        // unwrap parcels
        Movie movie = Parcels.unwrap(getIntent().getParcelableExtra("movie"));
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        ratingBar.setRating((float) movie.getRating());


        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(NOW_PLAYING, movie.getMovieId()),
                new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int i, Headers headers, JSON json) {
                        try {
                            JSONArray results = json.jsonObject.getJSONArray("results");

                            if (results.length() == 0 ){return;}
                            // we want to get the first video
                            final String youtubeKey = results.getJSONObject(0).getString("key");
                            Log.e("DetailActivity", youtubeKey);

                            // initialize youtube


                            // playing video
                            youTubePlayerView.initialize(YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
                                @Override
                                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                                    Log.d("DetailActivity","onsuccess");
                                    // do any work here to cue video, play video, etc.

                                    youTubePlayer.cueVideo(youtubeKey);
                                }

                                @Override
                                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                                    Log.d("DetailActivity","onfailure");
                                }
                            });




                        } catch (JSONException e) {
                            Log.e("DetailActivity", "Failed to parse json", e);
                        }
                    }
                    @Override
                    public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                        Log.e("DetailActivity", "on failure");
                    }
                });

    }
    public void initializeYoutube(final String youtubeKey) {

    }
}