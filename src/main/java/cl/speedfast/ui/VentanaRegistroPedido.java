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

    //private JTextField txtId;
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

        //add(new JLabel("ID:"));
        //txtId = new JTextField();
        //add(txtId);

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

            //int id = Integer.parseInt(txtId.getText());
            String direccion = txtDireccion.getText();
            double distancia = Double.parseDouble(txtDistancia.getText());
            String tipo = (String) comboTipo.getSelectedItem();

            if (direccion.isEmpty()) {
                throw new Exception("Dirección vacía");
            }

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
                    throw new Exception("Tipo inválido");
            }

            ControladorPedidos.agregarPedido(pedido);

            JOptionPane.showMessageDialog(this,
                    "Pedido registrado correctamente");

            dispose();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage());
        }
    }
}
