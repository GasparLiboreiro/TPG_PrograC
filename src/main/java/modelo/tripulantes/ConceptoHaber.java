package modelo.tripulantes;

/**
 * Concepto que forma parte del haber mensual de un tripulante (cargo, antigüedad, consejos, origen).
 * <p><b>Invariantes:</b> inmutable; la descripción no es nula ni vacía y el importe no es negativo.</p>
 */
public final class ConceptoHaber {
    private final String descripcion;
    private final double importe;

    /**
     * @throws IllegalArgumentException si la descripción es nula o vacía, o el importe es negativo
     */
    public ConceptoHaber(String descripcion, double importe) {
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("El concepto del haber debe tener una descripción.");
        }
        if (importe < 0) {
            throw new IllegalArgumentException("El importe de un concepto no puede ser negativo: " + importe);
        }
        this.descripcion = descripcion;
        this.importe = importe;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getImporte() {
        return importe;
    }

    @Override
    public String toString() {
        return descripcion + ": " + importe + " PG";
    }
}