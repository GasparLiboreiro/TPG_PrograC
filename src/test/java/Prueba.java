import javax.swing.SwingUtilities;

import controlador.Controlador;
import vista.IVista;
import vista.Ventana;



public class Prueba {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Controlador controlador = new Controlador();
            Ventana ventana = new Ventana(controlador);
            controlador.setVista(ventana);
            ventana.iniciarConfiguracionNave();
        });
    }
}
