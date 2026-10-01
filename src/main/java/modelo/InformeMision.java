package modelo;

public class InformeMision {
    private final String codigoMision;
    private final String estadoFinal;
    private final String cartelResultado;
    private final int combustibleRestante;
    private final int energiaRestante;
    private final int desgasteFinal;
    private final Bitacora bitacora;

    public InformeMision(Mision mision, Nave nave, Bitacora bitacora) {
        this.codigoMision = mision.getCodigo();
        this.estadoFinal = mision.isExitosa() ? "ÉXITO" : "FALLO";
        this.cartelResultado = mision.getCartelResultado();
        this.combustibleRestante = nave.getCombustible();
        this.energiaRestante = nave.getEnergia();
        this.desgasteFinal = nave.getDesgaste();
        this.bitacora = bitacora;
    }

    public String getCodigoMision() { return
            codigoMision;
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
    public Bitacora getBitacora() {
        return bitacora;
    }
    @Override
    public String toString() {
        return "=== INFORME DE MISIÓN [" + codigoMision + "] ===\n" +
                "Resultado: " + estadoFinal + "\n" +
                "Detalle: " + cartelResultado + "\n" +
                "Recursos finales -> Combustible: " + combustibleRestante +
                " | Energía: " + energiaRestante +
                " | Desgaste: " + desgasteFinal + "\n" +
                "Acontecimientos: " + bitacora.getEntradas();
    }
}