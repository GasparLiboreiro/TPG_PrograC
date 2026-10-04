import modelo.Bitacora;
import modelo.Nave;
import modelo.NaveFactory;
import modelo.OrigenTerricola;
import modelo.Tripulante;

import modelo.TripulanteCapitan;
import modelo.TripulanteInterfaz;
import modelo.TripulanteTeniente;

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

        TripulanteInterfaz trip1= new Tripulante("Mateo", "ID-001", "Capitan","Terricola",1);
        trip1 = new TripulanteCapitan(trip1);
        trip1 = new OrigenTerricola(trip1);
        System.out.println(trip1.calcularHaberes());
        
    }
}
