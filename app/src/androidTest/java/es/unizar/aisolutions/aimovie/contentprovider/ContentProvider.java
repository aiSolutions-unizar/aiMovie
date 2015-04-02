package es.unizar.aisolutions.aimovie.contentprovider;

import java.util.Vector;
import es.unizar.aisolutions.aimovie.data.Category;
import es.unizar.aisolutions.aimovie.data.Film;

/**
 * ContentProvider contains every method which queries database.
 *
 * Created by diego on 2/04/15.
 * Time spent: 8 minutes.
 */
public interface ContentProvider {

    /**
     * @return All categories stored in database.
     */
    public Vector<Category> fetchCategories();

    /**
     * @param c = Category used as filter.
     * @return All films belonging to 'c' category.
     */
    public Vector<Film> fetchFilms(Category c);

    /**
     * @param c = Categories list used as filter.
     * @return All films belonging to any category in the list.
     */
    public Vector<Film> fetchFilms(Vector<Category> c);

    /**
     * @return All films without filtering.
     */
    public Vector<Film> fetchFilms();

    /**
     * @param id Identifier from film to fetch.
     * @return The film whose identifier matches with 'id' or null.
     */
    public Film fetchFilms(String id);

}
