package cl.speedfast.ui;

import cl.speedfast.controller.ControladorPedidos;
import cl.speedfast.controller.ControladorRepartidores;
import cl.speedfast.dao.EntregaDAO;
import cl.speedfast.model.Pedido;
import cl.speedfast.model.Repartidor;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VentanaRegistroEntrega extends JFrame {

    private JComboBox<Pedido> comboPedidos;
    private JComboBox<Repartidor> comboRepartidores;
    private JButton btnGuardar;

    public VentanaRegistroEntrega() {

        setTitle("Registrar Entrega");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 2, 5, 5));

        inicializarComponentes();
        cargarCombos();

        setVisible(true);
    }

    private void inicializarComponentes() {

        add(new JLabel("Pedido:"));
        comboPedidos = new JComboBox<>();
        add(comboPedidos);

        add(new JLabel("Repartidor:"));
        comboRepartidores = new JComboBox<>();
        add(comboRepartidores);

        btnGuardar = new JButton("Guardar");
        add(new JLabel());
        add(btnGuardar);

        btnGuardar.addActionListener(e -> guardarEntrega());
    }

    private void cargarCombos() {

        comboPedidos.removeAllItems();
        comboRepartidores.removeAllItems();

        List<Pedido> pedidos = ControladorPedidos.obtenerPedidos();
        for (Pedido p : pedidos) {
            comboPedidos.addItem(p);
        }

        List<Repartidor> repartidores = ControladorRepartidores.listarRepartidores();
        for (Repartidor r : repartidores) {
            comboRepartidores.addItem(r);
        }
    }

    private void guardarEntrega() {

        try {

            Pedido pedido = (Pedido) comboPedidos.getSelectedItem();
            Repartidor repartidor = (Repartidor) comboRepartidores.getSelectedItem();

            if (pedido == null || repartidor == null) {
                throw new Exception("Debe seleccionar pedido y repartidor.");
            }

            EntregaDAO dao = new EntregaDAO();
            dao.create(pedido.getIdPedido(), repartidor.getId());

            JOptionPane.showMessageDialog(this,
                    "Entrega registrada correctamente.");
            dispose();

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
