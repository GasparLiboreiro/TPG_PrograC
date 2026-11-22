package modelo;

import modelo.excepciones.TransicionEstadoInvalida;

public class StateMWDisponible implements StateMotorWarp {
    private final MotorWarp motor;

    public StateMWDisponible(MotorWarp motor) {
        this.motor = motor;
    }

    @Override
    public int saltar() {
        return motor.saltoNormal();
    }

    @Override
    public int saltarWarp() {
        if (!motor.hayEnergiaParaWarp()) {
            // sin energía para el warp: se cancela la maniobra y se hace un salto simple
            return motor.saltoNormal();
        }
        motor.setEstado(new StateMWPreparandoSalto(motor)); // este turno es el primero de preparación
        return 0;
    }

    @Override
    public int getEstado() {
        return MotorWarp.ESTADO_DISPONIBLE;
    }
}