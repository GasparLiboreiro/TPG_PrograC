package modelo.misiones;

import modelo.Bitacora;
import modelo.nave.EstadoRecursos;
import modelo.nave.Nave;

public class InformeMision {
    private final String codigoMision;
    private final String estadoFinal;
    private final String cartelResultado;
    private final int combustibleRestante;
    private final int energiaRestante;
    private final int desgasteFinal;
    private final EstadoRecursos recursosIniciales;
    private final EstadoRecursos recursosAlLlegar;
    private final EstadoRecursos recursosFinales;
    private final Bitacora bitacora;

    public InformeMision(Mision mision, Nave nave) {
        this.codigoMision = mision.getCodigo();
        this.estadoFinal = mision.isExitosa() ? "ÉXITO" : "FALLO";
        this.cartelResultado = mision.getCartelResultado();
        this.combustibleRestante = nave.getCombustible();
        this.energiaRestante = nave.getEnergia();
        this.desgasteFinal = nave.getDesgaste();
        this.recursosIniciales = mision.getRecursosIniciales();
        this.recursosAlLlegar = mision.getRecursosAlLlegar();
        this.recursosFinales = mision.getRecursosFinales();
        this.bitacora = mision.getBitacora();
    }

    public String getCodigoMision() {
        return codigoMision;
    }
    public String getEstadoFinal() {
        return estadoFinal;
    }
    public String getCartelResultado() {
        return cartelResultado;
    }
    public int getCombustibleRestante() {
        return combustibleRestante;
    }
    public int getEnergiaRestante() {
        return energiaRestante;
    }
    public int getDesgasteFinal() {
        return desgasteFinal;
    }
    public EstadoRecursos getRecursosIniciales() {
        return recursosIniciales;
    }
    public EstadoRecursos getRecursosAlLlegar() {
        return recursosAlLlegar;
    }
    public EstadoRecursos getRecursosFinales() {
        return recursosFinales;
    }
    public Bitacora getBitacora() {
        return bitacora;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("=== INFORME DE MISIÓN [").append(codigoMision).append("] ===\n");
        ret.append("Resultado: ").append(estadoFinal).append("\n");
        ret.append("Detalle: ").append(cartelResultado).append("\n");
        ret.append("Recursos al iniciar -> ").append(recursosIniciales).append("\n");
        ret.append("Consumido en la travesía: ").append(recursosAlLlegar.variacionDesde(recursosIniciales)).append("\n");
        ret.append("Ganado/entregado al cerrar: ").append(recursosFinales.variacionDesde(recursosAlLlegar)).append("\n");
        ret.append("Recursos finales -> ").append(recursosFinales).append("\n");
        ret.append("Acontecimientos: \n");
        for (String entrada : bitacora.getEntradas()) {
            ret.append("> ").append(entrada).append("\n");
        }
        return ret.toString();
    }
}