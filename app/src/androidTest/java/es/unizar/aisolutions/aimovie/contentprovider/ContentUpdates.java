package es.unizar.aisolutions.aimovie.contentprovider;

import java.util.Vector;
import es.unizar.aisolutions.aimovie.data.Category;
import es.unizar.aisolutions.aimovie.data.Film;

/**
 * ContentUpdates defines every method that modifies database.
 * This methods can be divided in three types adding, updating and erasing.
 *
 * Created by diego on 2/04/15.
 * Time spent: 11 minutes.
 */
public interface ContentUpdates {

    /**
     * @param id Identifier from category to delete.
     * @return True if deletion went right, otherwise false.
     */
    public boolean deleteCategory(String id);

    /**
     * @param id Identifier from film to delete.
     * @return True if deletion went right, otherwise false.
     */
    public boolean deleteFilms(String id);

    /**
     * @param c Category from all films to delete.
     * @return For each film: true if deletion went right, otherwise false.
     */
    public Vector<Boolean> deleteFilms(Category c);

    /**
     * @param c Categories list from all films to delete.
     * @return For each film: true if deletion went right, otherwise false.
     */
    public Vector<Boolean> deleteFilms(Vector<Category> c);

    /**
     * @param id Identifier from the 'kind' relationship to delete.
     * @return True if deletion went right, otherwise false.
     */
    public boolean deleteKind(String id);

    /**
     * @param f Identifier from film whose relationship 'kind' be deleted.
     * @param c Identifier from category whose relationship 'kind' be deleted.
     * @return True if deletion went right, otherwise false.
     */
    public boolean deleteKind(String f,String c);

    /**
     * @param newCategory Category replacing one with the same _id.
     * @return Category which has been replaced.
     */
    public boolean updateCategory(Category newCategory);

    /**
     * @param newFilm Film replacing one with the same _id.
     * @return Film which has been replaced.
     */
    public boolean updateFilm(Film newFilm);

    /**
     * addFilm adds a new Film to database.
     * @param newFilm New film to be added.
     * @return True if the film has been added, otherwise false.
     */
    public boolean addFilm(Film newFilm);

    /**
     * addCategory adds a new Category to database.
     * @param newCategory New catecory to be added.
     * @return True if the category has been added, otherwise false.
     */
    public boolean addCategory(Category newCategory);

    /**
     * addKinds includes information about which film belongs to a category.
     * @param f Film to be categorized.
     * @param c Category in which film is included.
     * @return True if the relation has been added, otherwise false.
     */
    public boolean addKind(String f, String c);
}