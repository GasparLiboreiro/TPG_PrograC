package modelo;

public abstract class TripulanteDecorator implements TripulanteInterfaz {
    protected TripulanteInterfaz tripulante;

    public TripulanteDecorator(TripulanteInterfaz tripulante) {
        this.tripulante = tripulante;
    }

    @Override
    public String getIdentidad() {
        return tripulante.getIdentidad();
    }

    @Override
    public String getCargo() {
        return tripulante.getCargo();
    }

    @Override
    public String getPlanetaDeOrigen() {
        return tripulante.getPlanetaDeOrigen();
    }

    @Override
    public String getNombre(){
        return tripulante.getNombre();
    }

    @Override
    public int getAntiguedad() {
        return tripulante.getAntiguedad();
    }

    @Override
    public void setCargo(String cargo) {
        tripulante.setCargo(cargo);
    }

    @Override
    public void setAntiguedad(int antiguedad) {
        tripulante.setAntiguedad(antiguedad);
    }

    @Override
    public double calcularHaberes() {
        return tripulante.calcularHaberes();
    }
}
