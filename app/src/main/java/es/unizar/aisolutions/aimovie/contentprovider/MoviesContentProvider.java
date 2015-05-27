package es.unizar.aisolutions.aimovie.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

import es.unizar.aisolutions.aimovie.database.GenresTable;
import es.unizar.aisolutions.aimovie.database.KindTable;
import es.unizar.aisolutions.aimovie.database.MoviesDatabaseHelper;
import es.unizar.aisolutions.aimovie.database.MoviesTable;

public class MoviesContentProvider extends ContentProvider {
    public static final String AUTHORITY = "es.unizar.aisolutions.aimovie.contentprovider";
    public static final String MOVIE_GENRES_LIST = "genres_list";
    private static final int MOVIES = 1;
    private static final int MOVIE_ID = 2;
    private static final int GENRES = 3;
    private static final int KINDS = 4;
    private static final int GENRE_ID = 5;
    private static final int MOVIE_GENRES = 6;
    private static final int GENRE_MOVIES = 7;
    private static final String BASE_PATH = "MOVIES";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, MOVIES);                           // To manage all movies
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", MOVIE_ID);                  // To manage a movie
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#/GENRES", MOVIE_GENRES);       // To manage all the genres a movie belongs to
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/GENRES", GENRES);               // To manage all genres
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/KINDS", KINDS);                 // To manage relationships between movies & genres
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/GENRE/#", GENRE_ID);            // To manage a genre
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/GENRE/#/MOVIES", GENRE_MOVIES); // To manage all movies that belong to a given genre
    }

    private MoviesDatabaseHelper database;

    public MoviesContentProvider() {
    }

    public MoviesContentProvider(Context context) {
        database = new MoviesDatabaseHelper(context);
    }

    @Override
    public boolean onCreate() {
        database = new MoviesDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        //checkColumns(projection, sURIMatcher.match(uri));
        switch (sURIMatcher.match(uri)) {
            case MOVIES:
                // workaround to return a comma-separated list of every movie's genres
                // TODO: easier to duplicate genres list on each movie record
                String subquery = String.format(
                        "SELECT GROUP_CONCAT(%s.%s, ', ') AS %s, %s.%s " +
                                "FROM %s INNER JOIN %s ON %s.%s = %s.%s " +
                                "GROUP BY %s.%s",
                        GenresTable.TABLE_NAME, GenresTable.COLUMN_GENRE_NAME, MOVIE_GENRES_LIST,
                        KindTable.TABLE_NAME, KindTable.COLUMN_MOVIE_ID, GenresTable.TABLE_NAME,
                        KindTable.TABLE_NAME, GenresTable.TABLE_NAME, GenresTable.PRIMARY_KEY,
                        KindTable.TABLE_NAME, KindTable.COLUMN_GENRE_ID, KindTable.TABLE_NAME,
                        KindTable.COLUMN_MOVIE_ID);
                queryBuilder.setTables(String.format(
                        "%s INNER JOIN (%s) genres ON %s.%s = genres.%s",
                        MoviesTable.TABLE_NAME, subquery, MoviesTable.TABLE_NAME, MoviesTable.PRIMARY_KEY,
                        KindTable.COLUMN_MOVIE_ID));
                break;
            case MOVIE_ID:
                subquery = String.format(
                        "SELECT GROUP_CONCAT(%s.%s, ', ') AS %s, %s.%s " +
                                "FROM %s INNER JOIN %s ON %s.%s = %s.%s " +
                                "GROUP BY %s.%s",
                        GenresTable.TABLE_NAME, GenresTable.COLUMN_GENRE_NAME, MOVIE_GENRES_LIST,
                        KindTable.TABLE_NAME, KindTable.COLUMN_MOVIE_ID, GenresTable.TABLE_NAME,
                        KindTable.TABLE_NAME, GenresTable.TABLE_NAME, GenresTable.PRIMARY_KEY,
                        KindTable.TABLE_NAME, KindTable.COLUMN_GENRE_ID, KindTable.TABLE_NAME,
                        KindTable.COLUMN_MOVIE_ID);
                queryBuilder.setTables(String.format(
                        "%s INNER JOIN (%s) genres ON %s.%s = genres.%s",
                        MoviesTable.TABLE_NAME, subquery, MoviesTable.TABLE_NAME, MoviesTable.PRIMARY_KEY,
                        KindTable.COLUMN_MOVIE_ID));
                queryBuilder.appendWhere(MoviesTable.PRIMARY_KEY + " = " + uri.getLastPathSegment());
                break;
            case MOVIE_GENRES:
                for (int i = 0; i < projection.length; i++) {
                    // support for subqueries
                    if (projection[i].charAt(0) != '(') {
                        projection[i] = GenresTable.TABLE_NAME + "." + projection[i];
                    }
                }
                queryBuilder.setTables(String.format("%s INNER JOIN %s ON %s.%s = %s.%s",
                        KindTable.TABLE_NAME, GenresTable.TABLE_NAME, KindTable.TABLE_NAME, KindTable.COLUMN_GENRE_ID,
                        GenresTable.TABLE_NAME, GenresTable.PRIMARY_KEY));
                queryBuilder.appendWhere(String.format("%s.%s = %s",
                        KindTable.TABLE_NAME, KindTable.COLUMN_MOVIE_ID, uri.getPathSegments().get(1)));
                break;
            case GENRES:
                queryBuilder.setTables(GenresTable.TABLE_NAME);
                break;
            case KINDS:
                queryBuilder.setTables(KindTable.TABLE_NAME);
                break;
            case GENRE_ID:
                queryBuilder.setTables(GenresTable.TABLE_NAME);
                queryBuilder.appendWhere(GenresTable.PRIMARY_KEY + " = " + uri.getLastPathSegment());
                break;
            case GENRE_MOVIES:
                for (int i = 0; i < projection.length; i++) {
                    // fix
                    if (projection[i].charAt(0) != '(' && !projection[i].equals(MOVIE_GENRES_LIST)) {
                        projection[i] = MoviesTable.TABLE_NAME + "." + projection[i];
                    }
                }
                subquery = String.format(
                        "SELECT GROUP_CONCAT(%s.%s, ', ') AS %s, %s.%s " +
                                "FROM %s INNER JOIN %s ON %s.%s = %s.%s " +
                                "GROUP BY %s.%s",
                        GenresTable.TABLE_NAME, GenresTable.COLUMN_GENRE_NAME, MOVIE_GENRES_LIST,
                        KindTable.TABLE_NAME, KindTable.COLUMN_MOVIE_ID, GenresTable.TABLE_NAME,
                        KindTable.TABLE_NAME, GenresTable.TABLE_NAME, GenresTable.PRIMARY_KEY,
                        KindTable.TABLE_NAME, KindTable.COLUMN_GENRE_ID, KindTable.TABLE_NAME,
                        KindTable.COLUMN_MOVIE_ID);
                queryBuilder.setTables(String.format(
                        "%s INNER JOIN (%s) genres ON %s.%s = genres.%s " +
                                "INNER JOIN %s ON %s.%s = %s.%s",
                        MoviesTable.TABLE_NAME, subquery, MoviesTable.TABLE_NAME, MoviesTable.PRIMARY_KEY,
                        KindTable.COLUMN_MOVIE_ID, KindTable.TABLE_NAME, KindTable.TABLE_NAME, KindTable.COLUMN_MOVIE_ID,
                        MoviesTable.TABLE_NAME, MoviesTable.PRIMARY_KEY));
                /*queryBuilder.setTables(String.format("%s INNER JOIN %s ON %s.%s = %s.%s",
                        KindTable.TABLE_NAME, MoviesTable.TABLE_NAME, KindTable.TABLE_NAME, KindTable.COLUMN_MOVIE_ID,
                        MoviesTable.TABLE_NAME, MoviesTable.PRIMARY_KEY));*/
                queryBuilder.appendWhere(String.format("%s.%s = %s",
                        KindTable.TABLE_NAME, KindTable.COLUMN_GENRE_ID, uri.getPathSegments().get(2)));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        SQLiteDatabase db = database.getReadableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = database.getWritableDatabase();
        long id;
        switch (sURIMatcher.match(uri)) {
            case MOVIES:
                id = db.insertOrThrow(MoviesTable.TABLE_NAME, null, values);
                break;
            case KINDS:
                id = db.insertOrThrow(KindTable.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Uknown URI: " + uri);
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted = 0;
        SQLiteDatabase db = database.getWritableDatabase();
        switch (sURIMatcher.match(uri)) {
            case MOVIES:
                rowsDeleted = db.delete(MoviesTable.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_ID:
                String id = uri.getLastPathSegment();
                if (!TextUtils.isEmpty(id)) {
                    rowsDeleted = db.delete(MoviesTable.TABLE_NAME, MoviesTable.PRIMARY_KEY + " = " + id, null);
                }
                break;
            case MOVIE_GENRES:
                String movieId = uri.getPathSegments().get(1);
                if (!TextUtils.isEmpty(movieId)) {
                    rowsDeleted = db.delete(KindTable.TABLE_NAME, KindTable.COLUMN_MOVIE_ID + " = " + movieId, null);
                }
                break;
            default:
                throw new IllegalArgumentException("Uknown URI: " + uri);
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int rowsUpdated = 0;
        SQLiteDatabase db = database.getWritableDatabase();
        switch (sURIMatcher.match(uri)) {
            case MOVIES:
                rowsUpdated = db.update(MoviesTable.TABLE_NAME, values, selection, selectionArgs);
                break;
            case MOVIE_ID:
                String id = uri.getLastPathSegment();
                if (!TextUtils.isEmpty(id)) {
                    rowsUpdated = db.update(MoviesTable.TABLE_NAME, values, MoviesTable.PRIMARY_KEY + " = " + id, null);
                }
                break;
            default:
                throw new IllegalArgumentException("Uknown URI: " + uri);
        }
        if (getContext() != null) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    private void checkColumns(String[] projection, int uriType) {
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<>(Arrays.asList(projection));
            switch (uriType) {
                case MOVIE_ID:
                    if (!MoviesTable.AVAILABLE_COLUMNS.containsAll(requestedColumns)) {
                        throw new IllegalArgumentException("Unknown columns in projection");
                    }
                    break;
                case MOVIES:
                    if (!MoviesTable.AVAILABLE_COLUMNS.containsAll(requestedColumns)) {
                        throw new IllegalArgumentException("Unknown columns in projection");
                    }
                    break;
                case GENRES:
                    if (!GenresTable.AVAILABLE_COLUMNS.containsAll(requestedColumns)) {
                        throw new IllegalArgumentException("Unknown columns in projection");
                    }
                    break;
                case KINDS:
                    if (!KindTable.AVAILABLE_COLUMNS.containsAll(requestedColumns)) {
                        throw new IllegalArgumentException("Unknown columns in projection");
                    }
                    break;
                case GENRE_ID:
                    if (!GenresTable.AVAILABLE_COLUMNS.containsAll(requestedColumns)) {
                        throw new IllegalArgumentException("Unknown columns in projection");
                    }
                    break;
                case MOVIE_GENRES:
                    if (!GenresTable.AVAILABLE_COLUMNS.containsAll(requestedColumns)) {
                        throw new IllegalArgumentException("Unknown columns in projection");
                    }
                    break;
                case GENRE_MOVIES:
                    if (!MoviesTable.AVAILABLE_COLUMNS.containsAll(requestedColumns)) {
                        throw new IllegalArgumentException("Unknown columns in projection");
                    }
            }
        }
    }
}
