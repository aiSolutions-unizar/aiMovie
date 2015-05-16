package es.unizar.aisolutions.aimovie.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import es.unizar.aisolutions.aimovie.R;
import es.unizar.aisolutions.aimovie.contentprovider.MoviesManager;
import es.unizar.aisolutions.aimovie.data.Genre;
import es.unizar.aisolutions.aimovie.data.Movie;
import es.unizar.aisolutions.aimovie.data.StoredMovie;

public class MovieEditor extends ActionBarActivity {
    public static final String EXTRA_MOVIE_ID = "no_id";
    private static final int[] GENRE_CHECKBOXES_IDS = new int[]{
            R.id.genre_action, R.id.genre_adventure, R.id.genre_animation, R.id.genre_biography,
            R.id.genre_comedy, R.id.genre_crime, R.id.genre_documentary, R.id.genre_drama,
            R.id.genre_family, R.id.genre_fantasy, R.id.genre_filmnoir, R.id.genre_history,
            R.id.genre_horror, R.id.genre_music, R.id.genre_musical, R.id.genre_mystery,
            R.id.genre_romance, R.id.genre_scifi, R.id.genre_sport, R.id.genre_thriller,
            R.id.genre_war, R.id.genre_western
    };

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

        Long id;
        try {
            id = getIntent().getExtras().getLong(EXTRA_MOVIE_ID);
        } catch (Exception e) {
            id = 0L;
        }

        if (id != 0L) {
            MoviesManager mm = new MoviesManager(this);
            Movie m = mm.fetchMovie(id);

            EditText title = (EditText) findViewById(R.id.title);
            EditText year = (EditText) findViewById(R.id.year);
            EditText director = (EditText) findViewById(R.id.director);
            EditText plot = (EditText) findViewById(R.id.synopsis);

            title.setText(m.getTitle());
            year.setText(Integer.toString(m.getYear()));
            director.setText(m.getDirector());
            plot.setText(m.getPlot());

            // TODO Persist genres from oldMovie to newMovie
        }
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
        if (!title.isEmpty()) {
            int year = -1;
            try {
                year = Integer.parseInt(((EditText) findViewById(R.id.year)).getText().toString());
            } catch (NumberFormatException e) {

            }
            String director = ((EditText) findViewById(R.id.director)).getText().toString();
            MoviesManager mgr = new MoviesManager(this);
            List<Genre> genres = new ArrayList<>();
            for (int genreId : GENRE_CHECKBOXES_IDS) {
                CheckBox cb = (CheckBox) findViewById(genreId);
                if (cb.isChecked()) {
                    String genre = cb.getText().toString();
                    Genre g = mgr.fetchGenre(genre);
                    if (g != null) {
                        genres.add(g);
                    }
                }
            }
            MoviesManager db = new MoviesManager(this);
            Long id;
            try {
                id = getIntent().getExtras().getLong(EXTRA_MOVIE_ID);
            } catch (Exception e) {
                id = 0L;
            }
            Movie m;
            if (id != 0L) {
                Movie oldMovie = db.fetchMovie(id);
                URL poster = null;
                String plot = null;
                int stock = 0;

                if (oldMovie.getPoster() != null) poster = oldMovie.getPoster();
                if (oldMovie.getStock() != 0) stock = oldMovie.getStock();
                if (oldMovie.getPlot() != null) plot = oldMovie.getPlot();

                m = new StoredMovie(oldMovie.get_id(), title, year, null, null, null, genres, director, null, null,
                        plot, null, null, null, poster, -1, -1, -1, null, stock);
                db.updateMovie(m);
                // TODO Genres update
            } else {
                m = new StoredMovie(-1, title, year, null, null, null, genres, director, null, null,
                        null, null, null, null, null, -1, -1, -1, null, 0);
                db.addMovie(m);
            }


        } else {
            Toast.makeText(this, getString(R.string.title_not_entered), Toast.LENGTH_SHORT).show();
        }
    }
}