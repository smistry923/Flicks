package com.example.smistry.flicks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.smistry.flicks.models.Config;
import com.example.smistry.flicks.models.Movie;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.smistry.flicks.MovieAdapter.imageUrl_back;
import static com.example.smistry.flicks.MovieAdapter.imageUrl_front;

public class MovieDetailsActivity extends AppCompatActivity {

    // the movie to display
    Movie movie;

    // the view objects
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvOverview)TextView tvOverview;
    @BindView(R.id.rbVoteAverage)RatingBar rbVoteAverage;
    @BindView(R.id.frontPic) ImageView frontPic;
    @BindView(R.id.backPic) ImageView backPic;
    //config needed for image url
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        // resolve the view objects
        ButterKnife.bind(this);
        // unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvOverview.setMovementMethod(new ScrollingMovementMethod());

        //build url for poster image

        GlideApp.with(this)
                .load(getIntent().getStringExtra(imageUrl_front))
                .transform(new RoundedCorners(15))
                .placeholder(R.drawable.flicks_movie_placeholder)
                .error(R.drawable.flicks_movie_placeholder)
                .into(frontPic);

        GlideApp.with(this)
                .load(getIntent().getStringExtra(imageUrl_back))
                .transform(new RoundedCorners(15))
                .placeholder(R.drawable.flicks_backdrop_placeholder)
                .error(R.drawable.flicks_backdrop_placeholder)
                .into(backPic);

        // vote average is 0..10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);
    }

    public void loadTrailer(View view) {
        Intent trailer_load = new Intent(this, MovieTrailerActivity.class);
        trailer_load.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
        this.startActivity(trailer_load);
    }

}
