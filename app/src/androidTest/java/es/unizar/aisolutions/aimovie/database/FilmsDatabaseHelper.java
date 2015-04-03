package es.unizar.aisolutions.aimovie.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by diego on 2/04/15.
 */

public class FilmsDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "films.db";
    private static final int DATABASE_VERSION = 2;

    public FilmsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        FilmsTable.onCreate(db);
        CategoriesTable.onCreate(db);
        KindTable.onCreate(db);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        KindTable.onUpgrade(db, oldVersion, newVersion);
        FilmsTable.onUpgrade(db, oldVersion, newVersion);
        CategoriesTable.onUpgrade(db, oldVersion, newVersion);
    }


}