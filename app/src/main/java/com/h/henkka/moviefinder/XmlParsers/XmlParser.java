package com.h.henkka.moviefinder.XmlParsers;

import com.h.henkka.moviefinder.MovieFinderXmlParserInterface;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public abstract class XmlParser extends Thread {
    static final String XML_URL = "https://www.finnkino.fi/xml/";

    public abstract String getXmlUrl();

    MovieFinderXmlParserInterface iListener = null;

    public XmlParser(MovieFinderXmlParserInterface listener) {
        this.iListener = listener;
    }

    @Override
    public void run() {
        try {
            getXmlData(getXmlUrl());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void getXmlData(String url) throws IOException {
        URL mUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection)mUrl.openConnection();
        InputStream is = connection.getInputStream();
        try {
            processXmlData(is);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        finally {
            if (connection != null) {
                connection.disconnect();
            }

        }
    }

    public abstract void processXmlData(InputStream input) throws XmlPullParserException, IOException;

    public abstract List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException;

    public abstract List readChild(XmlPullParser parser) throws XmlPullParserException, IOException;

    final String readTag(XmlPullParser parser, String name) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, name);
        String tag = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, name);
        return tag;
    }

    final String readText(XmlPullParser parser) throws XmlPullParserException, IOException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }

        return result;
    }

    final void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;

                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }

    }


}
