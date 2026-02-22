package cl.speedfast.controller;

import cl.speedfast.config.AppConfig;
import cl.speedfast.dao.EntregaDAO;
import cl.speedfast.dao.PedidoDAO;
import cl.speedfast.dao.RepartidorDAO;
import cl.speedfast.model.Pedido;
import cl.speedfast.model.Repartidor;
import cl.speedfast.model.ZonaDeCarga;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador central que gestiona los pedidos en memoria.
 */
public class ControladorPedidos {

    private static final ZonaDeCarga zonaDeCarga = new ZonaDeCarga();

    private static final RepartidorDAO repartidorDAO = new RepartidorDAO();
    private static final PedidoDAO pedidoDAO = new PedidoDAO();
    private static final EntregaDAO entregaDAO = new EntregaDAO();

    public static void agregarPedido(Pedido pedido) {
        pedidoDAO.guardar(pedido);
    }

    public static List<Pedido> obtenerPedidos() {
        return pedidoDAO.listarTodos();
    }

    public static void actualizarEstadoPedido(int idPedido, AppConfig.EstadoPedido estado) {
        pedidoDAO.actualizarEstado(idPedido, estado.name());
    }

    public static ZonaDeCarga obtenerZonaDeCarga() {
        return zonaDeCarga;
    }

    public static void cargarPedidosPendientesEnZona() {
        zonaDeCarga.limpiar();
        List<Pedido> pedidos = pedidoDAO.listarTodos();

        for (Pedido p : pedidos) {
            // Solo agregar si está pendiente y no tiene repartidor asignado
            if (p.getEstado() == AppConfig.EstadoPedido.PENDIENTE && !p.tieneRepartidorAsignado()) {
                zonaDeCarga.agregarPedido(p);
            }
        }
    }

    public static void registrarRepartidor(String nombre) {
        repartidorDAO.guardar(nombre);
    }

    public static List<Repartidor> obtenerRepartidores() {
        return repartidorDAO.listarTodos();
    }

    public static void registrarEntrega(int idPedido, int idRepartidor) {
        entregaDAO.guardar(idPedido, idRepartidor);
    }



}
