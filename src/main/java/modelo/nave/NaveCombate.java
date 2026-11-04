package modelo.nave;

public class NaveCombate extends Nave {

    public NaveCombate() {
        super(80, 100, 0);
    }

    @Override
    public void interactuar(Nave nave) {
        nave.enfrentar(this);
    }

    @Override
    public void enfrentar(NaveExploradora nave) {
        nave.destruir();
        //acá abria que ver de hacer algo más como registrarlo en la bitacora o algo
    }

    @Override
    public void enfrentar(NaveCarguera nave) {
        destruir();
        // GGWP
    }

    @Override
    public void enfrentar(NaveCombate nave) {
        // Pacto de paz. se puede ver de que hacer en este caso, si permitirle al jugador tratar de pelearle igual y que decida el destino o que.
    }
}