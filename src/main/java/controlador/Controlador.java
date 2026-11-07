package controlador;

import modelo.Asistente;
import modelo.tripulantes.TripulanteFactory;
import vista.DatosTripulante;
import vista.IVista;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Controlador implements ActionListener {
    private IVista vista;
    private Asistente asistente;

    public Controlador() {
        this.vista = null;
        this.asistente = null;
    }

    public void setVista(IVista vista) {
        this.vista = vista;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("inicializar")) {
            try {
                this.asistente = new Asistente();
                this.asistente.create_nave(vista.getTipoNave());
                DatosTripulante datos_capitan = vista.getDatosCapitan();
                this.asistente.add_tripulante(TripulanteFactory.crearTripulante(
                            datos_capitan.getOficio(),
                            datos_capitan.getOrigen(),
                            datos_capitan.getNombre(),
                            datos_capitan.getIdentidad(),
                            datos_capitan.getAntiguedad()
                ));

                ArrayList<DatosTripulante> datos_tripulantes = vista.getDatosTripulantes();
                for(DatosTripulante datos_tripulante : datos_tripulantes) {
                    this.asistente.add_tripulante(TripulanteFactory.crearTripulante(
                                datos_tripulante.getOficio(),
                                datos_tripulante.getOrigen(),
                                datos_tripulante.getNombre(),
                                datos_tripulante.getIdentidad(),
                                datos_tripulante.getAntiguedad()
                    ));
                }

                this.vista.iniciarJuego(this.asistente);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
