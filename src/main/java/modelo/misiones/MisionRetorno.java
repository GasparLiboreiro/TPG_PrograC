package modelo.misiones;

import modelo.Asistente;
import modelo.nave.Nave;


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
    protected String ejecutarMision() {
        Nave navePropia = asistente.getNave();
        navePropia.cargarCombustible(navePropia.getEspacioCombustible());
        navePropia.cargarEnergia(navePropia.getEspacioEnergia());
        navePropia.realizarMantenimiento();

        bitacora.registrar("Arribo exitoso a la base: recarga total de combustible y energía, mantenimiento realizado (desgaste 0).");
        return "Mision M3: Nave retornada a la base de operaciones segura.";
    }

    /** El retorno a base se permite aunque la nave requiera mantenimiento: es la misión que lo realiza. */
    @Override
    protected boolean exigeNaveSinMantenimientoPendiente() {
        return false;
    }

    @Override
    protected void prepararHook() {
        bitacora.registrar("Comprobación de coordenadas y vector de reentrada a la base.");
    }
}