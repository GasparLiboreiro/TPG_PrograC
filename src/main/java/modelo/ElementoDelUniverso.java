package modelo;
import modelo.nave.*;

public interface ElementoDelUniverso {
    /**
     * Funcion que se encarga de analizar como es
     * que los elementos interactuan con la nave.
     *
     * @return Devuelve un String con el evento de lo sucedido
     */
    public String interactuar(Nave nave);


    /**
     * Indica si el radar de corto alcance debe tratar al elemento como un
     * obstáculo a esquivar con un salto warp.
     *
     * @return true si la nave debe intentar esquivarlo
     */
    public boolean esObstaculo();
}