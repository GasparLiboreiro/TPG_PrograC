package modelo.tripulantes;

import java.util.List;

public class TripulanteTeniente extends Tripulante {
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
    public List<ConceptoHaber> getDetalleHaberes() {
        double baseOficio = 400;
        return List.of(
                new ConceptoHaber("Cargo Teniente", baseOficio),
                new ConceptoHaber("Antigüedad (" + getAntiguedad() + " años x 3%)", baseOficio * 0.03 * getAntiguedad()));
    }
    public String toString(){
        return super.toString()+"  Oficio:'Teniente'";
    }
}
