package modelo;

public class MisionIntercepcion extends Mision {
    public MisionIntercepcion(Nave nave) {
        super(nave);
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
        return "Cartel M1: Intercepción y asistencia en ruta completada con éxito.";
    }
}