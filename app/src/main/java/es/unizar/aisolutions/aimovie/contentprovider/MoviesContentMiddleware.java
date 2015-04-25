package es.unizar.aisolutions.aimovie.contentprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.Vector;

import es.unizar.aisolutions.aimovie.data.Category;
import es.unizar.aisolutions.aimovie.data.Movie;
import es.unizar.aisolutions.aimovie.database.CategoriesTable;
import es.unizar.aisolutions.aimovie.database.KindTable;
import es.unizar.aisolutions.aimovie.database.MoviesTable;

/**
 * FilmsContentMiddleware implements all needed methods to manage database.
 * <p/>
 * Created by diego on 2/04/15.
 * Time spent: 1 minute.
 */
public class MoviesContentMiddleware implements ContentQueries, ContentUpdates {
    // TODO: use ContentProvider for all the queries/modifications of the database!
    private Context context;

    /**
     * Constructor
     *
     * @param context the Context within which to work
     */
    public MoviesContentMiddleware(Context context) {
        this.context = context;
    }

    /**
     * @return A vector with all categories from the database (it's possible with null)
     */
    @Override
    public Vector<Category> fetchCategories() {
        Vector<Category> c = new Vector<>();
        Uri uri = Uri.parse(MoviesContentProvider.CONTENT_URI + "/CATEGORIES");
        String[] projection = new String[]{CategoriesTable.PRIMARY_KEY, CategoriesTable.COLUMN_CATEGORY_NAME, CategoriesTable.COLUMN_DESCRIPTION};
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        if (cursor.moveToFirst()) {
            do {
                c.add(getCategory(cursor));
            } while (cursor.moveToNext());
        }
        return c;
    }

    /**
     * @param cursor Cursor to get the information of a category from
     * @return Get the information from a Cursor into one category
     */
    private Category getCategory(Cursor cursor) {
        if (cursor != null) {
            String _id = cursor.getString(cursor.getColumnIndex(CategoriesTable.PRIMARY_KEY));
            String description = cursor.getString(cursor.getColumnIndex(CategoriesTable.COLUMN_DESCRIPTION));
            String name = cursor.getString(cursor.getColumnIndex(CategoriesTable.COLUMN_CATEGORY_NAME));
            return new Category(_id, description, name);
        } else {
            return null;
        }
    }

    /**
     * @param c Category used as filter.
     * @return A vector with all films whose category is c
     */
    @Override
    public Vector<Movie> fetchFilms(Category c) {
        // TODO: use ContentProvider (fix query)
        Vector<Movie> result = new Vector<>();
        Uri uri = Uri.parse(MoviesContentProvider.CONTENT_URI + "/JOIN");
        String[] projection = new String[]{KindTable.COLUMN_MOVIE_ID};
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        if (cursor.moveToFirst()) {
            do {
                result.add(fetchFilms(cursor.getString(cursor.getColumnIndex())));
            } while (cursor.moveToNext());
        }
        return result;
    }

    /**
     * @param c = Categories list used as filter.
     * @return A vector with all films whose category is in c
     */
    @Override
    public Vector<Movie> fetchFilms(Vector<Category> c) {
        Vector<Movie> result = new Vector<>();
        int count = c.size();
        for (int i = count - 1; i >= 0; i--) {
            result.addAll(fetchFilms(c.get(i)));
        }
        return result;
    }

    /**
     * @return A vector with all films from the database (it's possible with null)
     */
    @Override
    public Vector<Movie> fetchFilms() {
        // TODO: use ContentProvider
        Vector<Movie> c = new Vector<>();
        Cursor cursor;
        cursor = mDb.query(MoviesTable.TABLE_NAME, new String[]{MoviesTable.PRIMARY_KEY,
                        MoviesTable.COLUMN_TITLE, MoviesTable.COLUMN_PLOT, MoviesTable.COLUMN_DIRECTOR,
                        MoviesTable.COLUMN_IN_STOCK, MoviesTable.COLUMN_RENTED, MoviesTable.COLUMN_YEAR},
                null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                c.add(getFilm(cursor));
            } while (cursor.moveToNext());
        }
        return c;
    }

    /**
     * @param id Identifier from film to fetch.
     * @return The film asked
     */
    @Override
    public Movie fetchFilms(String id) {
        // TODO: use ContentProvider
        Cursor mCursor = mDb.query(MoviesTable.TABLE_NAME, new String[]{MoviesTable.PRIMARY_KEY,
                        MoviesTable.COLUMN_TITLE, MoviesTable.COLUMN_PLOT, MoviesTable.COLUMN_DIRECTOR,
                        MoviesTable.COLUMN_IN_STOCK, MoviesTable.COLUMN_RENTED, MoviesTable.COLUMN_YEAR},
                MoviesTable.PRIMARY_KEY + "=" + id, null, null, null, null, null);
        mCursor.moveToFirst();
        return getFilm(mCursor);
    }

    /**
     * @param cursor Cursor where to get the information
     * @return Get the information from a Cursor into a film
     */
    private Movie getFilm(Cursor cursor) {
        if (cursor != null) {
            String _id = cursor.getString(cursor.getColumnIndex(MoviesTable.PRIMARY_KEY));
            String title = cursor.getString(cursor.getColumnIndex(MoviesTable.COLUMN_TITLE));
            String plot = cursor.getString(cursor.getColumnIndex(MoviesTable.COLUMN_PLOT));
            String director = cursor.getString(cursor.getColumnIndex(MoviesTable.COLUMN_DIRECTOR));
            int in_stock = cursor.getInt(cursor.getColumnIndex(MoviesTable.COLUMN_IN_STOCK));
            int rented = cursor.getInt(cursor.getColumnIndex(MoviesTable.COLUMN_RENTED));
            int year = cursor.getInt(cursor.getColumnIndex(MoviesTable.COLUMN_YEAR));
            return new Movie(_id, title, plot, director, in_stock, rented, year);
        } else {
            return null;
        }
    }

    /**
     * @param id Identifier from category to delete.
     * @return true if the delete have been successfully
     */
    @Override
    public boolean deleteCategory(String id) {
        // TODO: use ContentProvider
        return mDb.delete(CategoriesTable.TABLE_NAME, CategoriesTable.PRIMARY_KEY + "=" + id, null) > 0;
    }

    /**
     * @param id Identifier from film to delete.
     * @return true if the delete have been successfully
     */
    @Override
    public boolean deleteMovies(String id) {
        // TODO: use ContentProvider
        return mDb.delete(MoviesTable.TABLE_NAME, MoviesTable.PRIMARY_KEY + "=" + id, null) > 0;
    }

    @Override
    public Vector<Boolean> deleteMovies(Category c) {
        // TODO: use ContentProvider, implement
        return null;
    }

    @Override
    public Vector<Boolean> deleteMovies(Vector<Category> c) {
        // TODO: use ContentProvider, implement
        return null;
    }

    @Override
    public boolean deleteKind(String id) {
        // TODO: use ContentProvider, implement
        return false;
    }

    /**
     * @param f Identifier from film whose relationship 'kind' be deleted.
     * @param c Identifier from category whose relationship 'kind' be deleted.
     * @return true if the delete have been successfully
     */
    @Override
    public boolean deleteKind(String f, String c) {
        // TODO: use ContentProvider, implement
        return false;
    }

    /**
     * @param newCategory Category replacing one with the same _id.
     * @return True if the update have been successfully else false
     */
    @Override
    public boolean updateCategory(Category newCategory) {
        // TODO: use ContentProvider
        if (newCategory != null && categoryComplete(newCategory)) {
            ContentValues args = new ContentValues();
            args.put(CategoriesTable.PRIMARY_KEY, newCategory._id);
            args.put(CategoriesTable.COLUMN_CATEGORY_NAME, newCategory.name);
            args.put(CategoriesTable.COLUMN_DESCRIPTION, newCategory.description);
            return mDb.update(CategoriesTable.TABLE_NAME, args,
                    CategoriesTable.PRIMARY_KEY + "=" + newCategory._id, null) > 0;
        } else {
            return false;
        }
    }

    /**
     * @param newMovie Film replacing one with the same _id.
     * @return True if the update have been successfully else false
     */
    @Override
    public boolean updateMovie(Movie newMovie) {
        // TODO: use ContentProvider
        if (newMovie != null && movieComplete(newMovie)) {
            ContentValues args = new ContentValues();
            args.put(MoviesTable.PRIMARY_KEY, newMovie._id);
            args.put(MoviesTable.COLUMN_TITLE, newMovie.name);
            args.put(MoviesTable.COLUMN_PLOT, newMovie.plot);
            args.put(MoviesTable.COLUMN_IN_STOCK, newMovie.in_stock);
            args.put(MoviesTable.COLUMN_RENTED, newMovie.rented);
            args.put(MoviesTable.COLUMN_DIRECTOR, newMovie.director);
            args.put(MoviesTable.COLUMN_YEAR, newMovie.year);
            return mDb.update(MoviesTable.TABLE_NAME, args, MoviesTable.PRIMARY_KEY + "=" + newMovie._id, null) > 0;
        } else {
            return false;
        }
    }

    /**
     * @param newMovie New film to be added.
     * @return True if the film newFilm is added successfully else false
     */
    @Override
    public boolean addMovie(Movie newMovie) {
        // TODO: handle insertion of movies without _id or with missing fields
        // TODO: use ContentProvider
        if (newMovie != null && movieComplete(newMovie)) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(MoviesTable.PRIMARY_KEY, newMovie._id);
            initialValues.put(MoviesTable.COLUMN_TITLE, newMovie.name);
            initialValues.put(MoviesTable.COLUMN_PLOT, newMovie.plot);
            initialValues.put(MoviesTable.COLUMN_IN_STOCK, newMovie.in_stock);
            initialValues.put(MoviesTable.COLUMN_RENTED, newMovie.rented);
            initialValues.put(MoviesTable.COLUMN_DIRECTOR, newMovie.director);
            initialValues.put(MoviesTable.COLUMN_YEAR, newMovie.year);
            return mDb.insert(MoviesTable.TABLE_NAME, null, initialValues) >= 0;
        } else {
            return false;
        }
    }

    /**
     * @param movie_o Movie to check
     * @return True if the object movie_o's parameters are complete and have a correct value,
     * False otherwise
     */
    private boolean movieComplete(Movie movie_o) {
        return movie_o._id != null && movie_o.name != null && movie_o.plot != null
                && movie_o.director != null && movie_o._id.length() > 0 && movie_o.name.length() > 0
                && movie_o.plot.length() > 0 && movie_o.in_stock >= 0 && movie_o.rented >= 0
                && movie_o.director.length() > 0 && movie_o.year >= 1900;
    }

    /**
     * @param newCategory New category to be added.
     * @return True if the category newCategory is added successfully else false
     */
    @Override
    public boolean addCategory(Category newCategory) {
        // TODO: use ContentProvider
        if (categoryComplete(newCategory)) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(CategoriesTable.PRIMARY_KEY, newCategory._id);
            initialValues.put(CategoriesTable.COLUMN_CATEGORY_NAME, newCategory.name);
            initialValues.put(CategoriesTable.COLUMN_DESCRIPTION, newCategory.description);
            return mDb.insert(CategoriesTable.TABLE_NAME, null, initialValues) >= 0;
        } else {
            return false;
        }
    }

    /**
     * @param category_o Category to check
     * @return True if the object category_o's parameters are complete and have a correct value,
     * False if not
     */
    private boolean categoryComplete(Category category_o) {
        // TODO: use ContentProvider
        return category_o._id != null && category_o.name != null && category_o.description != null
                && category_o._id.length() > 0 && category_o.name.length() > 0 && category_o.description.length() > 0;
    }

    /**
     * @param f film to link, c Category to link
     * @return True if the parameters are complete, have a correct value and a kind is added successfully
     * False if not
     */
    @Override
    public boolean addKind(String f, String c) {
        // TODO: use ContentProvider
        if (f != null && c != null && f.length() > 0 && c.length() > 0) {
            ContentValues initialValues = new ContentValues();
            initialValues.put(KindTable.COLUMN_MOVIE_ID, f);
            initialValues.put(KindTable.COLUMN_CATEGORY_ID, c);
            return (mDb.insert(KindTable.TABLE_NAME, null, initialValues) == 1);
        } else {
            return false;
        }
    }
}