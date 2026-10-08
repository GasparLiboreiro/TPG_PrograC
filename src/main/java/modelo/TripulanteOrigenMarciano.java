package modelo;

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
}
