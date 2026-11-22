package modelo.tripulantes;

public abstract class Tripulante{
    private String nombre;
    private String identidad;
    private int antiguedad;

    public Tripulante(String nombre, String identidad, int antiguedad) {
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
        this.antiguedad = antiguedad;
    }

    public abstract String getCargo(); // las instancias hijas de "TripulanteConcreto" definen este return

    public abstract String getPlanetaDeOrigen(); // las instancias hijas de "TripulanteConcreto" definen este return como null porque no conoce el origen, el decorator hijo de "TripulanteOrigen" 'pisa' esta funcion

    public abstract double calcularHaberes();

    public String toString(){
        return "Nombre:'"+nombre+"'  No. de identidad:'"+identidad+"'  Anios de antiguedad:"+antiguedad;
    }
}