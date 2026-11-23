package modelo;



public class StateMWEnWarp implements StateMotorWarp {
    private final MotorWarp motor;

    public StateMWEnWarp(MotorWarp motor) {
        this.motor = motor;
    }

    @Override
    public int saltar() {
        return motor.saltoNormal();
    }

    @Override
    public int saltarWarp() {
        if (!motor.hayEnergiaParaWarp()) {
            motor.setEstado(new StateMWDisponible(motor));
            motor.registrarEvento("salto warp abortado por falta de energía, se realiza un salto normal.");
            return motor.saltoNormal();
        }
        motor.getNave().consumirEnergia(MotorWarp.COSTO_ENERGIA_WARP);
        motor.setEstado(new StateMWEnfriamiento(motor));
        motor.registrarEvento("salto warp ejecutado, la nave avanza 2 casillas (-" + MotorWarp.COSTO_ENERGIA_WARP + " energía).");
        return 2;
    }
    @Override
    public int getEstado() {
        return MotorWarp.ESTADO_EN_WARP;
    }
}