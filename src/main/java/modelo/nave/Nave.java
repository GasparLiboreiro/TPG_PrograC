package modelo.nave;

import modelo.ElementoDelUniverso;
import modelo.MotorWarp;
import modelo.tripulantes.*;
import modelo.excepciones.TransicionEstadoInvalida;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public abstract class Nave implements ElementoDelUniverso,Batalla {

    private final MotorWarp motor_warp;
    private int combustible;
    private int energia;
    private int desgaste;
    private boolean destruida;

    private final ArrayList<Tripulante> tripulantes = new ArrayList<>();

    protected Nave(int combustible, int energia, int desgaste) {
        this.combustible = combustible;
        this.energia = energia;
        this.desgaste = desgaste;
        this.motor_warp = new MotorWarp(this);

        verificarEstado();
    }

    private void verificarEstado() {
        if (destruida) {
            throw new IllegalStateException("La nave está destruida.");
        }
    }

    private void verificarRecursos() {
        if (combustible == 0 || energia == 0 || desgaste >= 100) {
            destruir();
        }
    }

    //tuve que sacar el final para que se pueda destruir en los enfrentamientos
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

    public int getEstadoMotorWarp() {
        return this.motor_warp.getEstado();
    }

    public void consumirCombustible(int cantidad) {
        verificarEstado();

        if (cantidad > combustible) {
            throw new IllegalStateException("Combustible insuficiente.");
        }

        combustible -= cantidad;
        verificarRecursos();
    }

    public void consumirEnergia(int cantidad) {
        verificarEstado();

        if (cantidad > energia) {
            throw new IllegalStateException("Energía insuficiente.");
        }

        energia -= cantidad;
        verificarRecursos();
    }

    public void aumentarDesgaste(int cantidad) {
        verificarEstado();

        desgaste += Math.min(cantidad, 100 - desgaste);
        verificarRecursos();
    }

    public void cargarCombustible(int cantidad) {
        if (cantidad + combustible < 100) {
            this.combustible += cantidad;
        }
        else {
            this.combustible = 100;
        }
    }

    public void cargarEnergia(int cantidad) {
        if (cantidad + energia < 100) {
            this.energia += cantidad;
        }
        else {
            this.energia = 100;
        }
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

    @Override
    public abstract void interactuar(Nave nave);
    @Override
    public boolean esObstaculo() {
        return false;
    }

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