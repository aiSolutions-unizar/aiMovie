import android.media.Image;

/** Es la interfaz que se encarga de interactuar con la interfaz grafica */

interface InterfazGUI<E> {

    /****************************************
     *************** PELICULA ***************
     ****************************************/

    // Metodo que se llamara cada vez que se solicite ver la informacion de una pelicula
    E obtenerDatos(String titulo);

    // Metodo que se llamara cada vez que se solicite ver la imagen de una pelicula
    Image obtenerImagen(String titulo);

    // Metodo que se llamara cada vez que se cambia la informacion de una pelicula
    boolean modificarDatos(String titulo, String categoria, String director, String sinopsis,
                           int estreno, int identificador, Image imagen, int disponibles);
    boolean modificarTitulo(int imbd, String tituloNuevo);
    boolean modificarCategoria(String titulo, String categoriaNueva);
    boolean modificarDirector(String titulo, String directorNuevo);
    boolean modificarSipnosis(String titulo, String sipnosisNueva);
    boolean modificarEstreno(String titulo, int estrenoNuevo);
    boolean modificarImbd(String titulo, int imbdNuevo);
    boolean modificarImagen(String titulo, Image imagenNuevo);

    // Metodo que se llama para que muestre las peliculas ordenadas por estreno
   // void mostrarPeliculas();

    // Metodo que se llamara cada vez que se solicite pedir una pelicula una pelicula
    boolean solicitarPelicula(String titulo, int cantidad);

    // Metodo que se llamara cada vez que se quiera anhadir una pelicula mas a las disponibles
    boolean devolverPelicula(String titulo, int cantidad);

    /****************************************
     *************** OPCIONES ***************
     ****************************************/

    // Metodo que se llamara cada vez que se quiera anhadir una pelicula nueva
    void nuevaPelicula(String titulo, String categoria, String director, String sinopsis,
                       int estreno, int identificador, Image imagen, int disponibles);

    //Metodo que añade una pelicula nueva mediante el imbd de la pelicula deseada
    void nuevaPelicula(int imdb);

    // Metodo que se llamara cada vez que se quiera quitar una pelicula de la lista
    void borrarPelicula(String titulo);

    // Metodo que se llamara cada vez que se quiera anhadir una pelicula nueva
    void actualizarBaseDatos();

    // Metodo que se llamara cada vez que se quiera buscar pelicula
    void buscar();

}
