package modelo.nave;

public class NaveCombate extends Nave {
    protected int combustibleMax = 80;
    protected int energiaMax = 100;

    public NaveCombate() {
        super(80, 100, 0);
    }

    @Override
    public String getTipo() {
        return "COMBATE";
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
        } else if ("COMBATE".equals(nave.getTipo())) {
            evento += " - Resultado: Hemos decidido hacer un pacto de paz";
        } else {
            destruir();
            evento += " - Resultado: Hemos ganado el combate";
        }
        return evento;
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