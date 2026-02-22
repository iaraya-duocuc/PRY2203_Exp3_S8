package cl.speedfast.ui;

import cl.speedfast.controller.ControladorRepartidores;
import cl.speedfast.model.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SistemaUI extends JFrame {

    public SistemaUI() {

        setTitle("Sistema de Gestión - SpeedFast");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Pedidos", crearPanelPedidos());
        tabs.addTab("Entregas", crearPanelEntregas());
        tabs.addTab("Repartidores", crearPanelRepartidores());

        add(tabs);
    }

    private JPanel crearPanelRepartidores() {

        JPanel panel = new JPanel(new BorderLayout(10,10));

        // ==== TABLA ====
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"ID", "Nombre"}, 0);

        JTable tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        panel.add(scroll, BorderLayout.CENTER);

        // ==== BOTONES ====
        JPanel botones = new JPanel();

        JButton btnRegistrar = new JButton("Registrar");
        JButton btnEditar = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnListar = new JButton("Recargar lista");

        botones.add(btnRegistrar);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        botones.add(btnListar);

        panel.add(botones, BorderLayout.SOUTH);

        // Actualiza lsita de repartidores
        btnListar.addActionListener(e -> cargarRepartidores(modelo));

        // Registra nuevo repartidor

        btnRegistrar.addActionListener(e ->
                new VentanaRepartidor(() -> cargarRepartidores(modelo))
        );

        btnEditar.addActionListener(e -> {

            int fila = tabla.getSelectedRow();

            if (fila == -1) {
                JOptionPane.showMessageDialog(panel,
                        "Debe seleccionar un repartidor.",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = (int) modelo.getValueAt(fila, 0);
            String nombre = (String) modelo.getValueAt(fila, 1);

            new VentanaRepartidor(
                    id,
                    nombre,
                    () -> cargarRepartidores(modelo)
            );
        });

        // Elimina repartidor seleccionado actualmente
        btnEliminar.addActionListener(e -> {

            int filaSeleccionada = tabla.getSelectedRow();

            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(panel,
                        "Debe seleccionar un repartidor.",
                        "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = (int) modelo.getValueAt(filaSeleccionada, 0);
            String nombre = (String) modelo.getValueAt(filaSeleccionada, 1);

            int confirmacion = JOptionPane.showConfirmDialog(panel,
                    "¿Eliminar al repartidor '" + nombre + "'?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirmacion == JOptionPane.YES_OPTION) {

                try {

                    ControladorRepartidores.eliminarRepartidor(id);

                    JOptionPane.showMessageDialog(panel,
                            "Repartidor eliminado correctamente.");

                    cargarRepartidores(modelo);

                }catch (Exception ex) {

                    JOptionPane.showMessageDialog(panel,
                            "Error inesperado: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }

            cargarRepartidores(modelo);
        });


        cargarRepartidores(modelo);

        return panel;
    }

    private void cargarRepartidores(DefaultTableModel modelo) {

        modelo.setRowCount(0);

        try {

            List<Repartidor> repartidores = ControladorRepartidores.listarRepartidores();

            for (Repartidor r : repartidores) {
                modelo.addRow(new Object[]{
                        r.getId(),
                        r.getNombre()
                });
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al listar repartidores: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel crearPanelPedidos() {

        JPanel panel = new JPanel(new BorderLayout(10,10));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Dirección:"), gbc);

        gbc.gridx = 1;
        JTextField txtDireccion = new JTextField(20);
        form.add(txtDireccion, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(new JLabel("Tipo:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> comboTipo = new JComboBox<>(
                new String[]{"COMIDA", "ENCOMIENDA", "EXPRESS"});
        form.add(comboTipo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        form.add(new JLabel("Estado:"), gbc);

        gbc.gridx = 1;
        JComboBox<String> comboEstado = new JComboBox<>(
                new String[]{"PENDIENTE", "EN_REPARTO", "ENTREGADO"});
        form.add(comboEstado, gbc);

        panel.add(form, BorderLayout.NORTH);

        JPanel botones = new JPanel();
        botones.add(new JButton("Registrar"));
        botones.add(new JButton("Editar"));
        botones.add(new JButton("Eliminar"));
        panel.add(botones, BorderLayout.CENTER);

        JTable tabla = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Dirección", "Tipo", "Estado"}, 0));
        panel.add(new JScrollPane(tabla), BorderLayout.SOUTH);

        return panel;
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
