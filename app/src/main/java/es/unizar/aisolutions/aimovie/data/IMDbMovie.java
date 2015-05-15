package es.unizar.aisolutions.aimovie.data;

import com.google.gson.JsonObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by dbarelop on 27/04/15.
 */
public class IMDbMovie implements Movie {
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("d MMM yyyy");
    private String title;
    private int year;
    private String rated;
    private Date released;
    private String runtime;
    private List<Genre> genres;
    private String director;
    private String writer;
    private List<String> actors;
    private String plot;
    private String language;
    private String country;
    private String awards;
    private URL poster;
    private int metascore;
    private float imdbRating;
    private int imdbVotes;
    private String imdbID;

    public IMDbMovie(JsonObject jsonMovie) throws MovieParseException {
        String response = jsonMovie.get("Response").getAsString();
        if (!response.equals("False")) {
            String type = jsonMovie.get("Type").getAsString();
            if (type.equals("movie")) {
                title = jsonMovie.get("Title").getAsString();
                year = jsonMovie.get("Year").getAsInt();
                rated = jsonMovie.get("Rated").getAsString();
                try {
                    released = DATE_FORMATTER.parse(jsonMovie.get("Released").getAsString());
                } catch (ParseException e) {
                    released = null;
                }
                runtime = jsonMovie.get("Runtime").getAsString();
                genres = new ArrayList<>();
                for (String genre : Arrays.asList(jsonMovie.get("Genre").getAsString().split(","))) {
                    Genre g = new Genre(-1, genre);
                    genres.add(g);
                }
                director = jsonMovie.get("Director").getAsString();
                writer = jsonMovie.get("Writer").getAsString();
                actors = Arrays.asList(jsonMovie.get("Actors").getAsString().split(","));
                plot = jsonMovie.get("Plot").getAsString();
                language = jsonMovie.get("Language").getAsString();
                country = jsonMovie.get("Country").getAsString();
                awards = jsonMovie.get("Awards").getAsString();
                try {
                    poster = new URL(jsonMovie.get("Poster").getAsString());
                } catch (MalformedURLException e) {
                    poster = null;
                }
                try {
                    metascore = Integer.parseInt(jsonMovie.get("Metascore").getAsString());
                } catch (NumberFormatException e) {
                    metascore = -1;
                }
                try {
                    imdbRating = Float.parseFloat(jsonMovie.get("imdbRating").getAsString());
                } catch (NumberFormatException e) {
                    imdbRating = -1;
                }
                try {
                    imdbVotes = Integer.parseInt(jsonMovie.get("imdbVotes").getAsString().replace(",", ""));
                } catch (NumberFormatException e) {
                    imdbVotes = -1;
                }
                imdbID = jsonMovie.get("imdbID").getAsString();
            } else {
                throw new MovieParseException("Provided JSON doesn't belong to a movie");
            }
        } else {
            throw new MovieParseException("Error parsing IMDbMovie from JSON");
        }
    }

    @Override
    public long get_id() {
        return -1;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getYear() {
        return year;
    }

    @Override
    public String getRated() {
        return rated;
    }

    @Override
    public Date getReleased() {
        return released;
    }

    @Override
    public String getRuntime() {
        return runtime;
    }

    @Override
    public List<Genre> getGenres() {
        return genres;
    }

    @Override
    public String getDirector() {
        return director;
    }

    @Override
    public String getWriter() {
        return writer;
    }

    @Override
    public List<String> getActors() {
        return actors;
    }

    @Override
    public String getPlot() {
        return plot;
    }

    @Override
    public String getLanguage() {
        return language;
    }

    @Override
    public String getCountry() {
        return country;
    }

    @Override
    public String getAwards() {
        return awards;
    }

    @Override
    public URL getPoster() {
        return poster;
    }

    @Override
    public int getMetascore() {
        return metascore;
    }

    @Override
    public float getImdbRating() {
        return imdbRating;
    }

    @Override
    public int getImdbVotes() {
        return imdbVotes;
    }

    @Override
    public String getImdbID() {
        return imdbID;
    }
}
