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
        try {
            repartidorDAO.create(nombre);
        } catch (SQLException e) {
            throw new RuntimeException("Error registrando repartidor", e);
        }
    }

    public static void actualizarRepartidor(int id, String nombre) {
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
}
