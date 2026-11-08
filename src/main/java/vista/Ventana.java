package vista;

import modelo.Asistente;
import modelo.nave.NaveFactory;
import modelo.tripulantes.TripulanteFactory;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

public class Ventana extends JFrame implements IVista {
    private final JComboBox<String> tipoNave = new JComboBox<>(NaveFactory.tipos_naves);
    private final JComboBox<String> origenCapitan = new JComboBox<>(TripulanteFactory.tipos_origen);
    private final JTextField nombreCapitan = new JTextField();
    private final JTextField identidadCapitan = new JTextField();
    private final JTextField antiguedadCapitan = new JTextField();

    private final JComboBox<String> oficioTripulante = new JComboBox<>(TripulanteFactory.tipos_oficio);
    private final JComboBox<String> origenTripulante = new JComboBox<>(TripulanteFactory.tipos_origen);
    private final JTextField nombreTripulante = new JTextField();
    private final JTextField identidadTripulante = new JTextField();
    private final JTextField antiguedadTripulante = new JTextField();

    // Esta es la única lista que guarda los tripulantes ingresados.
    private final ArrayList<DatosTripulante> tripulantes = new ArrayList<>();
    private final JList<String> listaTripulantes = new JList<>();
    private final ActionListener listenerFinalizar;

    public Ventana(ActionListener listenerFinalizar) {
        super("Configuración de la nave");
        this.listenerFinalizar = listenerFinalizar;
        construirInterfaz();
    }

    private void construirInterfaz() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(760, 650));
        setSize(850, 700);
        setLocationRelativeTo(null);

        JPanel contenido = new JPanel(new BorderLayout(12, 12));
        contenido.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        contenido.add(crearPanelSuperior(), BorderLayout.NORTH);
        contenido.add(crearPanelTripulacion(), BorderLayout.CENTER);
        contenido.add(crearBotonFinalizar(), BorderLayout.SOUTH);
        setContentPane(contenido);
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(crearPanelTipoNave(), BorderLayout.NORTH);
        panel.add(crearPanelCapitan(), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelTipoNave() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Tipo de nave"));
        GridBagConstraints c = restricciones();
        agregarCampo(panel, c, 0, "Tipo:", tipoNave);
        return panel;
    }

    private JPanel crearPanelCapitan() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Atributos del capitán"));
        GridBagConstraints c = restricciones();

        agregarCampo(panel, c, 0, "Origen:", origenCapitan);
        agregarCampo(panel, c, 1, "Nombre:", nombreCapitan);
        agregarCampo(panel, c, 2, "Código de identidad:", identidadCapitan);
        agregarCampo(panel, c, 3, "Antigüedad (años):", antiguedadCapitan);
        return panel;
    }

    private JPanel crearPanelTripulacion() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Ingreso de tripulantes"));
        panel.add(crearFormularioTripulante(), BorderLayout.NORTH);

        listaTripulantes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaTripulantes.setVisibleRowCount(8);
        panel.add(new JScrollPane(listaTripulantes), BorderLayout.CENTER);

        JButton quitar = new JButton("Quitar seleccionado");
        quitar.addActionListener(evento -> quitarTripulanteSeleccionado());
        JPanel acciones = new JPanel(new BorderLayout());
        acciones.add(quitar, BorderLayout.LINE_END);
        panel.add(acciones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearFormularioTripulante() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = restricciones();

        agregarCampo(panel, c, 0, "Oficio:", oficioTripulante);
        agregarCampo(panel, c, 1, "Origen:", origenTripulante);
        agregarCampo(panel, c, 2, "Nombre:", nombreTripulante);
        agregarCampo(panel, c, 3, "Código de identidad:", identidadTripulante);
        agregarCampo(panel, c, 4, "Antigüedad (años):", antiguedadTripulante);

        JButton agregar = new JButton("Agregar tripulante");
        agregar.addActionListener(evento -> agregarTripulante());
        c.gridx = 1;
        c.gridy = 5;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.LINE_END;
        panel.add(agregar, c);
        return panel;
    }

    private JButton crearBotonFinalizar() {
        JButton finalizar = new JButton("Finalizar configuración");
        finalizar.addActionListener(evento -> finalizarConfiguracion());
        return finalizar;
    }

    private GridBagConstraints restricciones() {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3, 5, 3, 5);
        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        return c;
    }

    private void agregarCampo(JPanel panel, GridBagConstraints c, int fila, String etiqueta,
                              java.awt.Component componente) {
        c.gridx = 0;
        c.gridy = fila;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        panel.add(new JLabel(etiqueta), c);
        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(componente, c);
    }

    private void agregarTripulante() {
        try {
            DatosTripulante tripulante = leerDatosTripulante(
                    (String) oficioTripulante.getSelectedItem(),
                    origenTripulante, nombreTripulante, identidadTripulante,
                    antiguedadTripulante);
            if ("CAPITAN".equalsIgnoreCase(tripulante.getOficio())) {
                throw new IllegalArgumentException(
                        "El capitán se completa en la sección de atributos del capitán.");
            }
            for (DatosTripulante existente : tripulantes) {
                if (existente.getIdentidad()
                        .equalsIgnoreCase(tripulante.getIdentidad())) {
                    throw new IllegalArgumentException(
                            "Ya existe un tripulante con ese código de identidad.");
                }
            }
            tripulantes.add(tripulante);
            actualizarListaTripulantes();
            limpiarCamposTripulante();
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    private DatosTripulante leerDatosTripulante(String oficio,
                                                JComboBox<String> origen,
                                                JTextField nombre, JTextField identidad,
                                                JTextField antiguedad) {
        int antiguedadNumero;
        try {
            antiguedadNumero = Integer.parseInt(
                    textoObligatorio(antiguedad, "la antigüedad"));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("La antigüedad debe ser un número entero.");
        }
        if (antiguedadNumero < 0) {
            throw new IllegalArgumentException("La antigüedad no puede ser negativa.");
        }
        return new DatosTripulante(oficio,
                (String) origen.getSelectedItem(),
                textoObligatorio(nombre, "el nombre"),
                textoObligatorio(identidad, "el código de identidad"),
                antiguedadNumero);
    }

    private String textoObligatorio(JTextField campo, String nombreCampo) {
        String texto = campo.getText().trim();
        if (texto.isEmpty()) {
            throw new IllegalArgumentException("Debe completar " + nombreCampo + ".");
        }
        return texto;
    }

    private void quitarTripulanteSeleccionado() {
        int indice = listaTripulantes.getSelectedIndex();
        if (indice < 0) {
            mostrarError("Seleccione un tripulante para quitar.");
            return;
        }
        tripulantes.remove(indice);
        actualizarListaTripulantes();
    }

    private void actualizarListaTripulantes() {
        String[] nombres = new String[tripulantes.size()];
        for (int i = 0; i < tripulantes.size(); i++) {
            nombres[i] = (i + 1) + ". " + tripulantes.get(i);
        }
        listaTripulantes.setListData(nombres);
    }

    private void finalizarConfiguracion() {
        try {
            validarDatosFinales();
            listenerFinalizar.actionPerformed(new ActionEvent(
                    this, ActionEvent.ACTION_PERFORMED, "inicializar"));
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    public DatosTripulante getDatosCapitan() {
        return leerDatosTripulante("CAPITAN", origenCapitan, nombreCapitan,
                identidadCapitan, antiguedadCapitan);
    }

    public ArrayList<DatosTripulante> getDatosTripulantes() {
        return new ArrayList<>(tripulantes);
    }

    public String getTipoNave() {
        return (String) tipoNave.getSelectedItem();
    }

    @Override
    public void iniciarConfiguracionNave() {
        this.setVisible(true);
    }

    @Override
    public void iniciarJuego(Asistente asistente) {
        setTitle("Programa en ejecución");
        setContentPane(crearPanelJuego(asistente));
        revalidate();
        repaint();
    }

    private JPanel crearPanelJuego(Asistente asistente) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel titulo = new JLabel("Programa en ejecución");
        titulo.setFont(titulo.getFont().deriveFont(java.awt.Font.BOLD, 18f));
        panel.add(titulo, BorderLayout.NORTH);

        javax.swing.JTextArea resumen = new javax.swing.JTextArea();
        resumen.setEditable(false);
        resumen.setText(crearResumenJuego(asistente));
        panel.add(new JScrollPane(resumen), BorderLayout.CENTER);

        JLabel estado = new JLabel("Estado del motor Warp: "
                + asistente.getEstadoMotorWarp());
        panel.add(estado, BorderLayout.SOUTH);
        return panel;
    }

    private String crearResumenJuego(Asistente asistente) {
        StringBuilder resumen = new StringBuilder("Tripulación ingresada:\n\n");
        Iterator<?> tripulantes = asistente.get_tripulantes();
        while (tripulantes.hasNext()) {
            resumen.append("- ").append(tripulantes.next()).append('\n');
        }
        return resumen.toString();
    }

    private void validarDatosFinales() {
        DatosTripulante capitan = getDatosCapitan();
        validarAntiguedad(capitan);
        for (DatosTripulante tripulante : tripulantes) {
            validarAntiguedad(tripulante);
            if (tripulante.getIdentidad().equalsIgnoreCase(capitan.getIdentidad())) {
                throw new IllegalArgumentException(
                        "El capitán y un tripulante no pueden tener la misma identidad.");
            }
        }
    }

    private void validarAntiguedad(DatosTripulante datos) {
        if (datos.getAntiguedad() < 0) {
            throw new IllegalArgumentException("La antigüedad no puede ser negativa.");
        }
    }

    private void limpiarCamposTripulante() {
        nombreTripulante.setText("");
        identidadTripulante.setText("");
        antiguedadTripulante.setText("");
        nombreTripulante.requestFocusInWindow();
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Datos inválidos",
                JOptionPane.ERROR_MESSAGE);
    }
}
