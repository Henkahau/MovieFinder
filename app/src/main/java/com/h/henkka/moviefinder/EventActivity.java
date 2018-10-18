package com.h.henkka.moviefinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.h.henkka.moviefinder.Models.Event;
import com.h.henkka.moviefinder.XmlParsers.EventXmlParser;
import com.h.henkka.moviefinder.XmlParsers.XmlParser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventActivity extends AppCompatActivity implements MovieFinderXmlParserInterface {

    private Event event;

    @BindView(R.id.hellou)
    TextView hello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        ButterKnife.bind(this);
        String eventID = getIntent().getStringExtra("EVENT_ID");
        XmlParser eventParser = new EventXmlParser(this, eventID);
        eventParser.start();
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
                hello.setText(event.getSynopsis());
            }
        });
    }
}
