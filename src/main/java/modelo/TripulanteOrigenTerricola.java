package modelo;

public class TripulanteOrigenTerricola extends TripulanteOrigenDecorator {
    public TripulanteOrigenTerricola(TripulanteConcreto tripulante_base) {
        super(tripulante_base);
    }

    @Override
    public String getPlanetaDeOrigen() {
        return "Terricola";
    }

    @Override
    public double calcularHaberes() {
        return this.tripulante_base.calcularHaberes() + 20; // plus de origen
    }
}
