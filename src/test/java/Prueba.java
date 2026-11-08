import controlador.Controlador;
import modelo.Asistente;
import modelo.ElementoDelUniverso;
import modelo.destinoytablero.Tablero;
import modelo.excepciones.EstadoInvalido;
import modelo.misiones.*;
import modelo.nave.Nave;
import modelo.nave.NaveFactory;
import modelo.tripulantes.*;
import vista.IVista;
import vista.Ventana;

import java.util.ArrayList;
import java.util.Iterator;

public class Prueba {
    private final ArrayList<ElementoDelUniverso> elementos = new ArrayList<>();
    private int x = 10;
    private int y = 10;
    public Tablero tablero = new Tablero(x,y, elementos);
    public static void main(String[] args) {
        Controlador controlador = new Controlador();
        IVista vista = new Ventana(controlador);
        controlador.setVista(vista);
        vista.iniciarConfiguracionNave();
    }
}
