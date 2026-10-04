package modelo;

public class OrigenVulcano extends TripulanteDecorator {
    public OrigenVulcano(TripulanteInterfaz tripulante) {
        super(tripulante);
    }
    
    @Override
    public double calcularHaberes(){
        return super.calcularHaberes() + 30;
    }
}
