package es.unizar.aisolutions.aimovie.data;

/**
 * Category defines information stored in a Category.
 * <p/>
 * Created by diego on 2/04/15.
 * Time spent: 2 minutes.
 */
public class Category {
    // TODO: make _id long
    private String _id;
    private String name;
    private String description;

    public Category(String _id, String description, String name) {
        this.name = name;
        this.description = description;
        this._id = _id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String get_id() {
        return _id;
    }
}
