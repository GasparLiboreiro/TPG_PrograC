package modelo.nave;

public class NaveCarguera extends Nave {

    public NaveCarguera() {
        super(100, 60, 0);
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
        destruir();
        // GGWP
    }

    @Override
    public void enfrentar(NaveCarguera nave) {
        // Pacto de paz. se puede ver de que hacer en este caso, si permitirle al jugador tratar de pelearle igual y que decida el destino o que.
    }

    @Override
    public void enfrentar(NaveCombate nave) {
        nave.destruir();
        //acá abria que ver de hacer algo más como registrarlo en la bitacora o algo
    }
}