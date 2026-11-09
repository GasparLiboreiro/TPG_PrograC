package modelo.misiones;

import modelo.Asistente;


public class MisionRetorno extends Mision {
    public MisionRetorno(Asistente asistente) {
        super(asistente);
    }

    @Override public String getCodigo() {
        return "M-03";
    }
    @Override public int getCostoCombustible() {
        return 4;
    }
    @Override public int getCostoEnergia() {
        return 0;
    }
    @Override public int getCostoDesgaste() {
        return 4;
    }

    @Override
    protected String ejecutarMision() { //cuando se desarrollen las misiones
        return "Cartel M3: Nave retornada a la base de operaciones segura.";
    }

    @Override
    protected void prepararHook() {
        bitacora.registrar("Comprobación de coordenadas y vector de reentrada a la base.");
    }
}