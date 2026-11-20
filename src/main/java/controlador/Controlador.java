package controlador;

import modelo.Asistente;
import modelo.tripulantes.TripulanteFactory;
import modelo.nave.Nave;
import modelo.nave.NaveFactory;
import modelo.destinoytablero.Tablero;
import modelo.destinoytablero.GeneradorDeUniverso;
import modelo.misiones.Mision;
import modelo.misiones.MisionFactory;
import modelo.misiones.InformeMision;

import vista.DatosTripulante;
import vista.IVista;

import javax.swing.SwingUtilities;
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
                Nave nave = NaveFactory.crearNave(vista.getTipoNave());
                Tablero tablero = GeneradorDeUniverso.porDefecto().crearTablero();
                this.asistente = new Asistente(nave, tablero);
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
        } else if (e.getActionCommand().equals("ejecutar_mision")) {
            ejecutarMisionSeleccionada();
        } else if (e.getActionCommand().equals("reiniciar")) {
            this.asistente = null;
            this.vista.iniciarConfiguracionNave();
}
    }

    private void ejecutarMisionSeleccionada() {
        String tipoMision = vista.getTipoMision();
        vista.setControlesMisionHabilitados(false);

        // Lanzamos la misión en un hilo separado para no congelar la interfaz Swing
        new Thread(() -> {
            try {
                Mision mision = MisionFactory.crearMision(tipoMision, asistente);
                InformeMision informe = asistente.ejecutarMision(mision);

                // Volvemos al hilo de Swing para actualizar los componentes visuales
                SwingUtilities.invokeLater(() -> {
                    vista.actualizarVistaJuego(informe);
                    if (!asistente.getNave().getDestruida()) {
                        vista.setControlesMisionHabilitados(true);
                    }
                });
            } catch (Exception ex) {
                SwingUtilities.invokeLater(() -> {
                    vista.mostrarError(ex.getMessage());
                    if (!asistente.getNave().getDestruida()) {
                        vista.setControlesMisionHabilitados(true);
                    }
                });
            }
        }).start();
    }
}
