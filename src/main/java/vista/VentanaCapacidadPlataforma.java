package vista;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaCapacidadPlataforma extends JDialog implements IVistaCapacidad {
    private final JTextField campoCapacidad = new JTextField(12);
    private Integer capacidadConfirmada = null;

    public VentanaCapacidadPlataforma() {
        setModal(true);
        construirInterfaz();
    }

    private void construirInterfaz() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(420, 175);
        setResizable(false);
        setLocationRelativeTo(getOwner());

        JPanel contenido = new JPanel(new BorderLayout(10, 10));
        contenido.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel centro = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.anchor = GridBagConstraints.LINE_START;

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        centro.add(new JLabel("Capacidad de la plataforma:"), c);

        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        centro.add(campoCapacidad, c);

        JButton aceptar = new JButton("Aceptar");
        aceptar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmarCapacidad();
            }
        });

        JButton cancelar = new JButton("Cancelar");
        cancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                capacidadConfirmada = null;
                dispose();
            }
        });

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        botones.add(cancelar);
        botones.add(aceptar);

        contenido.add(centro, BorderLayout.CENTER);
        contenido.add(botones, BorderLayout.SOUTH);
        setContentPane(contenido);
    }

    private void confirmarCapacidad() {
        String texto = campoCapacidad.getText().trim();
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese una capacidad positiva o cero.",
                    "Capacidad inválida",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int valor;
        try {
            valor = Integer.parseInt(texto);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "La capacidad debe ser un número entero positivo o cero.",
                    "Capacidad inválida",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        if (valor < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ingrese una capacidad positiva (o 0). No se permiten números negativos.",
                    "Capacidad inválida",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        this.capacidadConfirmada = Integer.valueOf(valor);
        dispose();
    }

    public Integer mostrarYEsperarCapacidad() {
        this.capacidadConfirmada = null;
        setVisible(true);
        return this.capacidadConfirmada;
    }
}