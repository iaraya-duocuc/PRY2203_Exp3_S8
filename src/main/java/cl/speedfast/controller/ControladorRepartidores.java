package cl.speedfast.controller;

import cl.speedfast.dao.RepartidorDAO;
import cl.speedfast.model.Repartidor;

import java.sql.SQLException;
import java.util.List;

/**
 * Controlador que gestiona repartidores.
 */
public class ControladorRepartidores {
    private static final RepartidorDAO repartidorDAO = new RepartidorDAO();

    public static void registrarRepartidor(String nombre) {
        validarNombre(nombre);
        try {
            repartidorDAO.create(nombre);
        } catch (SQLException e) {
            throw new RuntimeException("Error registrando repartidor", e);
        }
    }

    public static void actualizarRepartidor(int id, String nombre) {
        validarNombre(nombre);
        try {
            repartidorDAO.update(id, nombre);
        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando repartidor", e);
        }
    }

    public static void eliminarRepartidor(int id) {
        try {
            repartidorDAO.delete(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando repartidor");
        }
    }

    public static List<Repartidor> listarRepartidores() {
        try {
            return repartidorDAO.readAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error listando repartidores", e);
        }
    }

    private static void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio.");
        }

        int letras = 0;
        String nombreLimpio = nombre.trim();

        for (char c : nombreLimpio.toCharArray()) {
            if (Character.isLetter(c)) {
                letras++;
                continue;
            }

            if (Character.isWhitespace(c)) {
                continue;
            }

            throw new IllegalArgumentException("El nombre solo puede contener letras y espacios.");
        }

        if (letras < 3) {
            throw new IllegalArgumentException("El nombre debe tener al menos 3 letras.");
        }
    }
}
