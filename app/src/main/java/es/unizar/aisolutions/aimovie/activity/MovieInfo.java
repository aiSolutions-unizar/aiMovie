package es.unizar.aisolutions.aimovie.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import es.unizar.aisolutions.aimovie.R;
import es.unizar.aisolutions.aimovie.contentprovider.MoviesContentMiddleware;
import es.unizar.aisolutions.aimovie.data.Movie;
import es.unizar.aisolutions.aimovie.database.MoviesTable;

public class MovieInfo extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        TextView title = (TextView) findViewById(R.id.activity_movie_info_title);
        TextView director = (TextView) findViewById(R.id.activity_movie_info_director);
        TextView year = (TextView) findViewById(R.id.activity_movie_info_year);
        TextView genres = (TextView) findViewById(R.id.activity_movie_info_genres);
        TextView plot = (TextView) findViewById(R.id.activity_movie_info_plot);
        ImageView thumbnail = (ImageView) findViewById(R.id.activity_movie_info_image);

        Bundle extras = getIntent().getExtras();
        long id = extras.getParcelable(MoviesTable.PRIMARY_KEY);
        MoviesContentMiddleware mcm = new MoviesContentMiddleware(getApplicationContext());
        Movie m = mcm.fetchMovie(id);
        title.setText(m.getTitle());
        director.setText(m.getDirector());
        year.setText(m.getYear());
        // TODO: add genres
        plot.setText(m.getPlot());
        // TODO: add thumbnail
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
