package controlador;

import modelo.Asistente;
import modelo.Sistema;
import modelo.persistencia.DAOUsuarios;
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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("iniciar_secion"))
        {
            System.out.println("LogIn "+vista.getNombre()+" "+vista.getContrasenia());
            if(vista.getNombre()==null || vista.getNombre().length()==0 || vista.getNombre().length()>50)
                vista.notificarErrorLogin("Longitud de nombre invalida, debe tener entre 1 y 50 caracteres");
            else if(vista.getContrasenia()==null || vista.getContrasenia().length()==0 || vista.getContrasenia().length()>50)
                vista.notificarErrorLogin("Longitud de contrasenia invalida, debe tener entre 1 y 50 caracteres");
            else if(sistema.iniciar_secion(vista.getNombre(), vista.getContrasenia()))
                vista.iniciarConfiguracionNave();
            else
                vista.notificarErrorLogin("Datos de inicio de secion invalidos");
        }
        else if(e.getActionCommand().equals("registrarse"))
        {
            if(vista.getNombre()==null || vista.getNombre().length()==0 || vista.getNombre().length()>50)
                vista.notificarErrorLogin("Longitud de nombre invalida, debe tener entre 1 y 50 caracteres");
            else if(vista.getContrasenia()==null || vista.getContrasenia().length()==0 || vista.getContrasenia().length()>50)
                vista.notificarErrorLogin("Longitud de contrasenia invalida, debe tener entre 1 y 50 caracteres");
            else if(sistema.registrar_usuario(vista.getNombre(), vista.getContrasenia()))
                vista.iniciarConfiguracionNave();
            else
                vista.notificarErrorLogin("Registro insatisfactorio, ya hay un usuario con el nombre ingresado");
        }
        else if(e.getActionCommand().equals("inicializar")) {
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
                for(DatosTripulante datos_tripulante : datos_tripulantes) {
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
        } else if (e.getActionCommand().equals("ejecutar_mision")) {
            ejecutarMisionSeleccionada();
        } else if (e.getActionCommand().equals("reiniciar")) {
            this.sistema.setAsistente(null);
            this.vista.iniciarConfiguracionNave();
        }
    }

    private void ejecutarMisionSeleccionada() {
        String tipoMision = vista.getTipoMision();
        vista.setControlesMisionHabilitados(false);

        // Lanzamos la misión en un hilo separado para no congelar la interfaz Swing
        new Thread(() -> {
            try {
                Mision mision = MisionFactory.crearMision(tipoMision, sistema.getAsistente());
                InformeMision informe = sistema.getAsistente().ejecutarMision(mision);

                vista.actualizarVistaJuego(informe);
                if (!sistema.getAsistente().getNave().getDestruida()) {
                    vista.setControlesMisionHabilitados(true);
                }
            } catch (Exception ex) {
                vista.mostrarError(ex.getMessage());
                if (!sistema.getAsistente().getNave().getDestruida()) {
                    vista.setControlesMisionHabilitados(true);
                }
            }
        }).start();
    }
}
