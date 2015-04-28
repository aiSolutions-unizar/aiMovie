package es.unizar.aisolutions.aimovie.activity;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.getbase.floatingactionbutton.FloatingActionButton;

import es.unizar.aisolutions.aimovie.R;
import es.unizar.aisolutions.aimovie.contentprovider.MoviesContentMiddleware;
import es.unizar.aisolutions.aimovie.contentprovider.MoviesContentProvider;
import es.unizar.aisolutions.aimovie.data.Movie;
import es.unizar.aisolutions.aimovie.database.MoviesTable;
import es.unizar.aisolutions.aimovie.external_data.OMDbMovieFetcher;

public class MovieList extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        ListView listView = (ListView) findViewById(R.id.movie_list);
        String[] from = {MoviesTable.COLUMN_TITLE};
        int[] to = {R.id.activity_movie_list_item_title};
        adapter = new SimpleCursorAdapter(this, R.layout.activity_movie_list_item, null, from, to, 0);
        listView.setAdapter(adapter);
        getLoaderManager().initLoader(0, null, this);

        // FAB button
        final FloatingActionButton addManually = (FloatingActionButton) findViewById(R.id.add_movie_manually);
        addManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), MovieAdder.class);
                startActivity(i);
            }
        });
        final FloatingActionButton addIMDb = (FloatingActionButton) findViewById(R.id.add_movie_imdb);
        addIMDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle(getString(R.string.enter_imdb_id));
                final EditText input = new EditText(v.getContext());
                final MoviesContentMiddleware mcm = new MoviesContentMiddleware(v.getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String id = input.getText().toString();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Movie movie = new OMDbMovieFetcher().getMovieById(id);
                                mcm.addMovie(movie);
                            }
                        }).start();
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_list, menu);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = MoviesContentProvider.CONTENT_URI;
        String[] projection = {MoviesTable.PRIMARY_KEY, MoviesTable.COLUMN_TITLE};
        return new CursorLoader(this, uri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
