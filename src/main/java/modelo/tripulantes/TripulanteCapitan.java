package modelo.tripulantes;

import java.util.List;

public class TripulanteCapitan extends Tripulante {
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
    public List<ConceptoHaber> getDetalleHaberes() {
        double baseOficio = 1000;
        return List.of(
                new ConceptoHaber("Cargo Capitan", baseOficio),
                new ConceptoHaber("Antigüedad (" + getAntiguedad() + " años x 20%)", baseOficio * 0.20 * getAntiguedad()));
    }
    public String toString(){
        return super.toString()+"  Oficio:'Capitan'";
    }
}
