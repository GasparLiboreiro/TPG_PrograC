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

    public void preparar_salto() throws TransicionEstadoInvalida {

        try {
            verificarEstado();
            motor_warp.preparar_salto();
        }
        catch (Exception e) {
            //queda ver bien que tirar acá. yo pensaba cambiar la excepcion de verificar estado por otra pero no se bien cual poner.
        }
    }

    public void arrancar() throws TransicionEstadoInvalida {
        verificarEstado();
        motor_warp.arrancar();
    }

    public void detener() throws TransicionEstadoInvalida {
        verificarEstado();
        motor_warp.detener();
    }

    public void enfriar_motor() throws TransicionEstadoInvalida {
        verificarEstado();
        motor_warp.enfriar_motor();
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
        tripulantes.add(Objects.requireNonNull(tripulante));
    }

    public Iterator<Tripulante> getTripulantes() {
        return java.util.Collections
                .unmodifiableList(tripulantes)
                .iterator();
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