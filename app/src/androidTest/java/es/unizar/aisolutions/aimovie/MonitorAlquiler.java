/**
 * Created by Santiago on 09/04/2015.
 */
public class MonitorAlquiler {

    public MonitorAlquiler (){

    }

    synchronized boolean alquilar(Pelicula peli, int cantidad){
        int totalAlquiladas;
        int totalDisponibles;
        int alquiladas;
        int disponibles;

        try {
            // Obtenemos la cantidades necesarias
            disponibles = peli.getCantidadDisponible();
            alquiladas= peli.getCantidadAlquiladas();

            // Obtenemos las nuevas cantiadades
            totalDisponibles = disponibles - cantidad;
            totalAlquiladas = alquiladas + cantidad;

            // Actualizamos los valores
            peli.setCantidadDisponible(totalDisponibles);
            peli.setCantidadAlquiladas(totalAlquiladas);

            return true;
        }catch (NullPointerException e){
            e.printStackTrace();
            System.out.println("Se produjo problemas en la reserva de la pelicula.");
        }

        return  false;
    }

    synchronized boolean devolver(Pelicula peli, int cantidad){
        int totalAlquiladas;
        int totalDisponibles;
        int alquiladas;
        int disponibles;

        try {
            // Obtenemos la cantidades necesarias
            disponibles = peli.getCantidadDisponible();
            alquiladas= peli.getCantidadAlquiladas();

            // Obtenemos las nuevas cantiadades
            totalDisponibles = disponibles + cantidad;
            totalAlquiladas = alquiladas - cantidad;

            // Actualizamos los valores
            peli.setCantidadDisponible(totalDisponibles);
            peli.setCantidadAlquiladas(totalAlquiladas);

            return true;

        }catch (NullPointerException e){
            e.printStackTrace();
            System.out.println("Se produjo problemas en la devolucion de la pelicula.");
        }

        return  false;
    }


}
//dolphin