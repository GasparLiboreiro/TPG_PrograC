package modelo;

import modelo.excepciones.TransicionEstadoInvalida;

public class MotorWarp {
    public static int ESTADO_DISPONIBLE = 0;
    public static int ESTADO_PREPARANDO_SALTO = 1;
    public static int ESTADO_EN_WARP = 2;
    public static int ESTADO_ENFRIAMIENTO = 3;
    public static String[] ESTADO_NOMBRE = {"DISPONIBLE", "PREPARANDO SALTO", "EN WARP", "ENFRIAMIENTO"};

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

    public int getEstado() {
        return estado.getEstado();
    }
}
