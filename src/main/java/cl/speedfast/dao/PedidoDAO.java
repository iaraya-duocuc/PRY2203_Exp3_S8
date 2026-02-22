package cl.speedfast.dao;

import cl.speedfast.config.AppConfig;
import cl.speedfast.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public void guardar(Pedido pedido) {

        String sql = "INSERT INTO pedido (direccion, tipo, estado) VALUES (?, ?, ?)";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, pedido.getDireccion());
            ps.setString(2, pedido.getTipoPedido().name()); // COMIDA | ENCOMIENDA | EXPRESS
            ps.setString(3, pedido.getEstado().name());     // PENDIENTE | EN_REPARTO | ENTREGADO

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                pedido.setIdPedido(rs.getInt(1)); // IMPORTANTE
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Pedido> listarTodos() {

        List<Pedido> lista = new ArrayList<>();

        String sql = "SELECT p.id, p.direccion, p.tipo, p.estado, r.nombre AS repartidor " +
                "FROM pedido p " +
                "LEFT JOIN entrega e ON p.id = e.id_pedido " +
                "LEFT JOIN repartidor r ON e.id_repartidor = r.id";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                int id = rs.getInt("id");
                String direccion = rs.getString("direccion");
                String tipo = rs.getString("tipo");
                String estado = rs.getString("estado");

                Pedido pedido;

                switch (tipo) {
                    case "COMIDA":
                        pedido = new PedidoComida(direccion, true, 0);
                        break;
                    case "ENCOMIENDA":
                        pedido = new PedidoEncomienda(direccion, 5, true, 0);
                        break;
                    case "EXPRESS":
                        pedido = new PedidoExpress(direccion, true, 0);
                        break;
                    default:
                        continue;
                }

                pedido.setIdPedido(id);
                pedido.setEstado(AppConfig.EstadoPedido.valueOf(estado));
                String repartidor = rs.getString("repartidor");
                pedido.setRepartidorAsignado(repartidor != null ? repartidor : "No asignado");
                lista.add(pedido);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void actualizarEstado(int idPedido, String nuevoEstado) {

        String sql = "UPDATE pedido SET estado = ? WHERE id = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, idPedido);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
