package modelo;

import modelo.excepciones.TransicionEstadoInvalida;

public class MotorWarp {
    public static int ESTADO_DISPONIBLE = 0;
    public static int ESTADO_PREPARANDO_SALTO = 1;
    public static int ESTADO_EN_WARP = 2;
    public static int ESTADO_ENFRIAMIENTO = 3;
    public static String[] ESTADO_NOMBRE = {"DISPONIBLE", "PREPARANDO SALTO", "EN WARP", "ENFRIAMIENTO"};

    private StateMotorWarp estado;
    private int id=0;

    public MotorWarp()
    {
        this.estado = new StateMWDisponible(this);
    }

    protected void setEstado(StateMotorWarp estado)
    {
        this.estado = estado;
    }

    public void saltar(boolean prepSalto, boolean cooldown, int warpCD,boolean terminarSalto) throws TransicionEstadoInvalida{
        if (prepSalto && !cooldown){
            preparar_salto();
        }
        else if (cooldown && warpCD == 1){
            enfriar_motor();
        }
        else if (terminarSalto){
            detener();
        }
    }

    public void saltarWarp() throws TransicionEstadoInvalida{
        arrancar();
    }

    private void preparar_salto() throws TransicionEstadoInvalida {
        estado.preparar_salto();
        id= 1;
    }

    private void arrancar() throws TransicionEstadoInvalida {
        estado.arrancar();
        id= 2;
    }

    private void detener() throws TransicionEstadoInvalida {
        estado.detener();
        id= 3;
    }

    private void enfriar_motor() throws TransicionEstadoInvalida {
        estado.enfriar_motor();
        id= 0;
    }

    public int getId(){
        return id;
    }

    public int getEstado() {
        return estado.getEstado();
    }
}
