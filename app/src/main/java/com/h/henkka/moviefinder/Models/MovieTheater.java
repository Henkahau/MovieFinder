package com.h.henkka.moviefinder.Models;

public class MovieTheater {

    private String id;
    private String name;

    public MovieTheater(String id, String name) {
        this.id = id;
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
