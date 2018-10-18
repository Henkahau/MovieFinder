package com.h.henkka.moviefinder;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MovieXmlParser extends XmlParser {

    private String area;

    public MovieXmlParser(MovieFinderXmlParserInterface listener, String area) {
        super(listener);
        this.area = area;
    }


    @Override
    public String getXmlUrl() {
        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String dateString = formatter.format(today);
        return XML_URL + "Schedule/?area=" + area + "&dt=" + dateString;
    }

    @Override
    public void processXmlData(InputStream input) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(input, null);
        parser.nextTag();
        List data = readFeed(parser);
        iListener.movieDataRequestDone(data);
    }

    @Override
    public List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG, null, "Schedule");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            Log.d("READFEED", name);
            // starts looking for the "Shows" tag
            if (name.equals("Shows")) {
                return readChild(parser);
            }
            else {
                skip(parser);
            }
        }

        return null;
    }

    @Override
    public List readChild(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "Shows");
        ArrayList<Movie> movies = new ArrayList();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            Log.d("READSHOWS", name);
            if (name.equals("Show")) {
                movies.add(readShow(parser));
            }
            else {
                skip(parser);
            }
        }
        Log.d("READSHOWS FUNCTION", "RETURN" + movies.size());
        return movies;
    }


    private Movie readShow(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "Show");
        Movie movie = new Movie();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("Title")) {
                movie.setTitle(readTag(parser, name));
                Log.d("TITLE", movie.getTitle());
            }
            else if (name.equals("RatingImageUrl")) {
                movie.setRatingUrl(readTag(parser, name));
            }
            else if (name.equals("Genres")) {
                movie.setGenre(readTag(parser, name));
            }
            else if (name.equals("ID")) {
                movie.setId(readTag(parser, name));
            }
            else if (name.equals("ProductionYear")) {
                movie.setYear(readTag(parser, name));
            }
            else if (name.equals("dttmShowStart")) {
                movie.setStartTime(readTag(parser, name));
            }
            else if (name.equals("TheatreAndAuditorium")) {
                movie.setTheaterAuditorium(readTag(parser, name));
            }
            else if (name.equals("TheatreID")) {
                movie.setTheaterId(readTag(parser, name));
            }
            else if (name.equals("PresentationMethodAndLanguage")) {
                movie.setPresentationMethodAndLanguage(readTag(parser, name));
            }
            else if (name.equals("Images")) {
                parser.require(XmlPullParser.START_TAG, null, "Images");
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String imageName = parser.getName();

                    if (imageName.equals("EventSmallImagePortrait")) {
                        movie.setSmallPortraitUrl(readTag(parser, imageName));
                    } else if (imageName.equals("EventMediumImagePortrait")) {
                        movie.setMediumPortraitUrl(readTag(parser, imageName));
                    } else if (imageName.equals("EventLargeImagePortrait")) {
                        movie.setLargePortraitUrl(readTag(parser, imageName));
                    } else if (imageName.equals("EventSmallImageLandscape")) {
                        movie.setSmallLandscapeUrl(readTag(parser, imageName));
                    } else if (imageName.equals("EventLargeImageLandscape")) {
                        movie.setLargeLandscapeUrl(readTag(parser, imageName));
                    } else {
                        skip(parser);
                    }
                }
            }

            else {
                skip(parser);
            }

        }
        return movie;
    }
}
