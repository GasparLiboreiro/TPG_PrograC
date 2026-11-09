package modelo.nave;

public class NaveCombate extends Nave {

    public NaveCombate() {
        super(80, 100, 0);
    }

    @Override
    public String getTipo() {
    	return "COMBATE";
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
        	if (nave.getTipo() == "COMBATE") {
        		System.out.println("Pacto de paz");
        	} else {
        		System.out.println("Derrotado");
        		destruir();
        	}
    }
    
    @Override
    public boolean gana(Nave obj) {
    	return obj.ganaCombate();
    }
    @Override
    public boolean ganaCarguera() {
    	return false;
    }
    @Override
    public boolean ganaExploradora() {
    	return true;
    }
    @Override
    public boolean ganaCombate() {
    	return true;
    }
}