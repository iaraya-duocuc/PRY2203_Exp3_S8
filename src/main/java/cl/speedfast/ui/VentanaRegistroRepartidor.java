package cl.speedfast.ui;

import cl.speedfast.controller.ControladorPedidos;

import javax.swing.*;
import java.awt.*;

public class VentanaRegistroRepartidor extends JFrame {

    private JTextField txtNombre;
    private JButton btnGuardar;

    public VentanaRegistroRepartidor() {

        setTitle("Registrar Repartidor");
        setSize(300, 150);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2, 2, 5, 5));

        inicializarComponentes();

        setVisible(true);
    }

    private void inicializarComponentes() {

        add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        add(txtNombre);

        btnGuardar = new JButton("Guardar");
        add(new JLabel());
        add(btnGuardar);

        btnGuardar.addActionListener(e -> guardarRepartidor());
    }

    private void guardarRepartidor() {

        try {

            String nombre = txtNombre.getText().trim();

            if (nombre.isEmpty()) {
                throw new Exception("El nombre no puede estar vacío");
            }

            ControladorPedidos.registrarRepartidor(nombre);

            JOptionPane.showMessageDialog(this,
                    "Repartidor registrado correctamente");

            dispose();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage());
        }
    }
}
