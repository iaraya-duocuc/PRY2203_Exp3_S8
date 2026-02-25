package cl.speedfast.dao;

import cl.speedfast.config.AppConfig;
import cl.speedfast.model.Entrega;
import cl.speedfast.model.dto.EntregaDTO;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

// Gestiona las operaciones CRUD y filtros de entregas.
public class EntregaDAO {

    public void create(int idPedido, int idRepartidor)  throws SQLException {

        String sql = "INSERT INTO entregas (id_pedido, id_repartidor, fecha, hora) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, idPedido);
            ps.setInt(2, idRepartidor);
            ps.setDate(3, Date.valueOf(LocalDate.now()));
            ps.setTime(4, Time.valueOf(LocalTime.now()));


            ps.executeUpdate();

            //try (ResultSet rs = ps.getGeneratedKeys()) {
            //    if (rs.next()) {
            //        entrega.setId(rs.getInt(1));
            //    }
            //}

        }
    }

    public List<Entrega> readAll()  throws SQLException {

        List<Entrega> lista = new ArrayList<>();

        String sql = "SELECT * FROM entregas";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapEntrega(rs));
            }

        }

        return lista;
    }

    public List<EntregaDTO> readByFilters(AppConfig.TipoPedido tipo,
                                          Integer idRepartidor) throws SQLException {

        List<EntregaDTO> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT e.id AS entrega_id, " +
                        "       p.id AS pedido_id, " +
                        "       e.id_repartidor, " +
                        "       p.tipo, " +
                        "       p.direccion, " +
                        "       r.nombre, " +
                        "       e.fecha, " +
                        "       e.hora " +
                        "FROM entregas e " +
                        "JOIN pedidos p ON e.id_pedido = p.id " +
                        "JOIN repartidores r ON e.id_repartidor = r.id " +
                        "WHERE 1=1 "
        );

        if (tipo != null) {
            sql.append(" AND p.tipo = ? ");
        }

        if (idRepartidor != 0) {
            sql.append(" AND r.id = ? ");
        }

        sql.append(" ORDER BY e.id ");

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;

            if (tipo != null) {
                ps.setString(index++, tipo.name());
            }

            if (idRepartidor != 0) {
                ps.setInt(index, idRepartidor);
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    lista.add(
                            new EntregaDTO(
                                    rs.getInt("entrega_id"),
                                    rs.getInt("pedido_id"),
                                    rs.getInt("id_repartidor"),
                                    rs.getString("tipo"),
                                    rs.getString("nombre"),
                                    rs.getString("direccion"),
                                    rs.getDate("fecha").toLocalDate(),
                                    rs.getTime("hora").toLocalTime()
                            )
                    );
                }
            }
        }

        return lista;
    }

    /*public List<EntregaDTO> readAllView() throws SQLException {

        List<EntregaDTO> lista = new ArrayList<>();

        String sql = """
        SELECT e.id AS entrega_id,
               p.id AS pedido_id,
               p.tipo,
               p.direccion,
               r.nombre,
               e.fecha,
               e.hora
        FROM entregas e
        JOIN pedidos p ON e.id_pedido = p.id
        JOIN repartidores r ON e.id_repartidor = r.id
        ORDER BY e.id
    """;

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                lista.add(
                        new EntregaDTO(
                                rs.getInt("entrega_id"),
                                rs.getInt("pedido_id"),
                                rs.getString("tipo"),       // tipoPedido
                                rs.getString("nombre"),     // nombreRepartidor
                                rs.getString("direccion"),  // direccionPedido
                                rs.getDate("fecha").toLocalDate(),
                                rs.getTime("hora").toLocalTime()
                        )
                );
            }
        }

        return lista;
    }*/

    public void update(Entrega entrega) throws SQLException {

        String sql = "UPDATE entregas " +
                "SET id_pedido = ?, id_repartidor = ?, fecha = ?, hora = ? " +
                "WHERE id = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, entrega.getIdPedido());
            ps.setInt(2, entrega.getIdRepartidor());
            ps.setDate(3, Date.valueOf(entrega.getFecha()));
            ps.setTime(4, Time.valueOf(entrega.getHora()));
            ps.setInt(5, entrega.getId());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("No Entrega found with id: " + entrega.getId());
            }
        }
    }

    public void updateRepartidor(int idEntrega, int idRepartidor) throws SQLException {

        String sql = "UPDATE entregas SET id_repartidor = ? WHERE id = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idRepartidor);
            ps.setInt(2, idEntrega);

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("No Entrega found with id: " + idEntrega);
            }
        }
    }

    public void delete(int id) throws SQLException {

        String sql = "DELETE FROM entregas WHERE id = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        }
    }

    // mapeo customizado

    private Entrega mapEntrega(ResultSet rs) throws SQLException {

        int id = rs.getInt("id");
        int idPedido = rs.getInt("id_pedido");
        int idRepartidor = rs.getInt("id_repartidor");

        LocalDate fecha = rs.getDate("fecha").toLocalDate();
        LocalTime hora = rs.getTime("hora").toLocalTime();

        Entrega entrega = new Entrega(idPedido, idRepartidor, fecha, hora);
        entrega.setId(id);

        return entrega;
    }

}
