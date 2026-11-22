package modelo.Concurrencia;

public class GeneradoraDeEnergia implements Runnable {
    private static final long TIEMPO_PRODUCCION_MS = 6000L;

    private final String nombre;
    private final PlataformaDeCarga plataforma;
    private volatile boolean activa;

    public GeneradoraDeEnergia(String nombre, PlataformaDeCarga plataforma) {
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

    public void detener() {
        this.activa = false;
    }

    @Override
    public void run() {
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