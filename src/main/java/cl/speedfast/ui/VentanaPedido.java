package cl.speedfast.ui;

import cl.speedfast.config.AppConfig;
import cl.speedfast.controller.ControladorPedidos;
import cl.speedfast.model.Pedido;
import cl.speedfast.model.PedidoComida;
import cl.speedfast.model.PedidoEncomienda;
import cl.speedfast.model.PedidoExpress;

import javax.swing.*;
import java.awt.*;

// Ventana para registrar o editar pedidos.
public class VentanaPedido extends JFrame {

    private Integer idPedido; // null = registro, != null = edición
    private JTextField txtDireccion;
    //private JTextField txtDistancia;
    private JComboBox<AppConfig.TipoPedido> comboTipo;
    private JComboBox<AppConfig.EstadoPedido> comboEstado;

    private JButton btnGuardar;
    private final Runnable onSuccess;

    public VentanaPedido(Runnable onSuccess) {
        this(null, null, null, null, onSuccess);
    }

    public VentanaPedido(Integer id,
                         String direccion,
                         AppConfig.TipoPedido tipo,
                         AppConfig.EstadoPedido estado,
                         //Double distancia,
                         Runnable onSuccess) {

        this.idPedido = id;
        this.onSuccess = onSuccess;

        setTitle(id == null ? "Registrar Pedido" : "Editar Pedido");
        setSize(350, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inicializarComponentes();

        if (id != null) {
            txtDireccion.setText(direccion);
            //txtDistancia.setText(String.valueOf(distancia));
            comboTipo.setSelectedItem(tipo);
            comboEstado.setSelectedItem(estado);
        }

        setVisible(true);
    }

    private void inicializarComponentes() {

        setLayout(new GridLayout(4, 2, 5, 5));

        add(new JLabel("Dirección:"));
        txtDireccion = new JTextField();
        add(txtDireccion);

        //add(new JLabel("Distancia (km):"));
        //txtDistancia = new JTextField();
        //add(txtDistancia);

        add(new JLabel("Tipo:"));
        comboTipo = new JComboBox<>(AppConfig.TipoPedido.values());
        add(comboTipo);

        add(new JLabel("Estado:"));
        comboEstado = new JComboBox<>(AppConfig.EstadoPedido.values());
        add(comboEstado);

        btnGuardar = new JButton("Guardar");

        add(new JLabel());
        add(btnGuardar);

        btnGuardar.addActionListener(e -> guardarPedido());
    }

    private void guardarPedido() {

        try {

            String direccion = txtDireccion.getText().trim();

            if (direccion == null || direccion.isEmpty()) {
                throw new Exception("La dirección es obligatoria.");
            }

            AppConfig.TipoPedido tipo = (AppConfig.TipoPedido) comboTipo.getSelectedItem();
            AppConfig.EstadoPedido estado = (AppConfig.EstadoPedido) comboEstado.getSelectedItem();

            Pedido pedido = null;

            switch (tipo) {
                case COMIDA:
                    pedido = new PedidoComida(direccion, true, 5);
                    break;
                case EXPRESS:
                    pedido = new PedidoExpress(direccion, true, 5);
                    break;
                case ENCOMIENDA:
                    pedido = new PedidoEncomienda(direccion, 5, true, 5);
                    break;
            }

            pedido.setEstado(estado);

            if (idPedido == null) {
                ControladorPedidos.agregarPedido(pedido);
                JOptionPane.showMessageDialog(this,
                        "Pedido registrado correctamente.");
            }  else {

                Pedido pedidoActualizado;

                switch (tipo) {
                    case COMIDA:
                        pedidoActualizado = new PedidoComida(direccion, true, 5);
                        break;
                    case EXPRESS:
                        pedidoActualizado = new PedidoExpress(direccion, true, 5);
                        break;
                    case ENCOMIENDA:
                        pedidoActualizado = new PedidoEncomienda(direccion, 5, true, 5);
                        break;
                    default:
                        throw new Exception("Tipo inválido.");
                }

                pedidoActualizado.setIdPedido(idPedido);
                pedidoActualizado.setEstado(estado);
                ControladorPedidos.actualizarPedido(pedidoActualizado);

                JOptionPane.showMessageDialog(this,
                        "Pedido actualizado correctamente.");
            }

            if (onSuccess != null) {
                onSuccess.run();
            }

            dispose();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
