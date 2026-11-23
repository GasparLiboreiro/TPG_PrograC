package modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Evento registrado en la bitácora: cuándo sucedió y qué pasó.
 * <p><b>Invariante:</b> es inmutable; la descripción nunca es nula ni vacía.</p>
 */
public final class EventoBitacora {
    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final LocalDateTime momento;
    private final String descripcion;

    EventoBitacora(LocalDateTime momento, String descripcion) {
        this.momento = momento;
        this.descripcion = descripcion;
    }

    public LocalDateTime getMomento() {
        return momento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return "[" + momento.format(FORMATO) + "] " + descripcion;
    }
}