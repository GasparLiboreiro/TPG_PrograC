package modelo;

import modelo.excepciones.TransicionEstadoInvalida;

public class StateMWEnfriamiento implements StateMotorWarp {
    private final MotorWarp motor;

    public StateMWEnfriamiento(MotorWarp motor) {
        this.motor = motor;
    }

    // Durante el enfriamiento la nave espera, sin importar lo que detecte el radar.
    @Override
    public int saltar() {
        return enfriar();
    }

    @Override
    public int saltarWarp() {
        return enfriar();
    }

    private int enfriar() {
        motor.getNave().aumentarDesgaste(MotorWarp.DESGASTE_ENFRIAMIENTO);
        motor.setEstado(new StateMWDisponible(motor));
        return 0;
    }

    @Override
    public int getEstado() {
        return MotorWarp.ESTADO_ENFRIAMIENTO;
    }
}