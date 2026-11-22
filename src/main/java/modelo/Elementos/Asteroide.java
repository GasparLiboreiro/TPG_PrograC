package modelo.Elementos;

import modelo.ElementoDelUniverso;
import modelo.nave.Nave;

public class Asteroide implements ElementoDelUniverso {
    private int monto = 25;
    private int danio = 20;


    @Override
    public String interactuar(Nave nave) {
        nave.consumirEnergia(Math.min(this.monto, nave.getEnergia()));
        if (!nave.getDestruida()) {
            nave.aumentarDesgaste(this.danio);
        }
        String evento = "Evento: Encuentro con un asteroide, Se perdieron " + monto + " puntos de energía y " +  danio + " puntos de daño";
        if (nave.getDestruida()){
            evento += ". La nave no pudo soportar el impacto y resultó destruida.";
        }
        return evento;
    }
    @Override
    public boolean esObstaculo() {
        return true;
    }


}
