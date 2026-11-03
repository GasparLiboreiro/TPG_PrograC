package modelo;

import java.util.ArrayList;
import java.util.List;


public class Bitacora { 
    private final List<String> entradas;

    public Bitacora() {
        this.entradas = new ArrayList<>();
    }

    public void registrar(String entrada) {
        if (entrada != null && !entrada.trim().isEmpty()) {
            this.entradas.add(entrada);
        }
    }

    /**
     * Agrega todo el contenido de in encima de this
     * <b>Pre:</b> in es distinto de null
     *
     * @param tipo Bitacora cuyos datos se desean depositar en entradas
     * @throws IllegalArgumentException si in es null
     */
    public void append(Bitacora in) throws NullPointerException {
    	if (in==null) {
    		throw new NullPointerException("La bitacora no puede ser nula");
    	}
        entradas.addAll(in.getEntradas()); // testeado, funciona god
    }
    // Por contrato de negocio, quien consume esta lista solo la lee
    // para asegurar ese comportamiento podriamos retornar un iterator de entradas
    public List<String> getEntradas() {
        return entradas;
    }
    @Override
    public String toString() {
        return "Bitacora{" + "entradas=" + entradas + '}';
    }
}