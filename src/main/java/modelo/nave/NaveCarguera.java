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
    public String interactuar(Nave nave) {
        if (getDestruida()) {
            return "Evento: La nave esta destruida, ya no puede combatir :c"; // los restos de una nave destruida no combaten
        }
        String evento = "Evento: Encuentro con nave del tipo " + nave.getTipo();
        boolean rta = gana(nave);
        if (rta) {
            evento += " - Resultado: Hemos perdido la batalla";
        } else if ("CARGUERA".equals(nave.getTipo())) {
            evento += " - Resultado: Hemos decidido hacer un pacto de paz";
        } else {
            destruir();
            evento += " - Resultado: Hemos ganado el combate";
        }
        return evento;
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