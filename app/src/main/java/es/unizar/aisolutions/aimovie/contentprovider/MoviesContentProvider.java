package es.unizar.aisolutions.aimovie.contentprovider;

import android.content.ContentProvider;
import android.content.ContentResolver;
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

/**
 * moviesContentProvider manage access to our structured set of data.
 * They encapsulate the data, and provide mechanisms for defining data security.
 * <p/>
 * Created by diego on 18/04/15.
 * Time spent: 60 minutes.
 */
public class MoviesContentProvider extends ContentProvider {
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/movies";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/movie";
    public static final String AUTHORITY = "es.unizar.aisolutions.aimovie.contentprovider";
    private static final int MOVIE_ID = 1;
    private static final int MOVIES = 2;
    private static final int GENRES = 3;
    private static final int JOIN = 5;
    private static final int KINDS = 6;
    private static final int GENRE_ID = 7;
    private static final int MOVIE_GENRES = 8;
    private static final String BASE_PATH = "MOVIES";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, MOVIES);                       // To manage all movies
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", MOVIE_ID);              // To manage a movie
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#/GENRES", MOVIE_GENRES);   // To manage all the genres a movie belongs to
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/GENRES", GENRES);           // To manage all genres
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/JOIN", JOIN);               // To manage all genres & movies together
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/KINDS", KINDS);             // To manage relationships between movies & genres
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/GENRE/#", GENRE_ID);        // To manage a genre
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
        checkColumns(projection, sURIMatcher.match(uri));
        switch (sURIMatcher.match(uri)) {
            case MOVIES:
                queryBuilder.setTables(MoviesTable.TABLE_NAME);
                break;
            case MOVIE_ID:
                queryBuilder.setTables(MoviesTable.TABLE_NAME);
                queryBuilder.appendWhere(MoviesTable.PRIMARY_KEY + " = " + uri.getLastPathSegment());
                break;
            case MOVIE_GENRES:
                queryBuilder.setTables(String.format("%s INNER JOIN %s ON %s.%s = %s.%s",
                        KindTable.TABLE_NAME, GenresTable.TABLE_NAME, KindTable.TABLE_NAME, KindTable.PRIMARY_KEY,
                        GenresTable.TABLE_NAME, GenresTable.PRIMARY_KEY));
                queryBuilder.appendWhere(String.format("%s.%s = %s",
                        KindTable.TABLE_NAME, KindTable.COLUMN_MOVIE_ID, uri.getPathSegments().get(1)));
                for (int i = 0; i < projection.length; i++) {
                    projection[i] = GenresTable.TABLE_NAME + "." + projection[i];
                }
                break;
            case GENRES:
                queryBuilder.setTables(GenresTable.TABLE_NAME);
                break;
            case JOIN:
                queryBuilder.setTables(MoviesTable.TABLE_NAME + "," + GenresTable.TABLE_NAME);
                break;
            case KINDS:
                queryBuilder.setTables(KindTable.TABLE_NAME);
                break;
            case GENRE_ID:
                queryBuilder.setTables(GenresTable.TABLE_NAME);
                queryBuilder.appendWhere(GenresTable.PRIMARY_KEY + " = " + uri.getLastPathSegment());
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
            case GENRES:
                id = db.insertOrThrow(GenresTable.TABLE_NAME, null, values);
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
            case GENRE_ID:
                String category_id = uri.getLastPathSegment();
                if (!TextUtils.isEmpty(category_id)) {
                    rowsDeleted = db.delete(GenresTable.TABLE_NAME, GenresTable.PRIMARY_KEY + " = " + category_id, null);
                    rowsDeleted += db.delete(KindTable.TABLE_NAME, KindTable.COLUMN_GENRE_ID + " = " + category_id, null);
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
            case GENRE_ID:
                String category_id = uri.getLastPathSegment();
                if (!TextUtils.isEmpty(category_id)) {
                    rowsUpdated = db.update(GenresTable.TABLE_NAME, values, GenresTable.PRIMARY_KEY + " = " + category_id, null);
                    rowsUpdated += db.update(KindTable.TABLE_NAME, values, KindTable.COLUMN_GENRE_ID + " = " + category_id, null);
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
            if (uriType == GENRES && !GenresTable.AVAILABLE_COLUMNS.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            } else if (uriType == JOIN && !KindTable.AVAILABLE_COLUMNS.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            } else if ((uriType == MOVIE_ID || uriType == MOVIES) && !MoviesTable.AVAILABLE_COLUMNS.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}
