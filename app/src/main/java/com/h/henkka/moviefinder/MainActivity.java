package com.h.henkka.moviefinder;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.astuetz.PagerSlidingTabStrip;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieFinderXmlParserInterface, AdapterView.OnItemSelectedListener {

    private ArrayList<MovieTheater> theaters = new ArrayList<>();
    private ArrayList<Movie> movies = new ArrayList<>();

    @BindView(R.id.spinner_)
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        spinner.setOnItemSelectedListener(this);


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
        movies.clear();
        movies.addAll(data);
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateMovieListUi();
            }
        });
    }

    private void updateMovieListUi() {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String area = theaters.get(position).getId();
        XmlParser movieParser = new MovieXmlParser(this, area);
        movieParser.start();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
