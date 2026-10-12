package modelo.nave;

import modelo.MotorWarp;
import modelo.tripulantes.Tripulante;
import modelo.excepciones.TransicionEstadoInvalida;

import java.util.ArrayList;
import java.util.Iterator;

public class Nave {
    // tripulacion
    MotorWarp motor_warp;
    // asistente software
    private int combustible;
    private int energia;
    private int desgaste;
    private ArrayList<Tripulante> tripulantes = new ArrayList<Tripulante>();

    public Nave(int combustible, int energia, int desgaste) {
        this.combustible = combustible;
        this.energia = energia;
        this.desgaste = desgaste;
        this.motor_warp = new MotorWarp();
        this.tripulantes = new ArrayList<>();
    }

    // delega las funciones del motor al motor
    public void preparar_salto() throws TransicionEstadoInvalida {
        motor_warp.preparar_salto();
    }
    public void arrancar() throws TransicionEstadoInvalida {
        motor_warp.arrancar();
    }
    public void detener() throws TransicionEstadoInvalida {
        motor_warp.detener();
    }
    public void enfriar_motor() throws TransicionEstadoInvalida {
        motor_warp.enfriar_motor();
    }

    public int getCombustible() {
        return this.combustible;
    }

    public int getEnergia() {
        return this.energia;
    }

    public int getDesgaste() {
        return this.desgaste;
    }
    /**
     * Cambia el estado del combustible de la nave simulando el consumo de este
     * <b>Pre:</b>
     * -    cantidad es menor o igual a la cantidad de combustible de la nave
     *
     * @param int cantidad que indica la cantidad de combustible que se va a consumir de la nave
     * @throws IllegalArgumentException si el combustible a consumir es mayor al combustible que tiene la nave
     */
    public void consumirCombustible(int cantidad) {
        if (this.combustible < cantidad) {
            throw new IllegalStateException("Combustible insuficiente.");
        }
        this.combustible -= cantidad;
    }
    /**
     * Cambia el estado de la energia de la nave simulando el consumo de esta
     * <b>Pre:</b>
     * -    cantidad es menor o igual a la cantida de energia de la nave
     *
     * @param int cantidad que indica la cantidad de energia que se va a consumir de la nave
     * @throws IllegalArgumentException si la energia a consumir es mayor a la energia que tiene la nave
     */
    public void consumirEnergia(int cantidad) {
        if (this.energia < cantidad) {
            throw new IllegalStateException("Energía insuficiente.");
        }
        this.energia -= cantidad;
    }

    public void addTripulante(Tripulante t) {
        tripulantes.add(t);
    }

    public Iterator<Tripulante> getTripulantes() {
        return tripulantes.iterator();
    }

    public void aumentarDesgaste(int cantidad) {
        this.desgaste = Math.min(100, this.desgaste + cantidad);
    }
    public String toString()
    {
        return "Recursos: { "+combustible+", "+energia+", "+desgaste+" }";
    }

}
