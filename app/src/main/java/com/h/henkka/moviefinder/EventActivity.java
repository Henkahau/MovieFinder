package com.h.henkka.moviefinder;

import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.h.henkka.moviefinder.Models.Event;
import com.h.henkka.moviefinder.XmlParsers.EventXmlParser;
import com.h.henkka.moviefinder.XmlParsers.XmlParser;
import com.poliveira.parallaxrecyclerview.HeaderLayoutManagerFixed;
import com.poliveira.parallaxrecyclerview.ParallaxRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class EventActivity extends AppCompatActivity implements MovieFinderXmlParserInterface {

    private Event event;

    @BindView(R.id.movie_title_txt)
    TextView movieTitle;

    @BindView(R.id.card_image_view)
    ImageView movieImageView;

    @BindView(R.id.rating_image_view)
    ImageView ratigImage;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.progress_circular)
    ProgressBar progressCircular;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView.setHasFixedSize(true);
        String eventID = getIntent().getStringExtra("EVENT_ID");
        XmlParser eventParser = new EventXmlParser(this, eventID);
        eventParser.start();
        progressCircular.setVisibility(View.VISIBLE);
        movieImageView.setVisibility(View.GONE);
        ratigImage.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        movieTitle.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void theaterDataRequestDone(List data) {

    }

    @Override
    public void movieDataRequestDone(List data) {

    }

    @Override
    public void eventDataRequestDone(List<Event> data) {
        event = data.get(0);
        EventActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateUi();
            }
        });

    }

    private void updateUi() {
        Glide.with(this)
                .load(event.getLargeLandscapeUrl())
                .into(movieImageView);

        Glide.with(this)
                .load(event.getRatingUrl())
                .into(ratigImage);

        movieTitle.setText(event.getTitle());
        createRecyclerAdapter(recyclerView);
        movieImageView.setVisibility(View.VISIBLE);
        ratigImage.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
        movieTitle.setVisibility(View.VISIBLE);
        progressCircular.setVisibility(View.GONE);

    }

    private void createRecyclerAdapter(RecyclerView recyclerView) {
        final List<String> synopsis = new ArrayList<>();
        synopsis.add(event.getSynopsis());

        final ParallaxRecyclerAdapter<String> adapter = new ParallaxRecyclerAdapter<String>(synopsis) {
            @Override
            public void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder, ParallaxRecyclerAdapter<String> parallaxRecyclerAdapter, int i) {

                TextView synopsisTextView = ((SynopsisViewHolder) viewHolder).synopsisTv;
                synopsisTextView.setText(event.getSynopsis());
            }

            @Override
            public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, ParallaxRecyclerAdapter<String> parallaxRecyclerAdapter, int i) {
                return new SynopsisViewHolder(getLayoutInflater().
                        inflate(R.layout.text_item_layout, viewGroup, false));
            }

            @Override
            public int getItemCountImpl(ParallaxRecyclerAdapter<String> parallaxRecyclerAdapter) {
                return synopsis.size();
            }
        };

        HeaderLayoutManagerFixed layoutManagerFixed = new HeaderLayoutManagerFixed(this);
        recyclerView.setLayoutManager(layoutManagerFixed);

        View headerView = getLayoutInflater()
                .inflate(R.layout.event_header_layout, recyclerView, false);

        TextView genreTv = headerView.findViewById(R.id.movie_genre_txt);
        TextView yearTv = headerView.findViewById(R.id.movie_year_txt);
        yearTv.setText(event.getYear());
        genreTv.setText(event.getGenre());
        adapter.setParallaxHeader(headerView, recyclerView);
        adapter.setData(synopsis);
        recyclerView.setAdapter(adapter);

    }

    private static class SynopsisViewHolder extends RecyclerView.ViewHolder {
        public TextView synopsisTv;
        public SynopsisViewHolder(@NonNull View itemView) {
            super(itemView);
            synopsisTv = itemView.findViewById(R.id.text_item_label);
        }
    }
}
