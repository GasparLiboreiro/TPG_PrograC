import modelo.Asistente;
import modelo.excepciones.EstadoInvalido;
import modelo.misiones.*;
import modelo.nave.Nave;
import modelo.nave.NaveFactory;
import modelo.tripulantes.*;

import java.util.Iterator;

public class Prueba {
    public static void main(String[] args) {
        System.out.println("=== PRUEBAS DEL PATRÓN DECORATOR ===");

// 1. Instancia base sin decorar
// Base Alférez: 200 + 200 * 0.005 * 10 años = 210.0
Tripulante base = new TripulanteAlferez("Jordi", "ALF-99", 10);
System.out.println("1. Base pura:");
System.out.println("   Cargo: " + base.getCargo());
System.out.println("   Haberes base: $" + base.calcularHaberes()); // Esperado: 210.0

// 2. Primera capa de decoración (Origen Terricola)
// Plus Terrícola: +20 => 210.0 + 20 = 230.0
Tripulante decorado1 = new TripulanteOrigenTerricola(base);
System.out.println("\n2. Decorado con Terrícola:");
System.out.println("   Nombre delegado: " + decorado1.getNombre());       // Debe ser "Jordi"
System.out.println("   Cargo delegado: " + decorado1.getCargo());         // Debe ser "Alferez"
System.out.println("   Planeta: " + decorado1.getPlanetaDeOrigen());       // Debe ser "Terricola"
System.out.println("   Haberes con plus: $" + decorado1.calcularHaberes()); // Esperado: 230.0

// 3. Prueba de fuego: Encadenamiento múltiple (un decorador sobre otro decorador)
// Le agregamos encima el decorador Marciano (+18) sobre el ya decorado Terrícola (+20)
// Total esperado: 210.0 + 20 + 18 = 248.0
Tripulante doblementeDecorado = new TripulanteOrigenMarciano(decorado1);
System.out.println("\n3. Encadenamiento múltiple (Doble Decorador):");
System.out.println("   Identidad delegada: " + doblementeDecorado.getIdentidad()); // Debe seguir siendo "ALF-99"
System.out.println("   Planeta exterior: " + doblementeDecorado.getPlanetaDeOrigen()); // "Marciano"
System.out.println("   Haberes acumulados: $" + doblementeDecorado.calcularHaberes()); // Esperado: 248.0

// 4. Verificación de invariante contra null
try {
    new TripulanteOrigenVulcano(null);
    System.err.println("FALLO: El decorador permitió envolver un null.");
} catch (IllegalArgumentException e) {
    System.out.println("\n4. OK: El decorador rechazó null correctamente -> " + e.getMessage());
}
    }
}
