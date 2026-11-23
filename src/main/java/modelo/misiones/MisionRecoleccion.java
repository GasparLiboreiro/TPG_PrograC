package modelo.misiones;

import java.util.Random;

import modelo.Asistente;
import modelo.nave.Nave;

/** M-02: llegar al punto de interés y recolectar entre 20 y 100 gemas (se suman a la nave en el cierre). */
public class MisionRecoleccion extends Mision {
    private static final int GEMAS_MINIMAS = 20;
    private static final int GEMAS_MAXIMAS = 100;

    private final Random azar = new Random();
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
        this.gemasRecolectadas = azar.nextInt(GEMAS_MAXIMAS - GEMAS_MINIMAS + 1) + GEMAS_MINIMAS;
        bitacora.registrar("Extracción en el punto de interés: " + gemasRecolectadas + " gemas obtenidas.");
        return "Mision M2: Muestra recolectada e inventariada de forma segura.";
    }

    /** Las gemas se incorporan a la nave en el cierre. */
    @Override
    protected void aplicarResultado() {
        Nave nave = asistente.getNave();
        nave.agregarGemas(gemasRecolectadas);
        bitacora.registrar("Se incorporaron " + gemasRecolectadas + " gemas a la bodega (total: " + nave.getGemas() + ").");
    }

    public int getGemasRecolectadas() { return gemasRecolectadas; }
}