package modelo;

import modelo.excepciones.TransicionEstadoInvalida;

public class MotorWarp {
    private StateMotorWarp estado;

    public MotorWarp()
    {
        this.estado = new StateMWDisponible(this);
    }

    public void setEstado(StateMotorWarp estado)
    {
        this.estado = estado;
    }

    public void preparar_salto() throws TransicionEstadoInvalida {
        estado.preparar_salto();
    }

    public void arrancar() throws TransicionEstadoInvalida {
        estado.arrancar();
    }

    public void detener() throws TransicionEstadoInvalida {
        estado.detener();
    }

    public void enfriar_motor() throws TransicionEstadoInvalida {
        estado.enfriar_motor();
    }
}
