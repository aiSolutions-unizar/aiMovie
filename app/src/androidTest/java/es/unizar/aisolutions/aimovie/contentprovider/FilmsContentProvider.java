package es.unizar.aisolutions.aimovie.contentprovider;

import java.util.Vector;
import es.unizar.aisolutions.aimovie.data.Category;
import es.unizar.aisolutions.aimovie.data.Film;

/**
 * FilmsContentProvider implements all needed methods to manage database.
 *
 * Created by diego on 2/04/15.
 * Time spent: 1 minute.
 *
 */
public class FilmsContentProvider implements ContentProvider,ContentUpdater {
    @Override
    public Vector<Category> fetchCategories() {
        return null;
    }

    @Override
    public Vector<Film> fetchFilms(Category c) {
        return null;
    }

    @Override
    public Vector<Film> fetchFilms(Vector<Category> c) {
        return null;
    }

    @Override
    public Vector<Film> fetchFilms() {
        return null;
    }

    @Override
    public Film fetchFilms(String id) {
        return null;
    }

    @Override
    public boolean deleteCategory(String id) {
        return false;
    }

    @Override
    public boolean deleteFilms(String id) {
        return false;
    }

    @Override
    public Vector<Boolean> deleteFilms(Category c) {
        return null;
    }

    @Override
    public Vector<Boolean> deleteFilms(Vector<Category> c) {
        return null;
    }

    @Override
    public boolean deleteKind(String id) {
        return false;
    }

    @Override
    public boolean deleteKind(String f, String c) {
        return false;
    }

    @Override
    public Category updateCategory(Category newCategory) {
        return null;
    }

    @Override
    public Film updateFilm(Film newFilm) {
        return null;
    }

    @Override
    public boolean addFilm(Film newFilm) {
        return false;
    }

    @Override
    public boolean addCategory(Category newCategory) {
        return false;
    }

    @Override
    public boolean addKind(String f, String c) {
        return false;
    }
}
