package es.unizar.aisolutions.aimovie.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * GenresTable provides methods so as to create and update table called 'TABLE_NAME'
 * in a SQLite database.
 * <p/>
 * Created by diego on 2/04/15.
 * Time spent: 11 minutes.
 */
public class GenresTable {
    public static final String TABLE_NAME = "genres";
    public static final String PRIMARY_KEY = "_id";
    public static final String COLUMN_GENRE_NAME = "name";

    public static final Set<String> AVAILABLE_COLUMNS = new HashSet<>(Arrays.asList(new String[]{
            PRIMARY_KEY,
            COLUMN_GENRE_NAME
    }));


    // SQL code to create table called 'TABLE_NAME'
    public static final String CREATE_TABLE = String.format(
            "CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s TEXT UNIQUE NOT NULL);",
            TABLE_NAME, PRIMARY_KEY, COLUMN_GENRE_NAME
    );
    public static final String INITIALIZE_TABLE = String.format(
            "INSERT INTO %s (%s) VALUES " +
                    "('%s'), ('%s'), ('%s'), ('%s'), ('%s'), ('%s'), ('%s'), ('%s'), ('%s'), " +
                    "('%s'), ('%s'), ('%s'), ('%s'), ('%s'), ('%s'), ('%s'), ('%s'), ('%s'), " +
                    "('%s'), ('%s'), ('%s'), ('%s');",
            TABLE_NAME, COLUMN_GENRE_NAME,
            // TODO: parameterize (possible?)
            "Action", "Adventure", "Animation", "Biography", "Comedy", "Crime", "Documentary",
            "Drama", "Family", "Fantasy", "Film-Noir", "History", "Horror", "Music", "Musical",
            "Mystery", "Romance", "Sci-Fi", "Sport", "Thriller", "War", "Western"
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
        db.execSQL(INITIALIZE_TABLE);
        //db.execSQL(CREATE_TRIGGER);
    }

    /**
     * onUpgrade upgrades the database into a newer version.
     *
     * @param db         = Database to upgrade.
     * @param oldVersion = Old database version.
     * @param newVersion = New database version.
     */
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(GenresTable.class.getName(), "Upgrading database from version " + oldVersion
                + " to " + newVersion + " (all data will be destroyed)");
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }
}