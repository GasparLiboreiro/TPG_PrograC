package modelo;

import modelo.excepciones.TransicionEstadoInvalida;

public class StateMWPreparandoSalto implements StateMotorWarp {
    private final MotorWarp motor;
    private int turnosPreparados = 1; // el turno en que se inició la preparación ya cuenta

    public StateMWPreparandoSalto(MotorWarp motor) {
        this.motor = motor;
    }

    // Una vez iniciada la preparación, el motor la continúa sin importar lo que detecte el radar.
    @Override
    public int saltar() {
        return continuarPreparacion();
    }

    @Override
    public int saltarWarp() {
        return continuarPreparacion();
    }

    private int continuarPreparacion() {
        if (!motor.hayEnergiaParaWarp()) {
            // sin energía: se cancela el warp, el motor vuelve a estar disponible y se hace un salto simple
            motor.setEstado(new StateMWDisponible(motor));
            return motor.saltoNormal();
        }
        if (turnosPreparados < MotorWarp.TURNOS_PREPARACION) {
            turnosPreparados++;
            return 0; // la nave espera mientras el motor se prepara
        }
        StateMWEnWarp enWarp = new StateMWEnWarp(motor);
        motor.setEstado(enWarp);
        return enWarp.saltarWarp();
    }

    @Override
    public int getEstado() {
        return MotorWarp.ESTADO_PREPARANDO_SALTO;
    }
}
