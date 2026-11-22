package controlador;

import modelo.Concurrencia.*;
import vista.IVistaCapacidad;
import vista.IVistaConcurrencia;
import vista.VentanaCapacidadPlataforma;
import vista.VentanaVisorConcurrencia;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class ControladorConcurrencia {
    private PlataformaDeCarga plataforma;
    private IVistaConcurrencia visor;
    private final List<GeneradoraDeEnergia> generadoras = new ArrayList<GeneradoraDeEnergia>();
    private final List<NaveConsumidora> naves = new ArrayList<NaveConsumidora>();
    private final List<Thread> hilos = new ArrayList<Thread>();

    public ControladorConcurrencia() {
    }

    public void iniciarFlujoCapacidad() {
        IVistaCapacidad dialogo = new VentanaCapacidadPlataforma();
        Integer capacidad = dialogo.mostrarYEsperarCapacidad();

        if (capacidad == null) {
            return;
        }

        limpiarSimulacion();
        this.plataforma = new PlataformaDeCarga(capacidad.intValue());
        abrirVentanaVisor(this.plataforma);
    }

    protected void abrirVentanaVisor(final PlataformaDeCarga plataformaActual) {
        IVistaConcurrencia.AccionEntidadListener accionListener =
                new IVistaConcurrencia.AccionEntidadListener() {
                    @Override
                    public void onCrearGeneradora(String nombre) {
                        crearGeneradora(nombre);
                    }

                    @Override
                    public void onCrearNave(String nombre) {
                        crearNave(nombre);
                    }
                };

        this.visor = new VentanaVisorConcurrencia(
                plataformaActual.getCapacidad(),
                accionListener
        );
        this.visor.setAccionEntidadListener(accionListener);

        plataformaActual.addObserver(this.visor);

        this.visor.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                limpiarSimulacion();
            }
        });

        this.visor.agregarLineaLog("Plataforma iniciada con capacidad "
                + plataformaActual.getCapacidad() + ".");
        this.visor.mostrar();
    }

    private void crearGeneradora(String nombre) {
        if (this.plataforma == null) {
            return;
        }
        GeneradoraDeEnergia generadora = new GeneradoraDeEnergia(nombre, this.plataforma);
        Thread hilo = new Thread(generadora, "Generadora-" + nombre);
        this.generadoras.add(generadora);
        this.hilos.add(hilo);
        hilo.start();
        if (this.visor != null) {
            this.visor.agregarLineaLog("Se agregó la generadora " + nombre + ".");
        }
    }

    private void crearNave(String nombre) {
        if (this.plataforma == null) {
            return;
        }
        NaveConsumidora nave = new NaveConsumidora(nombre, this.plataforma);
        Thread hilo = new Thread(nave, "Nave-" + nombre);
        this.naves.add(nave);
        this.hilos.add(hilo);
        hilo.start();
        if (this.visor != null) {
            this.visor.agregarLineaLog("Se agregó la nave " + nombre + ".");
        }
    }

    public void limpiarSimulacion() {
        for (GeneradoraDeEnergia g : this.generadoras) {
            g.detener();
        }
        for (NaveConsumidora n : this.naves) {
            n.detener();
        }
        for (Thread t : this.hilos) {
            t.interrupt();
        }

        if (this.plataforma != null) {
            this.plataforma.detener();
        }

        if (this.plataforma != null && this.visor != null) {
            this.plataforma.deleteObserver(this.visor);
        }

        this.generadoras.clear();
        this.naves.clear();
        this.hilos.clear();
        this.plataforma = null;
        this.visor = null;
    }

    public PlataformaDeCarga getPlataforma() {
        return this.plataforma;
    }
}