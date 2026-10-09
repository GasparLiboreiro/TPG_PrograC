package modelo.misiones;
import modelo.excepciones.TipoMisionInvalida;
import modelo.nave.Nave;

public class MisionFactory {
    // este array podria usarse en la interfaz grafica para dejar elegir las misiones usando estos nombres
    public static final String[] tipos_misiones = {"INTERCEPCION", "RECOLECCION", "RETORNO"};

    /**
     * Crea una nueva mision del tipo proveido por parametro
     * <b>Pre:</b> tipo es igual a alguna de las opciones en MisionFactory.tipos_misiones
     * <b>Pre:</b> nave no es null
     *
     * @param tipo String con el nombre del tipo de mision, case sensitive
     * @param nave Instancia de la nave que va a ejecutar la mision
     * @throws TipoMisionInvalida si tipo no esta en tipos_misiones
     * @throws IllegalArgumentException si nave es null
     * @return Instancia de una nave del tipo especificado
     */
    public static Mision crearMision(String tipo, Nave nave) throws TipoMisionInvalida {
        if (nave==null) {
        	throw new IllegalArgumentException("La nave no puede ser nula");
        }
        switch(tipo.toUpperCase()){
            case "INTERCEPCION":
                return new MisionIntercepcion(nave);
            case "RECOLECCION":
                return new MisionRecoleccion(nave);
            case "RETORNO":
                return new MisionRetorno(nave);
            default:
            	throw new TipoMisionInvalida(tipo);
        }
    }
}
