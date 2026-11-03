package modelo.tripulantes;

public class TripulanteTeniente extends TripulanteConcreto {
    public TripulanteTeniente(String Nombre, String identidad, int antiguedad) {
        super(Nombre, identidad, antiguedad);
    }

    @Override
    public String getCargo() {
        return "Teniente";
    }

    @Override
    public String getPlanetaDeOrigen() {
        return ""; // no se sabe aca
    }

    @Override
    public double calcularHaberes() {
        double base_oficio = 400;
        return base_oficio + base_oficio*0.03*this.getAntiguedad();
    }
    public String toString(){
        return super.toString()+"  Oficio:'Teniente'";
    }
}
