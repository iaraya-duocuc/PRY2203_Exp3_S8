package cl.speedfast.ui;

import cl.speedfast.config.AppConfig;
import cl.speedfast.controller.ControladorEntregas;
import cl.speedfast.controller.ControladorPedidos;
import cl.speedfast.controller.ControladorRepartidores;
import cl.speedfast.model.Pedido;
import cl.speedfast.model.Repartidor;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class VentanaEntrega extends JFrame {

    private JComboBox<Pedido> comboPedidos;
    private JComboBox<Repartidor> comboRepartidores;

    private JLabel lblFecha;
    private JLabel lblHora;

    private JButton btnRegistrar;

    private final Runnable onSuccess;

    public VentanaEntrega(Runnable onSuccess) {

        this.onSuccess = onSuccess;

        setTitle("Registrar Entrega");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inicializarComponentes();
        cargarDatos();

        setVisible(true);
    }

    private void inicializarComponentes() {

        setLayout(new GridLayout(5, 2, 5, 5));

        add(new JLabel("Pedido pendiente / en reparto:"));
        comboPedidos = new JComboBox<>();
        add(comboPedidos);

        add(new JLabel("Repartidor:"));
        comboRepartidores = new JComboBox<>();
        add(comboRepartidores);

        add(new JLabel("Fecha:"));
        lblFecha = new JLabel(LocalDate.now().toString());
        add(lblFecha);

        add(new JLabel("Hora:"));
        lblHora = new JLabel(LocalTime.now().withNano(0).toString());
        add(lblHora);

        btnRegistrar = new JButton("Registrar Entrega");

        add(new JLabel());
        add(btnRegistrar);

        btnRegistrar.addActionListener(e -> registrarEntrega());
    }

    private void cargarDatos() {

        try {

            List<Pedido> pedidosPendientes =
                    ControladorPedidos.obtenerPedidos()
                            .stream()
                            .filter(p -> p.getEstado() == AppConfig.EstadoPedido.PENDIENTE || p.getEstado() == AppConfig.EstadoPedido.EN_REPARTO )
                            .collect(Collectors.toList());

            comboPedidos.removeAllItems();

            for (Pedido p : pedidosPendientes) {
                comboPedidos.addItem(p);
            }

            List<Repartidor> repartidores =
                    ControladorRepartidores.listarRepartidores();

            comboRepartidores.removeAllItems();

            for (Repartidor r : repartidores) {
                comboRepartidores.addItem(r);
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(this,
                    "Error cargando datos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarEntrega() {

        try {

            Pedido pedido = (Pedido) comboPedidos.getSelectedItem();
            Repartidor repartidor = (Repartidor) comboRepartidores.getSelectedItem();

            if (pedido == null) {
                throw new Exception("Debe seleccionar un pedido pendiente.");
            }

            if (repartidor == null) {
                throw new Exception("Debe seleccionar un repartidor.");
            }

            ControladorEntregas.registrarEntrega(
                    pedido.getIdPedido(),
                    repartidor.getId()
            );

            ControladorPedidos.actualizarEstadoPedido(
                    pedido.getIdPedido(),
                    AppConfig.EstadoPedido.ENTREGADO
            );

            JOptionPane.showMessageDialog(this,
                    "Entrega registrada correctamente.");

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