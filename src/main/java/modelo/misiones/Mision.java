/* cambios que creo a futuro a la espera de asistente(hecha asi para test):
* -integrar los estados del motor a preparar y ejecutar
*
* */

package modelo.misiones;
import modelo.Bitacora;
import modelo.ElementoDelUniverso;
import modelo.MotorWarp;
import modelo.excepciones.EstadoInvalido;
import modelo.Asistente;
import modelo.nave.*;
import modelo.destinoytablero.*;

import java.util.ArrayList;

public abstract class Mision {
    protected final Asistente asistente;
    protected Posicion objetivo;
    protected final Bitacora bitacora;
    protected String cartelResultado;
    protected boolean exitosa;
    protected ArrayList<Posicion> camino = new ArrayList<>();
    private int pausaTurnoMs = 300; // para que la vista pueda mostrar el avance
    protected boolean objetivoVerificado;     // resultado de la verificación en destino
    private EstadoRecursos recursosIniciales; // antes del consumo de despegue
    private EstadoRecursos recursosAlLlegar;  // al terminar la travesía
    private EstadoRecursos recursosFinales;   // después de aplicar el resultado en el cierre


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

        if (asistente.getNave().getDestruida()) {
            bitacora.registrar("Fallo en preparación: la nave está destruida.");
            throw new EstadoInvalido("La nave está destruida y no puede iniciar la misión " + getCodigo() + ".");
        }

        if (!alcanzanLosRecursos(asistente.getNave())) {

            bitacora.registrar("Fallo en preparación: recursos insuficientes.");

            throw new EstadoInvalido("Recursos insuficientes para iniciar la misión " + getCodigo() + ".");

        }
        if (!asistente.getNave().tieneTripulacionValida()) {
            bitacora.registrar("Fallo en preparación: dotación incompleta. Se requiere 1 capitán y al menos 4 tripulantes adicionales.");

            throw new EstadoInvalido("La nave no cuenta con la dotación mínima reglamentaria para iniciar la misión.");
}

        if (exigeNaveSinMantenimientoPendiente() && asistente.getNave().requiereMantenimiento()) {
            bitacora.registrar("Fallo en preparación: la nave requiere mantenimiento (desgaste " + asistente.getNave().getDesgaste() + "). Solo puede realizar el retorno a base.");
            throw new EstadoInvalido("La nave requiere mantenimiento: solo puede ejecutar el retorno a base (M-03).");
        }

        //EN CASO DE QUE SE PUEDA HACER LA MISION SE OBTIENE EL OBJETIVO Y SE PREPARA LA RUTA.
        objetivo = obtenerObjetivo();
        camino = asistente.generarCamino(objetivo);

        bitacora.registrar("Preparación exitosa: recursos suficientes.");
    }
    /**
     * Los recursos alcanzan si, después del consumo inicial, la nave sigue operativa:
     * combustible y energía mayores a 0 y desgaste menor al máximo.
     */
    private boolean alcanzanLosRecursos(Nave nave) {
        return nave.getCombustible() > getCostoCombustible()
                && nave.getEnergia() > getCostoEnergia()
                && nave.getDesgaste() + getCostoDesgaste() < Nave.DESGASTE_MAXIMO;
    }

    /**
     * Indica si la misión exige que la nave no requiera mantenimiento.
     * Por defecto sí; el retorno a base lo redefine porque es la misión que hace el mantenimiento.
     */
    protected boolean exigeNaveSinMantenimientoPendiente() {
        return true;
    }

    /** Objetivo de la misión. Por defecto lo calcula el radar de largo alcance; M-03 lo redefine (base de origen). */
    protected Posicion obtenerObjetivo() {
        return asistente.usarRLD();
    }

    protected void prepararHook() {
    }

    protected void ejecutar() {
        Destino destino = Destino.getInstancia(asistente.getTablero());
        Nave nave = asistente.getNave();
        int indiceCamino = 0;                        // posición actual de la nave dentro de camino

        recursosIniciales = EstadoRecursos.de(nave);

        //CONSUMO DE RECURSOS DE LA MISION
        nave.consumirCombustible(getCostoCombustible());
        nave.consumirEnergia(getCostoEnergia());
        nave.aumentarDesgaste(getCostoDesgaste());
        bitacora.registrar("Consumo aplicado: -" + getCostoCombustible() + " comb, -" +
                getCostoEnergia() + " energ, +" + getCostoDesgaste() + " desgaste.");



        while (!nave.getDestruida() && !asistente.getPosicion().equals(objetivo)) {
            // A. Turno de la nave
            // 1. Inspección de la posición actual
            ElementoDelUniverso elemento = asistente.analizarPosicion();
            if (elemento != null) {
                bitacora.registrar(elemento.interactuar(nave));
            }
            if (nave.getDestruida()) {
                break;
            }

            // 2. Escaneo con el radar de corto alcance: sólo informa si hay un elemento en la próxima casilla
            boolean hayElemento = asistente.usarRCD(asistente.getPosicion(), camino);
            if (hayElemento) {
                bitacora.registrar("Radar: elemento detectado en la próxima casilla. Se intenta un salto warp.");
            }

            // 3. Planificación y movimiento: el estado del motor decide cuántas casillas avanza
            int casillas = asistente.ordenarSalto(hayElemento);
            String eventoMotor = asistente.getUltimoEventoMotor();
            if (eventoMotor != null) {
                bitacora.registrar(eventoMotor);
            }
            if (casillas > 0) {
                indiceCamino = Math.min(indiceCamino + casillas, camino.size() - 1);
                asistente.moverNave(camino.get(indiceCamino));
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


        recursosAlLlegar = EstadoRecursos.de(nave);
        if (nave.getDestruida()) {
            this.objetivoVerificado = false;
            this.cartelResultado = "La nave fue destruida durante la travesía. Misión " + getCodigo() + " fallida.";
        } else if (!asistente.getPosicion().equals(objetivo)) {
            this.objetivoVerificado = false;
            this.cartelResultado = "La misión " + getCodigo() + " fue interrumpida antes de llegar al objetivo.";
        } else {
            // Verificación en destino: la misión comprueba si puede cumplir su objetivo (el descuento se hace en el cierre)
            this.objetivoVerificado = verificarEnDestino();
            this.cartelResultado = ejecutarMision();
        }
        bitacora.registrar("Acción realizada: " + this.cartelResultado);}
    protected void ejecutarHook() {
    }

    /**
     * Verificación en destino, al llegar al objetivo. Por defecto el objetivo se cumple al llegar;
     * las misiones que exigen condiciones adicionales (M-01) la redefinen.
     */
    protected boolean verificarEnDestino() {
        return true;
    }

    protected void evaluar() {
        this.exitosa = !asistente.getNave().getDestruida() && objetivoVerificado;
        bitacora.registrar("Evaluación de la misión: " + (exitosa ? "ÉXITO" : "FALLO") + ".");
        bitacora.registrar("Balance de la travesía (" + recursosAlLlegar.variacionDesde(recursosIniciales) + ").");
    }
    protected void evaluarHook() {
    }

    protected InformeMision cerrar() {
        if (exitosa) {
            aplicarResultado(); // ganancias o entregas de la misión, impactadas recién en el cierre
        }
        recursosFinales = EstadoRecursos.de(asistente.getNave());
        bitacora.registrar("Misión finalizada. Emitiendo informe.");
        return new InformeMision(this, asistente.getNave());
    }

    /** Aplica en el cierre las ganancias o entregas de la misión. Solo se llama si la misión fue exitosa. */
    protected void aplicarResultado() {
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
    public EstadoRecursos getRecursosIniciales() { return recursosIniciales; }
    public EstadoRecursos getRecursosAlLlegar() { return recursosAlLlegar; }
    public EstadoRecursos getRecursosFinales() { return recursosFinales; }

    public Bitacora getBitacora() {
        return bitacora;
    }

    // Métodos a definir por las misiones
    public abstract String getCodigo();
    public abstract int getCostoCombustible();
    public abstract int getCostoEnergia();
    public abstract int getCostoDesgaste();
    /** Acción en el destino: describe lo que hace la misión al llegar (sin modificar recursos, eso va en el cierre). */
    protected abstract String ejecutarMision();

}