package modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Registro en orden temporal de los eventos ocurridos.
 * <p><b>Invariantes:</b> no contiene eventos nulos ni vacíos; un evento registrado
 * no puede modificarse ni eliminarse; los eventos se conservan en el orden en que ocurrieron.</p>
 */
public class Bitacora {
    private final List<EventoBitacora> eventos = new ArrayList<>();

    /**
     * Registra un evento con la fecha y hora actuales.
     * <b>Pre:</b> descripcion no es nula ni vacía.
     * @throws IllegalArgumentException si la descripción es nula o vacía
     */
    public synchronized void registrar(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La bitácora no acepta eventos nulos o vacíos.");
        }
        eventos.add(new EventoBitacora(LocalDateTime.now(), descripcion.trim()));
    }

    /**
     * Agrega al final todos los eventos de otra bitácora, conservando su fecha y hora.
     * @throws IllegalArgumentException si otra es null
     */
    public synchronized void append(Bitacora otra) {
        if (otra == null) {
            throw new IllegalArgumentException("La bitácora a agregar no puede ser nula.");
        }
        eventos.addAll(otra.getEventos());
    }

    /** @return copia de solo lectura de los eventos, en orden temporal */
    public synchronized List<EventoBitacora> getEventos() {
        return Collections.unmodifiableList(new ArrayList<>(eventos));
    }

    /** @return los eventos como texto "[HH:mm:ss] descripción", de solo lectura y en orden temporal */
    public synchronized List<String> getEntradas() {
        List<String> entradas = new ArrayList<>();
        for (EventoBitacora evento : eventos) {
            entradas.add(evento.toString());
        }
        return Collections.unmodifiableList(entradas);
    }

    @Override
    public synchronized String toString() {
        return "Bitacora{" + "eventos=" + eventos + '}';
    }
}