package com.h.henkka.moviefinder.Models;


public class Movie extends Event{

    private String startTime;
    private String theaterAuditorium;
    private String theaterId;
    private String presentationMethodAndLanguage;

    public String getPresentationMethodAndLanguage() {
        return presentationMethodAndLanguage;
    }

    public void setPresentationMethodAndLanguage(String presentationMethodAndLanguage) {
        this.presentationMethodAndLanguage = presentationMethodAndLanguage;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTheaterAuditorium() {
        return theaterAuditorium;
    }

    public void setTheaterAuditorium(String theaterAuditorium) {
        this.theaterAuditorium = theaterAuditorium;
    }

    public String getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(String theaterId) {
        this.theaterId = theaterId;
    }
}
