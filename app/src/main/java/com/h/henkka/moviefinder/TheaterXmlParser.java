package com.h.henkka.moviefinder;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TheaterXmlParser extends XmlParser {

    @Override
    public String getXmlUrl() {
        return XML_URL + "TheatreAreas";
    }

    public TheaterXmlParser(MovieFinderXmlParserInterface listener) {
        super(listener);
    }


    @Override
    public void processXmlData(InputStream input) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(input, null);
        parser.nextTag();
        List data = readFeed(parser);
        iListener.theaterDataRequestDone(data);
    }

    @Override
    public List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "TheatreAreas");
        ArrayList<MovieTheater> theatres = new ArrayList<>();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            Log.d("READFEED", name);
            // starts looking for the "Shows" tag
            if (name.equals("TheatreArea")) {
                theatres.add(readTheater(parser));
            }
            else {
                skip(parser);
            }
        }

        return theatres;
    }

    @Override
    public List readChild(XmlPullParser parser) throws XmlPullParserException, IOException {
        return null;
    }


    private MovieTheater readTheater(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "TheatreArea");

        String id = null;
        String theatreName = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("ID")) {
                id = readTag(parser, name);
            } else if (name.equals("Name")) {
                theatreName = readTag(parser, name);
            } else {
                skip(parser);
            }

        }
        return new MovieTheater(id, theatreName);
    }
}
