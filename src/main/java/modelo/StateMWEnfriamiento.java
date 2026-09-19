package modelo;

import modelo.excepciones.TransicionEstadoInvalida;

public class StateMWEnfriamiento implements StateMotorWarp{
    private MotorWarp motor;

    public StateMWEnfriamiento(MotorWarp motor) {
        this.motor = motor;
    }

    @Override
    public void preparar_salto() throws TransicionEstadoInvalida {
        throw new TransicionEstadoInvalida("MotorWarp no puede preparar salto estando en enfriamiento");
    }

    @Override
    public void arrancar() throws TransicionEstadoInvalida {
        throw new TransicionEstadoInvalida("MotorWarp no puede arrancar estando en enfriamiento");
    }

    @Override
    public void detener() throws TransicionEstadoInvalida {
        throw new TransicionEstadoInvalida("MotorWarp no puede detenerse estando en enfriamiento");
    }

    @Override
    public void enfriar_motor() {
        motor.setEstado(new StateMWDisponible(motor));
    }
}