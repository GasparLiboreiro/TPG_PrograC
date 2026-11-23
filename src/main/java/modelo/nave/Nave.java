package modelo.nave;

import modelo.ElementoDelUniverso;
import modelo.MotorWarp;
import modelo.tripulantes.*;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Nave implements ElementoDelUniverso,Batalla {

    /** Capacidad máxima de combustible y de energía, igual para todos los tipos (Ficha de Inicio). */
    public static final int CAPACIDAD_MAXIMA = 100;
    /** Desgaste máximo: al alcanzarlo la nave se destruye. */
    public static final int DESGASTE_MAXIMO = 100;
    /** Desgaste a partir del cual la nave requiere mantenimiento (Ficha de Inicio). */
    public static final int UMBRAL_MANTENIMIENTO = 80;

    private final MotorWarp motor_warp;
    private int combustible;
    private int energia;
    private int desgaste;
    private int gemas;
    private boolean destruida;

    private final ArrayList<Tripulante> tripulantes = new ArrayList<>();

    /**
     * <b>Pre:</b> combustible y energía entre 0 y CAPACIDAD_MAXIMA; desgaste entre 0 y DESGASTE_MAXIMO.
     * @throws IllegalArgumentException si algún valor inicial está fuera de rango
     */
    protected Nave(int combustible, int energia, int desgaste) {
        validarRango("combustible", combustible, CAPACIDAD_MAXIMA);
        validarRango("energía", energia, CAPACIDAD_MAXIMA);
        validarRango("desgaste", desgaste, DESGASTE_MAXIMO);
        this.combustible = combustible;
        this.energia = energia;
        this.desgaste = desgaste;
        this.gemas = 0;
        this.motor_warp = new MotorWarp(this);
    }

    private static void validarRango(String recurso, int valor, int maximo) {
        if (valor < 0 || valor > maximo) {
            throw new IllegalArgumentException("Valor inicial de " + recurso + " fuera de rango: " + valor);
        }
    }

    private static void validarCantidad(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa: " + cantidad);
        }
    }

    public int getEnergiaMax() {
        return CAPACIDAD_MAXIMA;
    }

    public int getCombustibleMax() {
        return CAPACIDAD_MAXIMA;
    }

    /** @return combustible que todavía entra hasta la capacidad máxima */
    public int getEspacioCombustible() {
        return CAPACIDAD_MAXIMA - combustible;
    }

    /** @return energía que todavía entra hasta la capacidad máxima */
    public int getEspacioEnergia() {
        return CAPACIDAD_MAXIMA - energia;
    }

    public int getGemas() { return gemas; }

    private void verificarEstado() {
        if (destruida) {
            throw new IllegalStateException("La nave está destruida.");
        }
    }

    private void verificarRecursos() {
        if (combustible == 0 || energia == 0 || desgaste >= DESGASTE_MAXIMO) {
            destruir();
        }
    }

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

    /** @return true si el desgaste alcanzó el umbral de mantenimiento */
    public boolean requiereMantenimiento() {
        return desgaste >= UMBRAL_MANTENIMIENTO;
    }

    /**
     * Realiza el mantenimiento: lleva el desgaste a 0.
     * @throws IllegalStateException si la nave está destruida
     */
    public void realizarMantenimiento() {
        verificarEstado();
        desgaste = 0;
    }

    public int getEstadoMotorWarp() {
        return this.motor_warp.getEstado();
    }

    /**
     * <b>Pre:</b> la nave no está destruida, 0 &lt;= cantidad &lt;= combustible disponible.
     * <b>Post:</b> el combustible baja en cantidad; si llega a 0 la nave se destruye.
     * @throws IllegalArgumentException si cantidad es negativa
     * @throws IllegalStateException si la nave está destruida o no hay combustible suficiente (sin modificar la nave)
     */
    public void consumirCombustible(int cantidad) {
        verificarEstado();
        validarCantidad(cantidad);
        if (cantidad > combustible) {
            throw new IllegalStateException("Combustible insuficiente.");
        }
        combustible -= cantidad;
        verificarRecursos();
    }

    /**
     * <b>Pre:</b> la nave no está destruida, 0 &lt;= cantidad &lt;= energía disponible.
     * <b>Post:</b> la energía baja en cantidad; si llega a 0 la nave se destruye.
     * @throws IllegalArgumentException si cantidad es negativa
     * @throws IllegalStateException si la nave está destruida o no hay energía suficiente (sin modificar la nave)
     */
    public void consumirEnergia(int cantidad) {
        verificarEstado();
        validarCantidad(cantidad);
        if (cantidad > energia) {
            throw new IllegalStateException("Energía insuficiente.");
        }
        energia -= cantidad;
        verificarRecursos();
    }

    /**
     * <b>Post:</b> el desgaste sube en cantidad, con tope DESGASTE_MAXIMO; al alcanzarlo la nave se destruye.
     * @throws IllegalArgumentException si cantidad es negativa
     * @throws IllegalStateException si la nave está destruida
     */
    public void aumentarDesgaste(int cantidad) {
        verificarEstado();
        validarCantidad(cantidad);
        desgaste = Math.min(DESGASTE_MAXIMO, desgaste + cantidad);
        verificarRecursos();
    }

    /**
     * <b>Pre:</b> la nave no está destruida, 0 &lt;= cantidad &lt;= getEspacioCombustible().
     * @throws IllegalArgumentException si cantidad es negativa
     * @throws IllegalStateException si la nave está destruida o la carga supera la capacidad (sin modificar la nave)
     */
    public void cargarCombustible(int cantidad) {
        verificarEstado();
        validarCantidad(cantidad);
        if (cantidad > getEspacioCombustible()) {
            throw new IllegalStateException("La carga de " + cantidad + " de combustible supera la capacidad máxima ("
                    + combustible + "/" + CAPACIDAD_MAXIMA + ").");
        }
        combustible += cantidad;
    }

    /**
     * <b>Pre:</b> la nave no está destruida, 0 &lt;= cantidad &lt;= getEspacioEnergia().
     * @throws IllegalArgumentException si cantidad es negativa
     * @throws IllegalStateException si la nave está destruida o la carga supera la capacidad (sin modificar la nave)
     */
    public void cargarEnergia(int cantidad) {
        verificarEstado();
        validarCantidad(cantidad);
        if (cantidad > getEspacioEnergia()) {
            throw new IllegalStateException("La carga de " + cantidad + " de energía supera la capacidad máxima ("
                    + energia + "/" + CAPACIDAD_MAXIMA + ").");
        }
        energia += cantidad;
    }
    /**
     * Añade un tripulante a la nave</br>
     * <b>Pre:</b> </br>
     * - El tripulante debe ser un tripulante valido (no nulo)</br>
     * @throws Exception si ya hay un tripulante registrado con el mismo identificador o si se intenta añadir un capitan ya habiendo un capitan en la tripulacion
     * @param tripulante Tripulante a añadir a la nave
     */
    public void addTripulante(Tripulante tripulante) throws Exception { // hago que lanze la exepcion en vez de ser runtime para que no rompa el sistema y que lo resuelva
        verificarEstado();
        if (tripulante == null) {
            throw new IllegalArgumentException("El tripulante a agregar no puede ser nulo.");
        }

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

    public boolean tieneCapitan() {
        for (Tripulante t : tripulantes) {
            if ("capitan".equalsIgnoreCase(t.getCargo())) {
                return true;
            }
        }
        return false;
    }


    
    public boolean tieneTripulacionValida() {
        return tripulantes.size() >= 5 && tieneCapitan();
    }

    public Iterator<Tripulante> getTripulantes() {
        return java.util.Collections
                .unmodifiableList(tripulantes)
                .iterator();
    }

    /**
     * Turno de movimiento con el camino despejado. Delega en el motor warp.
     * @return casillas que avanza la nave (0, 1 o 2)
     */
    public int saltar() {
        verificarEstado();
        return motor_warp.saltar();
    }

    /**
     * Turno de movimiento con un obstáculo adelante. Delega en el motor warp.
     * @return casillas que avanza la nave (0, 1 o 2)
     */
    public int saltarWarp() {
        verificarEstado();
        return motor_warp.saltarWarp();
    }
    public String getUltimoEventoMotor() {
        return this.motor_warp.getUltimoEvento();
    }

    @Override
    public abstract String interactuar(Nave nave);


  //el unico proposito de este metodo es hacer mas facil el caso en que se enfrentan dos naves iguales
    public abstract String getTipo();
    
    public abstract boolean gana(Nave obj);
    public abstract boolean ganaExploradora();
    public abstract boolean ganaCarguera();
    public abstract boolean ganaCombate();

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + " {combustible=" + combustible
                + ", energia=" + energia
                + ", desgaste=" + desgaste
                + ", destruida=" + destruida + "}";
    }

}