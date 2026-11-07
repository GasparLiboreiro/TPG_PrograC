package vista;

import modelo.Asistente;

import java.util.ArrayList;

public interface IVista {
    DatosTripulante getDatosCapitan();
    ArrayList<DatosTripulante> getDatosTripulantes();
    String getTipoNave();
    void iniciarConfiguracionNave();
    void iniciarJuego(Asistente asistente);
}
