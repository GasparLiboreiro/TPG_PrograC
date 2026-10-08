package modelo;

// esta clase solo sirve para englobar a todos los decorators de origen
public abstract class TripulanteOrigenDecorator extends Tripulante{
    // array util para el factory de Tripulantes y para la interfaz grafica
    public static String[] tipos_origen = {"Terricola", "Vulcano", "Marciano"};

    protected TripulanteConcreto tripulante_base;

    public TripulanteOrigenDecorator(TripulanteConcreto tripulante_base) {
        super("", "", 0); // las propiedades de esta clase no importan
        this.tripulante_base = tripulante_base;
    }

    // importante!!
    // sobreescribo todos los metodos para que todas las acciones de la clase sean redirigidas al objecto tripulante_base
    @Override
    public String getIdentidad() {
        return tripulante_base.getIdentidad();
    }
    @Override
    public int getAntiguedad(){
        return tripulante_base.getAntiguedad();
    }
    @Override
    public String getNombre(){
        return tripulante_base.getNombre();
    }
    @Override
    public void setAntiguedad(int antiguedad){
        tripulante_base.setAntiguedad(antiguedad);
    }
    @Override
    public String getCargo(){
        return tripulante_base.getCargo();
    }



    // las subclases decoradoras de origen deben escribir estos metodos
    @Override
    public abstract String getPlanetaDeOrigen();
    @Override
    public abstract double calcularHaberes();
}
