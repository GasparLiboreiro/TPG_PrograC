package modelo;

import modelo.excepciones.FactoryTipoInvalido;

public class NaveFactory {
    public static Nave makeNave(String tipo) throws FactoryTipoInvalido
    {
        switch(tipo){
            case "Exploradora":
                return new Nave(60, 80, 0); // datos sacados de
            case "Carguero":
                return new Nave(100, 60, 0); // en un futuro estos new podrian ser de otras clases que heredan Nave
            case "Combate":
                return new Nave(80, 100, 0);
            default:
                throw new FactoryTipoInvalido(tipo, "NaveFactory.makeNave(tipo) recibio un tipo invalido");
        }
    }
}
