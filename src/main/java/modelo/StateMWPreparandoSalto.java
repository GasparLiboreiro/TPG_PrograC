package modelo;



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
            motor.setEstado(new StateMWDisponible(motor));
            motor.registrarEvento("carga del warp cancelada por falta de energía, se realiza un salto normal.");
            return motor.saltoNormal();
        }
        turnosPreparados++;
        if (turnosPreparados < MotorWarp.TURNOS_PREPARACION) {
            motor.registrarEvento("cargando salto warp (" + turnosPreparados + "/" + MotorWarp.TURNOS_PREPARACION
                    + "), se realiza un salto normal.");
        } else {
            motor.setEstado(new StateMWEnWarp(motor));
            motor.registrarEvento("salto warp cargado y listo (" + turnosPreparados + "/" + MotorWarp.TURNOS_PREPARACION
                    + "), se realiza un salto normal.");
        }
        return motor.saltoNormal();
    }

    @Override
    public int getEstado() {
        return MotorWarp.ESTADO_PREPARANDO_SALTO;
    }
}
