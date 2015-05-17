package es.unizar.aisolutions.aimovie.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.unizar.aisolutions.aimovie.R;
import es.unizar.aisolutions.aimovie.contentprovider.MovieManager;
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
    private static Movie m = null;
    private static MovieManager mgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_editor);

        mgr = new MovieManager(this);

        Button confirmButton = (Button) findViewById(R.id.confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (!getIntent().getExtras().isEmpty()) {
            final long id = getIntent().getExtras().getLong(EXTRA_MOVIE_ID);
            m = mgr.fetchMovie(id);

            final EditText title = (EditText) findViewById(R.id.title);
            final EditText year = (EditText) findViewById(R.id.year);
            final EditText director = (EditText) findViewById(R.id.director);
            final EditText plot = (EditText) findViewById(R.id.plot);

            title.setText(m.getTitle());
            year.setText(Integer.toString(m.getYear()));
            director.setText(m.getDirector());
            plot.setText(m.getPlot());

            // TODO mark  genres checkboxes to persist old genres
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
            String plot = ((EditText) findViewById(R.id.plot)).getText().toString();
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

            if (m != null) {
                m.setTitle(title);
                m.setYear(year);
                m.setDirector(director);
                m.setPlot(plot);
                m.setGenres(genres);
                mgr.updateMovie(m);
            } else {
                m = new StoredMovie(-1, title, year, null, null, null, genres, director, null, null,
                        plot, null, null, null, null, -1, -1, -1, null, 0, 0);
                mgr.addMovie(m);
            }
        } else {
            Toast.makeText(this, getString(R.string.title_not_entered), Toast.LENGTH_SHORT).show();
        }
    }
}