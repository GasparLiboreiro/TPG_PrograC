import modelo.Nave;
import modelo.NaveFactory;
import modelo.excepciones.FactoryTipoInvalido;

public class Prueba {
    public static void main(String[] args)
    {
        try {
            Nave n1 = NaveFactory.makeNave("Combate");
            System.out.println(n1);
        }
        catch(FactoryTipoInvalido e) {
            System.out.println("Tipo invalido en NaveFactory '"+e.tipo_ingresado+"'");
        }
        catch(Exception e) {
            System.out.println("Excepcion desconocida, msg: "+e.getMessage());
        }

    }
}
