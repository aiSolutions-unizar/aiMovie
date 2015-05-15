package es.unizar.aisolutions.aimovie.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import es.unizar.aisolutions.aimovie.R;

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
            TABLE_NAME + "." + PRIMARY_KEY,
            TABLE_NAME + "." + COLUMN_GENRE_NAME
    }));


    // SQL code to create table called 'TABLE_NAME'
    public static final String CREATE_TABLE = String.format(
            "CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "%s TEXT UNIQUE NOT NULL);" +
                    "INSERT INTO %s (%s) VALUES " +
                    "(%s), (%s), (%s), (%s), (%s), (%s), (%s), (%s), (%s), (%s), (%s), (%s), " +
                    "(%s), (%s), (%s), (%s), (%s), (%s), (%s), (%s), (%s), (%s);",
            TABLE_NAME, PRIMARY_KEY, COLUMN_GENRE_NAME, TABLE_NAME, COLUMN_GENRE_NAME,
            R.string.genre_action, R.string.genre_adventure, R.string.genre_animation,
            R.string.genre_biography, R.string.genre_comedy, R.string.genre_crime,
            R.string.genre_documentary, R.string.genre_drama, R.string.genre_family,
            R.string.genre_fantasy, R.string.genre_filmnoir, R.string.genre_history,
            R.string.genre_horror, R.string.genre_music, R.string.genre_musical,
            R.string.genre_mystery, R.string.genre_romance, R.string.genre_scifi,
            R.string.genre_sport, R.string.genre_thriller, R.string.genre_war,
            R.string.genre_western
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