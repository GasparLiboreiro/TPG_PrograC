package modelo.misiones;

import java.util.Random;

import modelo.Asistente;

public class MisionRecoleccion extends Mision {
	private int gemasRecolectadas;
	
    public MisionRecoleccion(Asistente asistente) {
        super(asistente);
        this.gemasRecolectadas = 0;
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
    protected String ejecutarMision() {
    	Random random = new Random();
        this.gemasRecolectadas = random.nextInt(81) + 20;
        bitacora.registrar("Extracción exitosa en punto de interés. Gemas obtenidas: " + gemasRecolectadas);
        return "Mision M2: Muestra recolectada e inventariada de forma segura.";
    }

    @Override
    protected void ejecutarHook() {
        bitacora.registrar("Muestras almacenadas en bodega estanca presurizada.");
    }
}