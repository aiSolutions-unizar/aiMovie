package es.unizar.aisolutions.aimovie.control_module;

import android.media.Image;

/**
 * Created by Santiago on 08/04/2015.
 */

public class Movie {

    private String titulo;
    private String categoria;
    private String director;
    private String sinopsis;
    private int añoEstreno;
    private int imbd;
    private Image imagen;

    private int cantidadDisponible;
    private int cantidadAlquiladas;


    public Movie(String titulo, String categoria, String director, String sinopsis,
                 int estreno, int identificador, Image imagen, int disponibles) {
        this.titulo = titulo;
        this.categoria = categoria;
        this.director = director;
        this.sinopsis = sinopsis;
        this.añoEstreno = estreno;
        this.imbd = identificador;
        this.imagen = imagen;
        this.cantidadDisponible = disponibles;
        this.cantidadAlquiladas = 0;
    }

    public Movie(String noEncontrada) {
        this.titulo = noEncontrada;
    }

    public String getTitulo() {
        return titulo;
    }

    /**
     * ******************
     * **** SET & GET *****
     * *******************
     */

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public Image getImagen() {
        return imagen;
    }

    public void setImagen(Image imagen) {
        this.imagen = imagen;
    }

    public int getAñoEstreno() {
        return añoEstreno;
    }

    public void setAñoEstreno(int añoEstreno) {
        this.añoEstreno = añoEstreno;
    }

    public int getImbd() {
        return imbd;
    }

    public void setImbd(int imbd) {
        this.imbd = imbd;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(int cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }

    public int getCantidadAlquiladas() {
        return cantidadAlquiladas;
    }

    public void setCantidadAlquiladas(int cantidadAlquiladas) {
        this.cantidadAlquiladas = cantidadAlquiladas;
    }
}