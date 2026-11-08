/* cambios que creo a futuro a la espera de asistente(hecha asi para test):
* -integrar los estados del motor a preparar y ejecutar
*
* */

package modelo.misiones;
import modelo.Bitacora;
import modelo.ElementoDelUniverso;
import modelo.excepciones.EstadoInvalido;
import modelo.Asistente;
import modelo.excepciones.TransicionEstadoInvalida;
import modelo.nave.*;
import modelo.destinoytablero.*;

import java.lang.reflect.Array;
import java.util.ArrayList;

public abstract class Mision {
    protected final Asistente asistente;
    protected Posicion objetivo;
    protected final Bitacora bitacora;
    protected String cartelResultado;
    protected boolean exitosa;
    protected ArrayList<Posicion> camino = new ArrayList();

    public Mision(Asistente asistente) {
        if (asistente == null) {
            throw new IllegalArgumentException("El asistente no puede ser nulo.");
        }
        this.asistente = asistente;
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

        if (asistente.getNave().getCombustible() < getCostoCombustible() || asistente.getNave().getEnergia() < getCostoEnergia()) {

            bitacora.registrar("Fallo en preparación: recursos insuficientes.");

            throw new EstadoInvalido("Recursos insuficientes para iniciar la misión " + getCodigo() + ".");

        }
        if (!asistente.getNave().tieneTripulacionValida()) {
            bitacora.registrar("Fallo en preparación: dotación incompleta. Se requiere 1 capitán y al menos 4 tripulantes adicionales.");

            throw new EstadoInvalido("La nave no cuenta con la dotación mínima reglamentaria para iniciar la misión.");
}

        //EN CASO DE QUE SE PUEDA HACER LA MISION SE OBTIENE EL OBJETIVO Y SE PREPARA LA RUTA.
        objetivo = asistente.usarRLD();
        camino = asistente.generarCamino(objetivo);

        bitacora.registrar("Preparación exitosa: recursos suficientes.");
    }
    protected void prepararHook() {
    }

    protected void ejecutar() {
        Destino destino =  Destino.getInstancia(asistente.getTablero());

        boolean peligro = false;
        int iP = 0;
        boolean prepWarp = false;
        boolean cooling = false;
        boolean endWarp = false;
        int warpCD = 0;
        int prepCD = 0;
        ElementoDelUniverso elemento;

        asistente.getNave().consumirCombustible(getCostoCombustible());
        asistente.getNave().consumirEnergia(getCostoEnergia());
        asistente.getNave().aumentarDesgaste(getCostoDesgaste());
        bitacora.registrar("Consumo aplicado: -" + getCostoCombustible() + " comb, -" +
                getCostoEnergia() + " energ, +" + getCostoDesgaste() + " desgaste.");



        while (!asistente.getNave().getDestruida() && !asistente.getPosicion().equals(objetivo)) {
            elemento = asistente.analizarPosicion();

            if (elemento != null) {
                elemento.interactuar(asistente.getNave());
                //Analizar como añade la bitacora.
            }
            // se usa el radar de corta distancia y se verifica si hay peligros en la zona.
            peligro = asistente.usarRCD(asistente.getPosicion(), camino);

            //Turno nave
            if (!asistente.getNave().getDestruida()) {
                if (peligro) {
                    if (asistente.getMotorId() == 1 && prepCD == 2 && asistente.puedeWarp()) {
                        try {
                            prepCD = 0;
                            asistente.saltarWarp(camino.get((iP + 2>= camino.size()) ? (camino.size() -1) : iP + 2));
                            cooling = true;
                            if (iP + 2 >= camino.size()) {
                                iP = camino.size() - 1;
                            }
                            else{
                                iP += 2;
                            }
                            endWarp = true;
                        }
                        catch (TransicionEstadoInvalida e) {
                            System.out.println(e.getMessage());
                        }
                    } else if (asistente.getMotorId() == 0 && !cooling) {
                        prepCD = 0;
                        prepWarp = true;
                        try{
                            asistente.saltar(prepWarp, cooling, warpCD, endWarp,camino.get(iP + 1));
                            iP++;
                            endWarp = false;
                        }
                        catch (TransicionEstadoInvalida e){
                            //ver que se hace acá
                        }
                    }
                    else {
                        prepWarp = false;
                        if (cooling && warpCD<1){
                            warpCD++;
                        }
                        if (asistente.getMotorId() == 1 && prepCD < 2 && !cooling) {
                            prepCD++;
                        }
                        try{
                            asistente.saltar(prepWarp, cooling, warpCD, endWarp,camino.get(iP + 1));

                            if (cooling && warpCD==1){
                                cooling = false;
                            }

                            iP++;
                            endWarp = false;
                        }
                        catch (TransicionEstadoInvalida e){
                            //ver que se hace acá
                        }
                    }
                } else {
                    if (asistente.getMotorId() == 1 && prepCD < 2 && !cooling){
                        prepCD++;
                    }
                    if (cooling && warpCD<1){
                        warpCD++;
                    }
                    try{
                        prepWarp = false;
                        asistente.saltar(prepWarp, cooling, warpCD, endWarp,camino.get(iP + 1));
                        iP++;
                        if (cooling && warpCD==1){
                            cooling = false;
                        }
                        endWarp = false;
                    }
                    catch (TransicionEstadoInvalida e){
                        //ver que se hace acá
                    }
                }
                //Turno del destino
                destino.turnoDestino();
            }

        }

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
        return new InformeMision(this, asistente.getNave());
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