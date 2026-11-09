package modelo.nave;

public class NaveExploradora extends Nave {

    public NaveExploradora() {
        super(60, 80, 0);
    }
    
    @Override
    public String getTipo() {
    	return "EXPLORADORA";
    }

    @Override
    public void interactuar(Nave nave) {
        if (getDestruida()) {
            return; // los restos de una nave destruida no combaten
        }
        boolean rta = gana(nave);
        if (rta) {
        	System.out.println("Nave enemiga destruida");
        } else
        	if (nave.getTipo() == "EXPLORADORA") {
        		System.out.println("Pacto de paz");
        	} else {
        		System.out.println("Derrotado");
        		destruir();
        	}
    }
    
    @Override
    public boolean gana(Nave obj) {
    	return obj.ganaExploradora();
    }
    @Override
    public boolean ganaCarguera() {
    	return true;
    }
    @Override
    public boolean ganaExploradora() {
    	return false;
    }
    @Override
    public boolean ganaCombate() {
    	return false;
    }
}