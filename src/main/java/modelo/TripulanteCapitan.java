package modelo;

public class TripulanteCapitan extends TripulanteDecorator {

    public TripulanteCapitan(TripulanteInterfaz tripulante) {
        super(tripulante);
    }

    @Override
    public double calcularHaberes() {
        return 1000 + calcularAdicionalAntiguedad();
    }

    private double calcularAdicionalAntiguedad() {
        return 1000 * 0.20 * getAntiguedad();
    }
}