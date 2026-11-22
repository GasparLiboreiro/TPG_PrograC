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
    	int combustibleFaltante = navePropia.getCombustibleMax() - navePropia.getCombustible();
        int energiaFaltante = navePropia.getEnergiaMax() - navePropia.getEnergia();

        if (combustibleFaltante > 0) {
        	navePropia.cargarCombustible(combustibleFaltante);
        }
        if (energiaFaltante > 0) {
        	navePropia.cargarEnergia(energiaFaltante);
        }
        
        navePropia.setDesgaste(0);

        bitacora.registrar("Arribo exitoso a la base, recursos recuperados.");
        return "Mision M3: Nave retornada a la base de operaciones segura.";
    }

    @Override
    protected void prepararHook() {
        bitacora.registrar("Comprobación de coordenadas y vector de reentrada a la base.");
    }
}