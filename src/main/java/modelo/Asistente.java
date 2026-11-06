package modelo;

import modelo.excepciones.TransicionEstadoInvalida;
import modelo.nave.*;
import modelo.misiones.*;
import modelo.tripulantes.*;
import modelo.misiones.*;

import java.util.Iterator;

public class Asistente {
    private Nave nave;
    private Bitacora bitacora_general; // la bitacora debe registrar lo acontecido a traves de la ejecucion entera, tras la ejecucion de una mision, la bitacora de la misison debe añadirse encima de la bitacora_general

    public Asistente(){
        this.nave = null;
        this.bitacora_general = new Bitacora();
    }

    // los incisos 1) 2) 3) etc son las categorias de funcionalidades exigidas en el Enunciado Trabajo Práctico - Parte 2.pdf

    //
    // 1) Acceso al Sistema (persistencia)
    //
    //public void login_cuenta(String nombre, String contrasenia) throws AccountException
    //public void register_cuenta(String nombre, String contrasenia) throws AccountException


    //
    // 2) Seccion de creacion
    //
    /**
     * Se crea una nueva nave del tipo proveido y se usa como la nave del sistema
     * <b>Pre:</b> el tipo debe pertenecer a las opciones en NaveFactory.tipos_naves
     * @param tipo tipo de nave a ser creada
     */
    public void create_nave(String tipo) {
        this.nave = NaveFactory.crearNave(tipo);
    }
    /**
     * Añade un tripulante a la nave</br>
     * <b>Pre:</b></br>
     * - El tripulante debe ser un tripulante valido (no nulo)</br>
     * - La nave debe haber sido inicializada previamente usando create_nave(tipo)</br>
     * @throws Exception si ya hay un tripulante registrado con el mismo identificador o si se intenta añadir un capitan ya habiendo un capitan en la tripulacion
     * @param tripulante Tripulante a añadir a la nave
     */
    public void add_tripulante(Tripulante tripulante) throws Exception {
        assert this.nave!=null : "Se intento añadir un tripulante sin haber creado la nave previamente";
        this.nave.addTripulante(tripulante);
    }


    //
    // 3) Seccion de Tripulantes
    //
    /**
     * Retorna un iterator con los tripulantes de la nave
     * <b>Pre:</b></br>
     * - La nave debe haber sido inicializada previamente usando create_nave(tipo)</br>
     * @return iterador de tripulantes
     */
    public Iterator<Tripulante> get_tripulantes() {
        assert this.nave!=null : "Se intento peidr los tripulantes sin haber creado la nave previamente";
        return this.nave.getTripulantes();
    }


    //
    // 4) Seccion de Control de la Nave
    //
    /**
     * <b>Pre:</b> La nave debe haber sido inicializada previamente usando create_nave(tipo)</br>
     */
    public int getCombustible(){
        assert this.nave!=null : "Se intento pedir el combustible sin haber creado la nave previamente";
        return this.nave.getCombustible();
    }
    /**
     * <b>Pre:</b> La nave debe haber sido inicializada previamente usando create_nave(tipo)</br>
     */
    public int getEnergia(){
        assert this.nave!=null : "Se intento pedir la energia sin haber creado la nave previamente";
        return this.nave.getEnergia();
    }
    /**
     * <b>Pre:</b> La nave debe haber sido inicializada previamente usando create_nave(tipo)</br>
     */
    public int getDesgaste(){
        assert this.nave!=null : "Se intento pedir el desgaste sin haber creado la nave previamente";
        return this.nave.getDesgaste();
    }
    /**
     * <b>Pre:</b> La nave debe haber sido inicializada previamente usando create_nave(tipo)</br>
     * @return devuelve un estado de los especificados en las constantes de la clase MotorWarp
     */
    public int getEstadoMotorWarp(){
        assert this.nave!=null : "Se intento pedir el estado del motor warp sin haber creado la nave previamente";
        return this.nave.getEstadoMotorWarp();
    }
    public Bitacora getBitacoraGeneral() {
        return this.bitacora_general;
    }
    //<<get informes finales misiones?>> hay que mostrar los "informes de misiones", habría que reestructurar varias cosas
    public InformeMision ejecutarMision(Mision mision) {
        InformeMision informe = mision.ejecutarCiclo();
        this.bitacora_general.append(informe.getBitacora());
        return informe;
    }

    //
    // 5) Sección del Visor Recóndito
    //
    //public void visorAgregarNaveConsumidora()
    //public void visorAgregarGeneradoraDeEnergia()
    //public ArrayList<String>? visorGetEventos()



    public void preparar_salto() {
        try {
            nave.preparar_salto();
            bitacora_general.registrar("Motor Warp preparado para el salto");
        }
        catch(TransicionEstadoInvalida e) {
            bitacora_general.registrar("Motor Warp no pudo preparar el salto: "+e.getMessage());
        }
    }
    public void arrancar() throws TransicionEstadoInvalida {
        try{
            nave.arrancar();
            bitacora_general.registrar("Motor Warp en salto");
        }
        catch(TransicionEstadoInvalida e)
        {
            bitacora_general.registrar("Motor Warp no pudo arrancar el salto: "+e.getMessage());
        }
    }
    public void detener() throws TransicionEstadoInvalida {
        try{
            nave.detener();
            bitacora_general.registrar("Motor Warp detubo el salto");
        }
        catch(TransicionEstadoInvalida e)
        {
            bitacora_general.registrar("Motor Warp no pudo detener salto: "+e.getMessage());
        }
    }
    public void enfriar_motor() throws TransicionEstadoInvalida {
        try{
            nave.enfriar_motor();
            bitacora_general.registrar("Motor Warp se enfrio");
        }
        catch(TransicionEstadoInvalida e)
        {
            bitacora_general.registrar("Motor Warp no pudo enfriarse: "+e.getMessage());
        }
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

}
