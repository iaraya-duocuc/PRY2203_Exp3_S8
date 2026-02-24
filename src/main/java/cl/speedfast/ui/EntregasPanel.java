package cl.speedfast.ui;

import cl.speedfast.config.AppConfig;
import cl.speedfast.controller.ControladorEntregas;
import cl.speedfast.controller.ControladorPedidos;
import cl.speedfast.controller.ControladorRepartidores;
import cl.speedfast.model.Repartidor;
import cl.speedfast.model.dto.EntregaDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class EntregasPanel extends JPanel {

    private final DefaultTableModel modelo;
    private final JTable tabla;
    private List<EntregaDTO> listaActual;


    private JCheckBox chkFiltrarTipo;
    private JCheckBox chkFiltrarRepartidor;

    private JComboBox<AppConfig.TipoPedido> comboFiltroTipo;
    private JComboBox<Repartidor> comboFiltroRepartidor;

    public EntregasPanel() {

        setLayout(new BorderLayout(10, 10));


        // PANEL DE FILTROS
        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT));

        chkFiltrarTipo = new JCheckBox("Filtrar por tipo");
        chkFiltrarRepartidor = new JCheckBox("Filtrar por repartidor");

        comboFiltroTipo = new JComboBox<>(AppConfig.TipoPedido.values());
        comboFiltroRepartidor = new JComboBox<>();

        comboFiltroTipo.setEnabled(false);
        comboFiltroRepartidor.setEnabled(false);

        filtros.add(chkFiltrarTipo);
        filtros.add(comboFiltroTipo);
        filtros.add(chkFiltrarRepartidor);
        filtros.add(comboFiltroRepartidor);

        add(filtros, BorderLayout.NORTH);

        modelo = new DefaultTableModel(
                new Object[]{"ID entrega", "ID pedido", "Direccion", "Tipo Pedido", "Repartidor", "Fecha", "Hora"}, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modelo);

        add(new JScrollPane(tabla), BorderLayout.CENTER);

        // BOTONES
        JPanel botones = new JPanel();

        JButton btnRegistrar = new JButton("Registrar entrega");
        JButton btnEditar = new JButton("Editar entrega");
        JButton btnEliminar = new JButton("Eliminar entrega");

        botones.add(btnRegistrar);
        botones.add(btnEditar);
        botones.add(btnEliminar);

        add(botones, BorderLayout.SOUTH);

        // EVENTOS

        btnRegistrar.addActionListener(e -> {

            boolean hayPendientes = ControladorPedidos
                    .obtenerPedidos()
                    .stream()
                    .anyMatch(p -> (p.getEstado() == AppConfig.EstadoPedido.PENDIENTE || p.getEstado() == AppConfig.EstadoPedido.EN_REPARTO)
                            && !p.tieneRepartidorAsignado());

            if (!hayPendientes) {
                JOptionPane.showMessageDialog(
                        this,
                        "No existen pedidos pendientes disponibles.",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            boolean hayRepartidores = comboFiltroRepartidor.getItemCount() > 0;

            if (!hayRepartidores) {
                JOptionPane.showMessageDialog(
                        this,
                        "No existen repartidores disponibles.",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            new VentanaEntrega(this::cargarEntregas);
        });

        btnEditar.addActionListener(e -> editarEntrega());

        btnEliminar.addActionListener(e -> eliminarEntrega());

        chkFiltrarTipo.addActionListener(e -> {
            boolean enabled = chkFiltrarTipo.isSelected();
            comboFiltroTipo.setEnabled(enabled);
            cargarEntregas();
        });

        chkFiltrarRepartidor.addActionListener(e -> {
            boolean enabled = chkFiltrarRepartidor.isSelected();
            comboFiltroRepartidor.setEnabled(enabled);
            cargarEntregas();
        });

        comboFiltroTipo.addActionListener(e -> {
            if (chkFiltrarTipo.isSelected()) {
                cargarEntregas();
            }
        });

        comboFiltroRepartidor.addActionListener(e -> {
            if (chkFiltrarRepartidor.isSelected()) {
                cargarEntregas();
            }
        });

        //cargarEntregas();
        cargarRepartidores();
    }

    private void editarEntrega() {

    }

    private void eliminarEntrega() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar una entrega.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {

            EntregaDTO entregaSeleccionada = listaActual.get(fila);

            Object[] opciones = {
                    "Liberar pedido (PENDIENTE)",
                    "Mantener estado actual",
                    "Cancelar"
            };

            int decision = JOptionPane.showOptionDialog(
                    this,
                    "¿Qué desea hacer con el pedido asociado?",
                    "Eliminar entrega",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            if (decision == 2 || decision == JOptionPane.CLOSED_OPTION) {
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(this,
                    "¿Confirmar eliminación de la entrega?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            // 1. Eliminar entrega
            ControladorEntregas.eliminarEntrega(entregaSeleccionada.getId());

            // 2. Si eligió liberar pedido
            if (decision == 0) {
                ControladorPedidos.actualizarEstadoPedido(
                        entregaSeleccionada.getIdPedido(),
                        cl.speedfast.config.AppConfig.EstadoPedido.PENDIENTE
                );
            }

            JOptionPane.showMessageDialog(this,
                    "Entrega eliminada correctamente.");

            cargarEntregas();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this,
                    "No se puede eliminar la entrega.\n\n" +
                            "Detalle: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cargarEntregas() {

        modelo.setRowCount(0);

        try {

            AppConfig.TipoPedido tipo = null;

            if (chkFiltrarTipo.isSelected()) {
                tipo = (AppConfig.TipoPedido) comboFiltroTipo.getSelectedItem();
            }

            Integer idRepartidor = 0;

            if (chkFiltrarRepartidor.isSelected()) {
                Repartidor seleccionado =
                        (Repartidor) comboFiltroRepartidor.getSelectedItem();

                if (seleccionado != null) {
                    idRepartidor = seleccionado.getId();
                }
            }

            listaActual = ControladorEntregas.listarEntregasFiltradas(tipo, idRepartidor);

            for (EntregaDTO e : listaActual) {

                modelo.addRow(new Object[]{
                        e.getId(),
                        e.getIdPedido(),
                        e.getDireccionPedido(),
                        e.getTipoPedido(),
                        e.getNombreRepartidor(),
                        e.getFecha(),
                        e.getHora()
                });
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this,
                    "Error al listar entregas: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cargarRepartidores() {

        //modelo.setRowCount(0);
        comboFiltroRepartidor.removeAllItems();

        try {

            List<Repartidor> repartidores =
                    ControladorRepartidores.listarRepartidores();

            for (Repartidor r : repartidores) {
                comboFiltroRepartidor.addItem(r);
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this,
                    "Error al listar repartidores: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}