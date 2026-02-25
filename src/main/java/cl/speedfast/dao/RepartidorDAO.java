package cl.speedfast.dao;

import cl.speedfast.model.Repartidor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// Gestiona las operaciones CRUD de repartidores.
public class RepartidorDAO {

    public void create(String nombre) throws SQLException {
        String sql = "INSERT INTO repartidores (nombre) VALUES (?)";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.executeUpdate();
        }
    }

    public List<Repartidor> readAll() throws SQLException {
        List<Repartidor> lista = new ArrayList<>();
        String sql = "SELECT * FROM repartidores";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Repartidor r = new Repartidor(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        null
                );
                lista.add(r);
            }
        }

        return lista;
    }

    public void update(int id, String nombre) throws SQLException {
        String sql = "UPDATE repartidores SET nombre = ? WHERE id = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM repartidores WHERE id = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }


}
