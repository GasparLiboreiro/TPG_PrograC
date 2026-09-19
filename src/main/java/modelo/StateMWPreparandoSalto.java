package modelo;

import modelo.excepciones.TransicionEstadoInvalida;

public class StateMWPreparandoSalto implements StateMotorWarp{
    private MotorWarp motor;

    public StateMWPreparandoSalto(MotorWarp motor) {
        this.motor = motor;
    }

    @Override
    public void preparar_salto() throws TransicionEstadoInvalida {
        throw new TransicionEstadoInvalida("MotorWarp no puede preparar salto si ya esta preparando salto");
    }

    @Override
    public void arrancar() {
        motor.setEstado(new StateMWEnWarp(motor));
    }

    @Override
    public void detener() throws TransicionEstadoInvalida {
        throw new TransicionEstadoInvalida("MotorWarp no puede detenerse estando preparando salto");
    }

    @Override
    public void enfriar_motor() throws TransicionEstadoInvalida {
        throw new TransicionEstadoInvalida("MotorWarp no puede enfriarse estando preparando salto");
    }
}
