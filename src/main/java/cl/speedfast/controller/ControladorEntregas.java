package cl.speedfast.controller;

import cl.speedfast.config.AppConfig;
import cl.speedfast.dao.EntregaDAO;
import cl.speedfast.model.Entrega;
import cl.speedfast.model.dto.EntregaDTO;

import java.sql.SQLException;
import java.util.List;

/**
 * Controlador que gestiona entregas.
 */
public class ControladorEntregas {

    private static final EntregaDAO entregaDAO = new EntregaDAO();

    public static void registrarEntrega(int idPedido, int idRepartidor) {
        try {
            entregaDAO.create(idPedido, idRepartidor);
        } catch (SQLException e) {
            throw new RuntimeException("Error registrando entrega", e);
        }
    }

    public void actualizarEntrega(Entrega entrega) {
        try {
            entregaDAO.update(entrega);
        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando entrega", e);
        }
    }

    public static void eliminarEntrega(int id) {
        try {
            entregaDAO.delete(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando entrega", e);
        }
    }

    public static List<Entrega> listarEntregas() {
        try {
            return entregaDAO.readAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error listando entregas", e);
        }
    }

    public static List<EntregaDTO> listarEntregasFiltradas(AppConfig.TipoPedido tipo,
                                                        Integer idRepartidor) {
        try {
            return entregaDAO.readByFilters(tipo, idRepartidor);
        } catch (SQLException e) {
            throw new RuntimeException("Error listando entregas", e);
        }
    }

}
