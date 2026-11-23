package modelo.nave;

/**
 * Foto inmutable de los recursos de una nave en un momento dado.
 * Se usa para calcular el balance de una misión.
 */
public final class EstadoRecursos {
    private final int combustible;
    private final int energia;
    private final int desgaste;
    private final int gemas;

    private EstadoRecursos(int combustible, int energia, int desgaste, int gemas) {
        this.combustible = combustible;
        this.energia = energia;
        this.desgaste = desgaste;
        this.gemas = gemas;
    }

    /** @return los recursos actuales de la nave */
    public static EstadoRecursos de(Nave nave) {
        return new EstadoRecursos(nave.getCombustible(), nave.getEnergia(), nave.getDesgaste(), nave.getGemas());
    }

    public int getCombustible() { return combustible; }
    public int getEnergia() { return energia; }
    public int getDesgaste() { return desgaste; }
    public int getGemas() { return gemas; }

    /** @return la variación de cada recurso desde "antes" hasta este estado, como texto con signo */
    public String variacionDesde(EstadoRecursos antes) {
        return "combustible " + conSigno(combustible - antes.combustible)
                + ", energía " + conSigno(energia - antes.energia)
                + ", desgaste " + conSigno(desgaste - antes.desgaste)
                + ", gemas " + conSigno(gemas - antes.gemas);
    }

    private static String conSigno(int valor) {
        return (valor > 0 ? "+" : "") + valor;
    }

    @Override
    public String toString() {
        return "Combustible: " + combustible + " | Energía: " + energia + " | Desgaste: " + desgaste + " | Gemas: " + gemas;
    }
}