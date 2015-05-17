package es.unizar.aisolutions.aimovie.data;

import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * Created by dbarelop on 29/04/15.
 */
public interface Movie {
    // TODO: make year, metascore, imdb_rating and imdb_votes Integer and Float (nullable)
    long get_id();

    String getTitle();

    int getYear();

    String getRated();

    Date getReleased();

    String getRuntime();

    List<Genre> getGenres();

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

    int getStock();

    void setStock(int newValue);

    int getRented();

    void setRented(int newValue);
}
