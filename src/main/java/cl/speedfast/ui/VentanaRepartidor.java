package cl.speedfast.ui;

import cl.speedfast.controller.ControladorRepartidores;

import javax.swing.*;
import java.awt.*;

public class VentanaRepartidor extends JFrame {

    private Integer idRepartidor; // null = registro, != null = edición
    private JTextField txtNombre;

    private JButton btnGuardar;
    private Runnable onSuccess;

    public VentanaRepartidor(Runnable onSuccess) {
        this(null, null, onSuccess);
    }

    public VentanaRepartidor(Integer idRepartidor,
                                     String nombreActual,
                                     Runnable onSuccess) {

        this.idRepartidor = idRepartidor;
        this.onSuccess = onSuccess;

        setTitle(idRepartidor == null ?
                "Registrar Repartidor" :
                "Editar Repartidor");

        setSize(300, 150);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2, 2, 5, 5));

        inicializarComponentes();

        if (nombreActual != null) {
            txtNombre.setText(nombreActual);
        }

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

            validarNombre(nombre);

            if (idRepartidor == null) {
                // REGISTRO
                ControladorRepartidores.registrarRepartidor(nombre);

                JOptionPane.showMessageDialog(this,
                        "Repartidor registrado correctamente");
            } else {
                // EDICIÓN
                ControladorRepartidores.actualizarRepartidor(idRepartidor, nombre);

                JOptionPane.showMessageDialog(this,
                        "Repartidor actualizado correctamente");
            }

            if (onSuccess != null) {
                onSuccess.run();
            }

            dispose();

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage());
        }
    }

    private void validarNombre(String nombre) throws Exception {
        if (nombre.isEmpty()) {
            throw new Exception("El nombre no puede estar vacío.");
        }

        int letras = 0;

        for (char c : nombre.toCharArray()) {
            if (Character.isLetter(c)) {
                letras++;
                continue;
            }

            if (Character.isWhitespace(c)) {
                continue;
            }

            throw new Exception("El nombre solo puede contener letras y espacios.");
        }

        if (letras < 3) {
            throw new Exception("El nombre debe tener al menos 3 letras.");
        }
    }



}
