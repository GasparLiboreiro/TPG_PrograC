package vista;

import java.awt.event.WindowListener;
import java.util.Observer;

public interface IVistaConcurrencia extends Observer {

    public interface AccionEntidadListener {
        void onCrearGeneradora(String nombre);
        void onCrearNave(String nombre);
    }

    void setAccionEntidadListener(AccionEntidadListener listener);

    void addWindowListener(WindowListener listener);

    void mostrar();

    void cerrar();

    void agregarLineaLog(String texto);
}