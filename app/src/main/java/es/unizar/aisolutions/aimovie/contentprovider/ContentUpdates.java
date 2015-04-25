package es.unizar.aisolutions.aimovie.contentprovider;

import java.util.List;

import es.unizar.aisolutions.aimovie.data.Category;
import es.unizar.aisolutions.aimovie.data.Movie;

/**
 * ContentUpdates defines every method that modifies database.
 * This methods can be divided in three types adding, updating and erasing.
 * <p/>
 * Created by diego on 2/04/15.
 * Time spent: 11 minutes.
 */
public interface ContentUpdates {

    /**
     * @param c Category to delete.
     * @return True if deletion went right, otherwise false.
     */
    boolean deleteCategory(Category c);

    /**
     * @param m Movie to delete.
     * @return True if deletion went right, otherwise false.
     */
    boolean deleteMovie(Movie m);

    /**
     * @param c Category from all movies to delete.
     * @return For each movie: true if deletion went right, otherwise false.
     */
    List<Boolean> deleteMovies(Category c);

    /**
     * @param c Categories list from all movies to delete.
     * @return For each movie: true if deletion went right, otherwise false.
     */
    List<Boolean> deleteMovies(List<Category> c);

    /**
     * @param id Identifier from the 'kind' relationship to delete.
     * @return True if deletion went right, otherwise false.
     */
    boolean deleteKind(String id);

    /**
     * @param f Identifier from movie whose relationship 'kind' be deleted.
     * @param c Identifier from category whose relationship 'kind' be deleted.
     * @return True if deletion went right, otherwise false.
     */
    boolean deleteKind(String f, String c);

    /**
     * @param newCategory Category replacing one with the same _id.
     * @return Category which has been replaced.
     */
    boolean updateCategory(Category newCategory);

    /**
     * @param newMovie Movie replacing one with the same _id.
     * @return Movie which has been replaced.
     */
    boolean updateMovie(Movie newMovie);

    /**
     * adds a new Movie to database.
     *
     * @param newMovie New movie to be added.
     * @return True if the movie has been added, otherwise false.
     */
    boolean addMovie(Movie newMovie);

    /**
     * adds a new Category to database.
     *
     * @param newCategory New category to be added.
     * @return True if the category has been added, otherwise false.
     */
    boolean addCategory(Category newCategory);

    /**
     * includes information about which movie belongs to a category.
     *
     * @param f Movie to be categorized.
     * @param c Category in which movie is included.
     * @return True if the relation has been added, otherwise false.
     */
    boolean addKind(String f, String c);
}
