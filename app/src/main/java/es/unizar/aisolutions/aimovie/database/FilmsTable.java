package es.unizar.aisolutions.aimovie.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;

/**
 * FilmsTable provides methods so as to create and update table called 'TABLE_NAME'
 * in a SQLite database.
 * <p/>
 * Created by diego on 2/04/15.
 * Time spent: 16 minutes.
 */
public class FilmsTable {

    public static final String TABLE_NAME = "films";
    public static final String PRIMARY_KEY = "_id";
    public static final String COLUMN_FILM_NAME = "name";
    public static final String COLUMN_SINOPSIS = "sinopsis";
    public static final String COLUMN_IN_STOCK = "in_stock";
    public static final String COLUMN_RENTED = "rented";
    public static final String COLUMN_DIRECTOR = "director";
    public static final String COLUMN_YEAR = "year";

    public static final HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(new String[]{PRIMARY_KEY, COLUMN_YEAR, COLUMN_DIRECTOR, COLUMN_RENTED, COLUMN_IN_STOCK, COLUMN_SINOPSIS, COLUMN_FILM_NAME}));


    // SQL code to create table called 'TABLE_NAME'
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME +
                    "( " + PRIMARY_KEY + " TEXT PRIMARY KEY, " +
                    COLUMN_FILM_NAME + " TEXT NOT NULL, " +
                    COLUMN_SINOPSIS + " TEXT NOT NULL," +
                    COLUMN_DIRECTOR + "TEXT NOT NULL," +
                    COLUMN_IN_STOCK + "INTEGER NOT NULL," +
                    COLUMN_RENTED + "INTEGER NOT NULL," +
                    COLUMN_YEAR + "INTEGER NOT NULL," + ");";

    // SQL code to create needed triggers
    public static final String CREATE_TRIGGER = "";

    /**
     * onCreate creates the table defined in 'CREATE_TABLE' and the trigger defined in 'CREATE_TRIGGER'
     *
     * @param db = Database where triggers and tables will be created.
     */
    public static void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_TRIGGER);
    }

    /**
     * onUpgrade upgrades the database into a newer version.
     *
     * @param db         = Database to upgrade.
     * @param oldVersion = Old database version.
     * @param newVersion = New database version.
     */
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(FilmsTable.class.getName(), "Upgrading database from version " + oldVersion
                + " to " + newVersion + " (all data will be destroyed)");
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }
}