package cl.speedfast.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Ventana principal.
 */
public class VentanaPrincipal extends JFrame {

    private JButton btnRegistrar;
    private JButton btnListar;
    private JButton btnIniciarEntrega;
    private JButton btnRegistrarRepartidor;

    public VentanaPrincipal() {

        setTitle("SpeedFast - Sistema de Gestión");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        inicializarComponentes();

        setVisible(true);
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Sistema SpeedFast", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));

        add(titulo, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(4, 1, 10, 10));

        btnRegistrar = new JButton("Registrar Pedido");
        btnListar = new JButton("Listar Pedidos");
        btnIniciarEntrega = new JButton("Asignar Repartidor / Iniciar Entrega");
        btnRegistrarRepartidor = new JButton("Registrar Repartidor");

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnListar);
        panelBotones.add(btnIniciarEntrega);
        panelBotones.add(btnRegistrarRepartidor);

        add(panelBotones, BorderLayout.CENTER);

        // Eventos
        btnRegistrar.addActionListener(e ->
                new VentanaRegistroPedido()
        );

        btnListar.addActionListener(e ->
                new VentanaListaPedidos()
        );

        btnIniciarEntrega.addActionListener(e ->
                new VentanaListaPedidos(true)
        );

        //btnRegistrarRepartidor.addActionListener(e ->
        //        new VentanaRepartidor()
        //);

        btnRegistrarEntrega.addActionListener(e ->
                new VentanaRegistroEntrega()
        );

    }
}
