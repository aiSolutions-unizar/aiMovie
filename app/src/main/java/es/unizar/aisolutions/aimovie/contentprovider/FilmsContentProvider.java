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

import es.unizar.aisolutions.aimovie.database.CategoriesTable;
import es.unizar.aisolutions.aimovie.database.FilmsDatabaseHelper;
import es.unizar.aisolutions.aimovie.database.FilmsTable;
import es.unizar.aisolutions.aimovie.database.KindTable;

/**
 * FilmsContentProvider manage access to our structured set of data.
 * They encapsulate the data, and provide mechanisms for defining data security.
 * <p/>
 * Created by diego on 18/04/15.
 * Time spent: 60 minutes.
 */
public class FilmsContentProvider extends ContentProvider {
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/films";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/film";
    private static final int FILM_ID = 1;
    private static final int FILMS = 2;
    private static final int CATEGORIES = 3;
    private static final int JOIN = 5;
    private static final int KINDS = 6;
    private static final int CATEGORY_ID = 7;
    private static final String AUTHORITY = "es.unizar.aisolutions.aimovie.contentprovider";
    private static final String BASE_PATH = "FILMS";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, FILMS);  // To manage all films
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", FILM_ID);  // To manage a film
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/CATEGORIES", CATEGORIES);  // To manage all categories
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/JOIN", JOIN); // To manage all categories & films together
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/KINDS", KINDS); // To manage relationship between films & categories
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/CATEGORY/#", CATEGORY_ID); // To manage a category

    }

    private FilmsDatabaseHelper database;

    public FilmsContentProvider() {
    }

    public FilmsContentProvider(Context context) {
        database = new FilmsDatabaseHelper(context);
    }

    @Override
    public boolean onCreate() {
        database = new FilmsDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        checkColumns(projection, sURIMatcher.match(uri));
        Cursor cursor;
        switch (sURIMatcher.match(uri)) {
            case FILMS:
                queryBuilder.setTables(FilmsTable.TABLE_NAME);
                cursor = queryBuilder.query(
                        database.getReadableDatabase(),
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case FILM_ID:
                queryBuilder.setTables(FilmsTable.TABLE_NAME);
                queryBuilder.appendWhere(FilmsTable.PRIMARY_KEY + " = " + uri.getLastPathSegment());
                cursor = queryBuilder.query(
                        database.getReadableDatabase(),
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CATEGORIES:
                queryBuilder.setTables(CategoriesTable.TABLE_NAME);
                cursor = queryBuilder.query(
                        database.getReadableDatabase(),
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case JOIN:
                queryBuilder.setTables(FilmsTable.TABLE_NAME + "," + CategoriesTable.TABLE_NAME);
                cursor = queryBuilder.query(
                        database.getReadableDatabase(),
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
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
            case FILMS:
                id = db.insertOrThrow(FilmsTable.TABLE_NAME, null, values);
                break;
            case KINDS:
                id = db.insertOrThrow(KindTable.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Uknown URI: " + uri);
        }
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted = 0;
        SQLiteDatabase db = database.getWritableDatabase();
        switch (sURIMatcher.match(uri)) {
            case FILMS:
                rowsDeleted = db.delete(FilmsTable.TABLE_NAME, selection, selectionArgs);
                break;
            case FILM_ID:
                String id = uri.getLastPathSegment();
                if (!TextUtils.isEmpty(id)) {
                    rowsDeleted = db.delete(FilmsTable.TABLE_NAME, FilmsTable.PRIMARY_KEY + " = " + id, null);
                }
                break;
            case CATEGORY_ID:
                String category_id = uri.getLastPathSegment();
                if (!TextUtils.isEmpty(category_id)) {
                    rowsDeleted = db.delete(CategoriesTable.TABLE_NAME, CategoriesTable.PRIMARY_KEY + " = " + category_id, null);
                    rowsDeleted += db.delete(KindTable.TABLE_NAME, KindTable.COLUMN_CATEGORIE_ID + " = " + category_id, null);
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
            case FILMS:
                rowsUpdated = db.update(FilmsTable.TABLE_NAME, values, selection, selectionArgs);
                break;
            case FILM_ID:
                String id = uri.getLastPathSegment();
                if (!TextUtils.isEmpty(id)) {
                    rowsUpdated = db.update(FilmsTable.TABLE_NAME, values, FilmsTable.PRIMARY_KEY + " = " + id, null);
                }
                break;
            case CATEGORY_ID:
                String category_id = uri.getLastPathSegment();
                if (!TextUtils.isEmpty(category_id)) {
                    rowsUpdated = db.update(CategoriesTable.TABLE_NAME, values, CategoriesTable.PRIMARY_KEY + " = " + category_id, null);
                    rowsUpdated += db.update(KindTable.TABLE_NAME, values, KindTable.COLUMN_CATEGORIE_ID + " = " + category_id, null);
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
            if (uriType == CATEGORIES && !CategoriesTable.availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            } else if (uriType == JOIN && !KindTable.availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            } else if ((uriType == FILM_ID || uriType == FILMS) && !FilmsTable.availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}
