package modelo.excepciones;

public class TransicionEstadoInvalida extends Exception {
    public TransicionEstadoInvalida(String message) {
        super(message);
    }
}
