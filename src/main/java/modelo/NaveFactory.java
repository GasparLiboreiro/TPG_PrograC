package modelo;

public class NaveFactory {
    // este array podria usarse en la interfaz grafica para dejar elegir el tipo de nave usando estos nombres
    public static final String[] tipos_naves = {"Exploradora", "Carguero", "Combate"};

    /**
     * Crea una nueva nave del tipo proveido por parametro </br>
     * <b>Pre:</b> tipo es igual a alguna de las opciones en NaveFactory.tipos_naves
     *
     * @param tipo String con el nombre del tipo de nave, case sensitive
     * @return Instancia de una nave del tipo especificado
     */
    public static Nave crearNave(String tipo)
    {
        assert tipo.equals(tipos_naves[0]) || tipo.equals(tipos_naves[1]) || tipo.equals(tipos_naves[2]) : "ASSERT: NaveFactory.crearNave recibio un tipo de nave invalido: \""+tipo+ "\"";
        switch(tipo){
            case "Exploradora":
                return new Nave(60, 80, 0);
            case "Carguero":
                return new Nave(100, 60, 0); // en un futuro estos new podrian ser de otras clases que heredan Nave
            case "Combate":
                return new Nave(80, 100, 0);
            default:
                return null; // el asserto previene este caso, java te obliga a escribirlo igual
        }
    }
}
