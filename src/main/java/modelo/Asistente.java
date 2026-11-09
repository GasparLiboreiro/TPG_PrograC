package modelo;

import modelo.excepciones.TransicionEstadoInvalida;
import modelo.nave.*;
import modelo.misiones.*;
import modelo.tripulantes.*;
import modelo.misiones.*;
import modelo.destinoytablero.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Asistente {
    private Nave nave;
    private Bitacora bitacora_general; // la bitacora debe registrar lo acontecido a traves de la ejecucion entera, tras la ejecucion de una mision, la bitacora de la misison debe añadirse encima de la bitacora_general
    private final Tablero tablero;

    public Asistente(Nave nave, Tablero tablero) {
        this.nave = nave;
        this.bitacora_general = new Bitacora();
        this.tablero = tablero;
    }
    public void add_tripulante(Tripulante tripulante) throws Exception {
        this.nave.addTripulante(tripulante);
    }

    public Iterator<Tripulante> get_tripulantes() {
        return this.nave.getTripulantes();
    }

    public int getEstadoMotorWarp() {
        return this.nave.getEstadoMotorWarp();
    }

    public InformeMision ejecutarMision(Mision mision) {
        InformeMision informe = mision.ejecutarCiclo();
        this.bitacora_general.append(informe.getBitacora());
        return informe;
    }

    public Bitacora getBitacoraGeneral() {
        return this.bitacora_general;
    }


    // todas las sig funciones eran getters de la nave, pero creo que no van a hacer falta para nada asi que simplemente son formas de pedir que el asistente registre los datos
    public void registrar_combustible() {
        bitacora_general.registrar("Combustible: "+nave.getCombustible());
    }

    public void registrar_energia() {
        bitacora_general.registrar("Energia: "+nave.getEnergia());
    }

    public void registrar_desgaste() {
        bitacora_general.registrar("Desgaste: "+nave.getDesgaste());
    }

    public void consumirCombustible(int cantidad) {
        if(cantidad<=nave.getCombustible())
        {
            nave.consumirCombustible(cantidad);
            bitacora_general.registrar("Se consiumio "+cantidad+"L de combustible, combustible actual: "+nave.getCombustible()+"L");
        }
        else
        {
            bitacora_general.registrar("No se pudo consumir "+cantidad+"L de combustible ya que no hay suficiente, combustible actual: "+nave.getCombustible()+"L");
        }
    }

    public void consumirEnergia(int cantidad) {
        if(cantidad<=nave.getEnergia())
        {
            nave.consumirEnergia(cantidad);
            bitacora_general.registrar("Se consiumio "+cantidad+" de energia, energia actual: "+nave.getEnergia());
        }
        else
        {
            bitacora_general.registrar("No se pudo consumir "+cantidad+" de energia ya que no hay suficiente, energia actual: "+nave.getEnergia());
        }
    }

    public void aumentarDesgaste(int cantidad) {
        nave.aumentarDesgaste(cantidad);
        bitacora_general.registrar("Se aumento el desgaste en "+cantidad+" unidades, desgaste actual: "+cantidad);
    }


    //REALACIONADO A MISIONES ----

    public Nave getNave() {
        return nave;
    }

    public Posicion getPosicion() {
        return tablero.getPosicionNave();
    }

    public Posicion usarRLD(){
        return tablero.obtenerObjetivo();
    }

    public ElementoDelUniverso analizarPosicion(){
        return tablero.inspeccionarPosicion();
    }

    public boolean usarRCD(Posicion pos, ArrayList<Posicion> camino){
        ElementoDelUniverso[][] elementosCercanos = tablero.observarAlrededores();
        int posI = camino.indexOf(pos);
        int y = camino.get(posI + 1).getY() - camino.get(posI).getY();
        int x = camino.get(posI + 1).getX() -  camino.get(posI).getX();
        ElementoDelUniverso proximo = elementosCercanos[x+1][y+1];
        return proximo != null && proximo.esObstaculo();

    }

    public ArrayList<Posicion> generarCamino(Posicion objetivo) {
        if (objetivo == null) {
            throw new IllegalArgumentException("El objetivo no puede ser nulo.");
        }

        int xActual = getPosicion().getX();
        int yActual = getPosicion().getY();

        ArrayList<Posicion> ruta = new ArrayList<>();
        ruta.add(new Posicion(xActual, yActual));

        while (xActual != objetivo.getX()
                || yActual != objetivo.getY()) {

            if (xActual != objetivo.getX()) {
                xActual += Integer.compare(objetivo.getX(), xActual);
            } else {
                yActual += Integer.compare(objetivo.getY(), yActual);
            }

            ruta.add(new Posicion(xActual, yActual));
        }

        return ruta;
    }

    /**
     * Ordena el movimiento del turno según lo que detectó el radar de corto alcance.
     * @param hayObstaculo true si hay un obstáculo en la próxima casilla del camino
     * @return casillas que avanza la nave (0, 1 o 2)
     */
    public int ordenarSalto(boolean hayObstaculo) {
        return hayObstaculo ? nave.saltarWarp() : nave.saltar();
    }

    public void moverNave(Posicion posicion) {
        tablero.moverNave(posicion);
    }

    public Tablero getTablero() {
        return tablero;
    }

}
