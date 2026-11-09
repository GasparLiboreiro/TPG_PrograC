package modelo.nave;

public class NaveExploradora extends Nave {

    public NaveExploradora() {
        super(60, 80, 0);
    }

    @Override
    public void interactuar(Nave nave) {
        if (getDestruida()) {
            return; // los restos de una nave destruida no combaten
        }
        nave.enfrentar(this);
    }

    @Override
    public void enfrentar(NaveExploradora nave) {
        // Pacto de paz. se puede ver de que hacer en este caso, si permitirle al jugador tratar de pelearle igual y que decida el destino o que.
    }

    @Override
    public void enfrentar(NaveCarguera nave) {
        nave.destruir();
        //acá abria que ver de hacer algo más como registrarlo en la bitacora o algo
    }

    @Override
    public void enfrentar(NaveCombate nave) {
        destruir();
        // GGWP
    }
}