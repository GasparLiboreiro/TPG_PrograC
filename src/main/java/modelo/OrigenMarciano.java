package modelo;

public class OrigenMarciano extends TripulanteDecorator {
    public OrigenMarciano(TripulanteInterfaz tripulante) {
        super(tripulante);
    }
    
    @Override
    public double calcularHaberes(){
        return super.calcularHaberes()+18;
    }
}
