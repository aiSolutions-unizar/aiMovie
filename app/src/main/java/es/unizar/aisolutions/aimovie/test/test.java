package es.unizar.aisolutions.aimovie.test;

import android.test.ActivityUnitTestCase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import es.unizar.aisolutions.aimovie.activity.MovieList;
import es.unizar.aisolutions.aimovie.contentprovider.MovieManager;
import es.unizar.aisolutions.aimovie.data.Genre;
import es.unizar.aisolutions.aimovie.data.Movie;
import es.unizar.aisolutions.aimovie.data.StoredMovie;

public class test extends ActivityUnitTestCase<MovieList> {
    // list movies created in test
    private ArrayList<Movie> created;
    private MovieManager mgr;
    public test() {
        super(es.unizar.aisolutions.aimovie.activity.MovieList.class);
    }

    @Override
    protected void setUp() throws Exception {
        getActivity();
        mgr = new MovieManager(this.getInstrumentation().getTargetContext().getApplicationContext());
        super.setUp();
        created = new ArrayList<>();
    }

    /**
     * Test. Add 1 movie to the application
     */

    public void testAddMovie() {
        Date date=new Date();
        date.setYear(1995);
        Genre genre=new Genre(100,"Crime");
        List<Genre> genres= Arrays.asList(genre);
        Movie movie = new StoredMovie(1,"name1",1998,"A",date,"runtime",genres,"director","writer",null,"plot","english",
                "italy","none",null,7,8,1,null,5,1);
        assertEquals(true, mgr.addMovie(movie));
    }

    /**
     * Test of black box
     */

    public void testBlackBox() {
        Date date=new Date();
        date.setYear(1995);
        Genre genre=new Genre(100,"Crime");
        List<Genre> genres= Arrays.asList(genre);
        // name is empty
        StoredMovie movie = new StoredMovie(3000,"",1998,"A",date,"runtime",genres,"director","writer",null,"plot","english",
                "italy","none",null,7,8,1,null,5,1);
        created.add(movie);
        assertEquals(false, mgr.addMovie(movie));
        // name is null
        movie = new StoredMovie(3001,null,1998,"A",date,"runtime",genres,"director","writer",null,"plot","english",
                "italy","none",null,7,8,1,null,5,1);
        created.add(movie);
        assertFalse(mgr.addMovie(movie));
        // stock is <0
        movie = new StoredMovie(3002,"name3002",1998,"A",date,"runtime",genres,"director","writer",null,"plot","english",
                "italy","none",null,7,8,1,null,-1,1);
        created.add(movie);
        assertFalse(mgr.addMovie(movie));
        // rented is <0
        movie = new StoredMovie(3003,"name3003",1998,"A",date,"runtime",genres,"director","writer",null,"plot","english",
                "italy","none",null,7,8,1,null,5,-1);
        created.add(movie);
        assertFalse(mgr.addMovie(movie));

    }

    /**
     * Test. Delete 1 movie to the application
     */

    public void testDeleteMovie() {
        Date date=new Date();
        date.setYear(1995);
        Genre genre=new Genre(100,"Crime");
        List<Genre> genres= Arrays.asList(genre);
        Movie movie = new StoredMovie(6002,"name6002",1995,"A",date,"runtime",genres,"director","writer",null,"plot","english",
                "italy","none",null,7,8,1,null,5,1);
        mgr.addMovie(movie);
        assertEquals(true, mgr.deleteMovie(movie));
    }

    /**
     * Test. Update 1 movie to the application
     */
    public void testUpdateMovie() {
        Date date=new Date();
        date.setYear(1995);
        Genre genre=new Genre(100,"Crime");
        List<Genre> genres= Arrays.asList(genre);
        Movie movie = new StoredMovie(6003,"name6003",1998,"A",date,"runtime",genres,"director","writer",null,"plot","english",
                "italy","none",null,7,8,1,null,5,1);
        mgr.addMovie(movie);
        Movie newmovie = new StoredMovie(6003,"newname6003",1998,"A",date,"runtime",genres,"director","writer",null,"plot","english",
                "italy","none",null,7,8,1,null,5,1);
        assertTrue(mgr.updateMovie(newmovie));
    }

    /**
     * Elimina las notas creadas por los test. Se ejecuta al finalizar
     */

    @Override
    protected void tearDown() throws Exception {
        int total=created.size();
        while (total != 0) {
            mgr.deleteMovie(created.get(total-1));
            total--;
        }
        super.tearDown();
    }
}