package modelo;

import modelo.excepciones.TransicionEstadoInvalida;

public class StateMWDisponible implements StateMotorWarp{
    private MotorWarp motor;

    public StateMWDisponible(MotorWarp motor) {
        this.motor = motor;
    }

    @Override
    public void preparar_salto() {
        motor.setEstado(new StateMWPreparandoSalto(motor));
    }

    @Override
    public void arrancar() throws TransicionEstadoInvalida {
        throw new TransicionEstadoInvalida("MotorWarp no puede arrancar estando Disponible");
    }

    @Override
    public void detener() throws TransicionEstadoInvalida {
        throw new TransicionEstadoInvalida("MotorWarp no puede detenerse estando Disponible");
    }

    @Override
    public void enfriar_motor() throws TransicionEstadoInvalida {
        throw new TransicionEstadoInvalida("MotorWarp no puede enfriarse estando Disponible");
    }
}