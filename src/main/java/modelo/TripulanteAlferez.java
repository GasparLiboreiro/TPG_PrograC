package modelo;

public class TripulanteAlferez extends TripulanteDecorator {

    public TripulanteAlferez(TripulanteInterfaz tripulante) {
        super(tripulante);
    }

    @Override
    public double calcularHaberes() {
        return 200 + calcularAdicionalAntiguedad() ;
    }

    private double calcularAdicionalAntiguedad() {
        return 200 * 0.005 * getAntiguedad();
    }
}
