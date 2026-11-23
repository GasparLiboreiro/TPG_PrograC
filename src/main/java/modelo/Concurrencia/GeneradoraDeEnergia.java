package modelo.Concurrencia;

public class GeneradoraDeEnergia implements Runnable {
    private static final long TIEMPO_PRODUCCION_MS = 6000L;

    private final String nombre;
    private final PlataformaDeCarga plataforma;
    private volatile boolean activa;

    /**
     * Inicializa una generadora de energía asociada a una plataforma de carga.<br>
     * <b>Pre:</b> {@code nombre != null} y {@code plataforma != null}.<br>
     * <b>Post:</b> la generadora queda inicializada con {@code nombre}, {@code plataforma} y {@code activa = true}.
     *
     * @param nombre Identificador o nombre de la generadora.
     * @param plataforma Plataforma de carga donde se depositarán las baterías.
     */
    public GeneradoraDeEnergia(String nombre, PlataformaDeCarga plataforma) {
        assert nombre != null : "El nombre de la generadora no puede ser nulo.";
        assert plataforma != null : "La plataforma de carga no puede ser nula.";

        this.nombre = nombre;
        this.plataforma = plataforma;
        this.activa = true;
    }

    public String getNombre() {
        return this.nombre;
    }

    public boolean isActiva() {
        return this.activa;
    }

    /**
     * Detiene la ejecución activa de la generadora de energía.<br>
     * <b>Pre:</b> ninguna.<br>
     * <b>Post:</b> {@code activa} pasa a {@code false}, impidiendo nuevos ciclos de producción.
     */
    public void detener() {
        this.activa = false;
    }

    /**
     * Ejecuta el ciclo periódico de producción y depósito de baterías en la plataforma de carga mientras esté activa y no sea interrumpida.<br>
     * <b>Pre:</b> {@code plataforma != null}.<br>
     * <b>Post:</b> deposita baterías periódicamente en {@code plataforma} hasta detenerse, ser interrumpida o que {@code depositarBateria(this)} retorne {@code false}.
     */
    @Override
    public void run() {
        assert this.plataforma != null : "La plataforma de carga no puede ser nula.";

        while (this.activa && !Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(TIEMPO_PRODUCCION_MS);
                if (!this.activa || Thread.currentThread().isInterrupted()) {
                    break;
                }
                if (!this.plataforma.depositarBateria(this)) {
                    break;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

}