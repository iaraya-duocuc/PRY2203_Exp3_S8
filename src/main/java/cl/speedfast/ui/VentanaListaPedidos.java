package cl.speedfast.ui;

import cl.speedfast.controller.ControladorPedidos;
import cl.speedfast.model.Pedido;
import cl.speedfast.model.Repartidor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Ventana que muestra los pedidos en una tabla.
 */
public class VentanaListaPedidos extends JFrame {

    private JTable tabla;
    private DefaultTableModel modelo;
    private boolean iniciarEntrega;

    public VentanaListaPedidos() {
        this(false);
    }

    public VentanaListaPedidos(boolean iniciarEntrega) {
        setTitle("Lista de Pedidos");
        setSize(700, 400);
        setLocationRelativeTo(null);

        inicializarComponentes();

        cargarDatos();

        setVisible(true);

        if (iniciarEntrega) {
            SwingUtilities.invokeLater(this::iniciarEntrega); // trigger manually, after GUI ready
        }
    }

    private void inicializarComponentes() {

        modelo = new DefaultTableModel();

        modelo.addColumn("ID");
        modelo.addColumn("Dirección");
        modelo.addColumn("Tipo");
        modelo.addColumn("Estado");
        modelo.addColumn("Repartidor");

        tabla = new JTable(modelo);

        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    private void cargarDatos() {

        modelo.setRowCount(0);

        List<Pedido> pedidos = ControladorPedidos.obtenerPedidos();

        for (Pedido p : pedidos) {

            modelo.addRow(new Object[]{
                    p.getIdPedido(),
                    p.getDireccion(),
                    p.getTipoPedido().obtenerNombre(),
                    p.getEstado().obtenerNombre(),
                    p.getRepartidorAsignado()
            });
        }
    }

    private void iniciarEntrega() {

        try {

            List<Repartidor> repartidores = ControladorPedidos.obtenerRepartidores();

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
            cargarDatos();

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
