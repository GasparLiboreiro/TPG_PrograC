package modelo;

import modelo.excepciones.TransicionEstadoInvalida;
import modelo.nave.*;
import modelo.misiones.*;
import modelo.tripulantes.*;
import modelo.destinoytablero.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Asistente {
    private Nave nave;
    private Bitacora bitacora_general; // la bitacora debe registrar lo acontecido a traves de la ejecucion entera, tras la ejecucion de una mision, la bitacora de la misison debe añadirse encima de la bitacora_general
    private final Tablero tablero;
    private Posicion objetivoActual;
    private final Posicion baseDeOrigen; // punto de partida del recorrido: destino del retorno a base (M-03)

    /**
     * Inicializa un asistente vinculado a una nave y a un tablero.<br>
     * <b>Pre:</b><br>
     * - {@code nave != null} y {@code tablero != null}.<br>
     * <b>Post:</b><br>
     * - El asistente queda asociado a {@code nave} y {@code tablero}, con una {@link Bitacora} general vacía y {@code baseDeOrigen} inicializada en la posición actual de la nave.
     *
     * @param nave Nave controlada o asistida.
     * @param tablero Tablero donde opera la nave.
     */
    public Asistente(Nave nave, Tablero tablero) {
        assert nave != null : "La nave no puede ser nula.";
        assert tablero != null : "El tablero no puede ser nulo.";

        this.nave = nave;
        this.bitacora_general = new Bitacora();
        this.tablero = tablero;
        this.baseDeOrigen = tablero.getPosicionNave();
    }

    /**
     * Agrega un tripulante a la nave asociada.<br>
     * <b>Pre:</b><br>
     * - {@code nave != null} y {@code tripulante != null}.<br>
     * <b>Post:</b><br>
     * - El tripulante queda incorporado a la tripulación de la nave si cumple las reglas de validación.
     *
     * @param tripulante Tripulante a agregar.
     * @throws Exception si el tripulante ya existe o se duplica el capitán.
     */
    public void add_tripulante(Tripulante tripulante) throws Exception {
        assert nave != null : "La nave no puede ser nula.";
        assert tripulante != null : "El tripulante no puede ser nulo.";

        this.nave.addTripulante(tripulante);
    }

    public Iterator<Tripulante> get_tripulantes() {
        return this.nave.getTripulantes();
    }

    public String getUltimoEventoMotor() {
        return this.nave.getUltimoEventoMotor();
    }

    public int getEstadoMotorWarp() {
        return this.nave.getEstadoMotorWarp();
    }

    /**
     * Ejecuta la misión y agrega su bitácora a la bitácora general.<br>
     * Si la misión es rechazada o falla con una excepción, igual se agrega lo registrado (con el motivo) y se propaga la excepción.<br>
     * <b>Pre:</b><br>
     * - {@code mision != null} y {@code bitacora_general != null}.<br>
     * <b>Post:</b><br>
     * - Si no ocurre excepción, se anexa la bitácora del informe a {@code bitacora_general} y se devuelve el {@link InformeMision}; si ocurre una {@link RuntimeException}, se anexa la bitácora de la misión y se relanza la excepción.
     *
     * @param mision Misión a ejecutar.
     * @return Informe resultante de la misión.
     */
    public InformeMision ejecutarMision(Mision mision) {
        assert mision != null : "La misión no puede ser nula.";
        assert bitacora_general != null : "La bitácora general no puede ser nula.";

        try {
            InformeMision informe = mision.ejecutarCiclo();
            this.bitacora_general.append(informe.getBitacora());
            return informe;
        } catch (RuntimeException e) {
            this.bitacora_general.append(mision.getBitacora());
            throw e;
        }
    }

    // ---- Operaciones de recursos y mantenimiento (E1-09): coordinan con la nave y registran el resultado ----

    /**
     * Carga combustible en la nave y registra el resultado en la bitácora general.<br>
     * <b>Pre:</b><br>
     * - {@code nave != null} y {@code bitacora_general != null}.<br>
     * <b>Post:</b><br>
     * - Si la carga es válida, se actualiza el combustible de la nave y se registra el éxito en {@code bitacora_general}; si falla, se registra el rechazo y se propaga la excepción.<br>
     *
     * @param cantidad Cantidad de combustible a cargar.
     * @throws IllegalArgumentException o IllegalStateException si la carga no es válida; el motivo queda registrado.
     */
    public void cargarCombustible(int cantidad) {
        assert nave != null && bitacora_general != null : "La nave y la bitácora general deben estar inicializadas.";

        try {
            nave.cargarCombustible(cantidad);
            bitacora_general.registrar("Carga de " + cantidad + " de combustible. Combustible actual: " + nave.getCombustible() + ".");
        } catch (RuntimeException e) {
            bitacora_general.registrar("Carga de combustible rechazada: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Carga energía en la nave y registra el resultado en la bitácora general.<br>
     * <b>Pre:</b><br>
     * - {@code nave != null} y {@code bitacora_general != null}.<br>
     * <b>Post:</b><br>
     * - Si la carga es válida, se actualiza la energía de la nave y se registra el éxito en {@code bitacora_general}; si falla, se registra el rechazo y se propaga la excepción.<br>
     *
     * @param cantidad Cantidad de energía a cargar.
     * @throws IllegalArgumentException o IllegalStateException si la carga no es válida; el motivo queda registrado.
     */
    public void cargarEnergia(int cantidad) {
        assert nave != null && bitacora_general != null : "La nave y la bitácora general deben estar inicializadas.";

        try {
            nave.cargarEnergia(cantidad);
            bitacora_general.registrar("Carga de " + cantidad + " de energía. Energía actual: " + nave.getEnergia() + ".");
        } catch (RuntimeException e) {
            bitacora_general.registrar("Carga de energía rechazada: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Realiza el mantenimiento de la nave y registra el resultado en la bitácora general.<br>
     * <b>Pre:</b><br>
     * - {@code nave != null} y {@code bitacora_general != null}.<br>
     * <b>Post:</b><br>
     * - Si el mantenimiento procede, el desgaste queda en {@code 0} y se registra en {@code bitacora_general}; si falla, se registra el rechazo y se propaga la excepción.<br>
     *
     * @throws IllegalStateException si la nave está destruida; el motivo queda registrado.
     */
    public void realizarMantenimiento() {
        assert nave != null && bitacora_general != null : "La nave y la bitácora general deben estar inicializadas.";

        try {
            int antes = nave.getDesgaste();
            nave.realizarMantenimiento();
            bitacora_general.registrar("Mantenimiento realizado. Desgaste: " + antes + " -> 0.");
        } catch (RuntimeException e) {
            bitacora_general.registrar("Mantenimiento rechazado: " + e.getMessage());
            throw e;
        }
    }

    public Bitacora getBitacoraGeneral() {
        return this.bitacora_general;
    }

    // todas las sig funciones eran getters de la nave, pero creo que no van a hacer falta para nada asi que simplemente son formas de pedir que el asistente registre los datos

    /**
     * Registra en la bitácora general el combustible actual de la nave.<br>
     * <b>Pre:</b><br>
     * - {@code nave != null} y {@code bitacora_general != null}.<br>
     * <b>Post:</b><br>
     * - Queda asentado en {@code bitacora_general} el combustible actual de la nave.
     */
    public void registrar_combustible() {
        assert nave != null && bitacora_general != null : "La nave y la bitácora general deben estar inicializadas.";

        bitacora_general.registrar("Combustible: " + nave.getCombustible());
    }

    /**
     * Registra en la bitácora general la energía actual de la nave.<br>
     * <b>Pre:</b><br>
     * - {@code nave != null} y {@code bitacora_general != null}.<br>
     * <b>Post:</b><br>
     * - Queda asentado en {@code bitacora_general} la energía actual de la nave.
     */
    public void registrar_energia() {
        assert nave != null && bitacora_general != null : "La nave y la bitácora general deben estar inicializadas.";

        bitacora_general.registrar("Energia: " + nave.getEnergia());
    }

    /**
     * Registra en la bitácora general el desgaste actual de la nave.<br>
     * <b>Pre:</b><br>
     * - {@code nave != null} y {@code bitacora_general != null}.<br>
     * <b>Post:</b><br>
     * - Queda asentado en {@code bitacora_general} el desgaste actual de la nave.
     */
    public void registrar_desgaste() {
        assert nave != null && bitacora_general != null : "La nave y la bitácora general deben estar inicializadas.";

        bitacora_general.registrar("Desgaste: " + nave.getDesgaste());
    }

    /**
     * Intenta consumir una cantidad de combustible de la nave y registra el resultado en la bitácora general.<br>
     * <b>Pre:</b><br>
     * - {@code nave != null}, {@code bitacora_general != null} y {@code cantidad >= 0}.<br>
     * <b>Post:</b><br>
     * - Si hay combustible suficiente, se consume en la nave y se registra el consumo; en caso contrario se registra la falta de combustible sin modificarlo.
     *
     * @param cantidad Cantidad de combustible a consumir.
     */
    public void consumirCombustible(int cantidad) {
        assert nave != null && bitacora_general != null : "La nave y la bitácora general deben estar inicializadas.";
        assert cantidad >= 0 : "La cantidad de combustible no puede ser negativa.";

        if (cantidad <= nave.getCombustible()) {
            nave.consumirCombustible(cantidad);
            bitacora_general.registrar("Se consiumio " + cantidad + "L de combustible, combustible actual: " + nave.getCombustible() + "L");
        } else {
            bitacora_general.registrar("No se pudo consumir " + cantidad + "L de combustible ya que no hay suficiente, combustible actual: " + nave.getCombustible() + "L");
        }
    }

    /**
     * Intenta consumir una cantidad de energía de la nave y registra el resultado en la bitácora general.<br>
     * <b>Pre:</b><br>
     * - {@code nave != null}, {@code bitacora_general != null} y {@code cantidad >= 0}.<br>
     * <b>Post:</b><br>
     * - Si hay energía suficiente, se consume en la nave y se registra el consumo; en caso contrario se registra la falta de energía sin modificarla.
     *
     * @param cantidad Cantidad de energía a consumir.
     */
    public void consumirEnergia(int cantidad) {
        assert nave != null && bitacora_general != null : "La nave y la bitácora general deben estar inicializadas.";
        assert cantidad >= 0 : "La cantidad de energía no puede ser negativa.";

        if (cantidad <= nave.getEnergia()) {
            nave.consumirEnergia(cantidad);
            bitacora_general.registrar("Se consiumio " + cantidad + " de energia, energia actual: " + nave.getEnergia());
        } else {
            bitacora_general.registrar("No se pudo consumir " + cantidad + " de energia ya que no hay suficiente, energia actual: " + nave.getEnergia());
        }
    }

    /**
     * Aumenta el desgaste de la nave y registra la operación en la bitácora general.<br>
     * <b>Pre:</b><br>
     * - {@code nave != null}, {@code bitacora_general != null} y {@code cantidad >= 0}.<br>
     * <b>Post:</b><br>
     * - El desgaste de la nave se incrementa en {@code cantidad} y se registra el evento en {@code bitacora_general}.
     *
     * @param cantidad Cantidad de desgaste a incrementar.
     */
    public void aumentarDesgaste(int cantidad) {
        assert nave != null && bitacora_general != null : "La nave y la bitácora general deben estar inicializadas.";
        assert cantidad >= 0 : "La cantidad de desgaste no puede ser negativa.";

        nave.aumentarDesgaste(cantidad);
        bitacora_general.registrar("Se aumento el desgaste en " + cantidad + " unidades, desgaste actual: " + nave.getDesgaste());
    }

    // RELACIONADO A MISIONES ----

    public Nave getNave() {
        return nave;
    }

    public Posicion getPosicion() {
        return tablero.getPosicionNave();
    }

    /**
     * Obtiene un objetivo desde el tablero mediante el radar de largo alcance y lo almacena como objetivo actual.<br>
     * <b>Pre:</b><br>
     * - {@code tablero != null}.<br>
     * <b>Post:</b><br>
     * - {@code objetivoActual} queda actualizado con la posición obtenida y se devuelve dicha posición.
     *
     * @return Posición objetivo obtenida del tablero.
     */
    public Posicion usarRLD() {
        assert tablero != null : "El tablero no puede ser nulo.";

        this.objetivoActual = tablero.obtenerObjetivo();
        return this.objetivoActual;
    }

    /**
     * Fija como objetivo la base de origen (punto de partida del recorrido) y la devuelve.<br>
     * <b>Pre:</b><br>
     * - {@code baseDeOrigen != null}.<br>
     * <b>Post:</b><br>
     * - {@code objetivoActual} queda igualado a {@code baseDeOrigen} y se devuelve dicha posición.
     *
     * @return Posición correspondiente a la base de origen.
     */
    public Posicion usarBaseDeOrigen() {
        assert this.baseDeOrigen != null : "La base de origen no puede ser nula.";

        this.objetivoActual = this.baseDeOrigen;
        return this.baseDeOrigen;
    }

    public Posicion getBaseDeOrigen() {
        return this.baseDeOrigen;
    }

    public Posicion getObjetivoActual() {
        return this.objetivoActual;
    }

    /**
     * Inspecciona el elemento del universo presente en la posición actual de la nave.<br>
     * <b>Pre:</b><br>
     * - {@code tablero != null}.<br>
     * <b>Post:</b><br>
     * - Devuelve el {@link ElementoDelUniverso} en la posición actual, o {@code null} si está vacía.
     *
     * @return Elemento en la posición actual o {@code null}.
     */
    public ElementoDelUniverso analizarPosicion() {
        assert tablero != null : "El tablero no puede ser nulo.";

        return tablero.inspeccionarPosicion();
    }

    /**
     * Evalúa mediante el radar de corto alcance si existe un elemento no nulo en la siguiente casilla del camino.<br>
     * <b>Pre:</b><br>
     * - {@code tablero != null}, {@code pos != null}, {@code camino != null}, {@code pos} pertenece a {@code camino} y existe una posición siguiente adyacente en {@code camino}.<br>
     * <b>Post:</b><br>
     * - Devuelve {@code true} si el elemento en la siguiente posición del camino no es nulo, o {@code false} en caso contrario.
     *
     * @param pos Posición actual de la nave en el camino.
     * @param camino Secuencia de posiciones del recorrido.
     * @return {@code true} si hay un elemento en la siguiente casilla, {@code false} en caso contrario.
     */
    public boolean usarRCD(Posicion pos, ArrayList<Posicion> camino) {
        assert tablero != null : "El tablero no puede ser nulo.";
        assert pos != null : "La posición actual no puede ser nula.";
        assert camino != null : "El camino no puede ser nulo.";

        ElementoDelUniverso[][] elementosCercanos = tablero.observarAlrededores();
        int posI = camino.indexOf(pos);
        assert posI >= 0 && posI + 1 < camino.size() : "La posición no está en el camino o no tiene siguiente posición.";

        int y = camino.get(posI + 1).getY() - camino.get(posI).getY();
        int x = camino.get(posI + 1).getX() - camino.get(posI).getX();
        assert Math.abs(x) <= 1 && Math.abs(y) <= 1 : "El desplazamiento entre posiciones consecutivas excede el rango del radar.";

        ElementoDelUniverso proximo = elementosCercanos[x + 1][y + 1];
        return proximo != null;
    }

    /**
     * Genera una ruta paso a paso desde la posicion actual de la nave hasta la posicion del objetivo.<br>
     * <p><b>Pre:</b> objetivo no puede ser null, debe ser una posicion valida<br></p>
     * <p><b>Pre:</b> la posicion actual de la nave debe estar previamente inicializada</p>
     * <b>Post:</b><br>
     * - Devuelve una lista ordenada de {@link Posicion} desde la posición actual hasta {@code objetivo}.
     *
     * @param objetivo posicion del destino al cual se dirige la nave
     * @return un objeto ArrayList con la secuencia ordenada del camino
     * @throws IllegalArgumentException si objetivo es null
     */
    public ArrayList<Posicion> generarCamino(Posicion objetivo) {
        if (objetivo == null) {
            throw new IllegalArgumentException("El objetivo no puede ser nulo.");
        }
        assert tablero != null && getPosicion() != null : "El tablero y la posición actual de la nave deben estar inicializados.";

        int xActual = getPosicion().getX();
        int yActual = getPosicion().getY();

        ArrayList<Posicion> ruta = new ArrayList<>();
        ruta.add(new Posicion(xActual, yActual));

        while (xActual != objetivo.getX() || yActual != objetivo.getY()) {

            if (xActual != objetivo.getX()) {
                xActual += Integer.compare(objetivo.getX(), xActual);
            } else {
                yActual += Integer.compare(objetivo.getY(), yActual);
            }

            ruta.add(new Posicion(xActual, yActual));
        }

        return ruta;
    }

    /**
     * Ordena el movimiento del turno según lo que detectó el radar de corto alcance.<br>
     * <b>Pre:</b><br>
     * - {@code nave != null}.<br>
     * <b>Post:</b><br>
     * - Delega en {@code nave.saltarWarp()} si {@code hayObstaculo} es {@code true}, o en {@code nave.saltar()} si es {@code false}, devolviendo las casillas a avanzar.
     *
     * @param hayObstaculo true si hay un obstáculo en la próxima casilla del camino
     * @return casillas que avanza la nave (0, 1 o 2)
     */
    public int ordenarSalto(boolean hayObstaculo) {
        assert nave != null : "La nave no puede ser nula.";

        return hayObstaculo ? nave.saltarWarp() : nave.saltar();
    }

    /**
     * Mueve la nave en el tablero hacia la posición indicada.<br>
     * <b>Pre:</b><br>
     * - {@code tablero != null} y {@code posicion != null}.<br>
     * <b>Post:</b><br>
     * - La posición de la nave en el tablero queda actualizada a {@code posicion}.
     *
     * @param posicion Nueva posición de destino para la nave.
     */
    public void moverNave(Posicion posicion) {
        assert tablero != null : "El tablero no puede ser nulo.";
        assert posicion != null : "La posición no puede ser nula.";

        tablero.moverNave(posicion);
    }

    public Tablero getTablero() {
        return tablero;
    }

}