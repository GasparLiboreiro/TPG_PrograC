import javax.swing.SwingUtilities;

import controlador.Controlador;
import modelo.persistencia.DAOUsuarios;
import modelo.persistencia.DAOUsuariosSQLite;
import vista.IVista;
import vista.Ventana;



public class Prueba {
    public static void main(String[] args) {
            DAOUsuarios dao = new DAOUsuariosSQLite();
            Controlador controlador = new Controlador(dao);
            Ventana ventana = new Ventana(controlador);
            controlador.setVista(ventana);
            ventana.iniciarLogin();
    }
}
