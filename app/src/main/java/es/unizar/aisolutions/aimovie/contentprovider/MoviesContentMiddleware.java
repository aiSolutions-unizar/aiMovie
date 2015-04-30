package es.unizar.aisolutions.aimovie.contentprovider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.unizar.aisolutions.aimovie.data.Category;
import es.unizar.aisolutions.aimovie.data.Movie;
import es.unizar.aisolutions.aimovie.data.StoredMovie;
import es.unizar.aisolutions.aimovie.database.CategoriesTable;
import es.unizar.aisolutions.aimovie.database.KindTable;
import es.unizar.aisolutions.aimovie.database.MoviesTable;

/**
 * moviesContentMiddleware implements all needed methods to manage database.
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
     * @return A list with all categories from the database (it's possible with null)
     */
    @Override
    public List<Category> fetchCategories() {
        List<Category> result = new ArrayList<>();
        Uri uri = Uri.parse(MoviesContentProvider.CONTENT_URI + "/CATEGORIES");
        String[] projection = new String[]{CategoriesTable.PRIMARY_KEY, CategoriesTable.COLUMN_CATEGORY_NAME, CategoriesTable.COLUMN_DESCRIPTION};
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        if (cursor.moveToFirst()) {
            do {
                result.add(extractCategory(cursor));
            } while (cursor.moveToNext());
        }
        return result;
    }

    /**
     * @param c Category used as filter.
     * @return A list with all movies whose category is c
     */
    @Override
    public List<Movie> fetchMovies(Category c) {
        List<Movie> result = new ArrayList<>();
        Uri uri = Uri.parse(MoviesContentProvider.CONTENT_URI + "/KINDS");
        String[] projection = KindTable.AVAILABLE_COLUMNS.toArray(new String[0]);
        String selection = KindTable.COLUMN_CATEGORY_ID + " = " + c.get_id();
        String[] selectionArgs = null;
        String sortOrder = null;
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        if (cursor.moveToFirst()) {
            do {
                // TODO: optimize using a more complex query (in ContentProvider)
                result.add(fetchMovie(cursor.getString(cursor.getColumnIndex(KindTable.COLUMN_MOVIE_ID))));
            } while (cursor.moveToNext());
        }
        return result;
    }

    /**
     * @param categories = Categories list used as filter.
     * @return A list with all movies whose category is in c
     */
    @Override
    public List<Movie> fetchMovies(List<Category> categories) {
        // TODO: optimize using a more complex query
        List<Movie> result = new ArrayList<>();
        for (Category c : categories) {
            result.addAll(fetchMovies(c));
        }
        return result;
    }

    /**
     * @return A list with all movies from the database (it's possible with null)
     */
    @Override
    public List<Movie> fetchMovies() {
        List<Movie> c = new ArrayList<>();
        Uri uri = MoviesContentProvider.CONTENT_URI;
        String[] projection = MoviesTable.AVAILABLE_COLUMNS.toArray(new String[0]);
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        if (cursor.moveToFirst()) {
            do {
                c.add(extractMovie(cursor));
            } while (cursor.moveToNext());
        }
        return c;
    }

    /**
     * @param id Identifier from movie to fetch.
     * @return The movie asked
     */
    @Override
    public Movie fetchMovie(String id) {
        Uri uri = Uri.parse(MoviesContentProvider.CONTENT_URI + "/" + id);
        String[] projection = MoviesTable.AVAILABLE_COLUMNS.toArray(new String[0]);
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        cursor.moveToFirst();
        return extractMovie(cursor);
    }

    /**
     * @param newMovie New movie to be added.
     * @return True if the movie newmovie is added successfully else false
     */
    @Override
    public boolean addMovie(Movie newMovie) {
        Uri uri = MoviesContentProvider.CONTENT_URI;
        ContentValues values = new ContentValues();
        values.put(MoviesTable.COLUMN_TITLE, newMovie.getTitle());
        values.put(MoviesTable.COLUMN_YEAR, newMovie.getYear() == -1 ? null : newMovie.getYear());
        values.put(MoviesTable.COLUMN_RATED, newMovie.getRated());
        values.put(MoviesTable.COLUMN_RELEASED, newMovie.getReleased() != null ? newMovie.getReleased().getTime() : null);
        values.put(MoviesTable.COLUMN_RUNTIME, newMovie.getRuntime());
        values.put(MoviesTable.COLUMN_DIRECTOR, newMovie.getDirector());
        values.put(MoviesTable.COLUMN_WRITER, newMovie.getWriter());
        values.put(MoviesTable.COLUMN_PLOT, newMovie.getPlot());
        values.put(MoviesTable.COLUMN_LANGUAGE, newMovie.getLanguage());
        values.put(MoviesTable.COLUMN_COUNTRY, newMovie.getCountry());
        values.put(MoviesTable.COLUMN_AWARDS, newMovie.getAwards());
        values.put(MoviesTable.COLUMN_POSTER, newMovie.getPoster() != null ? newMovie.getPoster().toString() : null);
        values.put(MoviesTable.COLUMN_METASCORE, newMovie.getMetascore() == -1 ? null : newMovie.getMetascore());
        values.put(MoviesTable.COLUMN_IMDB_RATING, newMovie.getImdbRating() == -1 ? null : newMovie.getImdbRating());
        values.put(MoviesTable.COLUMN_IMDB_VOTES, newMovie.getImdbVotes() == -1 ? null : newMovie.getImdbVotes());
        values.put(MoviesTable.COLUMN_IMDB_ID, newMovie.getImdbID());
        Uri insertedUri = context.getContentResolver().insert(uri, values);
        return true;
    }

    /**
     * @param newCategory New category to be added.
     * @return True if the category newCategory is added successfully else false
     */
    @Override
    public boolean addCategory(Category newCategory) {
        if (check(newCategory)) {
            Uri uri = Uri.parse(MoviesContentProvider.CONTENT_URI + "/CATEGORIES");
            ContentValues values = new ContentValues();
            values.put(CategoriesTable.PRIMARY_KEY, newCategory.get_id());
            values.put(CategoriesTable.COLUMN_CATEGORY_NAME, newCategory.getName());
            values.put(CategoriesTable.COLUMN_DESCRIPTION, newCategory.getDescription());
            Uri insertedUri = context.getContentResolver().insert(uri, values);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param f movie to link
     * @param c Category to link
     * @return True if the parameters are complete, have a correct value and a kind is added successfully
     * False if not
     */
    @Override
    public boolean addKind(String f, String c) {
        if (f != null && c != null && f.length() > 0 && c.length() > 0) {
            Uri uri = Uri.parse(MoviesContentProvider.CONTENT_URI + "/KINDS");
            ContentValues values = new ContentValues();
            values.put(KindTable.COLUMN_MOVIE_ID, f);
            values.put(KindTable.COLUMN_CATEGORY_ID, c);
            Uri insertedUri = context.getContentResolver().insert(uri, values);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param c Category to delete.
     * @return true if the delete have been successfully
     */
    @Override
    public boolean deleteCategory(Category c) {
        Uri uri = Uri.parse(MoviesContentProvider.CONTENT_URI + "/CATEGORY/" + c.get_id());
        String selection = null;
        String[] selectionArgs = null;
        int rowsDeleted = context.getContentResolver().delete(uri, selection, selectionArgs);
        return rowsDeleted > 0;
    }

    /**
     * @param m Movie to delete.
     * @return true if the delete have been successfully
     */
    @Override
    public boolean deleteMovie(Movie m) {
        Uri uri = Uri.parse(MoviesContentProvider.CONTENT_URI + "/" + m.get_id());
        String selection = null;
        String[] selectionArgs = null;
        int rowsDeleted = context.getContentResolver().delete(uri, selection, selectionArgs);
        return rowsDeleted > 0;
    }

    @Override
    public List<Boolean> deleteMovies(Category c) {
        // TODO: use ContentProvider, implement
        return null;
    }

    @Override
    public List<Boolean> deleteMovies(List<Category> c) {
        // TODO: use ContentProvider, implement
        return null;
    }

    @Override
    public boolean deleteKind(String id) {
        // TODO: use ContentProvider, implement
        return false;
    }

    /**
     * @param f Identifier from movie whose relationship 'kind' be deleted.
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
        if (newCategory != null && check(newCategory)) {
            Uri uri = Uri.parse(MoviesContentProvider.CONTENT_URI + "/CATEGORY/" + newCategory.get_id());
            ContentValues values = new ContentValues();
            values.put(CategoriesTable.PRIMARY_KEY, newCategory.get_id());
            values.put(CategoriesTable.COLUMN_CATEGORY_NAME, newCategory.getName());
            values.put(CategoriesTable.COLUMN_DESCRIPTION, newCategory.getDescription());
            String where = null;
            String[] selectionArgs = null;
            int rowsUpdated = context.getContentResolver().update(uri, values, where, selectionArgs);
            return rowsUpdated > 0;
        } else {
            return false;
        }
    }

    /**
     * @param newMovie Movie replacing one with the same _id.
     * @return True if the update have been successfully else false
     */
    @Override
    public boolean updateMovie(Movie newMovie) {
        Uri uri = Uri.parse(MoviesContentProvider.CONTENT_URI + "/MOVIE/" + newMovie.get_id());
        ContentValues values = new ContentValues();
        values.put(MoviesTable.PRIMARY_KEY, newMovie.get_id());
        values.put(MoviesTable.COLUMN_TITLE, newMovie.getTitle());
        values.put(MoviesTable.COLUMN_YEAR, newMovie.getYear() == -1 ? null : newMovie.getYear());
        values.put(MoviesTable.COLUMN_RATED, newMovie.getRated());
        values.put(MoviesTable.COLUMN_RELEASED, newMovie.getReleased() != null ? newMovie.getReleased().getTime() : null);
        values.put(MoviesTable.COLUMN_RUNTIME, newMovie.getRuntime());
        values.put(MoviesTable.COLUMN_DIRECTOR, newMovie.getDirector());
        values.put(MoviesTable.COLUMN_WRITER, newMovie.getWriter());
        values.put(MoviesTable.COLUMN_PLOT, newMovie.getPlot());
        values.put(MoviesTable.COLUMN_LANGUAGE, newMovie.getLanguage());
        values.put(MoviesTable.COLUMN_COUNTRY, newMovie.getCountry());
        values.put(MoviesTable.COLUMN_AWARDS, newMovie.getAwards());
        values.put(MoviesTable.COLUMN_POSTER, newMovie.getPoster() != null ? newMovie.getPoster().toString() : null);
        values.put(MoviesTable.COLUMN_METASCORE, newMovie.getMetascore() == -1 ? null : newMovie.getMetascore());
        values.put(MoviesTable.COLUMN_IMDB_RATING, newMovie.getImdbRating() == -1 ? null : newMovie.getImdbRating());
        values.put(MoviesTable.COLUMN_IMDB_VOTES, newMovie.getImdbVotes() == -1 ? null : newMovie.getImdbVotes());
        values.put(MoviesTable.COLUMN_IMDB_ID, newMovie.getImdbID());
        String where = null;
        String[] selectionArgs = null;
        int rowsUpdated = context.getContentResolver().update(uri, values, where, selectionArgs);
        return rowsUpdated > 0;
    }

    /**
     * @param cursor Cursor where to get the information
     * @return Get the information from a Cursor into a movie
     */
    private Movie extractMovie(Cursor cursor) {
        long _id = cursor.getLong(cursor.getColumnIndex(MoviesTable.PRIMARY_KEY));
        String title = cursor.getString(cursor.getColumnIndex(MoviesTable.COLUMN_TITLE));
        int year = cursor.getInt(cursor.getColumnIndex(MoviesTable.COLUMN_YEAR));
        year = cursor.isNull(cursor.getColumnIndex(MoviesTable.COLUMN_YEAR)) ? -1 : year;
        String rated = cursor.getString(cursor.getColumnIndex(MoviesTable.COLUMN_RATED));
        Date released = new Date(cursor.getInt(cursor.getColumnIndex(MoviesTable.COLUMN_RELEASED)));
        String runtime = cursor.getString(cursor.getColumnIndex(MoviesTable.COLUMN_RUNTIME));
        List<String> genres = null;
        String director = cursor.getString(cursor.getColumnIndex(MoviesTable.COLUMN_DIRECTOR));
        String writer = cursor.getString(cursor.getColumnIndex(MoviesTable.COLUMN_WRITER));
        List<String> actors = null;
        String plot = cursor.getString(cursor.getColumnIndex(MoviesTable.COLUMN_PLOT));
        String language = cursor.getString(cursor.getColumnIndex(MoviesTable.COLUMN_LANGUAGE));
        String country = cursor.getString(cursor.getColumnIndex(MoviesTable.COLUMN_COUNTRY));
        String awards = cursor.getString(cursor.getColumnIndex(MoviesTable.COLUMN_AWARDS));
        String link = cursor.getString(cursor.getColumnIndex(MoviesTable.COLUMN_POSTER));
        URL poster = null;
        try {
            poster = new URL(link);
        } catch (MalformedURLException e) {
        }
        int metascore = cursor.getInt(cursor.getColumnIndex(MoviesTable.COLUMN_METASCORE));
        float imdb_rating = cursor.getFloat(cursor.getColumnIndex(MoviesTable.COLUMN_IMDB_RATING));
        int imdb_votes = cursor.getInt(cursor.getColumnIndex(MoviesTable.COLUMN_IMDB_VOTES));
        String imdb_id = cursor.getString(cursor.getColumnIndex(MoviesTable.COLUMN_IMDB_ID));
        int stock = cursor.getInt(cursor.getColumnIndex(MoviesTable.COLUMN_STOCK));
        int rented = cursor.getInt(cursor.getColumnIndex(MoviesTable.COLUMN_RENTED));
        return new StoredMovie(_id, title, year, rated, released, runtime, genres, director,
                writer, actors, plot, language, country, awards, poster, metascore, imdb_rating,
                imdb_votes, imdb_id);
    }

    /**
     * @param cursor Cursor to get the information of a category from
     * @return Get the information from a Cursor into one category
     */
    private Category extractCategory(Cursor cursor) {
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
     * @param category_o Category to check
     * @return True if the object category_o's parameters are complete and have a correct value,
     * False if not
     */
    private boolean check(Category category_o) {
        return category_o.get_id() != null && category_o.getName() != null && category_o.getDescription() != null
                && category_o.get_id().length() > 0 && category_o.getName().length() > 0 && category_o.getDescription().length() > 0;
    }
}