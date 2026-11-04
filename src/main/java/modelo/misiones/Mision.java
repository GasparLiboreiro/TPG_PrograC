/* cambios que creo a futuro a la espera de asistente(hecha asi para test):
* -integrar los estados del motor a preparar y ejecutar
*
* */

package modelo.misiones;
import modelo.Bitacora;
import modelo.excepciones.EstadoInvalido;
import modelo.nave.Nave;

public abstract class Mision {
    protected final Nave nave;
    protected final Bitacora bitacora;
    protected String cartelResultado;
    protected boolean exitosa;

    public Mision(Nave nave) {
        if (nave == null) {
            throw new IllegalArgumentException("La nave no puede ser nula.");
        }
        this.nave = nave;
        this.bitacora = new Bitacora();
        this.exitosa = false;
    }

    public final InformeMision ejecutarCiclo() throws EstadoInvalido { //throws EstadoInvalido para que el que lo llame se entere si no se podia ejecutar aun
        InformeMision informe;

        preparar();
        prepararHook();
        ejecutar();
        ejecutarHook();
        evaluar();
        evaluarHook();
        informe = cerrar();
        informe = cerrarHook(informe); // cerrarHook recibe el informe default por si le quiciese hacer alguna modificacion
        return informe;
    }




    protected void preparar() throws EstadoInvalido {
        bitacora.registrar("Preparando misión " + getCodigo() + ". Verificando recursos y tripulacion.");
        if (nave.getCombustible() < getCostoCombustible() || nave.getEnergia() < getCostoEnergia()) {
            bitacora.registrar("Fallo en preparación: recursos insuficientes.");
            throw new EstadoInvalido("Recursos insuficientes para iniciar la misión " + getCodigo() + ".");
        }
        if (!nave.tieneTripulacionValida()) {
            bitacora.registrar("Fallo en preparación: dotación incompleta. Se requiere 1 capitán y al menos 4 tripulantes adicionales.");
            throw new EstadoInvalido("La nave no cuenta con la dotación mínima reglamentaria para iniciar la misión.");
}
        bitacora.registrar("Preparación exitosa: recursos suficientes.");
    }
    protected void prepararHook() {
    }

    protected void ejecutar() {
        nave.consumirCombustible(getCostoCombustible());
        nave.consumirEnergia(getCostoEnergia());
        nave.aumentarDesgaste(getCostoDesgaste());
        bitacora.registrar("Consumo aplicado: -" + getCostoCombustible() + " comb, -" +
                getCostoEnergia() + " energ, +" + getCostoDesgaste() + " desgaste.");

        this.cartelResultado = ejecutarMision();
        bitacora.registrar("Acción realizada: " + this.cartelResultado);
    }
    protected void ejecutarHook() {
    }

    protected void evaluar() {
        this.exitosa = (this.cartelResultado != null && !this.cartelResultado.isEmpty());
        bitacora.registrar("Evaluación de la misión: " + (exitosa ? "ÉXITO" : "FALLO"));
    }
    protected void evaluarHook() {
    }

    protected InformeMision cerrar() {
        bitacora.registrar("Misión finalizada. Emitiendo informe.");
        return new InformeMision(this, nave);
    }
    protected InformeMision cerrarHook(InformeMision informe_basico) {
        return informe_basico;
    }

    // Getters para InformeMision
    public boolean isExitosa() {
        return exitosa;
    }
    public String getCartelResultado() {
        return cartelResultado;
    }
    public Bitacora getBitacora() {
        return bitacora;
    }

    // Métodos a definir por las misiones
    public abstract String getCodigo();
    public abstract int getCostoCombustible();
    public abstract int getCostoEnergia();
    public abstract int getCostoDesgaste();
    protected abstract String ejecutarMision();
}