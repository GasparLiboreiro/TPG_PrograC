package modelo.Elementos;

import modelo.ElementoDelUniverso;
import modelo.nave.Nave;

public class Asteroide implements ElementoDelUniverso {
    private int monto = 25;//hay que ver cuanto le ponemos, SIX SEVEN! ~°.°~
    private int danio = 7; //me parecio piola que haga daño tambien asi que ahi le puse


    @Override
    public void interactuar(Nave nave) {
        nave.consumirEnergia(Math.min(this.monto, nave.getEnergia()));
        if (!nave.getDestruida()) {
            nave.aumentarDesgaste(this.danio);
        }
    }
    @Override
    public boolean esObstaculo() {
        return true;
    }
}
