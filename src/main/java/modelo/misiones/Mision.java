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
    private int pausaTurnoMs = 600; // para que la vista pueda mostrar el avance
    protected boolean objetivoVerificado;     // resultado de la verificación en destino
    private EstadoRecursos recursosIniciales; // antes del consumo de despegue
    private EstadoRecursos recursosAlLlegar;  // al terminar la travesía
    private EstadoRecursos recursosFinales;   // después de aplicar el resultado en el cierre

    /**
     * Inicializa una misión asociada a un asistente.<br>
     * <b>Pre:</b><br>
     * - {@code asistente != null}.<br>
     * <b>Post:</b><br>
     * - La misión queda vinculada a {@code asistente}, con una {@link Bitacora} vacía y {@code exitosa = false}.
     *
     * @param asistente Asistente encargado de operar en la misión.
     * @throws IllegalArgumentException si {@code asistente} es nulo.
     */
    public Mision(Asistente asistente) {
        if (asistente == null) {
            throw new IllegalArgumentException("El asistente no puede ser nulo.");
        }
        assert asistente != null : "El asistente no puede ser nulo.";

        this.asistente = asistente;
        this.bitacora = new Bitacora();
        this.exitosa = false;
    }

    /**
     * Ejecuta el ciclo completo de la misión (preparación, ejecución, evaluación y cierre).<br>
     * <b>Pre:</b><br>
     * - {@code asistente != null} y {@code asistente.getNave() != null}.<br>
     * <b>Post:</b><br>
     * - Se ejecutan las etapas del ciclo y se devuelve el {@link InformeMision} resultante.
     *
     * @return Informe final de la misión.
     * @throws EstadoInvalido si la misión no puede prepararse por estado de la nave, recursos insuficientes, dotación inválida o mantenimiento pendiente.
     */
    public final InformeMision ejecutarCiclo() throws EstadoInvalido { //throws EstadoInvalido para que el que lo llame se entere si no se podia ejecutar aun
        assert asistente != null && asistente.getNave() != null : "El asistente y su nave deben estar inicializados.";

        InformeMision informe;

        preparar();
        ejecutar();
        evaluar();
        informe = cerrar();
        informe = cerrarHook(informe); // cerrarHook recibe el informe default por si le quiciese hacer alguna modificacion
        return informe;
    }

    /**
     * Valida el estado de la nave, recursos, tripulación y mantenimiento, y calcula el objetivo y el camino de la misión.<br>
     * <b>Pre:</b><br>
     * - {@code asistente != null} y {@code asistente.getNave() != null}.<br>
     * <b>Post:</b><br>
     * - Quedan definidos {@code objetivo} y {@code camino}, y se registra el resultado en la bitácora.
     *
     * @throws EstadoInvalido si la nave está destruida, faltan recursos, la tripulación no es válida o se exige mantenimiento pendiente.
     */
    protected void preparar() throws EstadoInvalido {
        assert asistente != null && asistente.getNave() != null : "El asistente y su nave deben estar inicializados.";

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

    protected void prepararHook(){}

    /**
     * Los recursos alcanzan si, después del consumo inicial, la nave sigue operativa:
     * combustible y energía mayores a 0 y desgaste menor al máximo.<br>
     * <b>Pre:</b><br>
     * - {@code nave != null}.<br>
     * <b>Post:</b><br>
     * - Devuelve {@code true} si la nave conserva combustible y energía positivos y desgaste menor a {@link Nave#DESGASTE_MAXIMO} tras aplicar los costos de la misión; {@code false} en caso contrario.
     *
     * @param nave Nave a evaluar.
     * @return {@code true} si los recursos alcanzan, {@code false} en caso contrario.
     */
    private boolean alcanzanLosRecursos(Nave nave) {
        assert nave != null : "La nave no puede ser nula.";

        return nave.getCombustible() > getCostoCombustible()
                && nave.getEnergia() > getCostoEnergia()
                && nave.getDesgaste() + getCostoDesgaste() < Nave.DESGASTE_MAXIMO;
    }

    /**
     * Indica si la misión exige que la nave no requiera mantenimiento.
     * Por defecto sí; el retorno a base lo redefine porque es la misión que hace el mantenimiento.<br>
     * <b>Pre:</b><br>
     * - Ninguna.<br>
     * <b>Post:</b><br>
     * - Devuelve {@code true}.
     *
     * @return {@code true} si se exige que la nave no requiera mantenimiento.
     */
    protected boolean exigeNaveSinMantenimientoPendiente() {
        return true;
    }

    /**
     * Objetivo de la misión. Por defecto lo calcula el radar de largo alcance; M-03 lo redefine (base de origen).<br>
     * <b>Pre:</b><br>
     * - {@code asistente != null}.<br>
     * <b>Post:</b><br>
     * - Devuelve la {@link Posicion} objetivo obtenida mediante {@link Asistente#usarRLD()}.
     *
     * @return Posición objetivo de la misión.
     */
    protected Posicion obtenerObjetivo() {
        assert asistente != null : "El asistente no puede ser nulo.";

        return asistente.usarRLD();
    }

    /**
     * Registra los recursos iniciales, aplica el consumo de la misión y recorre el tablero por turnos hasta llegar al objetivo o interrumpirse/destruirse.<br>
     * <b>Pre:</b><br>
     * - {@code asistente != null}, {@code asistente.getNave() != null}, {@code asistente.getTablero() != null}, {@code objetivo != null} y {@code camino != null}.<br>
     * <b>Post:</b><br>
     * - Se actualizan {@code recursosIniciales}, {@code recursosAlLlegar}, {@code objetivoVerificado} y {@code cartelResultado}, registrando las acciones en {@code bitacora}.
     */
    protected void ejecutar() {
        assert asistente != null && asistente.getNave() != null && asistente.getTablero() != null : "El asistente, su nave y su tablero deben estar inicializados.";
        assert objetivo != null : "El objetivo de la misión debe estar definido antes de ejecutar.";
        assert camino != null : "El camino de la misión debe estar inicializado antes de ejecutar.";

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
        bitacora.registrar("Acción realizada: " + this.cartelResultado);
    }

    /**
     * Verificación en destino, al llegar al objetivo. Por defecto el objetivo se cumple al llegar;
     * las misiones que exigen condiciones adicionales (M-01) la redefinen.<br>
     * <b>Pre:</b><br>
     * - Ninguna.<br>
     * <b>Post:</b><br>
     * - Devuelve {@code true}.
     *
     * @return {@code true} si se cumplen las condiciones en destino.
     */
    protected boolean verificarEnDestino() {
        return true;
    }

    /**
     * Evalúa el resultado final de la misión y registra en la bitácora el estado y el balance de recursos.<br>
     * <b>Pre:</b><br>
     * - {@code asistente != null}, {@code asistente.getNave() != null}, {@code recursosAlLlegar != null} y {@code recursosIniciales != null}.<br>
     * <b>Post:</b><br>
     * - {@code exitosa} queda en {@code true} si la nave no fue destruida y {@code objetivoVerificado} es {@code true}; en caso contrario queda en {@code false}.
     */
    protected void evaluar() {
        assert asistente != null && asistente.getNave() != null : "El asistente y su nave deben estar inicializados.";
        assert recursosAlLlegar != null && recursosIniciales != null : "Los estados de recursos inicial y al llegar deben estar registrados.";

        this.exitosa = !asistente.getNave().getDestruida() && objetivoVerificado;
        bitacora.registrar("Evaluación de la misión: " + (exitosa ? "ÉXITO" : "FALLO") + ".");
        bitacora.registrar("Balance de la travesía (" + recursosAlLlegar.variacionDesde(recursosIniciales) + ").");
    }

    /**
     * Aplica el resultado si la misión fue exitosa, registra los recursos finales y construye el informe de misión.<br>
     * <b>Pre:</b><br>
     * - {@code asistente != null} y {@code asistente.getNave() != null}.<br>
     * <b>Post:</b><br>
     * - Si {@code exitosa} es {@code true}, se invoca {@link #aplicarResultado()}; se actualiza {@code recursosFinales} y se devuelve un {@link InformeMision} no nulo.
     *
     * @return Informe final de la misión.
     */
    protected InformeMision cerrar() {
        assert asistente != null && asistente.getNave() != null : "El asistente y su nave deben estar inicializados.";

        if (exitosa) {
            aplicarResultado(); // ganancias o entregas de la misión, impactadas recién en el cierre
        }
        recursosFinales = EstadoRecursos.de(asistente.getNave());
        bitacora.registrar("Misión finalizada. Emitiendo informe.");
        return new InformeMision(this, asistente.getNave());
    }

    /**
     * Aplica en el cierre las ganancias o entregas de la misión. Solo se llama si la misión fue exitosa.<br>
     * <b>Pre:</b><br>
     * - La misión fue evaluada como exitosa.<br>
     * <b>Post:</b><br>
     * - Por defecto no aplica cambios adicionales.
     */
    protected void aplicarResultado() {
    }

    /**
     * Hook extensible para modificar o envolver el informe básico emitido en {@link #cerrar()}.<br>
     * <b>Pre:</b><br>
     * - Ninguna.<br>
     * <b>Post:</b><br>
     * - Por defecto devuelve {@code informe_basico}.
     *
     * @param informe_basico Informe generado por {@link #cerrar()}.
     * @return Informe final de la misión.
     */
    protected InformeMision cerrarHook(InformeMision informe_basico) {
        return informe_basico;
    }

    /**
     * Cambia la pausa entre turnos de la travesía.<br>
     * <b>Pre:</b><br>
     * - {@code pausaTurnoMs >= 0}.<br>
     * <b>Post:</b><br>
     * - El tiempo de pausa entre turnos queda actualizado a {@code pausaTurnoMs}.
     *
     * @param pausaTurnoMs milisegundos de pausa, mayor o igual a 0
     */
    public void setPausaTurno(int pausaTurnoMs) {
        assert pausaTurnoMs >= 0 : "La pausa entre turnos no puede ser negativa.";

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

    /**
     * Obtiene el código identificador de la misión.<br>
     * <b>Pre:</b><br>
     * - Ninguna.<br>
     * <b>Post:</b><br>
     * - Devuelve una cadena no nula y no vacía con el código de la misión.
     *
     * @return Código de la misión.
     */
    public abstract String getCodigo();

    /**
     * Obtiene el costo de combustible requerido para la misión.<br>
     * <b>Pre:</b><br>
     * - Ninguna.<br>
     * <b>Post:</b><br>
     * - Devuelve un valor mayor o igual a {@code 0}.
     *
     * @return Costo de combustible.
     */
    public abstract int getCostoCombustible();

    /**
     * Obtiene el costo de energía requerido para la misión.<br>
     * <b>Pre:</b><br>
     * - Ninguna.<br>
     * <b>Post:</b><br>
     * - Devuelve un valor mayor o igual a {@code 0}.
     *
     * @return Costo de energía.
     */
    public abstract int getCostoEnergia();

    /**
     * Obtiene el desgaste producido por la misión.<br>
     * <b>Pre:</b><br>
     * - Ninguna.<br>
     * <b>Post:</b><br>
     * - Devuelve un valor mayor o igual a {@code 0}.
     *
     * @return Costo de desgaste.
     */
    public abstract int getCostoDesgaste();

    /**
     * Acción en el destino: describe lo que hace la misión al llegar (sin modificar recursos, eso va en el cierre).<br>
     * <b>Pre:</b><br>
     * - La nave ha alcanzado el objetivo sin ser destruida.<br>
     * <b>Post:</b><br>
     * - Devuelve una cadena descriptiva de la acción realizada en el destino.
     *
     * @return Cartel descriptivo del resultado de la misión.
     */
    protected abstract String ejecutarMision();

}