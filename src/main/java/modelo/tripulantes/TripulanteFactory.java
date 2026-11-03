package modelo.tripulantes;

public class TripulanteFactory {
    public static final String[] tipos_oficio = {"CAPITAN", "CONSEJERO", "TENIENTE", "ALFEREZ"};
    public static final String[] tipos_origen = {"TERRICOLA", "MARCIANO", "VULCANO"};

    /**
     * Crea un nuevo tripulante con el tipo que se especifique en la invocacion del metodo
     * <b>Pre:</b>
     * -    tipo_oficio es igual a null
     * -    tipo_origen es igual a null
     * -    nombre es igual a null
     * -    identidad es igual a null
     * -    antiguedad es igual a null
     * -    tipo_oficio es valido
     * -    tipo_origen es valido
     *
     * @param tipo_oficio String que indica el oficio del tripulante a crear
     * @param tipo_origen String que indica el oficio del tripulante a crear
     * @param nombre String que indica el nombre del tripulante a crear
     * @param identidad String que indica la identidad del tripulante a crear
     * @param int que indica los años de antiguedad del tripulante
     * @throws IllegalArgumentException si los parametros son null o si tipo_oficio y/o tipo_origen no son validos
     * @return Instancia de tripulante creado
     */
    public static Tripulante crearTripulante(String tipo_oficio, String tipo_origen, String nombre, String identidad, int antiguedad)
    {
        if(tipo_oficio == null)
            throw new IllegalArgumentException("TripulanteFactory.crearTripulante() recibio un tipo_oficio igual a null");
        if(tipo_origen == null)
            throw new IllegalArgumentException("TripulanteFactory.crearTripulante() recibio un tipo_origen igual a null");
        if(nombre == null)
            throw new IllegalArgumentException("TripulanteFactory.crearTripulante() recibio un nombre igual a null");
        if(identidad == null)
            throw new IllegalArgumentException("TripulanteFactory.crearTripulante() recibio un identidad igual a null");
        if(antiguedad < 0)
            throw new IllegalArgumentException("TripulanteFactory.crearTripulante() recibio una antiguedad menor a cero");

        Tripulante tripulante;

        switch(tipo_oficio){
            case "CAPITAN":
                tripulante = new TripulanteCapitan(nombre, identidad, antiguedad);
                break;
            case "CONSEJERO":
                tripulante = new TripulanteConsejero(nombre, identidad, antiguedad);
                break;
            case "TENIENTE":
                tripulante = new TripulanteTeniente(nombre, identidad, antiguedad);
                break;
            case "ALFEREZ":
                tripulante = new TripulanteAlferez(nombre, identidad, antiguedad);
                break;
            default:
                throw new IllegalArgumentException("TripulanteFactory.crearTripulante() recibio un tipo_oficio invalido: '"+tipo_oficio+"'");
        }

        switch(tipo_origen){
            case "TERRICOLA":
                tripulante = new TripulanteOrigenTerricola((TripulanteConcreto) tripulante);
                break;
            case "MARCIANO":
                tripulante = new TripulanteOrigenMarciano((TripulanteConcreto) tripulante);
                break;
            case "VULCANO":
                tripulante = new TripulanteOrigenVulcano((TripulanteConcreto) tripulante);
                break;
            default:
                throw new IllegalArgumentException("TripulanteFactory.crearTripulante() recibio un tipo_origen invalido: '"+tipo_origen+"'");
        }

        return tripulante;
    }
}
