package es.unizar.aisolutions.aimovie.activity;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.util.Log;
import android.util.LruCache;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.IOException;
import java.net.URL;

import es.unizar.aisolutions.aimovie.R;
import es.unizar.aisolutions.aimovie.contentprovider.MovieManager;
import es.unizar.aisolutions.aimovie.contentprovider.MoviesContentProvider;
import es.unizar.aisolutions.aimovie.data.Genre;
import es.unizar.aisolutions.aimovie.data.Movie;
import es.unizar.aisolutions.aimovie.database.MoviesTable;
import es.unizar.aisolutions.aimovie.external_data.OMDbMovieFetcher;

public class MovieList extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>, NavigationDrawerFragment.NavigationDrawerCallbacks {
    private static final int CACHE_SIZE = (int) (Runtime.getRuntime().maxMemory() / 8 / 1024);  // 1/8 available mem in KB
    private static LruCache<String, Bitmap> thumbnailCache = new LruCache<>(CACHE_SIZE);
    private static int i = 0;
    private NavigationDrawerFragment navigationDrawerFragment;
    private SimpleCursorAdapter adapter;
    private String titleFilter = null;
    private Long genreFilter = null;
    private String sortOrder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        navigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        navigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        ListView listView = (ListView) findViewById(R.id.movie_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MovieList.this, MovieInfo.class);
                i.putExtra(MovieInfo.EXTRA_MOVIE_ID, id);
                startActivity(i);
            }
        });
        listView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                getMenuInflater().inflate(R.menu.context_menu_movie_list, menu);
            }
        });

        // TODO: add genres
        String[] from = {MoviesTable.COLUMN_TITLE, MoviesTable.COLUMN_DIRECTOR, MoviesTable.COLUMN_YEAR, MoviesTable.COLUMN_POSTER, MoviesContentProvider.MOVIE_GENRES_LIST};
        int[] to = {R.id.activity_movie_list_item_title, R.id.activity_movie_list_item_director, R.id.activity_movie_list_item_year, R.id.activity_movie_list_item_image, R.id.activity_movie_list_item_genre};
        adapter = new SimpleCursorAdapter(this, R.layout.activity_movie_list_item, null, from, to, 0) {
            @Override
            public void setViewImage(final ImageView imageView, String value) {
                if (imageView != null && value != null) {
                    new AsyncTask<String, Void, Bitmap>() {
                        @Override
                        protected Bitmap doInBackground(String... link) {
                            try {
                                Bitmap thumbnail = thumbnailCache.get(link[0]);
                                if (thumbnail == null) {
                                    URL url = new URL(link[0]);
                                    thumbnail = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                    synchronized (thumbnailCache) {
                                        thumbnailCache.put(link[0], thumbnail);
                                    }
                                }
                                return thumbnail;
                            } catch (IOException e) {
                                Log.w(e.getMessage(), e.toString(), e);
                                return null;
                            }
                        }

                        @Override
                        protected void onPostExecute(Bitmap thumbnail) {
                            imageView.setImageBitmap(thumbnail);
                        }
                    }.execute(value);
                }
            }
        };
        listView.setAdapter(adapter);
        getLoaderManager().initLoader(0, null, this);

        // FAB button
        final FloatingActionsMenu fabButton = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        final FloatingActionButton addManually = (FloatingActionButton) findViewById(R.id.add_movie_manually);
        addManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabButton.collapse();
                Intent i = new Intent(MovieList.this, MovieEditor.class);
                startActivity(i);
            }
        });
        final FloatingActionButton addIMDb = (FloatingActionButton) findViewById(R.id.add_movie_imdb);
        addIMDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MovieList.this);
                builder.setTitle(getString(R.string.enter_imdb_id));
                final EditText input = new EditText(MovieList.this);
                final MovieManager mcm = new MovieManager(MovieList.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                // for debugging purposes
                final String[] ids = {"tt0070735", "tt2395427", "tt2967224", "tt1655441", "tt2820852",
                        "tt3450650", "tt1375666", "tt0137523", "tt1345836", "tt0133093"};
                if (i < ids.length) input.setText(ids[i++]);
                builder.setView(input);
                builder.setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String id = input.getText().toString();
                        new AsyncTask<String, Void, Movie>() {
                            @Override
                            protected Movie doInBackground(String... id) {
                                Movie movie = new OMDbMovieFetcher(MovieList.this).getMovieById(id[0]);
                                return movie;
                            }

                            @Override
                            protected void onPostExecute(Movie movie) {
                                if (movie != null) {
                                    mcm.addMovie(movie);
                                } else {
                                    Toast.makeText(MovieList.this, "Movie not found", Toast.LENGTH_LONG).show();
                                }
                            }
                        }.execute(id);
                    }
                });
                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                fabButton.collapse();
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                Uri uri = Uri.parse(MoviesContentProvider.CONTENT_URI + "/" + info.id);
                getContentResolver().delete(uri, null, null);
                return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_list, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconified(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                Bundle bundle = new Bundle();
                bundle.putString("query",s);
                getLoaderManager().restartLoader(0, bundle, MovieList.this);
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                menuItem.collapseActionView();
                getLoaderManager().restartLoader(0, null, MovieList.this);
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Bundle bundle = new Bundle();
            bundle.putString("query",query);
            getLoaderManager().restartLoader(0, bundle, MovieList.this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // Do nothing
                break;
            case R.id.action_sort:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                int selected = -1;
                if (sortOrder != null) {
                    switch (sortOrder) {
                        case MoviesTable.COLUMN_TITLE:
                            selected = 0;
                            break;
                        case MoviesTable.COLUMN_DIRECTOR:
                            selected = 1;
                            break;
                        case MoviesTable.COLUMN_YEAR:
                            selected = 2;
                    }
                }
                DialogInterface.OnClickListener selectListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: parameterize?
                        switch (which) {
                            case 0:
                                sortOrder = MoviesTable.COLUMN_TITLE;
                                break;
                            case 1:
                                sortOrder = MoviesTable.COLUMN_DIRECTOR;
                                break;
                            case 2:
                                sortOrder = MoviesTable.COLUMN_YEAR;
                        }
                        getLoaderManager().restartLoader(0, null, MovieList.this);
                        dialog.dismiss();
                    }
                };
                builder.setTitle(getString(R.string.sort_by));
                builder.setSingleChoiceItems(R.array.sorting_choices, selected, selectListener);
                builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = MoviesContentProvider.CONTENT_URI;
        String[] projection = MoviesTable.AVAILABLE_COLUMNS.toArray(new String[MoviesTable.AVAILABLE_COLUMNS.size() + 1]);
        projection[projection.length - 1] = MoviesContentProvider.MOVIE_GENRES_LIST;
        //String[] projection = MoviesTable.AVAILABLE_COLUMNS.toArray(new String[0]);
        String selection = null;
        String[] selectionArgs = null;
        if(args != null && args.containsKey("query")){
            selection = "name LIKE ?";
            selectionArgs = new String[] {"%"+ args.get("query")+ "%" };
        }
        if (genreFilter != null) {
            uri = Uri.parse(MoviesContentProvider.CONTENT_URI + "/GENRE/" + genreFilter + "/MOVIES");
        }
        return new CursorLoader(this, uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (navigationDrawerFragment != null) {
            Genre selectedGenre = navigationDrawerFragment.getGenreAtPosition(position);
            genreFilter = selectedGenre == null ? null : selectedGenre.get_id();
            getLoaderManager().restartLoader(0, null, this);
            String title = selectedGenre == null ? "All movies" : selectedGenre.getName();
            getSupportActionBar().setTitle(title);
        }
    }
}
