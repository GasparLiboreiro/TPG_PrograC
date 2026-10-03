package modelo;

public class TripulanteConsejero extends TripulanteDecorator {
    private int consejosRegistrados;

    public TripulanteConsejero(TripulanteInterfaz tripulante, int consejosRegistrados) {
        super(tripulante);
        this.consejosRegistrados = consejosRegistrados;
    }

    public TripulanteConsejero(TripulanteInterfaz tripulante){
        this(tripulante, 0);
    }

    @Override
    public double calcularHaberes() {
        return 600 + calcularAdicionalAntiguedad() + calcularAdicionalConsejos();
    }

    private double calcularAdicionalAntiguedad() {
        return 600 * 0.05 * getAntiguedad();
    }

    private double calcularAdicionalConsejos() {
        return consejosRegistrados * 2;
    }

    public int getConsejosRegistrados() {
        return consejosRegistrados;
    }

    public void addConsejosRegistrados(int consejos) {
        this.consejosRegistrados += consejos;
    }
}