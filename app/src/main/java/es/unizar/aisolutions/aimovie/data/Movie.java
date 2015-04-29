package es.unizar.aisolutions.aimovie.data;

import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * Created by dbarelop on 29/04/15.
 */
public interface Movie {
    long get_id();

    String getTitle();

    int getYear();

    String getRated();

    Date getReleased();

    String getRuntime();

    List<String> getGenres();

    String getDirector();

    String getWriter();

    List<String> getActors();

    String getPlot();

    String getLanguage();

    String getCountry();

    String getAwards();

    URL getPoster();

    int getMetascore();

    float getImdbRating();

    int getImdbVotes();

    String getImdbID();
}
