package modelo;

public abstract class Tripulante{
    private String Nombre;
    private String identidad;
    private int antiguedad;

    public Tripulante(String Nombre, String identidad, int antiguedad) {
        this.Nombre = Nombre;
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
        return Nombre;
    }

    public void setAntiguedad(int antiguedad) {
        this.antiguedad = antiguedad;
    }

    public abstract String getCargo(); // las instancias hijas de "TripulanteConcreto" definen este return

    public abstract String getPlanetaDeOrigen(); // las instancias hijas de "TripulanteConcreto" definen este return como null porque no conoce el origen, el decorator hijo de "TripulanteOrigen" 'pisa' esta funcion

    public abstract double calcularHaberes();
}