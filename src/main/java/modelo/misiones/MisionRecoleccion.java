package modelo.misiones;

import modelo.nave.Nave;

public class MisionRecoleccion extends Mision {
    public MisionRecoleccion(Nave nave) {
        super(nave);
    }

    @Override public String getCodigo() {
        return "M-02";
    }
    @Override public int getCostoCombustible() {
        return 4;
    }
    @Override public int getCostoEnergia() {
        return 5;
    }
    @Override public int getCostoDesgaste() {
        return 4;
    }

    @Override
    protected String ejecutarMision() { //cuando se desarrollen las misiones
        return "Cartel M2: Muestra recolectada e inventariada de forma segura.";
    }
}