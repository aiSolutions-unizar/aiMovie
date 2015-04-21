package es.unizar.aisolutions.aimovie.contentprovider;

import java.util.Vector;

import es.unizar.aisolutions.aimovie.data.Category;
import es.unizar.aisolutions.aimovie.data.Film;

/**
 * ContentQueries contains every method which queries database.
 *
 * Created by diego on 2/04/15.
 * Time spent: 8 minutes.
 */
public interface ContentQueries {

    /**
     * @return All categories stored in database.
     */
    Vector<Category> fetchCategories();

    /**
     * @param c = Category used as filter.
     * @return All films belonging to 'c' category.
     */
    Vector<Film> fetchFilms(Category c);

    /**
     * @param c = Categories list used as filter.
     * @return All films belonging to any category in the list.
     */
    Vector<Film> fetchFilms(Vector<Category> c);

    /**
     * @return All films without filtering.
     */
    Vector<Film> fetchFilms();

    /**
     * @param id Identifier from film to fetch.
     * @return The film whose identifier matches with 'id' or null.
     */
    Film fetchFilms(String id);

}
