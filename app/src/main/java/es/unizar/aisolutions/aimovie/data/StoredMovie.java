package es.unizar.aisolutions.aimovie.data;

import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * Created by dbarelop on 29/04/15.
 */
public class StoredMovie implements Movie {
    private long _id;
    private String title;
    private int year;
    private String rated;
    private Date released;
    private String runtime;
    private List<Genre> genres;
    private String director;
    private String writer;
    private List<String> actors;
    private String plot;
    private String language;
    private String country;
    private String awards;
    private URL poster;
    private int metascore;
    private float imdbRating;
    private int imdbVotes;
    private String imdbID;
    private int stock;
    private int rented;

    public StoredMovie(long _id, String title, int year, String rated, Date released, String runtime,
                       List<Genre> genres, String director, String writer, List<String> actors,
                       String plot, String language, String country, String awards, URL poster,
                       int metascore, float imdbRating, int imdbVotes, String imdbID, int stock, int rented) {
        this._id = _id;
        this.title = title;
        this.year = year;
        this.rated = rated;
        this.released = released;
        this.runtime = runtime;
        this.genres = genres;
        this.director = director;
        this.writer = writer;
        this.actors = actors;
        this.plot = plot;
        this.language = language;
        this.country = country;
        this.awards = awards;
        this.poster = poster;
        this.metascore = metascore;
        this.imdbRating = imdbRating;
        this.imdbVotes = imdbVotes;
        this.imdbID = imdbID;
        this.stock = stock;
        this.rented = rented;
    }

    @Override
    public long get_id() {
        return _id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public String getRated() {
        return rated;
    }

    @Override
    public Date getReleased() {
        return released;
    }

    @Override
    public String getRuntime() {
        return runtime;
    }

    @Override
    public List<Genre> getGenres() {
        return genres;
    }

    @Override
    public String getDirector() {
        return director;
    }

    @Override
    public String getWriter() {
        return writer;
    }

    @Override
    public List<String> getActors() {
        return actors;
    }

    @Override
    public String getPlot() {
        return plot;
    }

    @Override
    public String getLanguage() {
        return language;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public String getAwards() {
        return awards;
    }

    @Override
    public int getStock() {
        return stock;
    }

    @Override
    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public URL getPoster() {
        return poster;
    }

    @Override
    public int getMetascore() {
        return metascore;
    }

    @Override
    public float getImdbRating() {
        return imdbRating;
    }

    @Override
    public int getImdbVotes() {
        return imdbVotes;
    }

    @Override
    public String getImdbID() {
        return imdbID;
    }

    public int getRented() {
        return rented;
    }

    public void setRented(int rented) {
        this.rented = rented;
    }
}
