package cl.speedfast.ui;

import cl.speedfast.config.AppConfig;
import cl.speedfast.controller.ControladorRepartidores;
import cl.speedfast.model.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RepartidoresPanel extends JPanel {

    private final DefaultTableModel modelo;
    private final JTable tabla;
    private JCheckBox chkFiltrarTipo;
    private JCheckBox chkFiltrarRepartidor;

    private JComboBox<AppConfig.TipoPedido> comboFiltroTipo;
    private JComboBox<Repartidor> comboFiltroRepartidor;

    public RepartidoresPanel() {

        setLayout(new BorderLayout(10, 10));

        // MODELO Y TABLA
        modelo = new DefaultTableModel(
                new Object[]{"ID repartidor", "Nombre"}, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabla);
        add(scroll, BorderLayout.CENTER);

        // BOTONES
        JPanel botones = new JPanel();

        JButton btnRegistrar = new JButton("Registrar repartidor");
        JButton btnEditar = new JButton("Editar repartidor");
        JButton btnEliminar = new JButton("Eliminar repartidor");

        botones.add(btnRegistrar);
        botones.add(btnEditar);
        botones.add(btnEliminar);

        add(botones, BorderLayout.SOUTH);

        // EVENTOS

        btnRegistrar.addActionListener(e ->
                new VentanaRepartidor(this::cargarRepartidores)
        );

        btnEditar.addActionListener(e -> editarRepartidor());

        btnEliminar.addActionListener(e -> eliminarRepartidor());

        //cargarRepartidores();
    }

    private void editarRepartidor() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
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
                this::cargarRepartidores
        );
    }

    private void eliminarRepartidor() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un repartidor.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);
        String nombre = (String) modelo.getValueAt(fila, 1);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Eliminar al repartidor '" + nombre + "'?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            ControladorRepartidores.eliminarRepartidor(id);

            JOptionPane.showMessageDialog(this,
                    "Repartidor eliminado correctamente.");

            cargarRepartidores();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this,
                    "No se puede eliminar el repartidor.\n" +
                            "Puede estar asociado a entregas.\n\n" +
                            "Detalle: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cargarRepartidores() {

        modelo.setRowCount(0);

        try {

            List<Repartidor> repartidores =
                    ControladorRepartidores.listarRepartidores();

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
}
