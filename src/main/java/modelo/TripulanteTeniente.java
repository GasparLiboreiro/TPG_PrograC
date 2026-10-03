package modelo;

public class TripulanteTeniente extends TripulanteDecorator {

    public TripulanteTeniente(TripulanteInterfaz tripulante) {
        super(tripulante);
    }

    @Override
    public double calcularHaberes() {
        return 400 + calcularAdicionalAntiguedad();
    }

    private double calcularAdicionalAntiguedad() {
        return 400 * 0.03 * getAntiguedad();
    }
}
