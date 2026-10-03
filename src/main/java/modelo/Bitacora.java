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

    // agrega tdo el contenido de in encima de this
    public void append(Bitacora in){
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