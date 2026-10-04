package modelo.excepciones;

public class EstadoInvalido extends RuntimeException {
    public EstadoInvalido(String message) {
        super(message);
    }
}
