package es.unizar.aisolutions.aimovie.data;

/**
 * Genre defines information stored in a Genre.
 * <p/>
 * Created by diego on 2/04/15.
 * Time spent: 2 minutes.
 */
public class Genre {
    private long _id;
    private String name;

    public Genre(long _id, String name) {
        this._id = _id;
        this.name = name;
    }

    public long get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
