package cl.speedfast.dao;

import cl.speedfast.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Gestiona las operaciones CRUD de clientes.
public class ClienteDAO {

    public void create(Cliente cliente) {

        String sql = "INSERT INTO clientes (nombre, telefono, direccion) VALUES (?, ?, ?)";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getTelefono());
            ps.setString(3, cliente.getDireccion());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Cliente> readAll() {

        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT id, nombre, telefono, direccion FROM clientes";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setDireccion(rs.getString("direccion"));

                lista.add(cliente);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void update(Cliente cliente) {

        String sql = "UPDATE clientes SET nombre = ?, telefono = ?, direccion = ? WHERE id = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getTelefono());
            ps.setString(3, cliente.getDireccion());
            ps.setInt(4, cliente.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {

        String sql = "DELETE FROM clientes WHERE id = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
