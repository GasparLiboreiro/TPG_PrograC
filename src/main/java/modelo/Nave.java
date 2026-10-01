package modelo;

public class Nave {
    // tripulacion
    MotorWarp motor_warp;
    // asistente software
    private int combustible;
    private int energia;
    private int desgaste;

    public Nave(int combustible, int energia, int desgaste) {
        this.combustible = combustible;
        this.energia = energia;
        this.desgaste = desgaste;
        this.motor_warp = new MotorWarp();
    }

    public int getCombustible() {
        return this.combustible;
    }

    public int getEnergia() {
        return this.energia;
    }

    public int getDesgaste() {
        return this.desgaste;
    }

    public void consumirCombustible(int cantidad) {
        if (this.combustible < cantidad) {
            throw new IllegalStateException("Combustible insuficiente.");
        }
        this.combustible -= cantidad;
    }

    public void consumirEnergia(int cantidad) {
        if (this.energia < cantidad) {
            throw new IllegalStateException("Energía insuficiente.");
        }
        this.energia -= cantidad;
    }

    public void aumentarDesgaste(int cantidad) {
        this.desgaste = Math.min(100, this.desgaste + cantidad);
    }
    public String toString()
    {
        return "Recursos: { "+combustible+", "+energia+", "+desgaste+" }";
    }

}
