package modelo;

public class OrigenTerricola extends TripulanteDecorator {
    public OrigenTerricola(TripulanteInterfaz tripulante) {
        super(tripulante);
    }
    @Override
    public double calcularHaberes(){
        return super.calcularHaberes()+20;
    }
}
