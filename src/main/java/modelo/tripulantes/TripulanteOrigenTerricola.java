package modelo.tripulantes;

public class TripulanteOrigenTerricola extends TripulanteOrigenDecorator {
    public TripulanteOrigenTerricola(Tripulante tripulante_base) {
        super(tripulante_base);
    }

    @Override
    public String getPlanetaDeOrigen() {
        return "Terricola";
    }

    @Override
    protected double getSubsidioOrigen() {
        return 20;
    }

    @Override
    public String toString(){
        return this.tripulante_base.toString() + "  Planeta de origen:'Terricola'";
    }
}
