package vista;

import modelo.Asistente;
import modelo.ElementoDelUniverso;
import modelo.nave.Nave;
import modelo.misiones.InformeMision;
import modelo.Elementos.Asteroide;
import modelo.Elementos.EstacionDeCarga;
import modelo.destinoytablero.Observador;
import modelo.destinoytablero.Posicion;
import modelo.destinoytablero.Tablero;

import javax.swing.JTextArea;
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
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Iterator;

public class Ventana extends JFrame implements IVista, Observador {
    private final JTextField nombre = new JTextField(20);
    private final JTextField contrasenia = new JTextField(20);
    private JLabel mensajeErrorLogin;

    private String[] tiposNaves;
    private String[] tiposOrigen;
    private String[] tiposOficio;
    private String[] tiposMisiones;
    private String[] estadosMotorWarp;

    private final JComboBox<String> tipoNave = new JComboBox<>();
    private final JComboBox<String> origenCapitan = new JComboBox<>();
    private final JTextField nombreCapitan = new JTextField();
    private final JTextField identidadCapitan = new JTextField();
    private final JTextField antiguedadCapitan = new JTextField();

    private final JComboBox<String> oficioTripulante = new JComboBox<>();
    private final JComboBox<String> origenTripulante = new JComboBox<>();
    private final JTextField nombreTripulante = new JTextField();
    private final JTextField identidadTripulante = new JTextField();
    private final JTextField antiguedadTripulante = new JTextField();

    // Esta es la única lista que guarda los tripulantes ingresados.
    private final ArrayList<DatosTripulante> tripulantes = new ArrayList<>();
    private final JList<String> listaTripulantes = new JList<>();
    private final ActionListener listenerFinalizar;

    private final JComboBox<String> selectorMision = new JComboBox<>();
    private final JButton botonEjecutarMision = new JButton("Ejecutar misión");
    private final JButton botonReiniciar = new JButton("Configurar nueva nave");
    private final JTextArea areaResumen = new JTextArea();
    private final JLabel etiquetaEstado = new JLabel();

    private Asistente asistenteActual;
    private JLabel[][] celdasTablero;

    public Ventana(ActionListener listenerFinalizar) {
        super("Inicio de sesión");
        this.listenerFinalizar = listenerFinalizar;
        SwingUtilities.invokeLater(this::iniciarLogin);
    }


    @Override
    public void setTiposNaves(String[] tiposNaves) {
        this.tiposNaves = copiarArray(tiposNaves);
    }

    @Override
    public void setTiposOrigen(String[] tiposOrigen) {
        this.tiposOrigen = copiarArray(tiposOrigen);
    }

    @Override
    public void setTiposOficio(String[] tiposOficio) {
        this.tiposOficio = copiarArray(tiposOficio);
    }

    @Override
    public void setTiposMisiones(String[] tiposMisiones) {
        this.tiposMisiones = copiarArray(tiposMisiones);
    }

    @Override
    public void setEstadosMotorWarp(String[] estadosMotorWarp) {
        this.estadosMotorWarp = copiarArray(estadosMotorWarp);
    }

    private String[] copiarArray(String[] valores) {
        return valores == null ? new String[0] : valores.clone();
    }

    @Override
    public void iniciarLogin() {
        SwingUtilities.invokeLater(() -> {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(760, 650));
        setSize(850, 700);
        setLocationRelativeTo(null);
        setTitle("Inicio de sesión");

        JPanel contenido = new JPanel(new GridBagLayout());
        contenido.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(BorderFactory.createTitledBorder("Ingreso"));
        GridBagConstraints c = restricciones();

        agregarCampo(formulario, c, 0, "Nombre:", nombre);
        agregarCampo(formulario, c, 1, "Contraseña:", contrasenia);

        JPanel botones = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 8, 0));
        JButton iniciarSesion = new JButton("Iniciar sesión");
        iniciarSesion.setActionCommand("iniciar_secion");
        iniciarSesion.addActionListener(listenerFinalizar);

        JButton registrarse = new JButton("Registrarse");
        registrarse.setActionCommand("registrarse");
        registrarse.addActionListener(listenerFinalizar);

        botones.add(iniciarSesion);
        botones.add(registrarse);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        formulario.add(botones, c);

        mensajeErrorLogin = new JLabel();
        mensajeErrorLogin.setForeground(Color.RED);
        mensajeErrorLogin.setHorizontalAlignment(SwingConstants.CENTER);
        mensajeErrorLogin.setVisible(false);
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        formulario.add(mensajeErrorLogin, c);

        GridBagConstraints principal = new GridBagConstraints();
        principal.insets = new Insets(12, 12, 12, 12);
        principal.fill = GridBagConstraints.HORIZONTAL;
        principal.weightx = 1;
        contenido.add(formulario, principal);

        setContentPane(contenido);
        revalidate();
        repaint();
        setVisible(true);
        });
    }

    @Override
    public String getNombre() {
        return nombre.getText();
    }

    @Override
    public String getContrasenia() {
        return contrasenia.getText();
    }

    @Override
    public void notificarErrorLogin(String mensaje) {
        SwingUtilities.invokeLater(() -> {
            if (mensajeErrorLogin != null) {
                mensajeErrorLogin.setText(mensaje);
                mensajeErrorLogin.setVisible(true);
                revalidate();
                repaint();
            }
        });
    }

    private void construirInterfaz() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(760, 650));
        setSize(850, 700);
        setLocationRelativeTo(null);
        configurarCombo(tipoNave, tiposNaves);
        configurarCombo(origenCapitan, tiposOrigen);
        configurarCombo(oficioTripulante, tiposOficio);
        configurarCombo(origenTripulante, tiposOrigen);

        JPanel contenido = new JPanel(new BorderLayout(12, 12));
        contenido.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        contenido.add(crearPanelSuperior(), BorderLayout.NORTH);
        contenido.add(crearPanelTripulacion(), BorderLayout.CENTER);
        contenido.add(crearBotonFinalizar(), BorderLayout.SOUTH);
        setContentPane(contenido);
    }

    private void configurarCombo(JComboBox<String> combo, String[] opciones) {
        combo.removeAllItems();
        if (opciones != null) {
            for (String opcion : opciones) {
                combo.addItem(opcion);
            }
        }
    }

    private String getNombreEstadoMotorWarp(int estado) {
        if (estadosMotorWarp != null
                && estado >= 0
                && estado < estadosMotorWarp.length) {
            return estadosMotorWarp[estado];
        }
        return "";
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
        SwingUtilities.invokeLater(() -> {
        if (this.asistenteActual != null) {
            this.asistenteActual.getTablero().quitarObservador(this);
        }
        this.asistenteActual = null;
        this.tripulantes.clear();
        actualizarListaTripulantes();
        nombreCapitan.setText("");
        identidadCapitan.setText("");
        antiguedadCapitan.setText("");
        limpiarCamposTripulante();
        setControlesMisionHabilitados(true);
        setTitle("Configuración de la nave");
        construirInterfaz();
        revalidate();
        repaint();
        this.setVisible(true);
        });
    }

    @Override
    public void iniciarJuego(Asistente asistente) {
        this.asistenteActual = asistente;
        setTitle("Programa en ejecución");
        configurarCombo(selectorMision, tiposMisiones);
        setContentPane(crearPanelJuego(asistente));
        revalidate();
        repaint();
    }

    private JPanel crearPanelJuego(Asistente asistente) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel titulo = new JLabel("Centro de Control de Misiones");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 18f));
        panel.add(titulo, BorderLayout.NORTH);

        // Panel central: Grilla del tablero a la izquierda + Resumen y bitácora a la derecha
        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        panelCentral.add(crearPanelTablero(asistente.getTablero()), BorderLayout.WEST);

        areaResumen.setEditable(false);
        areaResumen.setText(crearResumenJuego(asistente, null));
        panelCentral.add(new JScrollPane(areaResumen), BorderLayout.CENTER);
        panel.add(panelCentral, BorderLayout.CENTER);

        // Panel inferior con el selector de misión, reiniciar y el estado del Motor Warp
        JPanel panelInferior = new JPanel(new BorderLayout(8, 8));

        JPanel panelControles = new JPanel(new BorderLayout(8, 8));
        panelControles.setBorder(BorderFactory.createTitledBorder("Selección de Misión"));
        panelControles.add(new JLabel("Misión a realizar: "), BorderLayout.WEST);
        panelControles.add(selectorMision, BorderLayout.CENTER);

        botonEjecutarMision.setActionCommand("ejecutar_mision");
        for (ActionListener al : botonEjecutarMision.getActionListeners()) {
            botonEjecutarMision.removeActionListener(al);
        }
        botonEjecutarMision.addActionListener(this.listenerFinalizar);

        botonReiniciar.setActionCommand("reiniciar");
        for (ActionListener al : botonReiniciar.getActionListeners()) {
            botonReiniciar.removeActionListener(al);
        }
        botonReiniciar.addActionListener(this.listenerFinalizar);

        JPanel panelBotonesMision = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));
        panelBotonesMision.add(botonEjecutarMision);
        panelBotonesMision.add(botonReiniciar);
        panelControles.add(panelBotonesMision, BorderLayout.EAST);

        etiquetaEstado.setText("Estado del motor Warp: "
                + getNombreEstadoMotorWarp(asistente.getEstadoMotorWarp()));

        panelInferior.add(panelControles, BorderLayout.NORTH);
        panelInferior.add(etiquetaEstado, BorderLayout.SOUTH);

        panel.add(panelInferior, BorderLayout.SOUTH);
        return panel;
    }

    public String crearResumenJuego(Asistente asistente, InformeMision ultimoInforme) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== ESTADO DE LA NAVE ===\n");
        sb.append(asistente.getNave().toString()).append("\n");
        sb.append("Posición actual en el tablero: (")
                .append(asistente.getPosicion().getX()).append(", ")
                .append(asistente.getPosicion().getY()).append(")\n\n");

        sb.append("=== TRIPULACIÓN Y HABERES ===\n");
        Iterator<modelo.tripulantes.Tripulante> it = asistente.get_tripulantes();
        while (it.hasNext()) {
            modelo.tripulantes.Tripulante t = it.next();
            sb.append("- ").append(t.toString())
                    .append(" | Haberes: $").append(t.calcularHaberes()).append("\n");
        }

        if (ultimoInforme != null) {
            sb.append("\n").append(ultimoInforme.toString()).append("\n");
        }

        sb.append("\n=== BITÁCORA GENERAL ===\n");
        for (String entrada : asistente.getBitacoraGeneral().getEntradas()) {
            sb.append("> ").append(entrada).append("\n");
        }
        return sb.toString();
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

    public void mostrarError(String mensaje) {
        SwingUtilities.invokeLater(() ->
            JOptionPane.showMessageDialog(this, mensaje, "Datos inválidos",
            JOptionPane.ERROR_MESSAGE));
    }
    @Override
    public String getTipoMision() {
        return (String) selectorMision.getSelectedItem();
    }

    @Override
    public void actualizarVistaJuego(InformeMision informe) {
        SwingUtilities.invokeLater(() -> {
            areaResumen.setText(crearResumenJuego(this.asistenteActual, informe));
            etiquetaEstado.setText("Estado del motor Warp: "
                + getNombreEstadoMotorWarp(this.asistenteActual.getEstadoMotorWarp()));
    });
    }

    @Override
    public void setControlesMisionHabilitados(boolean habilitado) {
        SwingUtilities.invokeLater(() -> {
            botonEjecutarMision.setEnabled(habilitado);
            selectorMision.setEnabled(habilitado);
        });
    }
    @Override
    public void setReinicioHabilitado(boolean habilitado) {
        SwingUtilities.invokeLater(() -> botonReiniciar.setEnabled(habilitado));
    }

    private JPanel crearPanelTablero(Tablero tablero) {
        int filas = tablero.getX();
        int columnas = tablero.getY();

        JPanel panelGrilla = new JPanel(new GridLayout(filas, columnas, 2, 2));
        panelGrilla.setBorder(BorderFactory.createTitledBorder("Mapa del Sector"));
        panelGrilla.setPreferredSize(new Dimension(380, 380));

        this.celdasTablero = new JLabel[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                JLabel celda = new JLabel("", SwingConstants.CENTER);
                celda.setOpaque(true);
                celda.setFont(new Font("Monospaced", Font.BOLD, 12));
                celda.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
                celdasTablero[i][j] = celda;
                panelGrilla.add(celda);
            }
        }

        tablero.agregarObservador(this);
        refrescarGrilla(tablero);

        return panelGrilla;
    }

    @Override
    public void actualizar() { // actualizar tablero
        if (this.asistenteActual != null) {
            SwingUtilities.invokeLater(() -> {
                refrescarGrilla(this.asistenteActual.getTablero());
                etiquetaEstado.setText("Estado del motor Warp: "
                        + getNombreEstadoMotorWarp(this.asistenteActual.getEstadoMotorWarp()));
            });
        }
    }

    private void refrescarGrilla(Tablero tablero) {
        if (celdasTablero == null) return;
        Posicion posNave = tablero.getPosicionNave();
        Posicion posObjetivo = (this.asistenteActual != null)
                    ? this.asistenteActual.getObjetivoActual()
                    : null;
        ElementoDelUniverso[][] matriz = tablero.verTablero();

        for (int i = 0; i < celdasTablero.length; i++) {
            for (int j = 0; j < celdasTablero[i].length; j++) {
                JLabel celda = celdasTablero[i][j];

                if (posNave != null && posNave.getX() == i && posNave.getY() == j) {
                    celda.setBackground(new Color(30, 136, 229));
                    celda.setForeground(Color.WHITE);
                    celda.setText("NAVE");
                } else if (posObjetivo != null && posObjetivo.getX() == i && posObjetivo.getY() == j) {
                        celda.setBackground(new Color(255, 193, 7)); // Amarillo/Dorado: Objetivo
                        celda.setForeground(Color.BLACK);
                        celda.setText("OBJ");
                    }
                    else {
                    ElementoDelUniverso elem = matriz[i][j];
                    if (elem == null) {
                        celda.setBackground(new Color(20, 24, 35));
                        celda.setForeground(Color.GRAY);
                        celda.setText("·");
                    } else if (elem instanceof Asteroide) {
                        celda.setBackground(new Color(117, 117, 117));
                        celda.setForeground(Color.WHITE);
                        celda.setText("AST");
                    } else if (elem instanceof EstacionDeCarga) {
                        celda.setBackground(new Color(67, 160, 71));
                        celda.setForeground(Color.WHITE);
                        celda.setText("EST");
                    } else if (elem instanceof Nave) {
                        Nave enemiga = (Nave) elem;
                        celda.setBackground(enemiga.getDestruida() ? new Color(60, 40, 40) : new Color(229, 57, 53));
                        celda.setForeground(Color.WHITE);
                        celda.setText(enemiga.getDestruida() ? "X" : "ENE");
                    }
                }
            }
        }
    }
}
