package es.unizar.aisolutions.aimovie.control_module;

import android.media.Image;

import java.util.Vector;

/**
 * Created by USUARIO on 09/04/2015.
 */

public class Gestor implements InterfazGUI<Pelicula> {

    private Vector<Pelicula> listaPeliculas;
    private MonitorAlquiler monitor;

    public Gestor(){
        this.listaPeliculas = new Vector<Pelicula>();
        this.monitor = new MonitorAlquiler();
    }

    @Override
    public Pelicula obtenerDatos(String titulo) {

        for(int i = 0; i < listaPeliculas.size(); i++){
            // Compara la pelicula del vector con la que desea
            if( listaPeliculas.elementAt(i).getTitulo().equalsIgnoreCase(titulo) ){
                // Devuelve la pelicula que buscaban
                return listaPeliculas.elementAt(i);
            }
        }

        // Crea una pelicula auxiliar por que no encuentra la pelicula deseada
        Pelicula p = new Pelicula("es.unizar.aisolutions.aimovie.control_module.Pelicula no encontrada");

        return p;
    }

    @Override
    public Image obtenerImagen(String titulo) {
        for(int i = 0; i < listaPeliculas.size(); i++){
            // Compara la pelicula del vector con la que desea
            if( listaPeliculas.elementAt(i).getTitulo().equalsIgnoreCase(titulo) ){
                // Devuelve la pelicula que buscaban
                return listaPeliculas.elementAt(i).getImagen();
            }
        }

        // Crea una pelicula auxiliar por que no encuentra la pelicula deseada
        Pelicula p = new Pelicula("Imagen no encontrada");

        return p.getImagen();
    }

    @Override
    public boolean modificarDatos (String titulo, String categoria, String director, String sinopsis,
                                int estreno, int identificador, Image imagen, int disponibles) {

        for(int i = 0; i < listaPeliculas.size(); i++){
            // Compara la pelicula del vector con la que desea
            if( listaPeliculas.elementAt(i).getTitulo().equalsIgnoreCase(titulo) ){
                // Modifica la pelicula que buscaban
                listaPeliculas.elementAt(i).setTitulo(titulo);
                listaPeliculas.elementAt(i).setCategoria(categoria);
                listaPeliculas.elementAt(i).setDirector(director);
                listaPeliculas.elementAt(i).setSinopsis(sinopsis);
                listaPeliculas.elementAt(i).setAñoEstreno(estreno);
                listaPeliculas.elementAt(i).setImagen(imagen);
                listaPeliculas.elementAt(i).setImbd(identificador);
                listaPeliculas.elementAt(i).setCantidadDisponible(disponibles);

                return true;
            }
        }

        return false;

    }

    @Override
    public boolean modificarTitulo(int imbd, String tituloNuevo) {
        for(int i = 0; i < listaPeliculas.size(); i++){
            // Compara la pelicula del vector con la que desea
            if( listaPeliculas.elementAt(i).getImbd() == imbd ){
                // Modifica la pelicula que buscaban
                listaPeliculas.elementAt(i).setTitulo(tituloNuevo);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean modificarCategoria(String titulo, String categoriaNueva) {
        for(int i = 0; i < listaPeliculas.size(); i++){
            // Compara la pelicula del vector con la que desea
            if( listaPeliculas.elementAt(i).getTitulo().equalsIgnoreCase(titulo) ){
                // Modifica la pelicula que buscaban
                listaPeliculas.elementAt(i).setCategoria(categoriaNueva);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean modificarDirector(String titulo, String directorNuevo) {
        for(int i = 0; i < listaPeliculas.size(); i++){
            // Compara la pelicula del vector con la que desea
            if( listaPeliculas.elementAt(i).getTitulo().equalsIgnoreCase(titulo) ){
                // Modifica la pelicula que buscaban
                listaPeliculas.elementAt(i).setDirector(directorNuevo);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean modificarSipnosis(String titulo, String sinopsisNueva) {
        for(int i = 0; i < listaPeliculas.size(); i++){
            // Compara la pelicula del vector con la que desea
            if( listaPeliculas.elementAt(i).getTitulo().equalsIgnoreCase(titulo) ){
                // Modifica la pelicula que buscaban
                listaPeliculas.elementAt(i).setSinopsis(sinopsisNueva);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean modificarEstreno(String titulo, int estrenoNuevo) {
        for(int i = 0; i < listaPeliculas.size(); i++){
            // Compara la pelicula del vector con la que desea
            if( listaPeliculas.elementAt(i).getTitulo().equalsIgnoreCase(titulo) ){
                // Modifica la pelicula que buscaban
                listaPeliculas.elementAt(i).setAñoEstreno(estrenoNuevo);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean modificarImbd(String titulo, int imbdNuevo) {
        for(int i = 0; i < listaPeliculas.size(); i++){
            // Compara la pelicula del vector con la que desea
            if( listaPeliculas.elementAt(i).getTitulo().equalsIgnoreCase(titulo) ){
                // Modifica la pelicula que buscaban
                listaPeliculas.elementAt(i).setImbd(imbdNuevo);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean modificarImagen(String titulo, Image imagenNuevo) {
        for(int i = 0; i < listaPeliculas.size(); i++){
            // Compara la pelicula del vector con la que desea
            if( listaPeliculas.elementAt(i).getTitulo().equalsIgnoreCase(titulo) ){
                // Modifica la pelicula que buscaban
                listaPeliculas.elementAt(i).setImagen(imagenNuevo);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean solicitarPelicula(String titulo, int cantidad) {

        Pelicula peli;

        for(int i = 0; i < listaPeliculas.size(); i++){
            // Compara la pelicula del vector con la que desea
            if( listaPeliculas.elementAt(i).getTitulo().equalsIgnoreCase(titulo) ){
                peli = listaPeliculas.elementAt(i);
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

        for(int i = 0; i < listaPeliculas.size(); i++){
            // Compara la pelicula del vector con la que desea
            if( listaPeliculas.elementAt(i).getTitulo().equalsIgnoreCase(titulo) ){
                // Devuelve la pelicula que buscaban
                return monitor.devolver(listaPeliculas.elementAt(i), cantidad);
            }
        }
        return false;
    }

    @Override
    public void nuevaPelicula(String titulo, String categoria, String director, String sinopsis,
                              int estreno, int identificador, Image imagen, int disponibles) {
        boolean peliAnhadida = false;

        Pelicula peli = new Pelicula(titulo, categoria, director, sinopsis, estreno, identificador, imagen, disponibles);

        for(int i = 0; i < listaPeliculas.size(); i++){
            if( listaPeliculas.elementAt(i).getAñoEstreno() < peli.getAñoEstreno() ){
                listaPeliculas.add(i, peli);
                peliAnhadida = true;
                break;
            }
        }

        if(!peliAnhadida) {
            listaPeliculas.add(peli);
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

        for(int i = 0; i < listaPeliculas.size(); i++){
            // Compara la pelicula del vector con la que desea
            if( listaPeliculas.elementAt(i).getTitulo().equalsIgnoreCase(titulo) ){
                // Elimina la pelicula
                listaPeliculas.remove(listaPeliculas.elementAt(i));
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
