package modelo;

import modelo.excepciones.TransicionEstadoInvalida;

public class StateMWEnWarp implements StateMotorWarp {
    private final MotorWarp motor;

    public StateMWEnWarp(MotorWarp motor) {
        this.motor = motor;
    }

    @Override
    public int saltar() {
        return ejecutarSalto();
    }

    @Override
    public int saltarWarp() {
        return ejecutarSalto();
    }

    private int ejecutarSalto() {
        if (!motor.hayEnergiaParaWarp()) {
            // los recursos ya no están: se aborta el warp y se hace un salto simple
            motor.setEstado(new StateMWDisponible(motor));
            return motor.saltoNormal();
        }
        motor.getNave().consumirEnergia(MotorWarp.COSTO_ENERGIA_WARP);
        motor.setEstado(new StateMWEnfriamiento(motor));
        return 2;
    }

    @Override
    public int getEstado() {
        return MotorWarp.ESTADO_EN_WARP;
    }
}