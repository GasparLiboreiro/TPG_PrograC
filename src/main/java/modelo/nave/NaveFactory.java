package modelo.nave;

import modelo.excepciones.TipoNaveInvalida;

public class NaveFactory {
    // este array podria usarse en la interfaz grafica para dejar elegir el tipo de nave usando estos nombres
    public static final String[] tipos_naves = {"EXPLORADORA", "CARGUERO", "COMBATE"};

	private static boolean tipo_valido(String tipo) {
		if(tipo==null)
			return false;
		else {
			int i;
			for(i=0; i<tipos_naves.length; i++)
				if(tipos_naves[i].equals(tipo))
					break;
			if(i>=tipos_naves.length)
				return false;
			else
				return true;
		}
	}

    /**
     * Crea una nueva nave del tipo proveido por parametro </br>
     * <b>Pre:</b> tipo es igual a alguna de las opciones en NaveFactory.tipos_naves
     * @throws TipoNaveInvalida si el tipo es nulo o no es uno de los admitidos
     *
     * @param tipo String con el nombre del tipo de nave, case sensitive
     * @return Instancia de una nave del tipo especificado
     */
    public static Nave crearNave(String tipo) {
        if (!tipo_valido(tipo)) {
            throw new TipoNaveInvalida(tipo);
        }

		switch (tipo.toUpperCase()) {
	        case "EXPLORADORA":
	            return new NaveExploradora();
	        case "CARGUERO":
	            return new NaveCarguera();
	        case "COMBATE":
                return new NaveCombate();
			default:
				throw new TipoNaveInvalida(tipo); // no se alcanza: el tipo ya fue validado
		}
    }
}
