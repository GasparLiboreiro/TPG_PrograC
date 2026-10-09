package modelo.tripulantes;

public class TripulanteCapitan extends TripulanteConcreto {
    public TripulanteCapitan(String nombre, String identidad, int antiguedad) {
        super(nombre, identidad, antiguedad);
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
    public String toString(){
        return super.toString()+"  Oficio:'Capitan'";
    }
}
