package es.unizar.aisolutions.aimovie.data;

/**
 * Film defines which information is stored in a Film.
 * <p/>
 * Created by diego on 2/04/15.
 * Time spent: 7 minutes.
 */
public class Movie {

    // _id should be an IMDb identifier.
    public String _id, name, sinopsis, director;
    public int in_stock, rented, year;


    public Movie(String _id, String name, String sinopsis, String director, int in_stock, int rented, int year) {
        this._id = _id;
        this.name = name;
        this.sinopsis = sinopsis;
        this.director = director;
        this.in_stock = in_stock;
        this.rented = rented;
        this.year = year;
    }

}
