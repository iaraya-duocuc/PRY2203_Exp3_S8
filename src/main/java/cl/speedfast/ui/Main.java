package cl.speedfast.ui;

import javax.swing.*;

/**
 * Clase principal de la aplicacion.
 */
public class Main {

    /**
     * Punto de entrada del programa.
     */
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new SistemaUI().setVisible(true);
        });

    }

}
