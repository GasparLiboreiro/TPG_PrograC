package modelo.misiones;
import modelo.Asistente;

public class MisionFactory {
    // este array podria usarse en la interfaz grafica para dejar elegir las misiones usando estos nombres
    public static final String[] tipos_misiones = {"INTERCEPCION", "RECOLECCION", "RETORNO"};

    /**
     * Crea una nueva mision del tipo proveido por parametro
     * <b>Pre:</b>
     * -    tipo es igual a alguna de las opciones en MisionFactory.tipos_misiones
     * -    asistente no es null
     *
     * @param tipo String con el nombre del tipo de mision, case sensitive
     * @param asistente Instancia del asistente que va a ejecutar la mision
     * @throws IllegalArgumentException si asistente es null o el tipo es invalido
     * @return Instancia de una mision del tipo especificado
     */
    public static Mision crearMision(String tipo, Asistente asistente) { // quite la
        if (asistente==null) {
        	throw new IllegalArgumentException("MisionFactory.crearMision() recibio un asistente nula");
        }
        switch(tipo.toUpperCase()){
            case "INTERCEPCION":
                return new MisionIntercepcion(asistente);
            case "RECOLECCION":
                return new MisionRecoleccion(asistente);
            case "RETORNO":
                return new MisionRetorno(asistente);
            default:
            	throw new IllegalArgumentException("MisionFactory.crearMision() recibio un tipo de mision invalido: '"+tipo+"'");
        }
    }
}
