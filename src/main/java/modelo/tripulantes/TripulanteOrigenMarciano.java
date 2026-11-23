package modelo.tripulantes;

public class TripulanteOrigenMarciano extends TripulanteOrigenDecorator{
    public TripulanteOrigenMarciano(Tripulante tripulante_base) {
        super(tripulante_base);
    }

    @Override
    public String getPlanetaDeOrigen() {
        return "Marciano";
    }

    @Override
    protected double getSubsidioOrigen() {
        return 18;
    }

    @Override
    public String toString(){
        return this.tripulante_base.toString() + "  Planeta de origen:'Marciano'";
    }
}
