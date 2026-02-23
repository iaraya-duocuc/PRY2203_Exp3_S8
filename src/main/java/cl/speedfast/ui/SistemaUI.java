package cl.speedfast.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class SistemaUI extends JFrame {

    public SistemaUI() {

        setTitle("Sistema de Gestión - SpeedFast");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Pedidos", new PedidosPanel());
        tabs.addTab("Entregas", crearPanelEntregas());
        tabs.addTab("Repartidores", new RepartidoresPanel());

        add(tabs);
    }

    private JPanel crearPanelEntregas() {

        JPanel panel = new JPanel(new BorderLayout(10,10));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Pedido:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> comboPedido = new JComboBox<>();
        form.add(comboPedido, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Repartidor:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> comboRepartidor = new JComboBox<>();
        form.add(comboRepartidor, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        form.add(new JLabel("Fecha:"), gbc);

        gbc.gridx = 1;
        JTextField txtFecha = new JTextField(10);
        form.add(txtFecha, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        form.add(new JLabel("Hora:"), gbc);

        gbc.gridx = 1;
        JTextField txtHora = new JTextField(10);
        form.add(txtHora, gbc);

        panel.add(form, BorderLayout.NORTH);

        JPanel botones = new JPanel();
        botones.add(new JButton("Registrar"));
        botones.add(new JButton("Editar"));
        botones.add(new JButton("Eliminar"));
        panel.add(botones, BorderLayout.CENTER);

        JTable tabla = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Pedido", "Repartidor", "Fecha", "Hora"}, 0));
        panel.add(new JScrollPane(tabla), BorderLayout.SOUTH);

        return panel;
    }

}
