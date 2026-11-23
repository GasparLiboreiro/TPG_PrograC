package modelo.tripulantes;

import java.util.List;

public class TripulanteAlferez extends Tripulante {

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
    public List<ConceptoHaber> getDetalleHaberes() {
        double baseOficio = 200;
        return List.of(
                new ConceptoHaber("Cargo Alferez", baseOficio),
                new ConceptoHaber("Antigüedad (" + getAntiguedad() + " años x 0,5%)", baseOficio * 0.005 * getAntiguedad()));
    }

    public String toString(){
        return super.toString()+"  Oficio:'Alferez'";
    }
}
