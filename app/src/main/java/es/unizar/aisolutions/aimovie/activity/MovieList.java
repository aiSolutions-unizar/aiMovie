package es.unizar.aisolutions.aimovie.activity;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.getbase.floatingactionbutton.FloatingActionButton;

import es.unizar.aisolutions.aimovie.R;
import es.unizar.aisolutions.aimovie.contentprovider.FilmsContentProvider;
import es.unizar.aisolutions.aimovie.database.MoviesTable;

public class MovieList extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        ListView listView = (ListView) findViewById(R.id.movie_list);
        String[] from = {MoviesTable.COLUMN_FILM_NAME};
        int[] to = {R.id.activity_movie_list_item_title};
        adapter = new SimpleCursorAdapter(this, R.layout.activity_movie_list_item, null, from, to, 0);
        listView.setAdapter(adapter);
        getLoaderManager().initLoader(0, null, this);

        // FAB button
        final FloatingActionButton addManually = (FloatingActionButton) findViewById(R.id.add_movie_manually);
        final FloatingActionButton addIMDB = (FloatingActionButton) findViewById(R.id.add_movie_imdb);
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
        Uri uri = FilmsContentProvider.CONTENT_URI;
        String[] projection = {MoviesTable.PRIMARY_KEY, MoviesTable.COLUMN_FILM_NAME};
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
