package es.unizar.aisolutions.aimovie.contentprovider;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.os.RemoteException;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import es.unizar.aisolutions.aimovie.data.Genre;
import es.unizar.aisolutions.aimovie.data.Movie;
import es.unizar.aisolutions.aimovie.data.StoredMovie;
import es.unizar.aisolutions.aimovie.database.GenresTable;
import es.unizar.aisolutions.aimovie.database.KindTable;
import es.unizar.aisolutions.aimovie.database.MoviesTable;

public class MovieManager {
    private Context context;

    /**
     * Constructor
     *
     * @param context the Context within which to work
     */
    public MovieManager(Context context) {
        this.context = context;
    }

    /**
     * @param cursor Cursor to get the information of a genre from
     * @return Get the information from a Cursor into one genre
     */
    public static Genre extractGenre(Cursor cursor) {
        if (cursor != null) {
            long _id = cursor.getLong(cursor.getColumnIndex(GenresTable.PRIMARY_KEY));
            String name = cursor.getString(cursor.getColumnIndex(GenresTable.COLUMN_GENRE_NAME));
            return new Genre(_id, name);
        } else {
            return null;
        }
    }

    /**
     * @param _id Identifier of movie to fetch.
     * @return The movie asked
     */
    public Movie fetchMovie(long _id) {
        Uri uri = Uri.parse(MoviesContentProvider.CONTENT_URI + "/" + _id);
        String[] projection = MoviesTable.AVAILABLE_COLUMNS.toArray(new String[0]);
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        if (cursor.moveToFirst()) {
            return extractMovie(cursor);
        } else {
            return null;
        }
    }

    /**
     * @return A list with all movies from the database (it's possible with null)
     */
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
     * @param g Genre used as filter.
     * @return A list with all movies whose genre is c
     */
    public List<Movie> fetchMovies(Genre g) {
        List<Movie> result = new ArrayList<>();
        Uri uri = Uri.parse(MoviesContentProvider.CONTENT_URI + "/GENRES/" + g.get_id() + "/MOVIES");
        String[] projection = MoviesTable.AVAILABLE_COLUMNS.toArray(new String[0]);
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        if (cursor.moveToFirst()) {
            do {
                result.add(extractMovie(cursor));
            } while (cursor.moveToNext());
        }
        return result;
    }

    /**
     * @param name Name of the genre to fetch
     * @return A genre object which represents the genre 'name'
     */
    public Genre fetchGenre(String name) {
        Uri uri = Uri.parse(MoviesContentProvider.CONTENT_URI + "/GENRES");
        String[] projection = GenresTable.AVAILABLE_COLUMNS.toArray(new String[0]);
        String selection = GenresTable.COLUMN_GENRE_NAME + " = ?";
        String[] selectionArgs = new String[]{name};
        String sortOrder = null;
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        if (cursor.moveToFirst()) {
            Genre g = extractGenre(cursor);
            return g;
        } else {
            return null;
        }
    }

    /**
     * @return A list with all genres from the database (may be null)
     */
    public List<Genre> fetchGenres() {
        List<Genre> result = new ArrayList<>();
        Uri uri = Uri.parse(MoviesContentProvider.CONTENT_URI + "/GENRES");
        String[] projection = GenresTable.AVAILABLE_COLUMNS.toArray(new String[0]);
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        if (cursor.moveToFirst()) {
            do {
                result.add(extractGenre(cursor));
            } while (cursor.moveToNext());
        }
        return result;
    }

    /**
     * @param newMovie New movie to be added.
     * @return True if the movie newmovie is added successfully else false
     */
    public boolean addMovie(Movie newMovie) {
        if (!newMovie.getTitle().isEmpty()) {
            ArrayList<ContentProviderOperation> operations = new ArrayList<>();
            Uri uriMovie = MoviesContentProvider.CONTENT_URI;
            Uri uriKind = Uri.parse(MoviesContentProvider.CONTENT_URI + "/KINDS");
            ContentValues valuesMovie = new ContentValues();
            valuesMovie.put(MoviesTable.COLUMN_TITLE, newMovie.getTitle());
            valuesMovie.put(MoviesTable.COLUMN_YEAR, newMovie.getYear() == -1 ? null : newMovie.getYear());
            valuesMovie.put(MoviesTable.COLUMN_RATED, newMovie.getRated());
            valuesMovie.put(MoviesTable.COLUMN_RELEASED, newMovie.getReleased() != null ? newMovie.getReleased().getTime() : null);
            valuesMovie.put(MoviesTable.COLUMN_RUNTIME, newMovie.getRuntime());
            valuesMovie.put(MoviesTable.COLUMN_DIRECTOR, newMovie.getDirector());
            valuesMovie.put(MoviesTable.COLUMN_WRITER, newMovie.getWriter());
            valuesMovie.put(MoviesTable.COLUMN_PLOT, newMovie.getPlot());
            valuesMovie.put(MoviesTable.COLUMN_LANGUAGE, newMovie.getLanguage());
            valuesMovie.put(MoviesTable.COLUMN_COUNTRY, newMovie.getCountry());
            valuesMovie.put(MoviesTable.COLUMN_AWARDS, newMovie.getAwards());
            valuesMovie.put(MoviesTable.COLUMN_POSTER, newMovie.getPoster() != null ? newMovie.getPoster().toString() : null);
            valuesMovie.put(MoviesTable.COLUMN_METASCORE, newMovie.getMetascore() == -1 ? null : newMovie.getMetascore());
            valuesMovie.put(MoviesTable.COLUMN_IMDB_RATING, newMovie.getImdbRating() == -1 ? null : newMovie.getImdbRating());
            valuesMovie.put(MoviesTable.COLUMN_IMDB_VOTES, newMovie.getImdbVotes() == -1 ? null : newMovie.getImdbVotes());
            valuesMovie.put(MoviesTable.COLUMN_IMDB_ID, newMovie.getImdbID());

            ContentProviderOperation movieAddition = ContentProviderOperation.newInsert(uriMovie)
                    .withValues(valuesMovie).build();
            operations.add(movieAddition);
            for (Genre g : newMovie.getGenres()) {
                ContentProviderOperation kindAddition = ContentProviderOperation.newInsert(uriKind)
                        .withValue(KindTable.COLUMN_GENRE_ID, g.get_id())
                        .withValueBackReference(KindTable.COLUMN_MOVIE_ID, 0).build();
                operations.add(kindAddition);
            }

            try {
                context.getContentResolver().applyBatch(MoviesContentProvider.AUTHORITY, operations);
            } catch (SQLiteConstraintException e) {
                Log.d(e.getMessage(), e.toString(), e);
                return false;
            } catch (RemoteException | OperationApplicationException e) {
                Log.e(e.getMessage(), e.toString(), e);
                return false;
            }
            return true;
        }

        else {
            return false;
        }
    }

    /**
     * @param m Movie to delete.
     * @return true if the deletion has been successful
     */
    public boolean deleteMovie(Movie m) {
        Uri uri = Uri.parse(MoviesContentProvider.CONTENT_URI + "/" + m.get_id());
        String selection = null;
        String[] selectionArgs = null;
        int rowsDeleted = context.getContentResolver().delete(uri, selection, selectionArgs);
        return rowsDeleted > 0;
    }

    /**
     * @param newMovie Movie replacing one with the same _id.
     * @return True if the update have been successfully else false
     */
    public boolean updateMovie(Movie newMovie) {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        Uri uriMovie = Uri.parse(MoviesContentProvider.CONTENT_URI + "/" + newMovie.get_id());
        Uri uriKind = Uri.parse(MoviesContentProvider.CONTENT_URI + "/KINDS");
        Uri uriMovieKinds = Uri.parse(MoviesContentProvider.CONTENT_URI + "/" + newMovie.get_id() + "/GENRES");

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
        values.put(MoviesTable.COLUMN_STOCK, newMovie.getStock());
        values.put(MoviesTable.COLUMN_RENTED, newMovie.getRented());

        ContentProviderOperation movieUpdate = ContentProviderOperation.newUpdate(uriMovie)
                .withValues(values).build();
        operations.add(movieUpdate);
        ContentProviderOperation kindsDeletion = ContentProviderOperation.newDelete(uriMovieKinds).build();
        operations.add(kindsDeletion);
        for (Genre g : newMovie.getGenres()) {
            ContentProviderOperation kindAddition = ContentProviderOperation.newInsert(uriKind)
                    .withValue(KindTable.COLUMN_MOVIE_ID, newMovie.get_id())
                    .withValue(KindTable.COLUMN_GENRE_ID, g.get_id()).build();
            operations.add(kindAddition);
        }

        try {
            context.getContentResolver().applyBatch(MoviesContentProvider.AUTHORITY, operations);
        } catch (RemoteException | OperationApplicationException e) {
            Log.e(e.getMessage(), e.toString(), e);
        }
        return true;
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

        Uri uriGenres = Uri.parse(MoviesContentProvider.CONTENT_URI + "/" + _id + "/GENRES");
        String[] projectionGenres = GenresTable.AVAILABLE_COLUMNS.toArray(new String[0]);
        String selectionGenres = null;
        String[] selectionArgsGenres = null;
        String sortOrderGenres = null;
        Cursor cursorGenres = context.getContentResolver().query(uriGenres, projectionGenres, selectionGenres, selectionArgsGenres, sortOrderGenres);
        List<Genre> genres = new ArrayList<>();
        if (cursorGenres != null && cursorGenres.moveToFirst()) {
            do {
                Genre g = extractGenre(cursorGenres);
                genres.add(g);
            } while (cursorGenres.moveToNext());
        }

        return new StoredMovie(_id, title, year, rated, released, runtime, genres, director,
                writer, actors, plot, language, country, awards, poster, metascore, imdb_rating,
                imdb_votes, imdb_id, stock, rented);
    }
}