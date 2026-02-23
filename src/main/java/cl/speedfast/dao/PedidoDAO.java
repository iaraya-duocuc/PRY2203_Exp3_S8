package cl.speedfast.dao;

import cl.speedfast.config.AppConfig;
import cl.speedfast.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public void create(Pedido pedido) throws SQLException {

        String sql = "INSERT INTO pedidos (direccion, tipo, estado) VALUES (?, ?, ?)";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, pedido.getDireccion());
            ps.setString(2, pedido.getTipoPedido().name());
            ps.setString(3, pedido.getEstado().name());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    pedido.setIdPedido(rs.getInt(1));
                }
            }

        }
    }

    public List<Pedido> readAll() throws SQLException {

        List<Pedido> lista = new ArrayList<>();

        String sql =
                "SELECT p.id, p.direccion, p.tipo, p.estado, " +
                        "r.nombre AS repartidor_nombre " +
                        "FROM pedidos p " +
                        "LEFT JOIN entregas e ON p.id = e.id_pedido " +
                        "LEFT JOIN repartidores r ON e.id_repartidor = r.id";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Pedido pedido = mapPedido(rs);

                String repartidor = rs.getString("repartidor_nombre");
                pedido.setRepartidorAsignado(
                        repartidor != null ? repartidor : "No asignado"
                );

                lista.add(pedido);
            }

        }

        return lista;
    }

    public void update(Pedido pedido) throws SQLException {

        String sql = "UPDATE pedidos SET direccion = ?, tipo = ?, estado = ? WHERE id = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pedido.getDireccion());
            ps.setString(2, pedido.getTipoPedido().name());
            ps.setString(3, pedido.getEstado().name());
            ps.setInt(4, pedido.getIdPedido());

            ps.executeUpdate();

        }
    }

    public void updateEstado(int idPedido, AppConfig.EstadoPedido nuevoEstado) throws SQLException {

        String sql = "UPDATE pedidos SET estado = ? WHERE id = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado.name());
            ps.setInt(2, idPedido);

            ps.executeUpdate();

        }
    }

    public void delete(int idPedido) throws SQLException {

        String sql = "DELETE FROM pedidos WHERE id = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPedido);
            ps.executeUpdate();

        }
    }

    // mapeo customizado

    private Pedido mapPedido(ResultSet rs) throws SQLException {

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
                throw new IllegalArgumentException("Tipo inválido: " + tipo);
        }

        pedido.setIdPedido(id);
        pedido.setEstado(AppConfig.EstadoPedido.valueOf(estado));

        return pedido;
    }

    public Pedido findById(int idPedido) throws SQLException {

        String sql =
                "SELECT p.id, p.direccion, p.tipo, p.estado, " +
                        "r.nombre AS repartidor_nombre " +
                        "FROM pedidos p " +
                        "LEFT JOIN entregas e ON p.id = e.id_pedido " +
                        "LEFT JOIN repartidores r ON e.id_repartidor = r.id " +
                        "WHERE p.id = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idPedido);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Pedido pedido = mapPedido(rs);

                    String repartidor = rs.getString("repartidor_nombre");
                    pedido.setRepartidorAsignado(
                            repartidor != null ? repartidor : "No asignado"
                    );

                    return pedido;
                }
            }
        }

        return null;
    }

    public List<Pedido> readByFilters(AppConfig.TipoPedido tipo,
                                      AppConfig.EstadoPedido estado) throws SQLException {

        List<Pedido> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT p.id, p.direccion, p.tipo, p.estado, " +
                        "r.nombre AS repartidor_nombre " +
                        "FROM pedidos p " +
                        "LEFT JOIN entregas e ON p.id = e.id_pedido " +
                        "LEFT JOIN repartidores r ON e.id_repartidor = r.id " +
                        "WHERE 1=1 "
        );

        if (tipo != null) {
            sql.append(" AND p.tipo = ? ");
        }

        if (estado != null) {
            sql.append(" AND p.estado = ? ");
        }

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (tipo != null) {
                ps.setString(index++, tipo.name());
            }

            if (estado != null) {
                ps.setString(index++, estado.name());
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Pedido pedido = mapPedido(rs);

                    String repartidor = rs.getString("repartidor_nombre");
                    pedido.setRepartidorAsignado(
                            repartidor != null ? repartidor : "No asignado"
                    );

                    lista.add(pedido);
                }
            }
        }

        return lista;
    }

}
