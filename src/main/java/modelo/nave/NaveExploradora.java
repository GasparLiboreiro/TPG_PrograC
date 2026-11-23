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
            return "Evento: Restos de una nave destruida, no hay combate.";
        }
        String evento = "Evento: Encuentro con una nave enemiga del tipo " + getTipo();
        if (gana(nave)) {               // la nave del jugador le gana a esta nave
            destruir();
            evento += " - Resultado: Hemos ganado el combate, la nave enemiga fue destruida";
        } else if (nave.gana(this)) {   // esta nave le gana a la nave del jugador
            nave.destruir();
            evento += " - Resultado: Hemos perdido la batalla, nuestra nave fue destruida";
        } else {
            evento += " - Resultado: Hemos decidido hacer un pacto de paz";
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