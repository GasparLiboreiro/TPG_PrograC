import modelo.*;

public class Prueba {
    public static void main(String[] args)
    {

        Tripulante tripulante = new TripulanteCapitan("Jose", "EBX-231", 10);

        tripulante = new TripulanteOrigenMarciano((TripulanteConcreto) tripulante);
        // calculo a mano: 1000 + 1000*0.2*10 + 18 = 3018
        System.out.println(tripulante.calcularHaberes());

    }
}
