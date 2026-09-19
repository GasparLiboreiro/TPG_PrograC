package modelo;

import modelo.excepciones.TransicionEstadoInvalida;

public interface StateMotorWarp { // los estados disponibles son: Disponible, Preparando salto, En warp, Enfriamiento
    public void preparar_salto() throws TransicionEstadoInvalida;
    public void arrancar() throws TransicionEstadoInvalida;
    public void detener() throws TransicionEstadoInvalida;
    public void enfriar_motor() throws TransicionEstadoInvalida;
}
