package modelo.Concurrencia;

import java.util.Observable;

public class PlataformaDeCarga extends Observable {
    private int capacidad;
    private int cantBaterias = 0;
    private boolean detenida = false;

    /**
     * Inicializa una plataforma de carga con la capacidad máxima de baterías indicada.<br>
     * <b>Pre:</b> {@code capacidad > 0}.<br>
     * <b>Post:</b> la plataforma queda inicializada con capacidad {@code capacidad}, {@code cantBaterias = 0} y {@code detenida = false}.
     *
     * @param capacidad Capacidad máxima de baterías de la plataforma.
     */
    public PlataformaDeCarga(int capacidad) {
        assert capacidad > 0 : "La capacidad de la plataforma debe ser mayor que cero.";

        this.capacidad = capacidad;
    }

    /**
     * Marca el estado como cambiado y notifica un evento a los observadores registrados.<br>
     * <b>Pre:</b> {@code mensaje != null}.<br>
     * <b>Post:</b> se notifica a los observadores pasando {@code mensaje} como argumento.
     *
     * @param mensaje Mensaje descriptivo del evento ocurrido.
     */
    public void notificarEvento(String mensaje) {
        assert mensaje != null : "El mensaje del evento no puede ser nulo.";

        setChanged();
        notifyObservers(mensaje);
    }

    /**
     * Detiene la operación de la plataforma y despierta a los hilos en espera.<br>
     * <b>Pre:</b> ninguna.<br>
     * <b>Post:</b> {@code detenida} pasa a {@code true} y se invoca {@code notifyAll()}.
     */
    public synchronized void detener() {
        this.detenida = true;
        notifyAll();
    }

    /**
     * Deposita una batería en la plataforma cuando hay espacio disponible, esperando si está llena.<br>
     * <b>Pre:</b> {@code generadora != null} y {@code capacidad > 0}.<br>
     * <b>Post:</b> si la plataforma y la generadora siguen activas y no hay interrupción, incrementa {@code cantBaterias} en 1, notifica el evento, despierta a los hilos en espera y devuelve {@code true}; en caso contrario devuelve {@code false} sin incrementar el stock.
     *
     * @param generadora Generadora de energía que produce la batería.
     * @return {@code true} si la batería fue depositada, {@code false} si se detuvo o interrumpió antes de depositar.
     * @throws InterruptedException si el hilo es interrumpido mientras espera.
     */
    public synchronized boolean depositarBateria(GeneradoraDeEnergia generadora) throws InterruptedException {
        assert generadora != null : "La generadora de energía no puede ser nula.";
        assert this.capacidad > 0 : "La capacidad de la plataforma debe ser mayor que cero.";

        notificarEvento("Bateria producida por " + generadora.getNombre() + '.');

        while (!this.detenida && this.cantBaterias >= this.capacidad && generadora.isActiva() && !Thread.currentThread().isInterrupted()) {
            notificarEvento(generadora.getNombre() + " esperando a que se libere espacio en la plataforma.");
            wait();
        }

        if (this.detenida || !generadora.isActiva() || Thread.currentThread().isInterrupted()) {
            return false;
        }

        this.cantBaterias++;
        notificarEvento("Bateria depositada por " + generadora.getNombre() + " (stock: " + this.cantBaterias + "/" + this.capacidad + ").");
        notifyAll();
        return true;
    }

    /**
     * Retira una batería de la plataforma cuando hay disponibilidad, esperando si no hay stock.<br>
     * <b>Pre:</b> {@code nave != null} y {@code cantBaterias >= 0}.<br>
     * <b>Post:</b> si la plataforma y la nave siguen activas y no hay interrupción, decrementa {@code cantBaterias} en 1, notifica el evento, despierta a los hilos en espera y devuelve {@code true}; en caso contrario devuelve {@code false} sin modificar el stock.
     *
     * @param nave Nave consumidora que solicita la batería.
     * @return {@code true} si la batería fue retirada, {@code false} si se detuvo o interrumpió antes de retirar.
     * @throws InterruptedException si el hilo es interrumpido mientras espera.
     */
    public synchronized boolean retirarBateria(NaveConsumidora nave) throws InterruptedException {
        assert nave != null : "La nave consumidora no puede ser nula.";
        assert this.cantBaterias >= 0 : "La cantidad de baterías no puede ser negativa.";

        notificarEvento(nave.getNombre() + " solicitando bateria.");

        while (!this.detenida && this.cantBaterias <= 0 && nave.isActiva() && !Thread.currentThread().isInterrupted()) {
            notificarEvento(nave.getNombre() + " esperando por falta de baterias en la Plataforma.");
            wait();
        }

        if (this.detenida || !nave.isActiva() || Thread.currentThread().isInterrupted()) {
            return false;
        }

        this.cantBaterias--;
        notificarEvento("Bateria retirada por " + nave.getNombre() + " (stock: " + this.cantBaterias + "/" + this.capacidad + ").");
        notifyAll();
        return true;
    }

    public int getCapacidad() {
        return this.capacidad;
    }

}