package modelo;
import modelo.nave.Nave;



public class MotorWarp {
    public static int ESTADO_DISPONIBLE = 0;
    public static int ESTADO_PREPARANDO_SALTO = 1;
    public static int ESTADO_EN_WARP = 2;
    public static int ESTADO_ENFRIAMIENTO = 3;
    public static String[] ESTADO_NOMBRE = {"DISPONIBLE", "PREPARANDO SALTO", "EN WARP", "ENFRIAMIENTO"};

    /** Combustible que consume un salto simple . */
    static final int COSTO_COMBUSTIBLE_SALTO = 2;
    /** Energía que consume un salto warp. */
    static final int COSTO_ENERGIA_WARP = 5;
    /** Desgaste que genera el turno de enfriamiento. */
    static final int DESGASTE_ENFRIAMIENTO = 3;
    /** Turnos de preparación previos al salto warp. */
    static final int TURNOS_PREPARACION = 2;

    private final Nave nave;
    private StateMotorWarp estado;
    private String ultimoEvento;

    public MotorWarp(Nave nave) {
        if (nave == null) {
            throw new IllegalArgumentException("El motor warp necesita una nave.");
        }
        this.nave = nave;
        this.estado = new StateMWDisponible(this);
    }

    /**
     * Ejecuta el turno del motor con el camino despejado.
     * @return casillas que avanza la nave (0, 1 o 2)
     */
    public int saltar() {
        ultimoEvento = null;
        return estado.saltar();
    }

    /**
     * Ejecuta el turno del motor con un obstáculo adelante.
     * @return casillas que avanza la nave (0, 1 o 2)
     */
    public int saltarWarp() {
        ultimoEvento = null;
        return estado.saltarWarp();
    }
    public int getEstado() {
        return estado.getEstado();
    }
    /** @return lo que hizo el motor en el último turno, o null si solo hizo un salto normal sin cambios */
    public String getUltimoEvento() {
        return ultimoEvento;
    }
    // ---- uso interno de los estados (visibilidad de paquete) ----

    void setEstado(StateMotorWarp estado) {
        this.estado = estado;
    }

    Nave getNave() {
        return nave;
    }

    boolean hayEnergiaParaWarp() {
        return nave.getEnergia() >= COSTO_ENERGIA_WARP;
    }

    /**
     * Salto simple: avanza una casilla y consume combustible. Si no alcanza,
     * consume lo que queda (con 0 de combustible la nave se destruye).
     * @return 1, la casilla que avanza
     */
    int saltoNormal() {
        if (nave.getDestruida()) {
            return 0; // la nave se destruyó en este mismo turno (por ejemplo, por el desgaste del enfriamiento)
        }
        nave.consumirCombustible(Math.min(COSTO_COMBUSTIBLE_SALTO, nave.getCombustible()));
        return 1;
    }
    void registrarEvento(String evento) {
        this.ultimoEvento = "Motor Warp: " + evento;
    }
}



