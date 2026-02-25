package cl.speedfast.ui;

import cl.speedfast.config.AppConfig;
import cl.speedfast.controller.ControladorPedidos;
import cl.speedfast.controller.ControladorRepartidores;
import cl.speedfast.model.Pedido;
import cl.speedfast.model.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Panel para gestionar pedidos del sistema.
public class PedidosPanel extends JPanel {

    private final DefaultTableModel modelo;
    private final JTable tabla;
    private JCheckBox chkFiltrarTipo;
    private JCheckBox chkFiltrarEstado;

    private JComboBox<AppConfig.TipoPedido> comboFiltroTipo;
    private JComboBox<AppConfig.EstadoPedido> comboFiltroEstado;

    public PedidosPanel() {

        setLayout(new BorderLayout(10, 10));

        // PANEL DE FILTROS
        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.LEFT));

        chkFiltrarTipo = new JCheckBox("Filtrar por tipo");
        chkFiltrarEstado = new JCheckBox("Filtrar por estado");

        comboFiltroTipo = new JComboBox<>(AppConfig.TipoPedido.values());
        comboFiltroEstado = new JComboBox<>(AppConfig.EstadoPedido.values());

        comboFiltroTipo.setEnabled(false);
        comboFiltroEstado.setEnabled(false);

        filtros.add(chkFiltrarTipo);
        filtros.add(comboFiltroTipo);
        filtros.add(chkFiltrarEstado);
        filtros.add(comboFiltroEstado);

        add(filtros, BorderLayout.NORTH);

        // MODELO Y TABLA
        modelo = new DefaultTableModel(
                new Object[]{"ID pedido", "Dirección", "Tipo", "Estado", "Repartidor asignado"}, 0) {

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

        JButton btnRegistrar = new JButton("Registrar pedido");
        JButton btnEditar = new JButton("Editar pedido");
        JButton btnEliminar = new JButton("Eliminar pedido");

        botones.add(btnRegistrar);
        botones.add(btnEditar);
        botones.add(btnEliminar);

        add(botones, BorderLayout.SOUTH);

        // EVENTOS

        btnRegistrar.addActionListener(e ->
                new VentanaPedido(this::cargarPedidos)
        );

        btnEditar.addActionListener(e -> editarPedido());

        btnEliminar.addActionListener(e -> eliminarPedido());

        chkFiltrarTipo.addActionListener(e -> {
            boolean enabled = chkFiltrarTipo.isSelected();
            comboFiltroTipo.setEnabled(enabled);
            cargarPedidos();
        });

        chkFiltrarEstado.addActionListener(e -> {
            boolean enabled = chkFiltrarEstado.isSelected();
            comboFiltroEstado.setEnabled(enabled);
            cargarPedidos();
        });

        comboFiltroTipo.addActionListener(e -> {
            if (chkFiltrarTipo.isSelected()) {
                cargarPedidos();
            }
        });

        comboFiltroEstado.addActionListener(e -> {
            if (chkFiltrarEstado.isSelected()) {
                cargarPedidos();
            }
        });

        //cargarPedidos();
    }

    private void editarPedido() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un pedido.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);

        Pedido pedido = ControladorPedidos.obtenerPedidoPorId(id);

        if (pedido == null) {
            JOptionPane.showMessageDialog(this,
                    "Pedido no encontrado.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String direccion = pedido.getDireccion();
        AppConfig.TipoPedido tipo = pedido.getTipoPedido();
        AppConfig.EstadoPedido estado = pedido.getEstado();

        //Double distancia = pedido.getDistanciaKm();

        new VentanaPedido(
                id,
                direccion,
                tipo,
                estado,
                //distancia,
                this::cargarPedidos
        );
    }


    private void eliminarPedido() {

        int fila = tabla.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un pedido.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modelo.getValueAt(fila, 0);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Eliminar el pedido seleccionado?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {

            ControladorPedidos.eliminarPedido(id);

            JOptionPane.showMessageDialog(this,
                    "Pedido eliminado correctamente.");

            cargarPedidos();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this,
                    "No se puede eliminar un pedido ya entregado o asociado a un repartidor.",
                            //"Detalle: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cargarPedidos() {

        modelo.setRowCount(0);

        try {

            AppConfig.TipoPedido tipo = null;
            AppConfig.EstadoPedido estado = null;

            if (chkFiltrarTipo.isSelected()) {
                tipo = (AppConfig.TipoPedido) comboFiltroTipo.getSelectedItem();
            }

            if (chkFiltrarEstado.isSelected()) {
                estado = (AppConfig.EstadoPedido) comboFiltroEstado.getSelectedItem();
            }

            List<Pedido> pedidos =
                    ControladorPedidos.obtenerPedidosFiltrados(tipo, estado);

            for (Pedido p : pedidos) {
                modelo.addRow(new Object[]{
                        p.getIdPedido(),
                        p.getDireccion(),
                        p.getTipoPedido().obtenerNombre(),
                        p.getEstado().obtenerNombre(),
                        p.getRepartidorAsignado()
                });
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this,
                    "Error al listar pedidos: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }


    /*private void cargarPedidos() {

        modelo.setRowCount(0);

        try {

            List<Pedido> pedidos = ControladorPedidos.obtenerPedidos();

            for (Pedido p : pedidos) {

                // FILTRO POR TIPO
                if (chkFiltrarTipo.isSelected()) {
                    AppConfig.TipoPedido tipoSeleccionado =
                            (AppConfig.TipoPedido) comboFiltroTipo.getSelectedItem();

                    if (!p.getTipoPedido().equals(tipoSeleccionado)) {
                        continue;
                    }
                }

                // FILTRO POR ESTADO
                if (chkFiltrarEstado.isSelected()) {
                    AppConfig.EstadoPedido estadoSeleccionado =
                            (AppConfig.EstadoPedido) comboFiltroEstado.getSelectedItem();

                    if (!p.getEstado().equals(estadoSeleccionado)) {
                        continue;
                    }
                }

                modelo.addRow(new Object[]{
                        p.getIdPedido(),
                        p.getDireccion(),
                        p.getTipoPedido().obtenerNombre(),
                        p.getEstado().obtenerNombre(),
                        p.getRepartidorAsignado()
                });
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this,
                    "Error al listar pedidos: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }*/


    private void iniciarEntrega() {

        try {

            List<Repartidor> repartidores = ControladorRepartidores.listarRepartidores();

            if (repartidores.isEmpty()) {
                throw new Exception("No existen repartidores registrados.");
            }

            // Cargar pedidos pendientes desde la BD a la zona
            ControladorPedidos.cargarPedidosPendientesEnZona();

            ExecutorService executor =
                    Executors.newFixedThreadPool(repartidores.size());

            for (Repartidor r : repartidores) {

                executor.execute(
                        new Repartidor(
                                r.getId(),
                                r.getNombre(),
                                ControladorPedidos.obtenerZonaDeCarga()
                        )
                );
            }

            executor.shutdown();

            try {
                executor.awaitTermination(1, java.util.concurrent.TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Ahora sí recargamos la tabla desde la BD
            cargarPedidos();

            JOptionPane.showMessageDialog(this,
                    "Entrega finalizada correctamente.");

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
