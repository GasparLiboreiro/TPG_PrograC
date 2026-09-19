package modelo;

import modelo.excepciones.TransicionEstadoInvalida;

public class StateMWEnWarp implements StateMotorWarp{
    private MotorWarp motor;

    public StateMWEnWarp(MotorWarp motor) {
        this.motor = motor;
    }

    @Override
    public void preparar_salto() throws TransicionEstadoInvalida {
        throw new TransicionEstadoInvalida("MotorWarp no puede preparar salto estando en warp");
    }

    @Override
    public void arrancar() throws TransicionEstadoInvalida {
        throw new TransicionEstadoInvalida("MotorWarp no puede arrancar estando en warp");
    }

    @Override
    public void detener() {
        motor.setEstado(new StateMWEnfriamiento(motor));
    }

    @Override
    public void enfriar_motor() throws TransicionEstadoInvalida {
        throw new TransicionEstadoInvalida("MotorWarp no puede enfriarse estando en warp");
    }
}