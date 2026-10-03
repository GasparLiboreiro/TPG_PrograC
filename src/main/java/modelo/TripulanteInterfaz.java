package modelo;

public interface TripulanteInterfaz {
    String getIdentidad();
    String getCargo();
    String getPlanetaDeOrigen();
    String getNombre();
    int getAntiguedad();

    void setCargo(String cargo);
    void setAntiguedad(int antiguedad);
    double calcularHaberes();
}
