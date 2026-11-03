package modelo.tripulantes;

public class TripulanteOrigenVulcano extends TripulanteOrigenDecorator {
    public TripulanteOrigenVulcano(TripulanteConcreto tripulante_base) {
        super(tripulante_base);
    }

    @Override
    public String getPlanetaDeOrigen() {
        return "Vulcano";
    }

    @Override
    public double calcularHaberes() {
        return this.tripulante_base.calcularHaberes() + 30; // plus de origen
    }

    @Override
    public String toString(){
        return this.tripulante_base.toString() + "  Planeta de origen:'Vulcano'";
    }
}
