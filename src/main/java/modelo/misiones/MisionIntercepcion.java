package modelo.misiones;

import java.util.Random;

import modelo.Asistente;
import modelo.nave.Nave;

/**
 * M-01: alcanzar a la nave aliada (el objetivo) y entregarle los recursos que solicita.
 * El pedido se define al preparar; al llegar se verifica que la nave todavía los tenga,
 * y la entrega efectiva se descuenta en el cierre.
 */
public class MisionIntercepcion extends Mision {
    private static final int PEDIDO_MAXIMO = 50; // Anexo: entre 0 y 50 de energía y/o combustible

    private final Random azar = new Random();
    private int energiaPedida;
    private int combustiblePedido;

    public MisionIntercepcion(Asistente asistente) {
        super(asistente);
    }

    @Override public String getCodigo() {
        return "M-01";
    }
    @Override public int getCostoCombustible() {
        return 4;
    }
    @Override public int getCostoEnergia() {
        return 5;
    }
    @Override public int getCostoDesgaste() {
        return 4;
    }

    @Override
    protected void prepararHook() {
        energiaPedida = azar.nextInt(PEDIDO_MAXIMO + 1);
        combustiblePedido = azar.nextInt(PEDIDO_MAXIMO + 1);
        bitacora.registrar("La nave aliada solicita auxilio: " + energiaPedida + " de energía y " + combustiblePedido + " de combustible.");
    }

    /** La nave debe conservar lo pedido y seguir operativa después de entregarlo. */
    @Override
    protected boolean verificarEnDestino() {
        Nave nave = asistente.getNave();
        boolean alcanza = nave.getEnergia() > energiaPedida && nave.getCombustible() > combustiblePedido;
        bitacora.registrar("Verificación en destino: la nave tiene " + nave.getEnergia() + " de energía y " + nave.getCombustible()
                + " de combustible para un pedido de " + energiaPedida + " y " + combustiblePedido + " -> "
                + (alcanza ? "alcanza." : "no alcanza."));
        return alcanza;
    }

    @Override
    protected String ejecutarMision() {
        if (objetivoVerificado) {
            return "Contacto establecido con la nave aliada: asistencia lista para transferir.";
        }
        return "La nave ya no conserva los recursos solicitados: la asistencia no se realiza.";
    }

    /** Entrega efectiva, descontada en el cierre. */
    @Override
    protected void aplicarResultado() {
        Nave nave = asistente.getNave();
        nave.consumirEnergia(energiaPedida);
        nave.consumirCombustible(combustiblePedido);
        bitacora.registrar("Asistencia realizada: se transfirieron " + energiaPedida + " de energía y " + combustiblePedido + " de combustible a la nave aliada.");
    }

    public int getEnergiaPedida() { return energiaPedida; }
    public int getCombustiblePedido() { return combustiblePedido; }
}