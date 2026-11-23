package modelo;



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
            motor.registrarEvento("warp rechazado por falta de energía, se realiza un salto normal.");
            return motor.saltoNormal();
        }
        // el warp todavía no está cargado: se inicia la preparación y la nave avanza con un salto normal
        motor.setEstado(new StateMWPreparandoSalto(motor));
        motor.registrarEvento("inicia la carga del salto warp (1/" + MotorWarp.TURNOS_PREPARACION + "), se realiza un salto normal.");
        return motor.saltoNormal();
    }

    @Override
    public int getEstado() {
        return MotorWarp.ESTADO_DISPONIBLE;
    }
}