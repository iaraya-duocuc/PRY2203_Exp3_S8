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

    private Integer idEntrega;
    private JComboBox<Object> comboPedidos;
    private JComboBox<Repartidor> comboRepartidores;

    private JLabel lblFecha;
    private JLabel lblHora;

    private JButton btnRegistrar;

    private final Runnable onSuccess;

    public VentanaEntrega(Runnable onSuccess) {
        this(null, null, null, null, null, null, null, onSuccess);
    }

    public VentanaEntrega(Integer idEntrega,
                          Integer idPedido,
                          Integer idRepartidorActual,
                          String direccionPedido,
                          String tipoPedido,
                          LocalDate fechaEntrega,
                          LocalTime horaEntrega,
                          Runnable onSuccess) {

        this.idEntrega = idEntrega;
        this.onSuccess = onSuccess;

        setTitle(idEntrega == null ? "Registrar Entrega" : "Editar Entrega");
        setSize(500, 280);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        inicializarComponentes();

        if (idEntrega == null) {
            cargarDatosRegistro();
        } else {
            cargarDatosEdicion(idPedido, idRepartidorActual, direccionPedido, tipoPedido, fechaEntrega, horaEntrega);
        }

        setVisible(true);
    }

    private void inicializarComponentes() {

        setLayout(new GridLayout(6, 2, 5, 5));

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

        btnRegistrar = new JButton(idEntrega == null ? "Registrar Entrega" : "Guardar cambios");

        add(new JLabel());
        add(btnRegistrar);

        btnRegistrar.addActionListener(e -> guardarEntrega());
    }

    private void cargarDatosRegistro() {

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

    private void cargarDatosEdicion(Integer idPedido,
                                    Integer idRepartidorActual,
                                    String direccionPedido,
                                    String tipoPedido,
                                    LocalDate fechaEntrega,
                                    LocalTime horaEntrega) {
        comboPedidos.removeAllItems();
        comboPedidos.addItem("Pedido #" + idPedido + " - " + tipoPedido + " - " + direccionPedido);
        comboPedidos.setEnabled(false);

        if (fechaEntrega != null) {
            lblFecha.setText(fechaEntrega.toString());
        }

        if (horaEntrega != null) {
            lblHora.setText(horaEntrega.withNano(0).toString());
        }

        try {
            comboRepartidores.removeAllItems();
            List<Repartidor> repartidores = ControladorRepartidores.listarRepartidores();
            for (Repartidor r : repartidores) {
                comboRepartidores.addItem(r);
                if (idRepartidorActual != null && r.getId() == idRepartidorActual) {
                    comboRepartidores.setSelectedItem(r);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error cargando repartidores: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void guardarEntrega() {

        if (idEntrega == null) {
            registrarEntrega();
            return;
        }

        editarEntrega();
    }

    private void registrarEntrega() {

        try {

            Object pedidoSeleccionado = comboPedidos.getSelectedItem();
            Repartidor repartidor = (Repartidor) comboRepartidores.getSelectedItem();

            if (!(pedidoSeleccionado instanceof Pedido)) {
                throw new Exception("Debe seleccionar un pedido pendiente.");
            }

            if (repartidor == null) {
                throw new Exception("Debe seleccionar un repartidor.");
            }

            Pedido pedido = (Pedido) pedidoSeleccionado;

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

    private void editarEntrega() {
        try {
            Repartidor repartidor = (Repartidor) comboRepartidores.getSelectedItem();

            if (repartidor == null) {
                throw new Exception("Debe seleccionar un repartidor.");
            }

            ControladorEntregas.actualizarRepartidorEntrega(idEntrega, repartidor.getId());

            JOptionPane.showMessageDialog(this,
                    "Entrega actualizada correctamente.");

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
