package modelo.nave;

import modelo.ElementoDelUniverso;
import modelo.MotorWarp;
import modelo.tripulantes.*;
import modelo.excepciones.TransicionEstadoInvalida;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public abstract class Nave implements ElementoDelUniverso {

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
        this.motor_warp = new MotorWarp();

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

    public final void destruir() {
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

    public void recibirDanio(int cantidad) {
        desgaste += cantidad;
        verificarRecursos();
    }

    public void addTripulante(Tripulante tripulante) {
        verificarEstado();
        if (tripulante == null) {
            throw new IllegalArgumentException("El tripulante a agregar no puede ser nulo.");
        }

        // 1. Validar identidad repetida
        for (Tripulante existente : tripulantes) {
            if (existente.getIdentidad() != null && existente.getIdentidad().equalsIgnoreCase(tripulante.getIdentidad())) {
                throw new IllegalArgumentException("Ya existe un tripulante registrado con la identidad: " + tripulante.getIdentidad());
            }
        }
        if ("capitan".equalsIgnoreCase(tripulante.getCargo()) && tieneCapitan()) {
            throw new IllegalStateException("La nave ya cuenta con un Capitán asignado.");
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

    public int getMotorId(){
        return motor_warp.getId();
    }

    public void saltar(boolean prepSalto, boolean cooldown, int warpCD,boolean terminarSalto) throws TransicionEstadoInvalida{
        try {
            motor_warp.saltar(prepSalto, cooldown, warpCD, terminarSalto);
            consumirCombustible(2);
            if (cooldown) {
                aumentarDesgaste(3);
            }
            verificarRecursos();
        } catch (TransicionEstadoInvalida e) {
            throw e;
        }
    }

    public void saltarWarp()throws TransicionEstadoInvalida, IllegalStateException{
        motor_warp.saltarWarp();
        consumirEnergia(5);
        verificarRecursos();
    }

    @Override
    public abstract void interactuar(Nave nave);

    public abstract void enfrentar(NaveExploradora otra);
    public abstract void enfrentar(NaveCarguera otra);
    public abstract void enfrentar(NaveCombate otra);

    @Override
    public String toString() {
        return getClass().getSimpleName()
                + " {combustible=" + combustible
                + ", energia=" + energia
                + ", desgaste=" + desgaste
                + ", destruida=" + destruida + "}";
    }

}