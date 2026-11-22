package vista;

// Guarda exactamente los datos escritos o seleccionados en el formulario.
public class DatosTripulante {
    private final String oficio;
    private final String origen;
    private final String nombre;
    private final String identidad;
    private final int antiguedad;

    public DatosTripulante(String oficio, String origen, String nombre,
                           String identidad, int antiguedad) {
        this.oficio = oficio;
        this.origen = origen;
        this.nombre = nombre;
        this.identidad = identidad;
        this.antiguedad = antiguedad;
    }

    public String getOficio() {
        return oficio;
    }

    public String getOrigen() {
        return origen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIdentidad() {
        return identidad;
    }

    public int getAntiguedad() {
        return antiguedad;
    }

    @Override
    public String toString() {
        return "Nombre: " + nombre + " | Identidad: " + identidad
                + " | Oficio: " + oficio + " | Origen: " + origen
                + " | Antigüedad: " + antiguedad;
    }
}
