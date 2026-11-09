package modelo.misiones;

import java.util.Random;

import modelo.Asistente;
import modelo.ElementoDelUniverso;
import modelo.nave.Nave;


public class MisionIntercepcion extends Mision {

    public MisionIntercepcion(Asistente asistente) {
        super(asistente);
    }

    @Override public String getCodigo() {

        return "M-01";

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
    	ElementoDelUniverso elemDestino = asistente.analizarPosicion();
    	if (elemDestino instanceof Nave) {
    		Nave propiaNave = asistente.getNave();
    		Random random = new Random();
            int energiaRequerida = random.nextInt(51);       // Genera un valor entre 0 y 50
            int combustibleRequerido = random.nextInt(51);
            if (propiaNave.getCombustible() >= combustibleRequerido && propiaNave.getEnergia() >= energiaRequerida) {
            	Nave naveAliada = (Nave) elemDestino;
            	if (energiaRequerida > 0) {
            		propiaNave.consumirEnergia(energiaRequerida);
            		naveAliada.cargarEnergia(energiaRequerida);
            	}
            	if (combustibleRequerido > 0) {
            		propiaNave.consumirCombustible(combustibleRequerido);
            		naveAliada.cargarCombustible(combustibleRequerido);
            	}
            	return "Mision M1: Intercepción y asistencia completada con éxito.";
            } else {
            	return "Mision M1: Intercepción y asistencia no completada por falta de recursos.";
            }
    	} else {
    		return "Mision M1: Intercepción y asistencia no completada debido a nave aliada no encontrada.";
    	}
    }
    @Override
    protected void ejecutarHook() {
        bitacora.registrar(" Análisis táctico de radar y escaneo perimetral completado.");
    }
}