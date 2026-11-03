package modelo.nave;

public class NaveFactory {
    // este array podria usarse en la interfaz grafica para dejar elegir el tipo de nave usando estos nombres
    public static final String[] tipos_naves = {"EXPLORADORA", "CARGUERO", "COMBATE"};

    /**
     * Crea una nueva nave del tipo proveido por parametro </br>
     * <b>Pre:</b> tipo es igual a alguna de las opciones en NaveFactory.tipos_naves
     *
     * @param tipo String con el nombre del tipo de nave, case sensitive
	 * @throws IllegalArgumentException si el tipo de nave es invalido
     * @return Instancia de una nave del tipo especificado
     */
    public static Nave crearNave(String tipo) {
    	
    	if (tipo == null)
            throw new IllegalArgumentException("NaveFactory.crearNave() recibio un tipo igual a null");
    	
    	switch (tipo.toUpperCase()) {
	        case "EXPLORADORA":
	            return new Nave(60, 80, 0);
	        case "CARGUERO":
	            return new Nave(100, 60, 0);
	        case "COMBATE":
	            return new Nave(80, 100, 0);
	        default:
				throw new IllegalArgumentException("NaveFactory.crearNave() recibio un tipo que no estaba invalido: '"+tipo+"'");
    	}
    }
}
