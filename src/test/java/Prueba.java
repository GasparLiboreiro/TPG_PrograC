import modelo.Bitacora;
import modelo.Nave;
import modelo.NaveFactory;
import modelo.excepciones.FactoryTipoInvalido;

public class Prueba {
    public static void main(String[] args)
    {
        Bitacora a = new Bitacora();
        Bitacora b = new Bitacora();
        a.registrar("1 bleh");
        b.registrar("3 bluh");
        a.registrar("2 blah");
        b.registrar("4 bloh");
        System.out.println(a);
        System.out.println(b);
        a.append(b);
        System.out.println(a);

    }
}
