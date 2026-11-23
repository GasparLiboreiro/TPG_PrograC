package modelo;



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
        motor.registrarEvento("enfriamiento (+" + MotorWarp.DESGASTE_ENFRIAMIENTO
                + " desgaste), warp no disponible, se realiza un salto normal. Motor disponible.");
        return motor.saltoNormal();
    }

    @Override
    public int getEstado() {
        return MotorWarp.ESTADO_ENFRIAMIENTO;
    }
}