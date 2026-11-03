package modelo.tripulantes;

public class TripulanteAlferez extends TripulanteConcreto {

    public TripulanteAlferez(String Nombre, String identidad, int antiguedad) {
        super(Nombre, identidad, antiguedad);
    }

    @Override
    public String getCargo() {
        return "Alferez";
    }

    @Override
    public String getPlanetaDeOrigen() {
        return ""; // aca no se sabe
    }

    @Override
    public double calcularHaberes() {
        double base_oficio = 200;
        return base_oficio + base_oficio*0.005*this.getAntiguedad();
    }

    public String toString(){
        return super.toString()+"  Oficio:'Alferez'";
    }
}
