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

    void setTitle(String title);

    int getYear();

    void setYear(int year);

    String getRated();

    void setRated(String rated);

    Date getReleased();

    void setReleased(Date released);

    String getRuntime();

    void setRuntime(String runtime);

    List<Genre> getGenres();

    void setGenres(List<Genre> genres);

    String getDirector();

    void setDirector(String director);

    String getWriter();

    void setWriter(String writer);

    List<String> getActors();

    void setActors(List<String> actors);

    String getPlot();

    void setPlot(String plot);

    String getLanguage();

    void setLanguage(String language);

    String getCountry();

    void setCountry(String country);

    String getAwards();

    void setAwards(String awards);

    URL getPoster();

    void setPoster(URL poster);

    int getMetascore();

    void setMetascore(int metascore);

    float getImdbRating();

    void setImdbRating(float imdbRating);

    int getImdbVotes();

    void setImdbVotes(int imdbVotes);

    String getImdbID();

    void setImdbID(String imdbID);

    int getStock();

    void setStock(int newValue);

    int getRented();

    void setRented(int rented);
}
