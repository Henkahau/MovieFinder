package com.h.henkka.moviefinder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.h.henkka.moviefinder.Models.Movie;
import com.h.henkka.moviefinder.Models.MovieTheater;
import com.h.henkka.moviefinder.XmlParsers.MovieXmlParser;
import com.h.henkka.moviefinder.XmlParsers.TheaterXmlParser;
import com.h.henkka.moviefinder.XmlParsers.XmlParser;
import com.poliveira.parallaxrecyclerview.HeaderLayoutManagerFixed;
import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieFinderXmlParserInterface, AdapterView.OnItemSelectedListener {

    private List<MovieTheater> theaters = new ArrayList<>();
    private ArrayList<Movie> movies = new ArrayList<>();

    @BindView(R.id.spinner_)
    Spinner spinner;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.progress_circular)
    ProgressBar progressCircular;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        progressCircular.setVisibility(View.GONE);
        spinner.setOnItemSelectedListener(this);

        mRecyclerView.setHasFixedSize(true);

        XmlParser theaterXmlParser = new TheaterXmlParser(this);
        theaterXmlParser.start();

    }


    private void updateTheaterList(){

        ArrayList<String> movieTitles = new ArrayList();
        for (int i = 0; i < theaters.size(); i++) {
            movieTitles.add(theaters.get(i).getName());
        }

        ArrayAdapter<String> movieAdapter = new ArrayAdapter<>(MainActivity.this,
                R.layout.support_simple_spinner_dropdown_item, movieTitles);

        movieAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(movieAdapter);

    }


    @Override
    public void theaterDataRequestDone(List data) {
        theaters.addAll(data);
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateTheaterList();
            }
        });

    }

    @Override
    public void movieDataRequestDone(List data) {
        movies.clear();
        movies.addAll(data);
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateMovieListUi();
            }
        });

    }

    @Override
    public void eventDataRequestDone(List data) {

    }

    private void updateMovieListUi() {
        progressCircular.setVisibility(View.GONE);
        createCardListAdapter(mRecyclerView);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String area = theaters.get(position).getId();
        XmlParser movieParser = new MovieXmlParser(this, area);
        movieParser.start();
        progressCircular.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void createCardListAdapter(RecyclerView recyclerView) {
        final ParallaxRecyclerAdapter<Movie> adapter = new ParallaxRecyclerAdapter<Movie>(movies) {
            @Override
            public void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder,
                                             ParallaxRecyclerAdapter<Movie> parallaxRecyclerAdapter, int i) {
                Movie movie = movies.get(i);
                String title = movie.getTitle();
                String genre = movie.getGenre();
                String audit = movie.getTheaterAuditorium();
                String lang = movie.getPresentationMethodAndLanguage();
                String imageUrl = movie.getLargeLandscapeUrl();
                String ratingUrl = movie.getRatingUrl();

                ((CardViewHolder)viewHolder).movieTitleTv.setText(title);
                ((CardViewHolder)viewHolder).movieGenreTv.setText(genre);
                ((CardViewHolder)viewHolder).movieTheatreAuditoriumTv.setText(audit);
                ((CardViewHolder)viewHolder).movieLangTv.setText(lang);

                View view = getLayoutInflater().inflate(R.layout.card_view_layout, null, false);
                Glide.with(view)
                        .load(imageUrl)
                        .into(((CardViewHolder)viewHolder).movieImageView);

                Glide.with(view)
                        .load(ratingUrl)
                        .into(((CardViewHolder)viewHolder).movieRatingImageView);
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, ParallaxRecyclerAdapter<Movie> parallaxRecyclerAdapter, int i) {
                return new CardViewHolder(getLayoutInflater()
                        .inflate(R.layout.card_view_layout, viewGroup, false));
            }

            @Override
            public int getItemCountImpl(ParallaxRecyclerAdapter<Movie> parallaxRecyclerAdapter) {
                return movies.size();
            }
        };

        HeaderLayoutManagerFixed layoutManagerFixed = new HeaderLayoutManagerFixed(this);
        recyclerView.setLayoutManager(layoutManagerFixed);

        View headerView = getLayoutInflater().inflate(R.layout.header_layout, recyclerView, false);
        TextView header = headerView.findViewById(R.id.header_text);
        adapter.setParallaxHeader(headerView, recyclerView);
        adapter.setData(movies);
        recyclerView.setAdapter(adapter);

        adapter.setOnClickEvent(new ParallaxRecyclerAdapter.OnClickEvent() {
            @Override
            public void onClick(View view, int i) {
                String eventId = movies.get(i).getId();
                Intent intent = new Intent(MainActivity.this, EventActivity.class);
                intent.putExtra("EVENT_ID", eventId);
                startActivity(intent);
            }
        });

    }

    static class CardViewHolder extends RecyclerView.ViewHolder {
        public ImageView movieImageView;
        public ImageView movieRatingImageView;

        public TextView movieTitleTv;
        public TextView movieGenreTv;
        public TextView movieTheatreAuditoriumTv;
        public TextView movieLangTv;


        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            movieImageView = itemView.findViewById(R.id.card_image_view);
            movieRatingImageView = itemView.findViewById(R.id.rating_image_view);

            movieTitleTv = itemView.findViewById(R.id.movie_title_txt);
            movieGenreTv = itemView.findViewById(R.id.movie_genre_txt);
            movieTheatreAuditoriumTv = itemView.findViewById(R.id.movie_auditorium_txt);
            movieLangTv = itemView.findViewById(R.id.movie_presentation_lang_txt);
        }
    }


}
