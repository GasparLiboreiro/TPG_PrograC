package modelo.tripulantes;

public class TripulanteOrigenMarciano extends TripulanteOrigenDecorator{
    public TripulanteOrigenMarciano(TripulanteConcreto tripulante_base) {
        super(tripulante_base);
    }

    @Override
    public String getPlanetaDeOrigen() {
        return "Marciano";
    }

    @Override
    public double calcularHaberes() {
        return this.tripulante_base.calcularHaberes() + 18;
    }

    @Override
    public String toString(){
        return this.tripulante_base.toString() + "  Planeta de origen:'Marciano'";
    }
}
