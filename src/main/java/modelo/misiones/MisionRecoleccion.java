package modelo.misiones;

import modelo.Asistente;


public class MisionRecoleccion extends Mision {
    public MisionRecoleccion(Asistente asistente) {
        super(asistente);
    }

    @Override public String getCodigo() {
        return "M-02";
    }
    @Override public int getCostoCombustible() {
        return 4;
    }
    @Override public int getCostoEnergia() {
        return 10;
    }
    @Override public int getCostoDesgaste() {
        return 4;
    }

    @Override
    protected String ejecutarMision() { //cuando se desarrollen las misiones
        return "Cartel M2: Muestra recolectada e inventariada de forma segura.";
    }

    @Override
    protected void ejecutarHook() {
        bitacora.registrar("Muestras almacenadas en bodega estanca presurizada.");
    }
}