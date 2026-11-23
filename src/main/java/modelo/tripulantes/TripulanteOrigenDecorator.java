package modelo.tripulantes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// esta clase solo sirve para englobar a todos los decorators de origen
public abstract class TripulanteOrigenDecorator extends Tripulante{
    // array util para el factory de Tripulantes y para la interfaz grafica
    public static String[] tipos_origen = {"Terricola", "Vulcano", "Marciano"};

    protected Tripulante tripulante_base;

    public TripulanteOrigenDecorator(Tripulante tripulante_base) {
        super("", "", 0); // las propiedades de esta clase no importan
        if (tripulante_base == null) {
            throw new IllegalArgumentException("El tripulante a decorar no puede ser nulo.");
        }
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
    /** @return el detalle del tripulante decorado más el subsidio por origen */
    @Override
    public List<ConceptoHaber> getDetalleHaberes() {
        List<ConceptoHaber> detalle = new ArrayList<>(tripulante_base.getDetalleHaberes());
        detalle.add(new ConceptoHaber("Subsidio por origen " + getPlanetaDeOrigen(), getSubsidioOrigen()));
        return Collections.unmodifiableList(detalle);
    }

    /** Subsidio mensual por origen; cada decorador de origen define su importe. */
    protected abstract double getSubsidioOrigen();
    @Override
    public abstract String toString();
}
