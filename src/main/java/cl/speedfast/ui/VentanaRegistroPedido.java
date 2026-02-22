package cl.speedfast.ui;

import cl.speedfast.controller.ControladorPedidos;
import cl.speedfast.model.Pedido;
import cl.speedfast.model.PedidoComida;
import cl.speedfast.model.PedidoEncomienda;
import cl.speedfast.model.PedidoExpress;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana para registrar pedidos.
 */
public class VentanaRegistroPedido extends JFrame {

    private JTextField txtDireccion;
    private JTextField txtDistancia;
    private JComboBox<String> comboTipo;

    private JButton btnGuardar;

    public VentanaRegistroPedido() {

        setTitle("Registrar Pedido");
        setSize(350, 300);
        setLocationRelativeTo(null);

        inicializarComponentes();

        setVisible(true);
    }

    private void inicializarComponentes() {

        setLayout(new GridLayout(5, 2, 5, 5));

        add(new JLabel("Dirección:"));
        txtDireccion = new JTextField();
        add(txtDireccion);

        add(new JLabel("Distancia (km):"));
        txtDistancia = new JTextField();
        add(txtDistancia);

        add(new JLabel("Tipo:"));
        comboTipo = new JComboBox<>();
        comboTipo.addItem("Comida");
        comboTipo.addItem("Express");
        comboTipo.addItem("Encomienda");

        add(comboTipo);

        btnGuardar = new JButton("Guardar");

        add(new JLabel());
        add(btnGuardar);

        btnGuardar.addActionListener(e -> guardarPedido());
    }

    private void guardarPedido() {

        try {

            String direccion = txtDireccion.getText().trim();
            String distanciaStr = txtDistancia.getText().trim();

            if (direccion.isEmpty()) {
                throw new Exception("La dirección es obligatoria.");
            }

            if (distanciaStr.isEmpty()) {
                throw new Exception("La distancia es obligatoria.");
            }

            double distancia;

            try {
                distancia = Double.parseDouble(distanciaStr);
            } catch (NumberFormatException e) {
                throw new Exception("Formato de distancia inválido.");
            }

            if (distancia <= 0) {
                throw new Exception("La distancia debe ser mayor a 0.");
            }

            String tipo = (String) comboTipo.getSelectedItem();

            Pedido pedido;

            switch (tipo) {
                case "Comida":
                    pedido = new PedidoComida(direccion, true, distancia);
                    break;
                case "Express":
                    pedido = new PedidoExpress(direccion, true, distancia);
                    break;
                case "Encomienda":
                    pedido = new PedidoEncomienda(direccion, 5, true, distancia);
                    break;
                default:
                    throw new Exception("Tipo inválido.");
            }

            ControladorPedidos.agregarPedido(pedido);

            JOptionPane.showMessageDialog(this, "Pedido registrado correctamente");
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
