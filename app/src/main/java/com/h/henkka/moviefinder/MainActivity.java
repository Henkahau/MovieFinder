package com.h.henkka.moviefinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieFinderXmlParserInterface {

    ArrayList<MovieTheater> theaters = new ArrayList<>();
    ArrayList<Movie> movies = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        XmlParser movieParser = new MovieXmlParser(this);
        movieParser.start();
        XmlParser theaterXmlParser = new TheaterXmlParser(this);
        theaterXmlParser.start();
    }


    private void updateUi(){

        Spinner spinner = findViewById(R.id.spinner_);
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
    public void theatreDataRequestDone(List data) {
        theaters.addAll(data);
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateUi();
            }
        });

    }

    @Override
    public void movieDataRequestDone(List data) {
        movies.addAll(data);
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("LEFFAA", movies.get(0).getTitle());
            }
        });

    }
}
