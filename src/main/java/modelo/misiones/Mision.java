/* cambios que creo a futuro a la espera de asistente(hecha asi para test):
* -integrar los estados del motor a preparar y ejecutar
*
* */

package modelo.misiones;
import modelo.Bitacora;
import modelo.ElementoDelUniverso;
import modelo.excepciones.EstadoInvalido;
import modelo.Asistente;
import modelo.excepciones.TransicionEstadoInvalida;
import modelo.nave.*;
import modelo.destinoytablero.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

public abstract class Mision {
    protected final Asistente asistente;
    protected Posicion objetivo;
    protected final Bitacora bitacora;
    protected String cartelResultado;
    protected boolean exitosa;
    protected ArrayList<Posicion> camino = new ArrayList<>();
    private int pausaTurnoMs = 300; // para que la vista pueda mostrar el avance


    public Mision(Asistente asistente) {
        if (asistente == null) {
            throw new IllegalArgumentException("El asistente no puede ser nulo.");
        }
        this.asistente = asistente;
        this.bitacora = new Bitacora();
        this.exitosa = false;
    }

    public final InformeMision ejecutarCiclo() throws EstadoInvalido { //throws EstadoInvalido para que el que lo llame se entere si no se podia ejecutar aun
        InformeMision informe;

        preparar();
        prepararHook();
        ejecutar();
        ejecutarHook();
        evaluar();
        evaluarHook();
        informe = cerrar();
        informe = cerrarHook(informe); // cerrarHook recibe el informe default por si le quiciese hacer alguna modificacion
        return informe;
    }


    protected void preparar() throws EstadoInvalido {
        bitacora.registrar("Preparando misión " + getCodigo() + ". Verificando recursos y tripulacion.");

        if (asistente.getNave().getCombustible() < getCostoCombustible() || asistente.getNave().getEnergia() < getCostoEnergia()) {

            bitacora.registrar("Fallo en preparación: recursos insuficientes.");

            throw new EstadoInvalido("Recursos insuficientes para iniciar la misión " + getCodigo() + ".");

        }
        if (!asistente.getNave().tieneTripulacionValida()) {
            bitacora.registrar("Fallo en preparación: dotación incompleta. Se requiere 1 capitán y al menos 4 tripulantes adicionales.");

            throw new EstadoInvalido("La nave no cuenta con la dotación mínima reglamentaria para iniciar la misión.");
}

        //EN CASO DE QUE SE PUEDA HACER LA MISION SE OBTIENE EL OBJETIVO Y SE PREPARA LA RUTA.
        objetivo = asistente.usarRLD();
        camino = asistente.generarCamino(objetivo);

        bitacora.registrar("Preparación exitosa: recursos suficientes.");
    }
    protected void prepararHook() {
    }

    protected void ejecutar() {
        Destino destino = Destino.getInstancia(asistente.getTablero());
        Nave nave = asistente.getNave();
        int indiceCamino = 0;                        // posición actual de la nave dentro de camino
        ElementoDelUniverso ultimoElemento = null;   // evita interactuar dos veces con el mismo elemento mientras la nave espera

        nave.consumirCombustible(getCostoCombustible());
        nave.consumirEnergia(getCostoEnergia());
        nave.aumentarDesgaste(getCostoDesgaste());
        bitacora.registrar("Consumo aplicado: -" + getCostoCombustible() + " comb, -" +
                getCostoEnergia() + " energ, +" + getCostoDesgaste() + " desgaste.");

        while (!nave.getDestruida() && !asistente.getPosicion().equals(objetivo)) {
            // A. Turno de la nave
            // 1. Inspección de la posición actual
            ElementoDelUniverso elemento = asistente.analizarPosicion();
            if (elemento != null && elemento != ultimoElemento) {
                elemento.interactuar(nave);
                ultimoElemento = elemento;
            }
            if (nave.getDestruida()) {
                break;
            }

            // 2. Escaneo con el radar de corto alcance
            boolean hayObstaculo = asistente.usarRCD(asistente.getPosicion(), camino);

            // 3. Planificación y movimiento: el estado del motor decide cuántas casillas avanza
            int casillas = asistente.ordenarSalto(hayObstaculo);
            if (casillas > 0) {
                indiceCamino = Math.min(indiceCamino + casillas, camino.size() - 1);
                asistente.moverNave(camino.get(indiceCamino));
                ultimoElemento = null;
            }

            // B. Turno del tablero
            destino.turnoDestino();

            try {
                Thread.sleep(pausaTurnoMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }


        if (asistente.getNave().getDestruida()) {
            this.cartelResultado = "La nave fue destruida durante la travesía. Misión " + getCodigo() + " fallida.";
        } else {
            this.cartelResultado = ejecutarMision();
        }
        bitacora.registrar("Acción realizada: " + this.cartelResultado);

    }
    protected void ejecutarHook() {
    }

    protected void evaluar() {
        this.exitosa = !asistente.getNave().getDestruida() && this.cartelResultado != null && !this.cartelResultado.isEmpty();
        bitacora.registrar("Evaluación de la misión: " + (exitosa ? "ÉXITO" : "FALLO"));

    }
    protected void evaluarHook() {
    }

    protected InformeMision cerrar() {
        bitacora.registrar("Misión finalizada. Emitiendo informe.");
        return new InformeMision(this, asistente.getNave());
    }
    protected InformeMision cerrarHook(InformeMision informe_basico) {
        return informe_basico;
    }
    /**
     * Cambia la pausa entre turnos de la travesía.
     * @param pausaTurnoMs milisegundos de pausa, mayor o igual a 0
     */
    public void setPausaTurno(int pausaTurnoMs) {
        this.pausaTurnoMs = pausaTurnoMs;
    }

    // Getters para InformeMision
    public boolean isExitosa() {
        return exitosa;
    }
    public String getCartelResultado() {
        return cartelResultado;
    }
    public Bitacora getBitacora() {
        return bitacora;
    }

    // Métodos a definir por las misiones
    public abstract String getCodigo();
    public abstract int getCostoCombustible();
    public abstract int getCostoEnergia();
    public abstract int getCostoDesgaste();
    protected abstract String ejecutarMision();

}