package es.unizar.aisolutions.aimovie.contentprovider;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Vector;
import es.unizar.aisolutions.aimovie.data.Category;
import es.unizar.aisolutions.aimovie.data.Film;
import es.unizar.aisolutions.aimovie.database.CategoriesTable;
import es.unizar.aisolutions.aimovie.database.FilmsDatabaseHelper;
import es.unizar.aisolutions.aimovie.database.FilmsTable;
import es.unizar.aisolutions.aimovie.database.KindTable;
import java.util.List;
import java.util.ArrayList;

/**
 * FilmsContentProvider implements all needed methods to manage database.
 *
 * Created by diego on 2/04/15.
 * Time spent: 1 minute.
 *
 */
public class FilmsContentProvider implements ContentProvider,ContentUpdater {
    /**
     *
     * @return  A vector with all categories from the database (it's possible with null)
     */
    @Override
    public Vector<Category> fetchCategories() {
        Vector<Category> c = new Vector<>();
        Cursor cursor;
        cursor=mDb.query(CategoriesTable.TABLE_NAME, new String[]{CategoriesTable.PRIMARY_KEY,
                        CategoriesTable.COLUMN_CATEGORIE_NAME, CategoriesTable.COLUMN_DESCRIPTION},
                null, null, null, null, null,null);

        if (cursor.moveToFirst()) {
            do {
                c.add(getCategory(cursor));
            } while (cursor.moveToNext());
        }
        return c;
    }

        /**
         *
         * @param mCursor Cursor where to get the information
         * @return Get the information from a Cursor into a category
         */
    private Category getCategory (Cursor mCursor) {
        if (mCursor != null) {
                return new Category(mCursor.getString(1), mCursor.getString(2), mCursor.getString(3));
        }
        else {
            return null;
        }
    }
    @Override
    public Vector<Film> fetchFilms(Category c) {
        return null;
    }

    @Override
    public Vector<Film> fetchFilms(Vector<Category> c) {
        return null;
    }

    /**
     *
     * @return A vector with all films from the database (it's possible with null)
     */
    @Override
    public Vector<Film> fetchFilms() {
        Vector<Film> c = new Vector<>();
        Cursor cursor;
        cursor= mDb.query(FilmsTable.TABLE_NAME, new String[]{FilmsTable.PRIMARY_KEY,
                        FilmsTable.COLUMN_FILM_NAME, FilmsTable.COLUMN_SINOPSIS, FilmsTable.COLUMN_DIRECTOR,
                        FilmsTable.COLUMN_IN_STOCK,FilmsTable.COLUMN_RENTED,FilmsTable.COLUMN_YEAR},
                        null, null, null, null, null,null);

        if (cursor.moveToFirst()) {
            do {
                c.add(getFilm(cursor));
            } while (cursor.moveToNext());
        }
        return c;
    }

    /**
     *
     * @param id Identifier from film to fetch.
     * @return The film asked
     */
    @Override
    public Film fetchFilms(String id) {
        Cursor mCursor = mDb.query(FilmsTable.TABLE_NAME, new String[]{FilmsTable.PRIMARY_KEY,
                FilmsTable.COLUMN_FILM_NAME, FilmsTable.COLUMN_SINOPSIS, FilmsTable.COLUMN_DIRECTOR,
                FilmsTable.COLUMN_IN_STOCK,FilmsTable.COLUMN_RENTED,FilmsTable.COLUMN_YEAR},
                FilmsTable.PRIMARY_KEY + "=" + id, null, null, null, null,null);
        mCursor.moveToFirst();
        return getFilm(mCursor);
    }

    /**
     *
     * @param mCursor Cursor where to get the information
     * @return Get the information from a Cursor into a film
     */
    private Film getFilm (Cursor mCursor) {
        if (mCursor != null) {
                return new Film(mCursor.getString(1), mCursor.getString(2), mCursor.getString(3),
                        mCursor.getString(4), mCursor.getInt(5), mCursor.getInt(6), mCursor.getInt(7));
        }
        else {
            return null;
        }
    }
    /**
     *
     * @param id Identifier from category to delete.
     * @return true if the delete have been successfully
     */
    @Override
    public boolean deleteCategory(String id) {
        return mDb.delete(CategoriesTable.TABLE_NAME, CategoriesTable.PRIMARY_KEY + "=" + id, null) > 0;
    }
    /**
     *
     * @param id Identifier from film to delete.
     * @return true if the delete have been successfully
     */
    @Override
    public boolean deleteFilms(String id) {
        return mDb.delete(FilmsTable.TABLE_NAME, FilmsTable.PRIMARY_KEY + "=" + id, null) > 0;
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

    /**
     *
     * @param newCategory Category replacing one with the same _id.
     * @return True if the update have been successfully else false
     */
    @Override
    public boolean updateCategory(Category newCategory) {
        if (newCategory!=null && categoryComplete(newCategory)) {
            ContentValues args = new ContentValues();
            args.put(CategoriesTable.PRIMARY_KEY, newCategory._id);
            args.put(CategoriesTable.COLUMN_CATEGORIE_NAME, newCategory.name);
            args.put(CategoriesTable.COLUMN_DESCRIPTION, newCategory.description);
            return mDb.update(CategoriesTable.TABLE_NAME, args,
                    CategoriesTable.PRIMARY_KEY + "=" + newCategory._id, null) > 0;
        }
        else {
            return false;
        }
    }

    /**
     *
     * @param newFilm Film replacing one with the same _id.
     * @return True if the update have been successfully else false
     */
    @Override
    public boolean updateFilm(Film newFilm) {
        if (newFilm!=null && filmComplete(newFilm)) {
            ContentValues args = new ContentValues();
            args.put(FilmsTable.PRIMARY_KEY, newFilm._id);
            args.put(FilmsTable.COLUMN_FILM_NAME, newFilm.name);
            args.put(FilmsTable.COLUMN_SINOPSIS, newFilm.sinopsis);
            args.put(FilmsTable.COLUMN_IN_STOCK, newFilm.in_stock);
            args.put(FilmsTable.COLUMN_RENTED, newFilm.rented);
            args.put(FilmsTable.COLUMN_DIRECTOR, newFilm.director);
            args.put(FilmsTable.COLUMN_YEAR, newFilm.year);
            return mDb.update(FilmsTable.TABLE_NAME, args, FilmsTable.PRIMARY_KEY + "=" + newFilm._id, null) > 0;
        }
        else {
            return false;
        }
    }

    /**
     *
     * @param newFilm New film to be added.
     * @return True if the film newFilm is added successfully else false
     */
    @Override
    public boolean addFilm(Film newFilm) {
        if (newFilm!=null && filmComplete(newFilm)) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(FilmsTable.PRIMARY_KEY, newFilm._id);
            initialValues.put(FilmsTable.COLUMN_FILM_NAME, newFilm.name);
            initialValues.put(FilmsTable.COLUMN_SINOPSIS, newFilm.sinopsis);
            initialValues.put(FilmsTable.COLUMN_IN_STOCK, newFilm.in_stock);
            initialValues.put(FilmsTable.COLUMN_RENTED, newFilm.rented);
            initialValues.put(FilmsTable.COLUMN_DIRECTOR, newFilm.director);
            initialValues.put(FilmsTable.COLUMN_YEAR, newFilm.year);
            return mDb.insert(FilmsTable.TABLE_NAME, null, initialValues) >= 0;
        }
        else {
            return false;
        }
    }

    /**
     *
     * @param film_o Film to check
     * @return True if the object film_o's parameters are complete and have a correct value,
     *          False if not
     */
    private boolean filmComplete (Film film_o) {
        if (film_o._id!=null && film_o.name!=null && film_o.sinopsis!=null &&
                film_o.name!=null && film_o.name!=null && film_o.name!=null
                && film_o.director!=null && film_o._id.length()>0 && film_o.name.length()>0
                && film_o.sinopsis.length()>0 && film_o.in_stock>=0 && film_o.rented>=0
                && film_o.director.length()>0 && film_o.year>=1900) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     *
     * @param newCategory New category to be added.
     * @return True if the category newCategory is added successfully else false
     */
    @Override
    public boolean addCategory(Category newCategory) {
        if (categoryComplete(newCategory)) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(CategoriesTable.PRIMARY_KEY, newCategory._id);
            initialValues.put(CategoriesTable.COLUMN_CATEGORIE_NAME, newCategory.name);
            initialValues.put(CategoriesTable.COLUMN_DESCRIPTION, newCategory.description);
            return mDb.insert(CategoriesTable.TABLE_NAME, null, initialValues) >= 0;
        }
        else {
            return false;
        }
    }

    /**
     *
     * @param category_o Category to check
     * @return True if the object category_o's parameters are complete and have a correct value,
     *          False if not
     */
    private boolean categoryComplete (Category category_o) {
        if (category_o._id!=null && category_o.name!=null && category_o.description!=null
                && category_o._id.length()>0 && category_o.name.length()>0 && category_o.description.length()>0) {
            return true;
        }
        else {
            return false;
        }
    }
    /**
     *
     * @param f film to link, c Category to link
     * @return True if the parameters are complete, have a correct value and a kind is added successfully
     *          False if not
     */
    @Override
    public boolean addKind(String f, String c) {
        if (f!=null && c!=null && f.length()>0 && c.length()>0) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(KindTable.COLUMN_FILM_ID, f);
            initialValues.put(KindTable.COLUMN_CATEGORIE_ID, c);
            if (! mDb.insert(KindTable.TABLE_NAME, null, initialValues) == 1) {
                return false;
            } else {
                return true;
            }
        }
        else {
            return false;
        }
    }
}