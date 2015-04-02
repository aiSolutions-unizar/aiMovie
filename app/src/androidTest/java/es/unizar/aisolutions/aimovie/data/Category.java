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

    public Category(String description, String name) {
        this.name = name;
        this.description = description;
    }
}
