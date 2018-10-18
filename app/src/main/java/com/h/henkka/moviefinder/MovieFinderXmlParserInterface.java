package com.h.henkka.moviefinder;

import com.h.henkka.moviefinder.Models.Event;
import com.h.henkka.moviefinder.Models.Movie;
import com.h.henkka.moviefinder.Models.MovieTheater;

import java.util.List;

public interface MovieFinderXmlParserInterface {
    void theaterDataRequestDone(List<MovieTheater> data);
    void movieDataRequestDone(List<Movie> data);
    void eventDataRequestDone(List<Event> data);
}
