package cl.speedfast.dao;

import cl.speedfast.model.Entrega;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
