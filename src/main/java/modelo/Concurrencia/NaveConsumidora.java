package modelo.Concurrencia;

public class NaveConsumidora implements Runnable {
    private static final long TIEMPO_CONSUMO_MS = 4000L;

    private PlataformaDeCarga plataforma;
    private String nombre;
    private volatile boolean activa = true;

    /**
     * Inicializa una nave consumidora asociada a una plataforma de carga.<br>
     * <b>Pre:</b> {@code nombre != null} y {@code plataforma != null}.<br>
     * <b>Post:</b> la nave consumidora queda inicializada con {@code nombre}, {@code plataforma} y {@code activa = true}.
     *
     * @param nombre Identificador o nombre de la nave consumidora.
     * @param plataforma Plataforma de carga desde la cual se retirarán baterías.
     */
    public NaveConsumidora(String nombre, PlataformaDeCarga plataforma) {
        assert nombre != null : "El nombre de la nave consumidora no puede ser nulo.";
        assert plataforma != null : "La plataforma de carga no puede ser nula.";

        this.plataforma = plataforma;
        this.nombre = nombre;
    }

    /**
     * Ejecuta el ciclo periódico de espera y retiro de baterías desde la plataforma de carga mientras esté activa y no sea interrumpida.<br>
     * <b>Pre:</b> {@code plataforma != null}.<br>
     * <b>Post:</b> retira baterías periódicamente de {@code plataforma} hasta detenerse, ser interrumpida o que {@code retirarBateria(this)} retorne {@code false}.
     */
    @Override
    public void run() {
        assert this.plataforma != null : "La plataforma de carga no puede ser nula.";

        while (this.activa && !Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(TIEMPO_CONSUMO_MS);
                if (!this.activa || Thread.currentThread().isInterrupted()) {
                    break;
                }
                if (!this.plataforma.retirarBateria(this)) {
                    break;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Detiene la ejecución activa de la nave consumidora.<br>
     * <b>Pre:</b> ninguna.<br>
     * <b>Post:</b> {@code activa} pasa a {@code false}, impidiendo nuevos ciclos de consumo.
     */
    public void detener() {
        this.activa = false;
    }

    public boolean isActiva() {
        return this.activa;
    }

    public String getNombre() {
        return this.nombre;
    }

}