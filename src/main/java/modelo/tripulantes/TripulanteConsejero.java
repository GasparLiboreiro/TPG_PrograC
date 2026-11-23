package modelo.tripulantes;

import java.util.List;

public class TripulanteConsejero extends Tripulante {

    private int consejos_dados; // el consejero tiene la mecanica de que gana un plus de plata por cada consejo dado entre liquidaciones de haberes

    public TripulanteConsejero(String Nombre, String identidad, int antiguedad) {
        super(Nombre, identidad, antiguedad);
    }

    @Override
    public String getCargo() {
        return "Consejero";
    }

    @Override
    public String getPlanetaDeOrigen() {
        return "";// no se sabe aca
    }

    public void registrarConsejo()
    {
        this.consejos_dados++;
    }

    @Override
    public List<ConceptoHaber> getDetalleHaberes() {
        double baseOficio = 600;
        return List.of(
                new ConceptoHaber("Cargo Consejero", baseOficio),
                new ConceptoHaber("Antigüedad (" + getAntiguedad() + " años x 5%)", baseOficio * 0.05 * getAntiguedad()),
                new ConceptoHaber("Consejos registrados (" + consejos_dados + " x 2 PG)", consejos_dados * 2));
    }

    /** Cierra el período liquidado: los consejos vuelven a 0 para el período siguiente. */
    public void cerrarPeriodo() {
        this.consejos_dados = 0;
    }
    public String toString(){
        return super.toString()+"  Oficio:'Consejero'";
    }
}
