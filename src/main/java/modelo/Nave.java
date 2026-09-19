package modelo;

public class Nave {
    // tripulacion
    MotorWarp motor_warp;
    // asistente software
    private double combustible;
    private double energia;
    private double desgaste;

    public Nave(double combustible, double energia, double desgaste) {
        this.combustible = combustible;
        this.energia = energia;
        this.desgaste = desgaste;
        this.motor_warp = new MotorWarp();
    }

    public String toString()
    {
        return "Recursos: { "+combustible+", "+energia+", "+desgaste+" }";
    }

}
