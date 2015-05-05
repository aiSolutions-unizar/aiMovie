package es.unizar.aisolutions.aimovie.contentprovider;

import java.util.List;

import es.unizar.aisolutions.aimovie.data.Category;
import es.unizar.aisolutions.aimovie.data.Movie;

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
    List<Category> fetchCategories();

    /**
     * @param c = Category used as filter.
     * @return All films belonging to 'c' category.
     */
    List<Movie> fetchMovies(Category c);

    /**
     * @param categories = Categories list used as filter.
     * @return All films belonging to any category in the list.
     */
    List<Movie> fetchMovies(List<Category> categories);

    /**
     * @return All films without filtering.
     */
    List<Movie> fetchMovies();

    /**
     * @param id Identifier from film to fetch.
     * @return The film whose identifier matches with 'id' or null.
     */
    Movie fetchMovie(long id);

}
