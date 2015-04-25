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
    private String name;
    private String plot;
    private String director;
    private int in_stock;
    private int rented;
    private int year;
    // TODO: add list of categories related to the movie

    public Movie(String _id, String name, String plot, String director, int in_stock, int rented, int year) {
        this._id = _id;
        this.name = name;
        this.plot = plot;
        this.director = director;
        this.in_stock = in_stock;
        this.rented = rented;
        this.year = year;
    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getPlot() {
        return plot;
    }

    public String getDirector() {
        return director;
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
