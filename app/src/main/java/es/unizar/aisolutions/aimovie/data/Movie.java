package es.unizar.aisolutions.aimovie.data;

/**
 * Film defines which information is stored in a Film.
 * <p/>
 * Created by diego on 2/04/15.
 * Time spent: 7 minutes.
 */
public class Movie {
    // TODO: make _id long and add another field for IMDB identifier
    private String _id;
    private String title;
    private String plot;
    private String director;
    private String thumbnail;
    private int in_stock;
    private int rented;
    private int year;
    // TODO: add list of categories related to the movie

    public Movie() {

    }

    public Movie(String _id, String title, String plot, String director, String thumbnail, int in_stock, int rented, int year) {
        this._id = _id;
        this.title = title;
        this.plot = plot;
        this.director = director;
        this.thumbnail = thumbnail;
        this.in_stock = in_stock;
        this.rented = rented;
        this.year = year;
    }

    public String get_id() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public String getPlot() {
        return plot;
    }

    public String getDirector() {
        return director;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public int getIn_stock() {
        return in_stock;
    }

    public int getRented() {
        return rented;
    }

    public int getYear() {
        return year;
    }
}
