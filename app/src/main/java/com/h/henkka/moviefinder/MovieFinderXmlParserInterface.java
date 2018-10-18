package com.h.henkka.moviefinder;

import java.util.List;

public interface MovieFinderXmlParserInterface {
    void theaterDataRequestDone(List data);
    void movieDataRequestDone(List data);
    void eventDataRequestDone(List data);
}
