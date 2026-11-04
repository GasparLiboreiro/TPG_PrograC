package modelo.Elementos;

import modelo.ElementoDelUniverso;
import modelo.nave.Nave;

public class Asteroide implements ElementoDelUniverso {
    private int monto = 67;//hay que ver cuanto le ponemos, SIX SEVEN! ~°.°~
    private int danio = 7; //me parecio piola que haga daño tambien asi que ahi le puse


    @Override
    public void interactuar(Nave nave) {
        nave.consumirEnergia(this.monto);
        nave.recibirDanio(this.danio);
    }
}
