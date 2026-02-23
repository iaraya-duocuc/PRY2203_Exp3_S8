package cl.speedfast.controller;

import cl.speedfast.config.AppConfig;
import cl.speedfast.dao.PedidoDAO;
import cl.speedfast.model.Pedido;
import cl.speedfast.model.ZonaDeCarga;

import java.sql.SQLException;
import java.util.List;

/**
 * Controlador que gestiona los pedidos.
 */
public class ControladorPedidos {

    private static final ZonaDeCarga zonaDeCarga = new ZonaDeCarga();
    private static final PedidoDAO pedidoDAO = new PedidoDAO();

    public static void agregarPedido(Pedido pedido) {
        try {
            pedidoDAO.create(pedido);
        } catch (SQLException e) {
            throw new RuntimeException("Error registrando pedido", e);
        }
    }

    public static void eliminarPedido(int id) {
        try {
            pedidoDAO.delete(id);
        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando pedido", e);
        }
    }

    public static List<Pedido> obtenerPedidos() {
        try {
            return pedidoDAO.readAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error obteniendo pedido", e);
        }
    }

    public static void actualizarPedido(Pedido pedido) {
        try {
            pedidoDAO.update(pedido);
        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando pedido", e);
        }
    }

    public static void actualizarEstadoPedido(int idPedido, AppConfig.EstadoPedido estado) {
        try {
            pedidoDAO.updateEstado(idPedido, estado);
        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando estado de pedido", e);
        }
    }

    public static ZonaDeCarga obtenerZonaDeCarga() {
        return zonaDeCarga;
    }

    public static void cargarPedidosPendientesEnZona() {
        zonaDeCarga.limpiar();
        List<Pedido> pedidos;

        try {
            pedidos = pedidoDAO.readAll();
        } catch (SQLException e) {
            throw new RuntimeException("Error cargando pedidos pendientes", e);
        }

        for (Pedido p : pedidos) {
            // Solo agregar si está pendiente y no tiene repartidor asignado
            if (p.getEstado() == AppConfig.EstadoPedido.PENDIENTE && !p.tieneRepartidorAsignado()) {
                zonaDeCarga.agregarPedido(p);
            }
        }
    }

    public static Pedido obtenerPedidoPorId(int idPedido) {
        try {
            return pedidoDAO.findById(idPedido);
        } catch (SQLException e) {
            throw new RuntimeException("Error obteniendo pedido por id", e);
        }
    }

}
