package modelo.Elementos;

import modelo.ElementoDelUniverso;
import modelo.nave.Nave;

public class EstacionDeCarga implements ElementoDelUniverso {
    private int monto = 50; // carga hasta 50 de combustible y 50 de energía, sin superar la capacidad de la nave

    @Override
    public String interactuar(Nave nave) {
        int combustible = Math.min(monto, nave.getEspacioCombustible());
        int energia = Math.min(monto, nave.getEspacioEnergia());
        nave.cargarCombustible(combustible);
        nave.cargarEnergia(energia);
        return "Evento: Encuentro con una estación de carga. Se cargaron " + combustible
                + " de combustible y " + energia + " de energía.";
    }
}