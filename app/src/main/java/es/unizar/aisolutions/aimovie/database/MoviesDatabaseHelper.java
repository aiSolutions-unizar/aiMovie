package es.unizar.aisolutions.aimovie.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by diego on 2/04/15.
 */

public class MoviesDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 5;

    public MoviesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        MoviesTable.onCreate(db);
        CategoriesTable.onCreate(db);
        KindTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        KindTable.onUpgrade(db, oldVersion, newVersion);
        MoviesTable.onUpgrade(db, oldVersion, newVersion);
        CategoriesTable.onUpgrade(db, oldVersion, newVersion);
    }
}
