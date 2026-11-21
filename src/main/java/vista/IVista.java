package vista;

import modelo.misiones.InformeMision;
import modelo.Asistente;

import java.util.ArrayList;

public interface IVista{
    DatosTripulante getDatosCapitan();
    ArrayList<DatosTripulante> getDatosTripulantes();
    String getTipoNave();
    void iniciarConfiguracionNave();
    void iniciarJuego(Asistente asistente);

    String getTipoMision();
    void actualizarVistaJuego(InformeMision informe);
    void mostrarError(String mensaje);
    void setControlesMisionHabilitados(boolean habilitado);

    String getNombre();
    String getContrasenia();
    void iniciarLogin();
    void notificarErrorLogin(String mensaje);
}
