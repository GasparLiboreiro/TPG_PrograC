package modelo.nave;

public class NaveCarguera extends Nave {

    public NaveCarguera() {
        super(100, 60, 0);
    }

    @Override
    public String getTipo() {
    	return "CARGUERA";
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
        	if (nave.getTipo() == "CARGUERA") {
        		System.out.println("Pacto de paz");
        	} else {
        		System.out.println("Derrotado");
        		destruir();
        	}
    }
    
    @Override
    public boolean gana(Nave obj) {
    	return obj.ganaCarguera();
    }
    @Override
    public boolean ganaCarguera() {
    	return false;
    }
    @Override
    public boolean ganaExploradora() {
    	return false;
    }
    @Override
    public boolean ganaCombate() {
    	return true;
    }
}