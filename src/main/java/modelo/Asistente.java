package modelo;

import modelo.excepciones.TransicionEstadoInvalida;

public class Asistente {
    private Nave nave;
    private Bitacora bitacora_general; // la bitacora debe registrar lo acontecido a traves de la ejecucion entera, tras la ejecucion de una mision, la bitacora de la misison debe añadirse encima de la bitacora_general

    public Asistente(Nave nave){
        this.nave = nave;
        this.bitacora_general = new Bitacora();
    }

    public InformeMision ejecutarMision(Mision mision) {
        InformeMision informe = mision.ejecutarCiclo();
        this.bitacora_general.append(informe.getBitacora());
        return informe;
    }

    public Bitacora getBitacoraGeneral() {
        return this.bitacora_general;
    }

    public void preparar_salto() {
        try{
            nave.preparar_salto();
            bitacora_general.registrar("Motor Warp preparado para el salto");
        }
        catch(TransicionEstadoInvalida e)
        {
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
