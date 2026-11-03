import modelo.Asistente;
import modelo.misiones.Mision;
import modelo.misiones.MisionFactory;
import modelo.nave.Nave;
import modelo.nave.NaveFactory;
import modelo.tripulantes.TripulanteFactory;

import java.util.Iterator;

public class Prueba {
    public static void main(String[] args)
    {
    	Nave n = NaveFactory.crearNave("carguero");
        Asistente asistente = new Asistente(n);
        Mision m = MisionFactory.crearMision("Recoleccion", n);

        asistente.addTripulante(TripulanteFactory.crearTripulante("CAPITAN", "TERRICOLA", "Juan Hernandez", "GFA-322", 10));
        asistente.addTripulante(TripulanteFactory.crearTripulante("ALFEREZ", "VULCANO", "Enzo Fanti", "FFF-115", 2));

        asistente.preparar_salto();
        asistente.arrancar();

        asistente.setMision(m);
        asistente.ejecutarMision();

        asistente.detener();
        asistente.enfriar_motor();

        asistente.mostrarHaberesTripulantes();

        Iterator<String> i_entradas = asistente.getBitacoraGeneral().getEntradas().iterator();
        String entrada;
        while(i_entradas.hasNext()){
            entrada = i_entradas.next();
            System.out.println("- "+entrada);
        }


    }
}
