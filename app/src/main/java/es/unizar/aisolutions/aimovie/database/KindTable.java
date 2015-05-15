package es.unizar.aisolutions.aimovie.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.HashSet;

/**
 * KindTable provides methods so as to create and update table called 'TABLE_NAME'
 * in a SQLite database.
 * <p/>
 * Created by diego on 2/04/15.
 * Time spent: 11 minutes.
 */
public class KindTable {
    public static final String TABLE_NAME = "kind";
    public static final String PRIMARY_KEY = "_id";
    public static final String COLUMN_MOVIE_ID = "movie";
    public static final String COLUMN_GENRE_ID = "genre";

    public static final HashSet<String> AVAILABLE_COLUMNS = new HashSet<>();


    // SQL code to create table called 'TABLE_NAME'
    public static final String CREATE_TABLE = String.format(
            "CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s INTEGER NOT NULL, " +
                    "%s INTEGER NOT NULL, " +
                    "foreign key (%s) references %s (%s), " +
                    "foreign key (%s) references %s (%s));",
            TABLE_NAME, PRIMARY_KEY, COLUMN_GENRE_ID, COLUMN_MOVIE_ID, COLUMN_GENRE_ID, GenresTable.TABLE_NAME, GenresTable.PRIMARY_KEY, COLUMN_MOVIE_ID, MoviesTable.TABLE_NAME, MoviesTable.PRIMARY_KEY
    );

    // SQL code to create needed triggers
    public static final String CREATE_TRIGGER = "";

    /**
     * onCreate creates the table defined in 'CREATE_TABLE' and the trigger defined in 'CREATE_TRIGGER'
     *
     * @param db = Database where triggers and tables will be created.
     */
    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        //db.execSQL(CREATE_TRIGGER);
        AVAILABLE_COLUMNS.addAll(MoviesTable.AVAILABLE_COLUMNS);
        AVAILABLE_COLUMNS.addAll(GenresTable.AVAILABLE_COLUMNS);
    }

    /**
     * onUpgrade upgrades the database into a newer version.
     *
     * @param db         = Database to upgrade.
     * @param oldVersion = Old database version.
     * @param newVersion = New database version.
     */
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MoviesTable.class.getName(), "Upgrading database from version " + oldVersion
                + " to " + newVersion + " (all data will be destroyed)");
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }
}
