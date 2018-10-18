package com.h.henkka.moviefinder;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EventXmlParser extends XmlParser {


    public EventXmlParser(MovieFinderXmlParserInterface listener) {
        super(listener);
    }

    @Override
    public String getXmlUrl() {
        return XML_URL + "Events";
    }

    @Override
    public void processXmlData(InputStream input) throws XmlPullParserException, IOException {
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(input, null);
        parser.nextTag();
        List data = readFeed(parser);
        iListener.eventDataRequestDone(data);
    }

    @Override
    public List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "Events");
        ArrayList<Event> events = new ArrayList<>();
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();
            Log.d("READFEED", name);
            // starts looking for the "Shows" tag
            if (name.equals("Event")) {
                events.add(readEvent(parser));
            }
            else {
                skip(parser);
            }
        }

        return events;
    }

    @Override
    public List readChild(XmlPullParser parser) throws XmlPullParserException, IOException {
        return null;
    }

    private Event readEvent(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "Event");
        Event event = new Movie();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("Title")) {
                event.setTitle(readTag(parser, name));
                Log.d("TITLE", event.getTitle());
            }
            else if (name.equals("RatingImageUrl")) {
                event.setRatingUrl(readTag(parser, name));
            }
            else if (name.equals("Genres")) {
                event.setGenre(readTag(parser, name));
            }
            else if (name.equals("ProductionYear")) {
                event.setYear(readTag(parser, name));
            }
            else if (name.equals("ID")) {
                event.setId(readTag(parser, name));
            }
            else if (name.equals("Images")) {
                parser.require(XmlPullParser.START_TAG, null, "Images");
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String imageName = parser.getName();

                    if (imageName.equals("EventSmallImagePortrait")) {
                        event.setSmallPortraitUrl(readTag(parser, imageName));
                    } else if (imageName.equals("EventMediumImagePortrait")) {
                        event.setMediumPortraitUrl(readTag(parser, imageName));
                    } else if (imageName.equals("EventLargeImagePortrait")) {
                        event.setLargePortraitUrl(readTag(parser, imageName));
                    } else if (imageName.equals("EventSmallImageLandscape")) {
                        event.setSmallLandscapeUrl(readTag(parser, imageName));
                    } else if (imageName.equals("EventLargeImageLandscape")) {
                        event.setLargeLandscapeUrl(readTag(parser, imageName));
                    } else {
                        skip(parser);
                    }
                }
            }

            else {
                skip(parser);
            }

        }
        return event;
    }
}
