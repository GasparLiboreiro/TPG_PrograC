package modelo.Concurrencia;

import java.util.Observable;


public class PlataformaDeCarga extends Observable {
    private int capacidad;
    private int cantBaterias = 0;
    private boolean detenida = false;
    
    public PlataformaDeCarga(int capacidad) {
        this.capacidad = capacidad;
    }
    
    public void notificarEvento(String mensaje){
        setChanged();
        notifyObservers(mensaje);
    }
    
    public synchronized void detener() {
        this.detenida = true;
        notifyAll();
    }
    
    public synchronized boolean depositarBateria(GeneradoraDeEnergia generadora) throws InterruptedException{         
        notificarEvento("Bateria producida por "+ generadora.getNombre() + '.' );
        
        while (!this.detenida && this.cantBaterias >= this.capacidad  && generadora.isActiva() && !Thread.currentThread().isInterrupted() ) {
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
    
    public synchronized boolean retirarBateria(NaveConsumidora nave)throws InterruptedException{
        notificarEvento(nave.getNombre() + " solicitando bateria.");
        
        while (!this.detenida && this.cantBaterias <= 0 && nave.isActiva() && !Thread.currentThread().isInterrupted() ) {
            notificarEvento(nave.getNombre()+ " esperando por falta de baterias en la Plataforma.");
            wait();
        }
        
        if(this.detenida || !nave.isActiva() || Thread.currentThread().isInterrupted()) {
            return false;
        }
        this.cantBaterias--;
        notificarEvento("Bateria retirada por " + nave.getNombre() + " (stock: " + this.cantBaterias + "/" + this.capacidad + ").");
        notifyAll();
        return true;
    }
    
    public int getCapacidad(){
        return this.capacidad;
    }

}
