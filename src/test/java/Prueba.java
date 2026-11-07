import controlador.Controlador;
import modelo.Asistente;
import modelo.excepciones.EstadoInvalido;
import modelo.misiones.*;
import modelo.nave.Nave;
import modelo.nave.NaveFactory;
import modelo.tripulantes.*;
import vista.IVista;
import vista.Ventana;

import java.util.Iterator;

public class Prueba {
    public static void main(String[] args) {
        Controlador controlador = new Controlador();
        IVista vista = new Ventana(controlador);
        controlador.setVista(vista);
        vista.iniciarConfiguracionNave();
    }
}
