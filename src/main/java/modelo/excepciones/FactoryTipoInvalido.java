package modelo.excepciones;

public class FactoryTipoInvalido extends Exception {
    public String tipo_ingresado;
    // this.detailMessage tiene la descripcion del mensaje

    public FactoryTipoInvalido(String tipo_ingresado, String mensaje)
    {
        super(mensaje);
        this.tipo_ingresado = tipo_ingresado;
    }
}
