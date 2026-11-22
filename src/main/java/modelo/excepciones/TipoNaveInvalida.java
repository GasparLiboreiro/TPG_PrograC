package modelo.excepciones;
import modelo.nave.*;

public class TipoNaveInvalida extends RuntimeException {
	
    public TipoNaveInvalida(String tipo) {
        super("Tipo de nave invalido: \"" + tipo + "\". Las opciones validas son: "
                + String.join(", ", NaveFactory.tipos_naves));
    }
}
