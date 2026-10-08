package modelo;

import com.sun.jdi.InvalidTypeException;

public class MisionFactory {
    // este array podria usarse en la interfaz grafica para dejar elegir las misiones usando estos nombres
    public static final String[] tipos_misiones = {"Intercepcion", "Recoleccion", "Retorno"};

    /**
     * Crea una nueva mision del tipo proveido por parametro
     * <b>Pre:</b> tipo es igual a alguna de las opciones en MisionFactory.tipos_misiones y nave no es null
     *
     * @param tipo String con el nombre del tipo de mision, case sensitive
     * @param nave Instancia de la nave que va a ejecutar la mision
     * @return Instancia de una nave del tipo especificado
     */
    public static Mision crearMision(String tipo, Nave nave){
        assert nave!=null : "ASSERT: MisionFactory.crearMision recibio nave=null";
        assert tipo.equals(tipos_misiones[0]) || tipo.equals(tipos_misiones[1]) || tipo.equals(tipos_misiones[2]): "ASSERT: MisionFactory.crearMision recibio un tipo de mision invalido: \""+tipo+ "\"";
        switch(tipo){
            case "Intercepcion":
                return new MisionIntercepcion(nave);
            case "Recoleccion":
                return new MisionRecoleccion(nave);
            case "Retorno":
                return new MisionRetorno(nave);
            default:
                return null; // el asserto previene este caso, java te obliga a escribirlo igual
        }
    }
}
