package modelo;

// esta clase solo sirve para englobar a todas las subclases de Tripulante concretas
// con concretas se refiere a que son las clases base de la decoracion
public abstract class TripulanteConcreto extends Tripulante{
    // array util para el factory de Tripulantes y para la interfaz grafica
    public static String[] tipos_tripulante_concreto = {"Capitan", "Consejero", "Teniente", "Alferez"};
    public TripulanteConcreto(String Nombre, String identidad, int antiguedad) {
        super(Nombre, identidad, antiguedad);
    }
}
