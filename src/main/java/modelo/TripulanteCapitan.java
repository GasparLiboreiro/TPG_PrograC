package modelo;

public class TripulanteCapitan extends TripulanteConcreto {
    public TripulanteCapitan(String Nombre, String identidad, int antiguedad) {
        super(Nombre, identidad, antiguedad);
    }

    @Override
    public String getCargo() {
        return "Capitan";
    }

    @Override
    public String getPlanetaDeOrigen() {
        return ""; // aca no se sabe
    }

    @Override
    public double calcularHaberes() {
        double base_oficio = 1000;
        return base_oficio + base_oficio*0.20*this.getAntiguedad();
    }
}
