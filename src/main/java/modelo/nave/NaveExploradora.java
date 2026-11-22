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
    public String interactuar(Nave nave) {
        if (getDestruida()) {
            return "Evento: La nave esta destruida, ya no puede combatir :c"; // los restos de una nave destruida no combaten
        }
        String evento = "Evento: Encuentro con nave del tipo " + nave.getTipo();
        boolean rta = gana(nave);
        if (rta) {
            evento += " - Resultado: Hemos perdido la batalla";
        } else if ("EXPLORADORA".equals(nave.getTipo())) {
            evento += " - Resultado: Hemos decidido hacer un pacto de paz";
        } else {
            destruir();
            evento += " - Resultado: Hemos ganado el combate contra";
        }
        return evento;
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