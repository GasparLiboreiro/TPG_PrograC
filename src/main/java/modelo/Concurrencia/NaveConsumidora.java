package modelo.Concurrencia;

public class NaveConsumidora implements Runnable {
    private static final long TIEMPO_CONSUMO_MS = 4000L;
    
    private PlataformaDeCarga plataforma;
    private String nombre;
    private volatile boolean activa = true;
    
    public NaveConsumidora(String nombre, PlataformaDeCarga plataforma) {
        this.plataforma = plataforma;
        this.nombre = nombre;
    }
    
    @Override 
    public void run(){
        while (this.activa && !Thread.currentThread().isInterrupted()){
            try {
                Thread.sleep(TIEMPO_CONSUMO_MS);
                if (!this.activa || Thread.currentThread().isInterrupted() ){
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
    
    public void detener() {
        this.activa = false;
    }
    public boolean isActiva(){
        return this.activa;
    }
            
    public String getNombre(){
        return this.nombre;
    }
    
}
