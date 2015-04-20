package es.unizar.aisolutions.aimovie.control_module;

import android.media.Image;

import java.util.Vector;

/**
 * Created by Santiago Peralta on 09/04/2015.
 */

public class Manager implements InterfazGUI<Movie> {

    private Vector<Movie> movieList;
    private MonitorRental monitor;

    public Manager(){
        this.movieList = new Vector<Movie>();
        this.monitor = new MonitorRental();
    }

    @Override
    public Movie obtenerDatos(String titulo) {

        for(int i = 0; i < movieList.size(); i++){
            // Compara la pelicula del vector con la que desea
            if( movieList.elementAt(i).getTitulo().equalsIgnoreCase(titulo) ){
                // Devuelve la pelicula que buscaban
                return movieList.elementAt(i);
            }
        }

        // Crea una pelicula auxiliar por que no encuentra la pelicula deseada
        Movie p = new Movie("Movie not found");

        return p;
    }

    @Override
    public Image obtenerImagen(String titulo) {
        for(int i = 0; i < movieList.size(); i++){
            // Compara la pelicula del vector con la que desea
            if( movieList.elementAt(i).getTitulo().equalsIgnoreCase(titulo) ){
                // Devuelve la pelicula que buscaban
                return movieList.elementAt(i).getImagen();
            }
        }

        // Crea una pelicula auxiliar por que no encuentra la pelicula deseada
        Movie p = new Movie("Imagen no encontrada");

        return p.getImagen();
    }

    @Override
    public boolean modificarDatos (String titulo, String categoria, String director, String sinopsis,
                                int estreno, int identificador, Image imagen, int disponibles) {

        for(int i = 0; i < movieList.size(); i++){
            // Compara la pelicula del vector con la que desea
            if( movieList.elementAt(i).getTitulo().equalsIgnoreCase(titulo) ){
                // Modifica la pelicula que buscaban
                movieList.elementAt(i).setTitulo(titulo);
                movieList.elementAt(i).setCategoria(categoria);
                movieList.elementAt(i).setDirector(director);
                movieList.elementAt(i).setSinopsis(sinopsis);
                movieList.elementAt(i).setAñoEstreno(estreno);
                movieList.elementAt(i).setImagen(imagen);
                movieList.elementAt(i).setImbd(identificador);
                movieList.elementAt(i).setCantidadDisponible(disponibles);

                return true;
            }
        }

        return false;

    }

    @Override
    public boolean modificarTitulo(int imbd, String tituloNuevo) {
        for(int i = 0; i < movieList.size(); i++){
            // Compara la pelicula del vector con la que desea
            if( movieList.elementAt(i).getImbd() == imbd ){
                // Modifica la pelicula que buscaban
                movieList.elementAt(i).setTitulo(tituloNuevo);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean modificarCategoria(String titulo, String categoriaNueva) {
        for(int i = 0; i < movieList.size(); i++){
            // Compara la pelicula del vector con la que desea
            if( movieList.elementAt(i).getTitulo().equalsIgnoreCase(titulo) ){
                // Modifica la pelicula que buscaban
                movieList.elementAt(i).setCategoria(categoriaNueva);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean modificarDirector(String titulo, String directorNuevo) {
        for(int i = 0; i < movieList.size(); i++){
            // Compara la pelicula del vector con la que desea
            if( movieList.elementAt(i).getTitulo().equalsIgnoreCase(titulo) ){
                // Modifica la pelicula que buscaban
                movieList.elementAt(i).setDirector(directorNuevo);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean modificarSipnosis(String titulo, String sinopsisNueva) {
        for(int i = 0; i < movieList.size(); i++){
            // Compara la pelicula del vector con la que desea
            if( movieList.elementAt(i).getTitulo().equalsIgnoreCase(titulo) ){
                // Modifica la pelicula que buscaban
                movieList.elementAt(i).setSinopsis(sinopsisNueva);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean modificarEstreno(String titulo, int estrenoNuevo) {
        for(int i = 0; i < movieList.size(); i++){
            // Compara la pelicula del vector con la que desea
            if( movieList.elementAt(i).getTitulo().equalsIgnoreCase(titulo) ){
                // Modifica la pelicula que buscaban
                movieList.elementAt(i).setAñoEstreno(estrenoNuevo);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean modificarImbd(String titulo, int imbdNuevo) {
        for(int i = 0; i < movieList.size(); i++){
            // Compara la pelicula del vector con la que desea
            if( movieList.elementAt(i).getTitulo().equalsIgnoreCase(titulo) ){
                // Modifica la pelicula que buscaban
                movieList.elementAt(i).setImbd(imbdNuevo);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean modificarImagen(String titulo, Image imagenNuevo) {
        for(int i = 0; i < movieList.size(); i++){
            // Compara la pelicula del vector con la que desea
            if( movieList.elementAt(i).getTitulo().equalsIgnoreCase(titulo) ){
                // Modifica la pelicula que buscaban
                movieList.elementAt(i).setImagen(imagenNuevo);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean solicitarPelicula(String titulo, int cantidad) {

        Movie peli;

        for(int i = 0; i < movieList.size(); i++){
            // Compara la pelicula del vector con la que desea
            if( movieList.elementAt(i).getTitulo().equalsIgnoreCase(titulo) ){
                peli = movieList.elementAt(i);
                // Devuelve la pelicula que buscaban
                if(peli.getCantidadDisponible() > cantidad){
                    return monitor.alquilar(peli, cantidad);
                } else {
                    System.out.print("No hay peliculassuficiente para realizar la reserva.");
                }
            }
        }
        return false;
    }

    @Override
    public boolean devolverPelicula(String titulo, int cantidad) {

        for(int i = 0; i < movieList.size(); i++){
            // Compara la pelicula del vector con la que desea
            if( movieList.elementAt(i).getTitulo().equalsIgnoreCase(titulo) ){
                // Devuelve la pelicula que buscaban
                return monitor.devolver(movieList.elementAt(i), cantidad);
            }
        }
        return false;
    }

    @Override
    public void nuevaPelicula(String titulo, String categoria, String director, String sinopsis,
                              int estreno, int identificador, Image imagen, int disponibles) {
        boolean peliAnhadida = false;

        Movie peli = new Movie(titulo, categoria, director, sinopsis, estreno, identificador, imagen, disponibles);

        for(int i = 0; i < movieList.size(); i++){
            if( movieList.elementAt(i).getAñoEstreno() < peli.getAñoEstreno() ){
                movieList.add(i, peli);
                peliAnhadida = true;
                break;
            }
        }

        if(!peliAnhadida) {
            movieList.add(peli);
        }
    }

    /**
     * Anhade automatiamente la pelicula
     * @param imdb, por lo se se identificara la pelicula
     */
    @Override
    public void nuevaPelicula(int imdb) {

    }

    @Override
    public void borrarPelicula(String titulo) {

        for(int i = 0; i < movieList.size(); i++){
            // Compara la pelicula del vector con la que desea
            if( movieList.elementAt(i).getTitulo().equalsIgnoreCase(titulo) ){
                // Elimina la pelicula
                movieList.remove(movieList.elementAt(i));
            }
        }

    }

    @Override
    public void actualizarBaseDatos() {

    }

    @Override
    public void buscar() {

    }
}
