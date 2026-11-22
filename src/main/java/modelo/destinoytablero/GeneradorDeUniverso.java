package modelo.destinoytablero;
import java.util.ArrayList;
import java.util.Random;

import modelo.ElementoDelUniverso;
import modelo.Elementos.Asteroide;
import modelo.Elementos.EstacionDeCarga;
import modelo.nave.NaveFactory;
/**
 * Genera el universo en el que se desarrollan las misiones: decide qué
 * elementos lo componen (asteroides, estaciones de carga y naves enemigas)
 * y en qué cantidad, y construye el {@link Tablero} que los contiene.
 * <p>Esta clase solo define la <b>composición</b> del universo. La validación
 * de dimensiones y de cantidad máxima de elementos, y la distribución en las
 * casillas, son responsabilidad de {@link Tablero}. El movimiento de los
 * elementos en cada turno es responsabilidad de {@link Destino}.</p>
 */
public class GeneradorDeUniverso {

        private final int filas;
        private final int columnas;
        private final int cantAsteroides;
        private final int cantEstaciones;
    /** Cantidad de naves enemigas, de tipo aleatorio, que se ubicarán en el tablero. */
        private final int cantNavesEnemigas;
    /** Generador aleatorio usado para elegir el tipo de cada nave enemiga. */
        private final Random azar = new Random();
    /**
     * Crea un generador con la configuración indicada.
     * <p><b>Precondiciones:</b> las cantidades de elementos son mayores o
     * iguales a 0. Las dimensiones y la suma de elementos se validan recién
     * al invocar {@link #crearTablero()}.</p>
     * <p><b>Postcondiciones:</b> el generador queda configurado y puede
     * usarse para crear uno o más tableros. No crea ningún elemento.</p>
     * @param filas cantidad de filas del tablero, entre 5 y 100 inclusive
     * @param columnas cantidad de columnas del tablero, entre 5 y 100 inclusive
     * @param cantAsteroides cantidad de asteroides a generar
     * @param cantEstaciones cantidad de estaciones de carga a generar
     * @param cantNavesEnemigas cantidad de naves enemigas a generar
     */

        public GeneradorDeUniverso(int filas, int columnas, int cantAsteroides, int cantEstaciones, int cantNavesEnemigas) {
            this.filas = filas;
            this.columnas = columnas;
            this.cantAsteroides = cantAsteroides;
            this.cantEstaciones = cantEstaciones;
            this.cantNavesEnemigas = cantNavesEnemigas;
        }

    /**
     * Devuelve un generador con la configuración estándar del juego:
     * tablero de 10x10 con 8 asteroides, 4 estaciones de carga y 3 naves
     * enemigas.
     * <p><b>Precondiciones:</b> ninguna.</p>
     * <p><b>Postcondiciones:</b> devuelve una instancia nueva en cada llamada.
     * La configuración respeta los límites de {@link Tablero}, por lo que
     * {@link #crearTablero()} no lanza excepciones con estos valores.</p>
     * @return generador con la configuración por defecto
     */
        public static GeneradorDeUniverso porDefecto() {
            return new GeneradorDeUniverso(10, 10, 8, 4, 3);
        }
    /**
     * Genera los elementos configurados y construye un tablero nuevo con
     * ellos distribuidos aleatoriamente.
     * <p><b>Precondiciones:</b> ninguna.</p>
     * <p><b>Postcondiciones:</b> devuelve un tablero nuevo en cada llamada, con
     * instancias de elementos nuevas que no se comparten con tableros creados
     * anteriormente. La nave del jugador no forma parte de los elementos.</p>
     * @return tablero con los elementos generados
     * @throws DimensionesDeTableroInconsistentesException si alguna dimensión
     *         está fuera del rango permitido por {@link Tablero}
     * @throws CantidadDeElementosSuperaCasillaException si la cantidad total de
     *         elementos supera el máximo permitido por {@link Tablero}
     */

        public Tablero crearTablero()
                throws DimensionesDeTableroInconsistentesException, CantidadDeElementosSuperaCasillaException {
            return new Tablero(filas, columnas, generarElementos());
        }
    /**
     * Crea la lista de elementos del universo según las cantidades configuradas.
     * <p><b>Precondiciones:</b> ninguna.</p>
     * <p><b>Postcondiciones:</b> devuelve una lista nueva con
     * {@code cantAsteroides + cantEstaciones + cantNavesEnemigas} elementos,
     * agrupados por tipo en ese orden. La posición de cada elemento en el
     * tablero la decide {@link Tablero}, no el orden de la lista.</p>
     * @return lista de elementos a ubicar en el tablero
     */

        private ArrayList<ElementoDelUniverso> generarElementos() {
            ArrayList<ElementoDelUniverso> elementos = new ArrayList<>();
            for (int i = 0; i < cantAsteroides; i++) {
                elementos.add(new Asteroide());
            }
            for (int i = 0; i < cantEstaciones; i++) {
                elementos.add(new EstacionDeCarga());
            }
            for (int i = 0; i < cantNavesEnemigas; i++) {
                elementos.add(crearNaveEnemigaAleatoria());
            }
            return elementos;
        }
    /**
     * Crea una nave enemiga de un tipo elegido al azar entre los disponibles
     * en {@link NaveFactory#tipos_naves}, con igual probabilidad para cada tipo.
     * <p><b>Precondiciones:</b> {@link NaveFactory#tipos_naves} no está vacío.</p>
     * <p><b>Postcondiciones:</b> devuelve una nave nueva, sin tripulación y con
     * los recursos iniciales de su tipo. Si se agregan tipos a
     * {@link NaveFactory}, se incluyen automáticamente en el sorteo.</p>
     * @return nave enemiga, como elemento del universo
     */

        private ElementoDelUniverso crearNaveEnemigaAleatoria() {
            String[] tipos = NaveFactory.tipos_naves;
            return NaveFactory.crearNave(tipos[azar.nextInt(tipos.length)]);
        }
    }

