package modelo.excepciones;
import modelo.misiones.MisionFactory;

public class TipoMisionInvalida extends RuntimeException {
	public TipoMisionInvalida(String tipo) {
        super("Tipo de mision invalido: \"" + tipo + "\". Las opciones validas son: "
                + String.join(", ", MisionFactory.tipos_misiones));
    }
}
