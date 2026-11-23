package modelo.misiones;

import modelo.Asistente;
import modelo.destinoytablero.Posicion;
import modelo.nave.Nave;

/** M-03: regresar a la base de origen (punto de partida del recorrido), recargar y reparar el desgaste. */
public class MisionRetorno extends Mision {
    public MisionRetorno(Asistente asistente) {
        super(asistente);
    }

    @Override public String getCodigo() {
        return "M-03";
    }
    @Override public int getCostoCombustible() {
        return 4;
    }
    @Override public int getCostoEnergia() {
        return 0;
    }
    @Override public int getCostoDesgaste() {
        return 4;
    }

    /** El objetivo no lo calcula el radar: es la base de origen. */
    @Override
    protected Posicion obtenerObjetivo() {
        return asistente.usarBaseDeOrigen();
    }

    /** El retorno a base se permite aunque la nave requiera mantenimiento: es la misión que lo realiza. */
    @Override
    protected boolean exigeNaveSinMantenimientoPendiente() {
        return false;
    }

    @Override
    protected void prepararHook() {
        bitacora.registrar("Comprobación de coordenadas y vector de reentrada a la base.");
    }

    @Override
    protected String ejecutarMision() {
        return "Mision M3: Nave retornada a la base de operaciones segura.";
    }

    /** En el cierre: recarga total de combustible y energía, y mantenimiento (desgaste a 0). */
    @Override
    protected void aplicarResultado() {
        Nave nave = asistente.getNave();
        nave.cargarCombustible(nave.getEspacioCombustible());
        nave.cargarEnergia(nave.getEspacioEnergia());
        nave.realizarMantenimiento();
        bitacora.registrar("Arribo a la base: recarga total de combustible y energía, mantenimiento realizado (desgaste 0).");
    }
}