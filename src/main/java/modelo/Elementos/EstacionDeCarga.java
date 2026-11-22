package modelo.Elementos;

import modelo.ElementoDelUniverso;
import modelo.nave.Nave;

public class EstacionDeCarga implements ElementoDelUniverso {
    private int monto = 50; //analizar como hacemos con esto si por monto unico, monto separado o porcentaje del total.

    @Override
    public String interactuar(Nave nave) {
        nave.cargarCombustible(monto);
        nave.cargarEnergia(monto);
        return "Evento: Encuentro con una estación de carga! Se cargaron " + monto + " puntos de energía y combustible";
    }
    @Override
    public boolean esObstaculo() {
        return false; // es una ayuda: conviene pasar por ella, no esquivarla
    }


}
