package modelo;

import com.sun.jdi.InvalidTypeException;

public class MisionFactory {
    // este array podria usarse en la interfaz grafica para dejar elegir las misiones usando estos nombres
    public static final String[] tipos_misiones = {"Intercepcion", "Recoleccion", "Retorno"};
    public static Mision crearMision(String tipo, Nave nave){
        switch(tipo){
            case "Intercepcion":
                return new MisionIntercepcion(nave);
            case "Recoleccion":
                return new MisionRecoleccion(nave);
            case "Retorno":
                return new MisionRetorno(nave);
            default:
                // runtime exception porque esto no deberia permitir que pase jamas, si sucede es un error de programacion nuestro
                throw new RuntimeException("MisionFactory.crearMision recibio tipo="+tipo);
        }
    }
}
