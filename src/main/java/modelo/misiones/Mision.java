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
    private int pausaTurnoMs = 1000; // para que la vista pueda mostrar el avance

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
     * - El asistente y su nave deben estar en condiciones válidas para iniciar el ciclo.<br>
     * <b>Post:</b><br>
     * - Se completan las etapas del ciclo y se devuelve el {@link InformeMision} resultante.
     *
     * @return Informe final de la misión.
     * @throws EstadoInvalido si la misión no puede prepararse o iniciarse por falta de recursos o tripulación inválida.
     */
    public final InformeMision ejecutarCiclo() throws EstadoInvalido {
        InformeMision informe;

        preparar();
        ejecutar();
        evaluar();
        informe = cerrar();
        informe = cerrarHook(informe); // cerrarHook recibe el informe default por si le quiciese hacer alguna modificacion
        return informe;
    }



    /**
     * Valida recursos y tripulación, y calcula el objetivo y el camino de la misión.<br>
     * <b>Pre:</b><br>
     * - {@code asistente != null} y {@code asistente.getNave() != null}.<br>
     * <b>Post:</b><br>
     * - Quedan definidos {@code objetivo} y {@code camino}, y se registra el avance en la bitácora.
     *
     * @throws EstadoInvalido si faltan combustible/energía o si la tripulación no es válida.
     */
    protected void preparar() throws EstadoInvalido {
        assert asistente != null && asistente.getNave() != null : "El asistente y su nave deben estar inicializados.";

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





    /**
     * Aplica el consumo inicial de recursos y avanza la nave por turnos hasta llegar al objetivo o destruirse.<br>
     * <b>Pre:</b><br>
     * - {@code asistente != null}, {@code asistente.getNave() != null}, {@code objetivo != null} y {@code camino != null}.<br>
     * <b>Post:</b><br>
     * - Se consumen recursos, se recorre el tablero y se asigna {@code cartelResultado} junto con sus registros en {@code bitacora}.
     */
    protected void ejecutar() {
        assert asistente != null && asistente.getNave() != null : "El asistente y su nave deben estar inicializados.";
        assert objetivo != null : "El objetivo de la misión debe estar definido antes de ejecutar.";
        assert camino != null : "El camino de la misión debe estar inicializado antes de ejecutar.";

        Destino destino = Destino.getInstancia(asistente.getTablero());
        Nave nave = asistente.getNave();
        int indiceCamino = 0;                        // posición actual de la nave dentro de camino

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

            // 2. Escaneo con el radar de corto alcance
            boolean hayObstaculo = asistente.usarRCD(asistente.getPosicion(), camino);
            if (hayObstaculo) {
                bitacora.registrar("Obstáculo detectado. Estado del motor: Motor Warp ["
                        + MotorWarp.ESTADO_NOMBRE[asistente.getEstadoMotorWarp()] + "].");
            }

            // 3. Planificación y movimiento: el estado del motor decide cuántas casillas avanza
            int casillas = asistente.ordenarSalto(hayObstaculo);
            if (casillas == 2) {
                bitacora.registrar("¡Salto Warp ejecutado! La nave avanzó 2 casillas evadiendo el obstáculo.");
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

        if (asistente.getNave().getDestruida()) {
            this.cartelResultado = "La nave fue destruida durante la travesía. Misión " + getCodigo() + " fallida.";
        } else {
            this.cartelResultado = ejecutarMision();
        }
        bitacora.registrar("Acción realizada: " + this.cartelResultado);
    }


    /**
     * Evalúa si la misión resultó exitosa y registra el resultado en la bitácora.<br>
     * <b>Pre:</b><br>
     * - {@code asistente != null} y {@code asistente.getNave() != null}.<br>
     * <b>Post:</b><br>
     * - {@code exitosa} queda en {@code true} si la nave no fue destruida y {@code cartelResultado} no es nulo ni vacío; en caso contrario queda en {@code false}.
     */
    protected void evaluar() {
        assert asistente != null && asistente.getNave() != null : "El asistente y su nave deben estar inicializados.";

        this.exitosa = !asistente.getNave().getDestruida() && this.cartelResultado != null && !this.cartelResultado.isEmpty();
        bitacora.registrar("Evaluación de la misión: " + (exitosa ? "ÉXITO" : "FALLO"));
    }



    /**
     * Registra la finalización y construye el informe básico de la misión.<br>
     * <b>Pre:</b><br>
     * - {@code asistente != null} y {@code asistente.getNave() != null}.<br>
     * <b>Post:</b><br>
     * - Devuelve un {@link InformeMision} no nulo construido a partir de esta misión y la nave actual.
     *
     * @return Informe básico de la misión.
     */
    protected InformeMision cerrar() {
        assert asistente != null && asistente.getNave() != null : "El asistente y su nave deben estar inicializados.";

        bitacora.registrar("Misión finalizada. Emitiendo informe.");
        return new InformeMision(this, asistente.getNave());
    }

    /**
     * Hook extensible para personalizar o envolver el informe emitido en {@link #cerrar()}.<br>
     * <b>Pre:</b><br>
     * - Ninguna.<br>
     * <b>Post:</b><br>
     * - Por defecto devuelve el mismo {@code informe_basico} recibido.
     *
     * @param informe_basico Informe generado por {@link #cerrar()}.
     * @return Informe final a retornar.
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
     * Ejecuta la acción específica de la misión al alcanzar el objetivo.<br>
     * <b>Pre:</b><br>
     * - La nave ha alcanzado el objetivo sin ser destruida.<br>
     * <b>Post:</b><br>
     * - Devuelve un mensaje descriptivo del resultado de la acción realizada.
     *
     * @return Cartel descriptivo del resultado de la misión.
     */
    protected abstract String ejecutarMision();

}