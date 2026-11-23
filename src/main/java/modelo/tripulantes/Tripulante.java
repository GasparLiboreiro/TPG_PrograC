package modelo.tripulantes;

import java.util.List;

public abstract class Tripulante{
    private String nombre;
    private String identidad;
    private int antiguedad;

    public Tripulante(String nombre, String identidad, int antiguedad) {
        if (antiguedad < 0) {
            throw new IllegalArgumentException("La antigüedad no puede ser negativa: " + antiguedad);
        }
        this.nombre = nombre;
        this.identidad = identidad;
        this.antiguedad = antiguedad;
    }

    public String getIdentidad() {
        return identidad;
    }
    public int getAntiguedad() {
        return antiguedad;
    }

    public String getNombre(){
        return nombre;
    }

    public void setAntiguedad(int antiguedad) {
        if (antiguedad < 0) {
            throw new IllegalArgumentException("La antigüedad no puede ser negativa: " + antiguedad);
        }
        this.antiguedad = antiguedad;
    }

    public abstract String getCargo(); // las instancias hijas de "TripulanteConcreto" definen este return

    public abstract String getPlanetaDeOrigen(); // las instancias hijas de "TripulanteConcreto" definen este return como null porque no conoce el origen, el decorator hijo de "TripulanteOrigen" 'pisa' esta funcion

    /**
     * Detalle del haber mensual: un concepto por cada aporte (cargo, antigüedad, consejos, origen).
     * @return lista de solo lectura con los conceptos
     */
    public abstract List<ConceptoHaber> getDetalleHaberes();

    /** @return el haber mensual total: la suma de los conceptos del detalle */
    public final double calcularHaberes() {
        double total = 0;
        for (ConceptoHaber concepto : getDetalleHaberes()) {
            total += concepto.getImporte();
        }
        return total;
    }

    /** @return el detalle como texto, por ejemplo "Cargo Capitán: 1000.0 PG + Antigüedad (2 años x 20%): 400.0 PG" */
    public String getDetalleHaberesTexto() {
        StringBuilder texto = new StringBuilder();
        for (ConceptoHaber concepto : getDetalleHaberes()) {
            if (texto.length() > 0) {
                texto.append(" + ");
            }
            texto.append(concepto);
        }
        return texto.toString();
    }

    public String toString(){
        return "Nombre:'"+nombre+"'  No. de identidad:'"+identidad+"'  Anios de antiguedad:"+antiguedad;
    }
}