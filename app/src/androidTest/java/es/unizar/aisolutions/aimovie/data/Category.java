package es.unizar.aisolutions.aimovie.data;

/**
 * Category defines information stored in a Category.
 *
 * Created by diego on 2/04/15.
 * Time spent: 2 minutes.
 */
public class Category {

    public String description;
    public String name;
    public String _id;

    public Category(String _id,String description, String name) {
        this.name = name;
        this.description = description;
        this._id=_id;
    }
}
