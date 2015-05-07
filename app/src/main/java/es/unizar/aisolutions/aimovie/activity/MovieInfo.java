package es.unizar.aisolutions.aimovie.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

import es.unizar.aisolutions.aimovie.R;
import es.unizar.aisolutions.aimovie.contentprovider.MoviesContentMiddleware;
import es.unizar.aisolutions.aimovie.data.Movie;
import es.unizar.aisolutions.aimovie.database.MoviesTable;

public class MovieInfo extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        final TextView title = (TextView) findViewById(R.id.activity_movie_info_title);
        final TextView director = (TextView) findViewById(R.id.activity_movie_info_director);
        final TextView year = (TextView) findViewById(R.id.activity_movie_info_year);
        final TextView genres = (TextView) findViewById(R.id.activity_movie_info_genres);
        final TextView plot = (TextView) findViewById(R.id.activity_movie_info_plot);
        final ImageView image = (ImageView) findViewById(R.id.activity_movie_info_image);

        if (savedInstanceState != null || getIntent().getExtras() != null) {
            long id = getIntent().getExtras().getLong(MoviesTable.PRIMARY_KEY);
            final MoviesContentMiddleware mcm = new MoviesContentMiddleware(this);
            Movie m = mcm.fetchMovie(id);
            title.setText(m.getTitle());
            director.setText(m.getDirector());
            year.setText(Integer.toString(m.getYear()));
            genres.setText(m.getGenres() != null ? TextUtils.join(", ", m.getGenres()) : null);
            plot.setText(m.getPlot());
            new AsyncTask<String, Void, Bitmap>() {
                @Override
                protected Bitmap doInBackground(String... link) {
                    try {
                        // TODO: cache thumbnail into resource when inserting movie
                        URL url = new URL(link[0]);
                        Bitmap thumbnail = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        return thumbnail;
                    } catch (IOException e) {
                        // TODO: error handling
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                protected void onPostExecute(Bitmap thumbnail) {
                    image.setImageBitmap(thumbnail);
                }
            }.execute(m.getPoster().toString());
        }
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
