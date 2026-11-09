import controlador.Controlador;
import vista.IVista;
import vista.Ventana;



public class Prueba {

    public static void main(String[] args) {
        Controlador controlador = new Controlador();
        IVista vista = new Ventana(controlador);
        controlador.setVista(vista);
        vista.iniciarConfiguracionNave();
    }
}
