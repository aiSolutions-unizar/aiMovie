package es.unizar.aisolutions.aimovie.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * FilmsTable provides methods so as to create and update table called 'TABLE_NAME'
 * in a SQLite database.
 * <p/>
 * Created by diego on 2/04/15.
 * Time spent: 16 minutes.
 */
public class MoviesTable {
    public static final String TABLE_NAME = "movies";
    public static final String PRIMARY_KEY = "_id";
    public static final String COLUMN_TITLE = "name";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_RATED = "rated";
    public static final String COLUMN_RELEASED = "released";
    public static final String COLUMN_RUNTIME = "runtime";
    public static final String COLUMN_DIRECTOR = "director";
    public static final String COLUMN_WRITER = "writer";
    public static final String COLUMN_PLOT = "plot";
    public static final String COLUMN_LANGUAGE = "language";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_AWARDS = "awards";
    public static final String COLUMN_POSTER = "poster";
    public static final String COLUMN_METASCORE = "metascore";
    public static final String COLUMN_IMDB_RATING = "imdb_rating";
    public static final String COLUMN_IMDB_VOTES = "imdb_votes";
    public static final String COLUMN_IMDB_ID = "imdb_id";
    public static final String COLUMN_STOCK = "stock";
    public static final String COLUMN_RENTED = "rented";

    public static final Set<String> AVAILABLE_COLUMNS = new HashSet<>(Arrays.asList(new String[]{
            TABLE_NAME + "." + PRIMARY_KEY,
            TABLE_NAME + "." + COLUMN_TITLE,
            TABLE_NAME + "." + COLUMN_YEAR,
            TABLE_NAME + "." + COLUMN_RATED,
            TABLE_NAME + "." + COLUMN_RELEASED,
            TABLE_NAME + "." + COLUMN_RUNTIME,
            TABLE_NAME + "." + COLUMN_DIRECTOR,
            TABLE_NAME + "." + COLUMN_WRITER,
            TABLE_NAME + "." + COLUMN_PLOT,
            TABLE_NAME + "." + COLUMN_LANGUAGE,
            TABLE_NAME + "." + COLUMN_COUNTRY,
            TABLE_NAME + "." + COLUMN_AWARDS,
            TABLE_NAME + "." + COLUMN_POSTER,
            TABLE_NAME + "." + COLUMN_METASCORE,
            TABLE_NAME + "." + COLUMN_IMDB_RATING,
            TABLE_NAME + "." + COLUMN_IMDB_VOTES,
            TABLE_NAME + "." + COLUMN_IMDB_ID,
            TABLE_NAME + "." + COLUMN_STOCK,
            TABLE_NAME + "." + COLUMN_RENTED
    }));

    // SQL code to create table called 'TABLE_NAME'
    public static final String CREATE_TABLE = String.format(
            "CREATE TABLE %s (" +
                    "%s INTEGER PRIMARY KEY AUTOINCREMENT, " +  // _id
                    "%s TEXT NOT NULL, " +          // title
                    "%s INTEGER, " +                // year
                    "%s TEXT, " +                   // rated
                    "%s INTEGER, " +                // released
                    "%s TEXT, " +                   // runtime
                    "%s TEXT, " +                   // director
                    "%s TEXT, " +                   // writer
                    "%s TEXT, " +                   // plot
                    "%s TEXT, " +                   // language
                    "%s TEXT, " +                   // country
                    "%s TEXT, " +                   // awards
                    "%s TEXT, " +                   // poster
                    "%s INTEGER, " +                // metascore
                    "%s REAL, " +                   // imdb_rating
                    "%s INTEGER, " +                // imdb_votes
                    "%s TEXT, " +                   // imdb_id
                    "%s INTEGER NOT NULL DEFAULT 0, " +         // stock
                    "%s INTEGER NOT NULL DEFAULT 0);",          // rented
            TABLE_NAME, PRIMARY_KEY, COLUMN_TITLE, COLUMN_YEAR, COLUMN_RATED, COLUMN_RELEASED, COLUMN_RUNTIME,
            COLUMN_DIRECTOR, COLUMN_WRITER, COLUMN_PLOT, COLUMN_LANGUAGE, COLUMN_COUNTRY, COLUMN_AWARDS, COLUMN_POSTER,
            COLUMN_METASCORE, COLUMN_IMDB_RATING, COLUMN_IMDB_VOTES, COLUMN_IMDB_ID, COLUMN_STOCK, COLUMN_RENTED
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
        Log.w(MoviesTable.class.getName(), "Upgrading database from version " + oldVersion
                + " to " + newVersion + " (all data will be destroyed)");
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }
}