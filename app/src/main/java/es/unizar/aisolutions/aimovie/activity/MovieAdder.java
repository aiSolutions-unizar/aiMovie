package es.unizar.aisolutions.aimovie.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import es.unizar.aisolutions.aimovie.R;
import es.unizar.aisolutions.aimovie.contentprovider.ContentUpdates;
import es.unizar.aisolutions.aimovie.contentprovider.MoviesContentMiddleware;
import es.unizar.aisolutions.aimovie.data.Movie;
import es.unizar.aisolutions.aimovie.data.StoredMovie;

public class MovieAdder extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_adder);

        Button confirmButton = (Button) findViewById(R.id.confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_adder, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    private void saveState() {
        String title = ((EditText) findViewById(R.id.title)).getText().toString();
        int year = -1;
        try {
            year = Integer.parseInt(((EditText) findViewById(R.id.year)).getText().toString());
        } catch (NumberFormatException e) {

        }
        String director = ((EditText) findViewById(R.id.director)).getText().toString();
        Movie m = new StoredMovie(-1, title, year, null, null, null, null, director, null, null,
                null, null, null, null, null, -1, -1, -1, null);
        ContentUpdates db = new MoviesContentMiddleware(this);
        db.addMovie(m);
    }
}
