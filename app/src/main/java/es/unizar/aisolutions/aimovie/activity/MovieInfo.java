package es.unizar.aisolutions.aimovie.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

import es.unizar.aisolutions.aimovie.R;
import es.unizar.aisolutions.aimovie.contentprovider.MoviesManager;
import es.unizar.aisolutions.aimovie.data.Movie;

public class MovieInfo extends ActionBarActivity {
    public static final String EXTRA_MOVIE_ID = "movie_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        final TextView title = (TextView) findViewById(R.id.activity_movie_info_title);
        final TextView director = (TextView) findViewById(R.id.activity_movie_info_director);
        final TextView year = (TextView) findViewById(R.id.activity_movie_info_year);
        final TextView genres = (TextView) findViewById(R.id.activity_movie_info_genres);
        final TextView plot = (TextView) findViewById(R.id.activity_movie_info_plot);
        final TextView stock = (TextView) findViewById(R.id.activity_movie_info_stock_value);
        final ImageView image = (ImageView) findViewById(R.id.activity_movie_info_image);
        final TextView rented = (TextView) findViewById(R.id.activity_movie_info_rented_value);

        if (savedInstanceState != null || getIntent().getExtras() != null) {
            long id = getIntent().getExtras().getLong(EXTRA_MOVIE_ID);
            final MoviesManager mcm = new MoviesManager(this);
            final Movie m = mcm.fetchMovie(id);

            Button editButton = (Button) findViewById(R.id.activity_movie_info_editButton);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MovieInfo.this);
                    builder.setTitle("Add stock");
                    final EditText input = new EditText(MovieInfo.this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);
                    builder.setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int add = Integer.parseInt(input.getText().toString());
                            m.setStock(m.getStock() + add);
                            mcm.updateMovie(m);
                            stock.setText(Integer.toString(m.getStock()));
                        }
                    });
                    builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });

            Button delButton = (Button) findViewById(R.id.activity_movie_info_button_delete);
            delButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long id = getIntent().getExtras().getLong(EXTRA_MOVIE_ID);
                    MoviesManager mm = new MoviesManager(MovieInfo.this);
                    mm.deleteMovie(mm.fetchMovie(id));
                    MovieInfo.this.finish();
                }
            });

            Button rentButton = (Button) findViewById(R.id.activity_movie_info_rentButton);
            rentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MovieInfo.this, UserInfo.class);
                    long id = getIntent().getExtras().getLong(EXTRA_MOVIE_ID);
                    i.putExtra(UserInfo.EXTRA_MOVIE_ID, id);
                    startActivity(i);
                }
            });

            title.setText(m.getTitle());
            director.setText(m.getDirector());
            year.setText(Integer.toString(m.getYear()));
            genres.setText(m.getGenres() != null ? TextUtils.join(", ", m.getGenres()) : null);
            plot.setText(m.getPlot());
            stock.setText(Integer.toString(m.getStock()));
            rented.setText(Integer.toString(m.getRented()));
            if (m.getPoster() != null) {
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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent i = new Intent(MovieInfo.this, MovieEditor.class);
                long idd = getIntent().getExtras().getLong(EXTRA_MOVIE_ID);
                i.putExtra(MovieEditor.EXTRA_MOVIE_ID, idd);
                startActivity(i);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        long id = getIntent().getExtras().getLong(EXTRA_MOVIE_ID);
        final MoviesManager mcm = new MoviesManager(this);
        final Movie m = mcm.fetchMovie(id);
        TextView stock = (TextView) findViewById(R.id.activity_movie_info_stock_value);
        TextView title = (TextView) findViewById(R.id.activity_movie_info_title);
        TextView plot = (TextView) findViewById(R.id.activity_movie_info_plot);
        TextView director = (TextView) findViewById(R.id.activity_movie_info_director);
        TextView year = (TextView) findViewById(R.id.activity_movie_info_year);
        TextView genres = (TextView) findViewById(R.id.activity_movie_info_genres);
        TextView rented = (TextView) findViewById(R.id.activity_movie_info_rented_value);
        if (m != null) {
            title.setText(m.getTitle());
            director.setText(m.getDirector());
            year.setText(Integer.toString(m.getYear()));
            genres.setText(m.getGenres() != null ? TextUtils.join(", ", m.getGenres()) : null);
            plot.setText(m.getPlot());
            stock.setText(Integer.toString(m.getStock()));
            rented.setText(Integer.toString(m.getRented()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_info, menu);
        return true;
    }

}