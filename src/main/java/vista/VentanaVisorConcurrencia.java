package vista;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

public class VentanaVisorConcurrencia extends JFrame implements IVistaConcurrencia {

    private final JTextField campoNombre = new JTextField(16);
    private final JTextArea areaConsola = new JTextArea();
    private AccionEntidadListener listener;

    public VentanaVisorConcurrencia(int capacidadPlataforma, AccionEntidadListener listener) {
        super("Simulación Concurrente - Capacidad: " + capacidadPlataforma);
        this.listener = listener;
        construirInterfaz(capacidadPlataforma);
    }

    private void construirInterfaz(int capacidadPlataforma) {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(820, 460));
        setSize(900, 520);
        setLocationRelativeTo(null);

        JPanel panelIzquierdo = crearPanelControles(capacidadPlataforma);
        panelIzquierdo.setPreferredSize(new Dimension(280, 0));

        JPanel panelDerecho = crearPanelConsola();

        JPanel contenido = new JPanel(new BorderLayout(12, 12));
        contenido.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        contenido.add(panelIzquierdo, BorderLayout.WEST);
        contenido.add(panelDerecho, BorderLayout.CENTER);
        setContentPane(contenido);
    }

    private JPanel crearPanelControles(int capacidadPlataforma) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Controles"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 0;

        int fila = 0;

        c.gridy = fila++;
        panel.add(new JLabel("Plataforma (capacidad: " + capacidadPlataforma + ")"), c);

        c.gridy = fila++;
        panel.add(new JLabel("Nombre de la entidad:"), c);

        c.gridy = fila++;
        panel.add(campoNombre, c);

        JButton btnGeneradora = new JButton("Agregar generadora");
        btnGeneradora.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solicitarCreacion(true);
            }
        });
        c.gridy = fila++;
        panel.add(btnGeneradora, c);

        JButton btnNave = new JButton("Agregar nave");
        btnNave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solicitarCreacion(false);
            }
        });
        c.gridy = fila++;
        panel.add(btnNave, c);

        return panel;
    }

    private JPanel crearPanelConsola() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createTitledBorder("Eventos de la simulación"));

        areaConsola.setEditable(false);
        areaConsola.setLineWrap(true);
        areaConsola.setWrapStyleWord(true);

        panel.add(new JScrollPane(areaConsola), BorderLayout.CENTER);
        return panel;
    }

    private void solicitarCreacion(boolean esGeneradora) {
        String nombre = campoNombre.getText().trim();
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese un nombre para la entidad.",
                    "Dato faltante",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (listener != null) {
            if (esGeneradora) {
                listener.onCrearGeneradora(nombre);
            } else {
                listener.onCrearNave(nombre);
            }
        }

        campoNombre.setText("");
        campoNombre.requestFocusInWindow();
    }

    @Override
    public void agregarLineaLog(final String texto) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                areaConsola.append(texto + "\n");
                areaConsola.setCaretPosition(areaConsola.getDocument().getLength());
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg != null) {
            agregarLineaLog(arg.toString());
        }
    }

    @Override
    public void setAccionEntidadListener(AccionEntidadListener listener) {
        this.listener = listener;
    }

    @Override
    public void mostrar() {
        setVisible(true);
    }

    @Override
    public void cerrar() {
        dispose();
    }
}