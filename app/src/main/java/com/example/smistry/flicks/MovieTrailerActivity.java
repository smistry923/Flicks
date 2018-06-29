package com.example.smistry.flicks;

import android.os.Bundle;
import android.util.Log;

import com.example.smistry.flicks.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    String videoId;
    Movie movie;

    public final static String API_BASE_URL = "http://api.themoviedb.org/3";
    //the parameter name for the API key
    public final static String API_KEY_PARAM = "api_key";
    //tag for logging from this activity
    public final static String TAG = "MovieListActivity";
    boolean got_trailer = false;
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);
        client = new AsyncHttpClient();

        // temporary test video id -- TODO replace with movie trailer video id
        //final String videoId = "tKodtNFpzBA";

        // resolve the player view from the layout
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for %s", movie.getTitle()));
        getTrailer();

    }
        //get the list of currently playing movies from the  API
        public void getTrailer(){
            //create the url
            String url = API_BASE_URL + "/movie/" + movie.getId()+"/videos";
            //set the request parameters
            RequestParams params = new RequestParams();
            params.put(API_KEY_PARAM, getString(R.string.api_key)); //API key, always required
            //execute a GET Request expecting a JSON object response
            client.get(url,params, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    //load the results into movies list
                    try {
                        JSONArray results = response.getJSONArray("results");
                        //iterate through result set and create Movie objects

                        JSONObject trailer_first = results.getJSONObject(0);
                        videoId = trailer_first.getString("key");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject list = (results.getJSONObject(i));
                            String tempVideoId = trailer_first.getString("type");
                            if (tempVideoId.equals("Trailer")) {
                                videoId = list.getString("key");
                                got_trailer = true;
                                break;
                            }
                            Log.i(TAG, String.format("We got the key%s", results.length()));
                        }
                        loadTrailer();

                    } catch (JSONException e) {
                       Log.i(TAG,"Failed to get video");
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.i(TAG,"Failed to get video");
                }
            });
        }

        public void loadTrailer(){
            YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);
            // initialize with API key stored in secrets.xml
            playerView.initialize(getString(R.string.api_key_2), new YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                    YouTubePlayer youTubePlayer, boolean b) {
                    // do any work here to cue video, play video, etc.
                    youTubePlayer.cueVideo(videoId);
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                    YouTubeInitializationResult youTubeInitializationResult) {
                    // log the error
                    Log.e("MovieTrailerActivity", "Error initializing YouTube player");
                }
            });
        }


}
