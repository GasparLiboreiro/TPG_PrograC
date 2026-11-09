package modelo;
import modelo.nave.*;

public interface ElementoDelUniverso {
    public void interactuar(Nave nave);
    /**
     * Indica si el radar de corto alcance debe tratar al elemento como un
     * obstáculo a esquivar con un salto warp.
     * @return true si la nave debe intentar esquivarlo
     */
    public boolean esObstaculo();
}
