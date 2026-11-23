package modelo.tripulantes;

public class TripulanteOrigenVulcano extends TripulanteOrigenDecorator {
    public TripulanteOrigenVulcano(Tripulante tripulante_base) {
        super(tripulante_base);
    }

    @Override
    public String getPlanetaDeOrigen() {
        return "Vulcano";
    }

    @Override
    protected double getSubsidioOrigen() {
        return 30;
    }

    @Override
    public String toString(){
        return this.tripulante_base.toString() + "  Planeta de origen:'Vulcano'";
    }
}
