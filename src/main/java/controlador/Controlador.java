package controlador;

import modelo.Asistente;
import modelo.MotorWarp;
import modelo.Sistema;
import modelo.persistencia.DAOUsuarios;
import modelo.persistencia.DTOUsuario;
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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Controlador implements ActionListener {
    private IVista vista;
    private Sistema sistema;

    public Controlador(DAOUsuarios dao) {
        this.vista = null;
        this.sistema = new Sistema(dao);
    }

    public void setVista(IVista vista) {
        this.vista = vista;
        this.vista.setTiposNaves(NaveFactory.tipos_naves);
        this.vista.setTiposOficio(TripulanteFactory.tipos_oficio);
        this.vista.setTiposOrigen(TripulanteFactory.tipos_origen);
        this.vista.setTiposMisiones(MisionFactory.tipos_misiones);
        this.vista.setEstadosMotorWarp(MotorWarp.ESTADO_NOMBRE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if ("iniciar_secion".equals(cmd)) {
            DTOUsuario usuario = new DTOUsuario(vista.getNombre(), vista.getContrasenia());

            if (!this.sistema.dtoUsuarioCumpleFormatoMinimo(usuario)) {
                vista.notificarErrorLogin("Nombre y/o Contraseña vacia o de longitud mayor a 50");
            } else if (!sistema.iniciar_secion(usuario)) {
                vista.notificarErrorLogin("Datos de inicio de secion invalidos");
            } else {
                vista.iniciarConfiguracionNave();
            }
        } else if ("ir_registro".equals(cmd)) {
            // La vista ya cambia a la tarjeta de registro
        } else if ("registrarse".equals(cmd)) {
            DTOUsuario usuario = new DTOUsuario(vista.getNombre(), vista.getContrasenia());

            if (!this.sistema.dtoUsuarioCumpleFormatoMinimo(usuario)) {
                vista.notificarErrorLogin("Nombre y/o Contraseña vacia o de longitud mayor a 50");
            } else if (!sistema.registrar_usuario(usuario)) {
                vista.notificarErrorLogin("Registro insatisfactorio, ya hay un usuario con el nombre ingresado");
            } else {
                vista.iniciarLogin();
            }
        } else if ("inicializar".equals(cmd)) {
            try {
                Nave nave = NaveFactory.crearNave(vista.getTipoNave());
                Tablero tablero = GeneradorDeUniverso.porDefecto().crearTablero();
                Asistente asistente = new Asistente(nave, tablero);
                DatosTripulante datos_capitan = vista.getDatosCapitan();
                asistente.add_tripulante(TripulanteFactory.crearTripulante(
                        datos_capitan.getOficio(),
                        datos_capitan.getOrigen(),
                        datos_capitan.getNombre(),
                        datos_capitan.getIdentidad(),
                        datos_capitan.getAntiguedad()
                ));

                ArrayList<DatosTripulante> datos_tripulantes = vista.getDatosTripulantes();
                for (DatosTripulante datos_tripulante : datos_tripulantes) {
                    asistente.add_tripulante(TripulanteFactory.crearTripulante(
                            datos_tripulante.getOficio(),
                            datos_tripulante.getOrigen(),
                            datos_tripulante.getNombre(),
                            datos_tripulante.getIdentidad(),
                            datos_tripulante.getAntiguedad()
                    ));
                }

                this.sistema.setAsistente(asistente);
                this.vista.iniciarJuego(asistente);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if ("ejecutar_mision".equals(cmd)) {
            ejecutarMisionSeleccionada();
        } else if ("reiniciar".equals(cmd)) {
            this.sistema.setAsistente(null);
            this.vista.iniciarConfiguracionNave();
        }
    }

    private void ejecutarMisionSeleccionada() {
        String tipoMision = vista.getTipoMision();
        vista.setControlesMisionHabilitados(false);

        // Lanzamos la misión en un hilo separado para no congelar la interfaz
        new Thread(() -> {
            try {
                Mision mision = MisionFactory.crearMision(tipoMision, sistema.getAsistente());
                InformeMision informe = sistema.getAsistente().ejecutarMision(mision);

                vista.actualizarVistaJuego(informe);
                if (!sistema.getAsistente().getNave().getDestruida()) {
                    vista.setControlesMisionHabilitados(true);
                }
            } catch (Exception ex) {
                System.out.println("throws");
                vista.mostrarError(ex.getMessage());
                if (!sistema.getAsistente().getNave().getDestruida()) {
                    vista.setControlesMisionHabilitados(true);
                }
            }
        }).start();
    }
}