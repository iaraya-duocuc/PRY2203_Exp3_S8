package cl.speedfast.ui;

import javax.swing.*;
import java.awt.*;

public class SistemaUI extends JFrame {

    public SistemaUI() {

        setTitle("Sistema de Gestión - SpeedFast");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTabbedPane tabs = new JTabbedPane();

        PedidosPanel pedidosPanel = new PedidosPanel();
        EntregasPanel entregasPanel = new EntregasPanel();
        RepartidoresPanel repartidoresPanel = new RepartidoresPanel();

        tabs.addTab("Pedidos", pedidosPanel);
        tabs.addTab("Entregas", entregasPanel);
        tabs.addTab("Repartidores", repartidoresPanel);

        tabs.addChangeListener(e -> {

            int index = tabs.getSelectedIndex();

            if (index == 0) {
                pedidosPanel.cargarPedidos();
            } else if (index == 1) {
                entregasPanel.cargarEntregas();
                entregasPanel.cargarRepartidores();
            } else if (index == 2) {
                repartidoresPanel.cargarRepartidores();
            }
        });

        pedidosPanel.cargarPedidos();
        add(tabs);
    }

}
