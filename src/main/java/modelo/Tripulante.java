package modelo;

public class Tripulante implements TripulanteInterfaz {
    private String Nombre;
    private String identidad;
    private String cargo;
    private String planetaDeOrigen;
    private int antiguedad;

    public Tripulante(String Nombre, String identidad, String cargo, String planetaDeOrigen, int antiguedad) {
        this.Nombre = Nombre;
        this.identidad = identidad;
        this.cargo = cargo;
        this.planetaDeOrigen = planetaDeOrigen;
        this.antiguedad = antiguedad;
    }

    @Override
    public String getIdentidad() {
        return identidad;
    }

    @Override
    public String getCargo() {
        return cargo;
    }

    @Override
    public String getPlanetaDeOrigen() {
        return planetaDeOrigen;
    }

    @Override
    public int getAntiguedad() {
        return antiguedad;
    }

    @Override
    public String getNombre(){
        return Nombre;
    }

    @Override
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    @Override
    public void setAntiguedad(int antiguedad) {
        this.antiguedad = antiguedad;
    }

    @Override
    public double calcularHaberes() {
        return 0;
    }
}