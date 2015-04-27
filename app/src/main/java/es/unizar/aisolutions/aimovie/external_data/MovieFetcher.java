package es.unizar.aisolutions.aimovie.external_data;

import es.unizar.aisolutions.aimovie.data.Movie;

/**
 * Created by dbarelop on 27/04/15.
 */
public interface MovieFetcher {
    /**
     * Gets information about a title
     *
     * @param imdbId the title's IMDb id
     * @return a Movie object containing the given title's information or null if not found
     */
    Movie getMovieById(String imdbId);

    /**
     * Gets information about a title
     *
     * @param title the movie's title
     * @return a Movie object containing the given title's information or null if not found
     */
    Movie getMovieByTitle(String title);
}
