package modelo;

import modelo.excepciones.TransicionEstadoInvalida;

public interface StateMotorWarp {
    /**
     * Turno en el que el radar de corto alcance no detectó obstáculos.
     * @return casillas que avanza la nave en este turno (0, 1 o 2)
     */
    public int saltar();

    /**
     * Turno en el que el radar de corto alcance detectó un obstáculo en la
     * próxima casilla del camino.
     * @return casillas que avanza la nave en este turno (0, 1 o 2)
     */
    public int saltarWarp();

    /** @return constante de estado definida en {@link MotorWarp} */
    public int getEstado();
}
