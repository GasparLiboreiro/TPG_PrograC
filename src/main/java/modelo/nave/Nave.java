package modelo.nave;

import modelo.ElementoDelUniverso;
import modelo.MotorWarp;
import modelo.tripulantes.*;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Nave implements ElementoDelUniverso, Batalla {

    private final MotorWarp motor_warp;
    private int combustible;
    private int energia;
    private int desgaste;
    private int gemas;
    private boolean destruida;
    private final int combustibleMax;
    private final int energiaMax;

    private final ArrayList<Tripulante> tripulantes = new ArrayList<>();



    /**
     * Inicializa una nave con sus valores iniciales de combustible, energía y desgaste.<br>
     * <b>Pre:</b><br>
     * - {@code combustible >= 0}, {@code energia >= 0} y {@code desgaste >= 0}.<br>
     * <b>Post:</b><br>
     * - La nave queda inicializada con los recursos indicados, {@code gemas = 0}, un {@link MotorWarp} asociado y su estado verificado.
     *
     * @param combustible Combustible inicial y máximo de la nave.
     * @param energia Energía inicial y máxima de la nave.
     * @param desgaste Desgaste inicial de la nave.
     */
    protected Nave(int combustible, int energia, int desgaste) {
        assert combustible >= 0 : "El combustible inicial no puede ser negativo.";
        assert energia >= 0 : "La energía inicial no puede ser negativa.";
        assert desgaste >= 0 : "El desgaste inicial no puede ser negativo.";

        this.combustible = combustible;
        this.energia = energia;
        this.desgaste = desgaste;
        this.gemas = 0;
        this.motor_warp = new MotorWarp(this);
        this.combustibleMax = combustible;
        this.energiaMax = energia;

        verificarEstado();
    }

    public int getEnergiaMax() {
        return energiaMax;
    }

    public int getCombustibleMax() {
        return combustibleMax;
    }

    public int getGemas() {
        return gemas;
    }

    /**
     * Verifica que la nave no se encuentre destruida.<br>
     * <b>Pre:</b><br>
     * - Ninguna.<br>
     * <b>Post:</b><br>
     * - Si {@code destruida} es {@code true}, lanza {@link IllegalStateException}; en caso contrario no modifica el estado.
     *
     * @throws IllegalStateException si la nave está destruida.
     */
    private void verificarEstado() {
        if (destruida) {
            throw new IllegalStateException("La nave está destruida.");
        }
    }

    /**
     * Evalúa si los recursos o el desgaste obligan a destruir la nave.<br>
     * <b>Pre:</b><br>
     * - Ninguna.<br>
     * <b>Post:</b><br>
     * - Si {@code combustible == 0}, {@code energia == 0} o {@code desgaste >= 100}, la nave pasa a estado destruido.
     */
    private void verificarRecursos() {
        if (combustible == 0 || desgaste >= 100) {
            destruir();
        }
    }


    /**
     * Marca la nave como destruida.<br>
     * <b>Pre:</b><br>
     * - Ninguna.<br>
     * <b>Post:</b><br>
     * - {@code destruida} queda establecido en {@code true}.
     */
    public void destruir() {
        destruida = true;
    }

    public boolean getDestruida() {
        return destruida;
    }

    public int getCombustible() {
        return combustible;
    }

    public int getEnergia() {
        return energia;
    }

    public int getDesgaste() {
        return desgaste;
    }

    public void setDesgaste(int cantidad) {
        this.desgaste = cantidad;
    }

    public int getEstadoMotorWarp() {
        return this.motor_warp.getEstado();
    }

    /**
     * Consume una cantidad de combustible de la nave y verifica sus recursos.<br>
     * <b>Pre:</b><br>
     * - La nave no debe estar destruida.<br>
     * - {@code cantidad >= 0}.<br>
     * <b>Post:</b><br>
     * - El combustible disminuye en {@code cantidad} (mínimo {@code 0}) y se verifica si la nave queda destruida por falta de recursos.
     *
     * @param cantidad Cantidad de combustible a consumir.
     * @throws IllegalStateException si la nave está destruida o si {@code cantidad} supera el combustible disponible.
     */
    public void consumirCombustible(int cantidad) {
        verificarEstado();
        assert cantidad >= 0 : "La cantidad de combustible a consumir no puede ser negativa.";

        if (cantidad > combustible) {
            throw new IllegalStateException("Combustible insuficiente.");
        }

        combustible = Math.max(0, combustible - cantidad);
        verificarRecursos();
    }

    /**
     * Consume una cantidad de energía de la nave y verifica sus recursos.<br>
     * <b>Pre:</b><br>
     * - La nave no debe estar destruida.<br>
     * - {@code cantidad >= 0}.<br>
     * <b>Post:</b><br>
     * - La energía disminuye en {@code cantidad} (mínimo {@code 0}) y se verifica si la nave queda destruida por falta de recursos.
     *
     * @param cantidad Cantidad de energía a consumir.
     * @throws IllegalStateException si la nave está destruida o si {@code cantidad} supera la energía disponible.
     */
    public void consumirEnergia(int cantidad) {
        verificarEstado();
        assert cantidad >= 0 : "La cantidad de energía a consumir no puede ser negativa.";

        if (cantidad > energia) {
            throw new IllegalStateException("Energía insuficiente.");
        }

        energia = Math.max(0, energia - cantidad);
        verificarRecursos();
    }

    /**
     * Incrementa el desgaste de la nave y verifica si alcanza el límite de destrucción.<br>
     * <b>Pre:</b><br>
     * - La nave no debe estar destruida.<br>
     * - {@code cantidad >= 0}.<br>
     * <b>Post:</b><br>
     * - El desgaste aumenta según {@code cantidad} y se verifica si la nave debe destruirse.
     *
     * @param cantidad Valor de desgaste a sumar.
     * @throws IllegalStateException si la nave está destruida.
     */
    public void aumentarDesgaste(int cantidad) {
        verificarEstado();
        assert cantidad >= 0 : "La cantidad de desgaste a aumentar no puede ser negativa.";

        desgaste += Math.min(100, desgaste + cantidad);
        verificarRecursos();
    }

    /**
     * Carga combustible en la nave hasta un máximo de 100.<br>
     * <b>Pre:</b><br>
     * - {@code cantidad >= 0}.<br>
     * <b>Post:</b><br>
     * - El combustible aumenta en {@code cantidad} sin superar {@code 100}.
     *
     * @param cantidad Cantidad de combustible a cargar.
     */
    public void cargarCombustible(int cantidad) {
        assert cantidad >= 0 : "La cantidad de combustible a cargar no puede ser negativa.";

        if (cantidad + combustible < 100) {
            this.combustible += cantidad;
        } else {
            this.combustible = 100;
        }
    }

    /**
     * Carga energía en la nave hasta un máximo de 100.<br>
     * <b>Pre:</b><br>
     * - {@code cantidad >= 0}.<br>
     * <b>Post:</b><br>
     * - La energía aumenta en {@code cantidad} sin superar {@code 100}.
     *
     * @param cantidad Cantidad de energía a cargar.
     */
    public void cargarEnergia(int cantidad) {
        assert cantidad >= 0 : "La cantidad de energía a cargar no puede ser negativa.";

        if (cantidad + energia < 100) {
            this.energia += cantidad;
        } else {
            this.energia = 100;
        }
    }

    /**
     * - Añade un tripulante a la nave</br>
     * - <b>Pre:</b> </br>
     * - El tripulante debe ser un tripulante valido (no nulo)</br>
     * - @throws Exception si ya hay un tripulante registrado con el mismo identificador o si se intenta añadir un capitan ya habiendo un capitan en la tripulacion
     * - @param tripulante Tripulante a añadir a la nave
     */
    public void addTripulante(Tripulante tripulante) throws Exception { // hago que lanze la exepcion en vez de ser runtime para que no rompa el sistema y que lo resuelva
        verificarEstado();
        assert tripulante != null : "El tripulante a agregar no puede ser nulo.";

        // 1. Validar identidad repetida
        for (Tripulante existente : tripulantes) {
            if (existente.getIdentidad() != null && existente.getIdentidad().equalsIgnoreCase(tripulante.getIdentidad())) {
                throw new Exception("Ya existe un tripulante registrado con la identidad: " + tripulante.getIdentidad());
            }
        }

        if ("capitan".equalsIgnoreCase(tripulante.getCargo()) && tieneCapitan()) {
            throw new Exception("La nave ya cuenta con un Capitán asignado.");
        }
        tripulantes.add(tripulante);
    }

    /**
     * Indica si la nave ya cuenta con un capitán en su tripulación.<br>
     * <b>Pre:</b><br>
     * - Ninguna.<br>
     * <b>Post:</b><br>
     * - Devuelve {@code true} si existe al menos un tripulante con cargo {@code "capitan"} (ignorando mayúsculas/minúsculas), o {@code false} en caso contrario.
     *
     * @return {@code true} si hay un capitán asignado, {@code false} en caso contrario.
     */
    public boolean tieneCapitan() {
        for (Tripulante t : tripulantes) {
            if ("capitan".equalsIgnoreCase(t.getCargo())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Indica si la tripulación cumple con los requisitos mínimos para operar.<br>
     * <b>Pre:</b><br>
     * - Ninguna.<br>
     * <b>Post:</b><br>
     * - Devuelve {@code true} si hay al menos 5 tripulantes y un capitán asignado, o {@code false} en caso contrario.
     *
     * @return {@code true} si la tripulación es válida, {@code false} en caso contrario.
     */
    public boolean tieneTripulacionValida() {
        return tripulantes.size() >= 5 && tieneCapitan();
    }

    public Iterator<Tripulante> getTripulantes() {
        return java.util.Collections
                .unmodifiableList(tripulantes)
                .iterator();
    }

    /**
     * - Turno de movimiento con el camino despejado. Delega en el motor warp.
     * - @return casillas que avanza la nave (0, 1 o 2)
     */
    public int saltar() {
        verificarEstado();
        return motor_warp.saltar();
    }

    /**
     * - Turno de movimiento con un obstáculo adelante. Delega en el motor warp.
     * - @return casillas que avanza la nave (0, 1 o 2)
     */
    public int saltarWarp() {
        verificarEstado();
        return motor_warp.saltarWarp();
    }

    /**
     * Ejecuta la interacción de esta nave con otra nave.<br>
     * <b>Pre:</b><br>
     * - {@code nave != null}.<br>
     * <b>Post:</b><br>
     * - Devuelve una descripción textual del resultado de la interacción.
     *
     * @param nave Nave con la que se interactúa.
     * @return Resultado de la interacción en formato texto.
     */
    @Override
    public abstract String interactuar(Nave nave);

    /**
     * Indica si la nave actúa como obstáculo en el tablero.<br>
     * <b>Pre:</b><br>
     * - Ninguna.<br>
     * <b>Post:</b><br>
     * - Devuelve {@code true}.
     *
     * @return {@code true}.
     */
    @Override
    public boolean esObstaculo() {
        return true;
    }

    // el unico proposito de este metodo es hacer mas facil el caso en que se enfrentan dos naves iguales
    public abstract String getTipo();

    /**
     * Determina si esta nave vence en combate a la nave objetivo.<br>
     * <b>Pre:</b><br>
     * - {@code obj != null}.<br>
     * <b>Post:</b><br>
     * - Devuelve {@code true} si esta nave gana el enfrentamiento contra {@code obj}, o {@code false} en caso contrario.
     *
     * @param obj Nave rival.
     * @return {@code true} si esta nave gana, {@code false} en caso contrario.
     */
    public abstract boolean gana(Nave obj);

    /**
     * Determina si esta nave vence a una nave exploradora.<br>
     * <b>Pre:</b><br>
     * - Ninguna.<br>
     * <b>Post:</b><br>
     * - Devuelve {@code true} si esta nave vence a una nave exploradora, o {@code false} en caso contrario.
     *
     * @return {@code true} si vence a una nave exploradora, {@code false} en caso contrario.
     */
    public abstract boolean ganaExploradora();

    /**
     * Determina si esta nave vence a una nave carguera.<br>
     * <b>Pre:</b><br>
     * - Ninguna.<br>
     * <b>Post:</b><br>
     * - Devuelve {@code true} si esta nave vence a una nave carguera, o {@code false} en caso contrario.
     *
     * @return {@code true} si vence a una nave carguera, {@code false} en caso contrario.
     */
    public abstract boolean ganaCarguera();

    /**
     * Determina si esta nave vence a una nave de combate.<br>
     * <b>Pre:</b><br>
     * - Ninguna.<br>
     * <b>Post:</b><br>
     * - Devuelve {@code true} si esta nave vence a una nave de combate, o {@code false} en caso contrario.
     *
     * @return {@code true} si vence a una nave de combate, {@code false} en caso contrario.
     */
    public abstract boolean ganaCombate();

    /**
     * Devuelve una representación en cadena del estado actual de la nave.<br>
     * <b>Pre:</b><br>
     * - Ninguna.<br>
     * <b>Post:</b><br>
     * - Devuelve una cadena no nula con los atributos principales de la nave.
     *
     * @return Representación textual de la nave.
     */
    @Override
    public String toString() {
        return getClass().getSimpleName()
                + " {combustible=" + combustible
                + ", energia=" + energia
                + ", desgaste=" + desgaste
                + ", destruida=" + destruida + "}";
    }

}