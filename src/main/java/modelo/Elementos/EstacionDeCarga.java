package modelo.Elementos;

import modelo.ElementoDelUniverso;
import modelo.nave.Nave;

public class EstacionDeCarga implements ElementoDelUniverso {
    private int monto = 67; //analizar como hacemos con esto si por monto unico, monto separado o porcentaje del total.
    @Override
    public void interactuar(Nave nave) {
        nave.cargarCombustible(monto);
        nave.cargarEnergia(monto);
    }
    @Override
    public boolean esObstaculo() {
        return false; // es una ayuda: conviene pasar por ella, no esquivarla
    }
}
