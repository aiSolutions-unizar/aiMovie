package es.unizar.aisolutions.aimovie.external_data;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import es.unizar.aisolutions.aimovie.data.IMDbMovie;
import es.unizar.aisolutions.aimovie.data.Movie;
import es.unizar.aisolutions.aimovie.data.MovieParseException;

/**
 * Created by dbarelop on 27/04/15.
 */
public class OMDbMovieFetcher implements MovieFetcher {
    private static final String OMDB_BASE_URL = "http://www.omdbapi.com/?";
    private static final String OMDB_PARAM_ID = "i";
    private static final String OMDB_PARAM_TITLE = "t";
    private static final String OMDB_PARAM_TYPE = "type";
    private static final String OMDB_PARAM_TYPE_OPT_MOVIE = "movie";
    private static final String OMDB_PARAM_TYPE_OPT_SERIES = "series";
    private static final String OMDB_PARAM_TYPE_OPT_EPISODE = "episode";
    private static final String OMDB_PARAM_YEAR = "y";
    private static final String OMDB_PARAM_PLOT = "plot";
    private static final String OMDB_PARAM_PLOT_OPT_SHORT = "short";
    private static final String OMDB_PARAM_PLOT_OPT_FULL = "full";
    private static final String OMDB_PARAM_DATA_TYPE = "r";
    private static final String OMDB_PARAM_DATA_TYPE_OPT_JSON = "json";
    private static final String OMDB_PARAM_DATA_TYPE_OPT_XML = "xml";
    private static final String OMDB_PARAM_INCLUDE_RT_RATINGS = "tomatoes";
    private static final String OMDB_PARAM_INCLUDE_RT_RATINGS_OPT_TRUE = "true";
    private static final String OMDB_PARAM_INCLUDE_RT_RATINGS_OPT_FALSE = "false";
    private Context context;

    public OMDbMovieFetcher(Context context) {
        this.context = context;
    }

    @Override
    public IMDbMovie getMovieById(String imdbId) {
        Uri.Builder builtUri = Uri.parse(OMDB_BASE_URL).buildUpon()
                .appendQueryParameter(OMDB_PARAM_ID, imdbId);
        return getIMDbMovie(builtUri.toString());
    }

    @Override
    public Movie getMovieByTitle(String title) {
        Uri.Builder builtUri = Uri.parse(OMDB_BASE_URL).buildUpon()
                .appendQueryParameter(OMDB_PARAM_TITLE, title);
        return getIMDbMovie(builtUri.toString());
    }

    private IMDbMovie getIMDbMovie(String builtUri) {
        try {
            URL url = new URL(builtUri.toString());
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();
            JsonParser jp = new JsonParser();
            JsonObject jsonMovie = jp.parse(new InputStreamReader((InputStream) request.getContent())).getAsJsonObject();
            IMDbMovie movie = new IMDbMovie(jsonMovie, context);
            return movie;
        } catch (IOException | MovieParseException e) {
            Log.e(e.getMessage(), e.toString(), e);
            return null;
        }
    }
}
