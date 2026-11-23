package modelo.nave;

import modelo.ElementoDelUniverso;
import modelo.MotorWarp;
import modelo.tripulantes.*;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class Nave implements ElementoDelUniverso, Batalla {

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
     * Inicializa una nave con sus valores iniciales de combustible, energía y desgaste.<br>
     * <b>Pre:</b> combustible y energía entre 0 y {@link #CAPACIDAD_MAXIMA}; desgaste entre 0 y {@link #DESGASTE_MAXIMO}.<br>
     * <b>Post:</b> la nave queda inicializada con los recursos indicados, {@code gemas = 0}, {@code destruida = false} y un {@link MotorWarp} asociado.
     *
     * @param combustible Combustible inicial de la nave.
     * @param energia Energía inicial de la nave.
     * @param desgaste Desgaste inicial de la nave.
     * @throws IllegalArgumentException si algún valor inicial está fuera de rango.
     */
    protected Nave(int combustible, int energia, int desgaste) {
        assert combustible >= 0 && combustible <= CAPACIDAD_MAXIMA : "El combustible inicial debe estar entre 0 y CAPACIDAD_MAXIMA.";
        assert energia >= 0 && energia <= CAPACIDAD_MAXIMA : "La energía inicial debe estar entre 0 y CAPACIDAD_MAXIMA.";
        assert desgaste >= 0 && desgaste <= DESGASTE_MAXIMO : "El desgaste inicial debe estar entre 0 y DESGASTE_MAXIMO.";

        validarRango("combustible", combustible, CAPACIDAD_MAXIMA);
        validarRango("energía", energia, CAPACIDAD_MAXIMA);
        validarRango("desgaste", desgaste, DESGASTE_MAXIMO);
        this.combustible = combustible;
        this.energia = energia;
        this.desgaste = desgaste;
        this.gemas = 0;
        this.motor_warp = new MotorWarp(this);
    }

    /**
     * Valida que un valor numérico se encuentre dentro del rango {@code [0, maximo]}.<br>
     * <b>Pre:</b> {@code recurso != null} y {@code maximo >= 0}.<br>
     * <b>Post:</b> no modifica el estado; lanza {@link IllegalArgumentException} si {@code valor < 0} o {@code valor > maximo}.
     *
     * @param recurso Nombre del recurso validado.
     * @param valor Valor a verificar.
     * @param maximo Cota superior permitida.
     * @throws IllegalArgumentException si {@code valor} está fuera de rango.
     */
    private static void validarRango(String recurso, int valor, int maximo) {
        assert recurso != null : "El nombre del recurso no puede ser nulo.";
        assert maximo >= 0 : "El máximo no puede ser negativo.";

        if (valor < 0 || valor > maximo) {
            throw new IllegalArgumentException("Valor inicial de " + recurso + " fuera de rango: " + valor);
        }
    }

    /**
     * Valida que una cantidad sea mayor o igual a cero.<br>
     * <b>Pre:</b> ninguna.<br>
     * <b>Post:</b> no modifica el estado; lanza {@link IllegalArgumentException} si {@code cantidad < 0}.
     *
     * @param cantidad Cantidad a verificar.
     * @throws IllegalArgumentException si {@code cantidad < 0}.
     */
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

    /**
     * Calcula el combustible disponible para cargar hasta alcanzar la capacidad máxima.<br>
     * <b>Pre:</b> ninguna.<br>
     * <b>Post:</b> devuelve {@code CAPACIDAD_MAXIMA - combustible}.
     *
     * @return combustible que todavía entra hasta la capacidad máxima.
     */
    public int getEspacioCombustible() {
        return CAPACIDAD_MAXIMA - combustible;
    }

    /**
     * Calcula la energía disponible para cargar hasta alcanzar la capacidad máxima.<br>
     * <b>Pre:</b> ninguna.<br>
     * <b>Post:</b> devuelve {@code CAPACIDAD_MAXIMA - energia}.
     *
     * @return energía que todavía entra hasta la capacidad máxima.
     */
    public int getEspacioEnergia() {
        return CAPACIDAD_MAXIMA - energia;
    }

    public int getGemas() {
        return gemas;
    }

    /**
     * Agrega gemas a la bodega de la nave.<br>
     * <b>Pre:</b> ninguna.<br>
     * <b>Post:</b> {@code gemas} aumenta en {@code cantidad}.
     *
     * @param cantidad Cantidad de gemas a agregar.
     * @throws IllegalArgumentException si cantidad es negativa.
     * @throws IllegalStateException si la nave está destruida.
     */
    public void agregarGemas(int cantidad) {
        verificarEstado();
        validarCantidad(cantidad);
        gemas += cantidad;
    }

    /**
     * Verifica que la nave no esté destruida.<br>
     * <b>Pre:</b> ninguna.<br>
     * <b>Post:</b> si la nave está destruida, lanza {@link IllegalStateException}; en caso contrario no modifica el estado.
     *
     * @throws IllegalStateException si la nave está destruida.
     */
    private void verificarEstado() {
        if (destruida) {
            throw new IllegalStateException("La nave está destruida.");
        }
    }

    /**
     * Evalúa si los niveles de combustible, energía o desgaste obligan a destruir la nave.<br>
     * <b>Pre:</b> ninguna.<br>
     * <b>Post:</b> si {@code combustible == 0} o {@code desgaste >= DESGASTE_MAXIMO}, invoca {@link #destruir()}.
     */
    private void verificarRecursos() {
        if (combustible == 0 || desgaste >= DESGASTE_MAXIMO) {
            destruir();
        }
    }

    /**
     * Marca la nave como destruida.<br>
     * <b>Pre:</b> ninguna.<br>
     * <b>Post:</b> {@code destruida} queda en {@code true}.
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

    /**
     * Indica si el desgaste de la nave alcanzó o superó el umbral de mantenimiento.<br>
     * <b>Pre:</b> ninguna.<br>
     * <b>Post:</b> devuelve {@code true} si {@code desgaste >= UMBRAL_MANTENIMIENTO}, o {@code false} en caso contrario.
     *
     * @return true si el desgaste alcanzó el umbral de mantenimiento.
     */
    public boolean requiereMantenimiento() {
        return desgaste >= UMBRAL_MANTENIMIENTO;
    }

    /**
     * Realiza el mantenimiento: lleva el desgaste a 0.<br>
     * <b>Pre:</b> ninguna.<br>
     * <b>Post:</b> {@code desgaste} queda en {@code 0}.
     *
     * @throws IllegalStateException si la nave está destruida.
     */
    public void realizarMantenimiento() {
        verificarEstado();
        desgaste = 0;
    }

    public int getEstadoMotorWarp() {
        return this.motor_warp.getEstado();
    }

    /**
     * Consume una cantidad de combustible de la nave y verifica sus recursos.<br>
     * <b>Pre:</b> ninguna.<br>
     * <b>Post:</b> el combustible baja en cantidad; si llega a 0 la nave se destruye.<br>
     *
     * @param cantidad Cantidad de combustible a consumir.
     * @throws IllegalArgumentException si cantidad es negativa.
     * @throws IllegalStateException si la nave está destruida o no hay combustible suficiente (sin modificar la nave).
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
     * Consume una cantidad de energía de la nave y verifica sus recursos.<br>
     * <b>Pre:</b> ninguna.<br>
     * <b>Post:</b> la energía baja en cantidad.<br>
     *
     * @param cantidad Cantidad de energía a consumir.
     * @throws IllegalArgumentException si cantidad es negativa.
     * @throws IllegalStateException si la nave está destruida o no hay energía suficiente (sin modificar la nave).
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
     * Incrementa el desgaste de la nave hasta el tope máximo permitido.<br>
     * <b>Pre:</b> ninguna.<br>
     * <b>Post:</b> el desgaste sube en cantidad, con tope {@link #DESGASTE_MAXIMO}; al alcanzarlo la nave se destruye.<br>
     *
     * @param cantidad Cantidad de desgaste a sumar.
     * @throws IllegalArgumentException si cantidad es negativa.
     * @throws IllegalStateException si la nave está destruida.
     */
    public void aumentarDesgaste(int cantidad) {
        verificarEstado();
        validarCantidad(cantidad);
        desgaste = Math.min(DESGASTE_MAXIMO, desgaste + cantidad);
        verificarRecursos();
    }

    /**
     * Carga combustible en la nave sin superar la capacidad máxima.<br>
     * <b>Pre:</b> ninguna.<br>
     * <b>Post:</b> el combustible aumenta en {@code cantidad}.<br>
     *
     * @param cantidad Cantidad de combustible a cargar.
     * @throws IllegalArgumentException si cantidad es negativa.
     * @throws IllegalStateException si la nave está destruida o la carga supera la capacidad (sin modificar la nave).
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
     * Carga energía en la nave sin superar la capacidad máxima.<br>
     * <b>Pre:</b> ninguna.<br>
     * <b>Post:</b> la energía aumenta en {@code cantidad}.<br>
     *
     * @param cantidad Cantidad de energía a cargar.
     * @throws IllegalArgumentException si cantidad es negativa.
     * @throws IllegalStateException si la nave está destruida o la carga supera la capacidad (sin modificar la nave).
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
     * Añade un tripulante a la nave.<br>
     * <b>Pre:</b> ninguna.<br>
     * <b>Post:</b> si la identidad no está repetida y no se duplica el capitán, el tripulante queda añadido a la tripulación.<br>
     *
     * @param tripulante Tripulante a añadir a la nave.
     * @throws Exception si ya hay un tripulante registrado con el mismo identificador o si se intenta añadir un capitán ya habiendo un capitán en la tripulación.
     * @throws IllegalArgumentException si {@code tripulante} es nulo.
     * @throws IllegalStateException si la nave está destruida.
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

    /**
     * Indica si la tripulación cuenta con un capitán.<br>
     * <b>Pre:</b> ninguna.<br>
     * <b>Post:</b> devuelve {@code true} si existe algún tripulante con cargo {@code "capitan"}, o {@code false} en caso contrario.
     *
     * @return {@code true} si hay un capitán en la tripulación, {@code false} en caso contrario.
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
     * Verifica si la tripulación cumple con los requisitos mínimos de dotación.<br>
     * <b>Pre:</b> ninguna.<br>
     * <b>Post:</b> devuelve {@code true} si hay al menos 5 tripulantes y un capitán asignado, o {@code false} en caso contrario.
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
     * Turno de movimiento con el camino despejado. Delega en el motor warp.<br>
     * <b>Pre:</b> {@code motor_warp != null}.<br>
     * <b>Post:</b> delega el avance en {@code motor_warp.saltar()} y devuelve las casillas avanzadas.<br>
     *
     * @return casillas que avanza la nave (0, 1 o 2).
     * @throws IllegalStateException si la nave está destruida.
     */
    public int saltar() {
        assert motor_warp != null : "El motor warp no puede ser nulo.";

        verificarEstado();
        return motor_warp.saltar();
    }

    /**
     * Turno de movimiento con un obstáculo adelante. Delega en el motor warp.<br>
     * <b>Pre:</b> {@code motor_warp != null}.<br>
     * <b>Post:</b> delega el avance en {@code motor_warp.saltarWarp()} y devuelve las casillas avanzadas.<br>
     *
     * @return casillas que avanza la nave (0, 1 o 2).
     * @throws IllegalStateException si la nave está destruida.
     */
    public int saltarWarp() {
        assert motor_warp != null : "El motor warp no puede ser nulo.";

        verificarEstado();
        return motor_warp.saltarWarp();
    }

    public String getUltimoEventoMotor() {
        return this.motor_warp.getUltimoEvento();
    }

    /**
     * Ejecuta la interacción de esta nave con otra nave.<br>
     * <b>Pre:</b> {@code nave != null}.<br>
     * <b>Post:</b> devuelve una descripción textual del resultado de la interacción.
     *
     * @param nave Nave con la que se interactúa.
     * @return Resultado de la interacción.
     */
    @Override
    public abstract String interactuar(Nave nave);

    // el unico proposito de este metodo es hacer mas facil el caso en que se enfrentan dos naves iguales
    /**
     * Obtiene el identificador de tipo de la nave.<br>
     * <b>Pre:</b> ninguna.<br>
     * <b>Post:</b> devuelve una cadena no nula que identifica el tipo de nave.
     *
     * @return Tipo de nave.
     */
    public abstract String getTipo();

    /**
     * Determina si esta nave vence en combate a la nave indicada.<br>
     * <b>Pre:</b> {@code obj != null}.<br>
     * <b>Post:</b> devuelve {@code true} si esta nave gana el enfrentamiento contra {@code obj}, o {@code false} en caso contrario.
     *
     * @param obj Nave rival.
     * @return {@code true} si esta nave gana, {@code false} en caso contrario.
     */
    public abstract boolean gana(Nave obj);

    /**
     * Determina si esta nave vence a una nave exploradora.<br>
     * <b>Pre:</b> ninguna.<br>
     * <b>Post:</b> devuelve {@code true} si esta nave vence a una nave exploradora, o {@code false} en caso contrario.
     *
     * @return {@code true} si vence a una nave exploradora, {@code false} en caso contrario.
     */
    public abstract boolean ganaExploradora();

    /**
     * Determina si esta nave vence a una nave carguera.<br>
     * <b>Pre:</b> ninguna.<br>
     * <b>Post:</b> devuelve {@code true} si esta nave vence a una nave carguera, o {@code false} en caso contrario.
     *
     * @return {@code true} si vence a una nave carguera, {@code false} en caso contrario.
     */
    public abstract boolean ganaCarguera();

    /**
     * Determina si esta nave vence a una nave de combate.<br>
     * <b>Pre:</b> ninguna.<br>
     * <b>Post:</b> devuelve {@code true} si esta nave vence a una nave de combate, o {@code false} en caso contrario.
     *
     * @return {@code true} si vence a una nave de combate, {@code false} en caso contrario.
     */
    public abstract boolean ganaCombate();

    /**
     * Devuelve una representación textual del estado actual de la nave.<br>
     * <b>Pre:</b> ninguna.<br>
     * <b>Post:</b> devuelve una cadena no nula con los atributos principales de la nave.
     *
     * @return Representación en cadena de la nave.
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