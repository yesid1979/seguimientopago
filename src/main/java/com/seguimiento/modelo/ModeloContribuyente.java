package com.seguimiento.modelo;

import com.seguimiento.bean.Contribuyente;
import java.sql.*;
import com.seguimiento.config.Conexion;
import java.util.ArrayList;
import java.util.List;

public class ModeloContribuyente {

    /* =============================================
       MÉTODOS PARA DATATABLES (SERVER-SIDE)
    ============================================= */
    // Listar contribuyentes con soporte DataTables
    public List<Contribuyente> listarContribuyentes(int start, int length, String searchValue, String orderColumn, String orderDir) {
        List<Contribuyente> lista = new ArrayList<>();
        String[] columnas = {
            "c.id_contribuyente", "c.ced_contribuyente", "c.nom_contribuyente", "c.cel_contribuyente",
            "c.correo_contribuyente", "c.dir_contribuyente", "c.estado_contribuyente"
        };
        String columnaOrden = "c.id_contribuyente";
        if (orderColumn != null && !orderColumn.isEmpty()) {
            int colIndex = Integer.parseInt(orderColumn);
            if (colIndex >= 0 && colIndex < columnas.length) {
                columnaOrden = columnas[colIndex];
            }
        }
        String sql = "SELECT c.id_contribuyente, c.ced_contribuyente, c.nom_contribuyente, "
                + "c.cel_contribuyente, c.correo_contribuyente, c.dir_contribuyente, c.estado_contribuyente "
                + "FROM contribuyente c "
                + "WHERE (CAST(c.id_contribuyente AS TEXT) ILIKE ? OR c.ced_contribuyente ILIKE ? OR c.nom_contribuyente ILIKE ? "
                + "OR c.cel_contribuyente ILIKE ? OR c.correo_contribuyente ILIKE ? OR c.dir_contribuyente ILIKE ? OR c.estado_contribuyente ILIKE ?) "
                + "ORDER BY " + columnaOrden + " " + orderDir + " LIMIT ? OFFSET ?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            String filtro = "%" + searchValue + "%";
            for (int i = 1; i <= 7; i++) {
                ps.setString(i, filtro);
            }
            ps.setInt(8, length);
            ps.setInt(9, start);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Contribuyente c = new Contribuyente();
                    c.setIdContribuyente(rs.getInt("id_contribuyente"));
                    c.setCedContribuyente(rs.getString("ced_contribuyente"));
                    c.setNomContribuyente(rs.getString("nom_contribuyente"));
                    c.setCelContribuyente(rs.getString("cel_contribuyente"));
                    c.setCorreoContribuyente(rs.getString("correo_contribuyente"));
                    c.setDirContribuyente(rs.getString("dir_contribuyente"));
                    c.setEstadoContribuyente(rs.getString("estado_contribuyente"));
                    lista.add(c);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Contar total de contribuyentes
    public int contarTotalContribuyentes() {
        String sql = "SELECT COUNT(*) FROM contribuyente";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Contar contribuyentes filtrados
    public int contarContribuyentesFiltrados(String searchValue) {
        String sql = "SELECT COUNT(*) FROM contribuyente c "
                + "WHERE (CAST(c.id_contribuyente AS TEXT) ILIKE ? OR c.ced_contribuyente ILIKE ? OR c.nom_contribuyente ILIKE ? "
                + "OR c.cel_contribuyente ILIKE ? OR c.correo_contribuyente ILIKE ? OR c.dir_contribuyente ILIKE ? OR c.estado_contribuyente ILIKE ?)";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            String filtro = "%" + searchValue + "%";
            for (int i = 1; i <= 7; i++) {
                ps.setString(i, filtro);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /* =============================================
       CRUD BÁSICO
    ============================================= */
    // Insertar contribuyente
    public boolean insertar(Contribuyente c) {
        String sql = "INSERT INTO contribuyente (ced_contribuyente, nom_contribuyente, cel_contribuyente, correo_contribuyente, dir_contribuyente, estado_contribuyente) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getCedContribuyente());
            ps.setString(2, c.getNomContribuyente());
            ps.setString(3, c.getCelContribuyente());
            ps.setString(4, c.getCorreoContribuyente());
            ps.setString(5, c.getDirContribuyente());
            ps.setString(6, c.getEstadoContribuyente());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Actualizar contribuyente
    public boolean actualizar(Contribuyente c) {
        String sql = "UPDATE contribuyente SET ced_contribuyente = ?, nom_contribuyente = ?, "
                + "cel_contribuyente = ?, correo_contribuyente = ?, dir_contribuyente = ?, estado_contribuyente = ? "
                + "WHERE id_contribuyente = ?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getCedContribuyente());
            ps.setString(2, c.getNomContribuyente());
            ps.setString(3, c.getCelContribuyente());
            ps.setString(4, c.getCorreoContribuyente());
            ps.setString(5, c.getDirContribuyente());
            ps.setString(6, c.getEstadoContribuyente());
            ps.setInt(7, c.getIdContribuyente());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar contribuyente
    public boolean eliminar(int idContribuyente) {
        String sql = "DELETE FROM contribuyente WHERE id_contribuyente = ?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idContribuyente);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtener contribuyente por ID
    public Contribuyente obtenerPorId(int idContribuyente) {
        Contribuyente c = null;
        String sql = "SELECT id_contribuyente, ced_contribuyente, nom_contribuyente, "
                + "cel_contribuyente, correo_contribuyente, dir_contribuyente, estado_contribuyente "
                + "FROM contribuyente WHERE id_contribuyente = ?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idContribuyente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    c = new Contribuyente();
                    c.setIdContribuyente(rs.getInt("id_contribuyente"));
                    c.setCedContribuyente(rs.getString("ced_contribuyente"));
                    c.setNomContribuyente(rs.getString("nom_contribuyente"));
                    c.setCelContribuyente(rs.getString("cel_contribuyente"));
                    c.setCorreoContribuyente(rs.getString("correo_contribuyente"));
                    c.setDirContribuyente(rs.getString("dir_contribuyente"));
                    c.setEstadoContribuyente(rs.getString("estado_contribuyente"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    public Contribuyente buscarPorCedula(String cedula) {
        String sql = "SELECT * FROM contribuyente WHERE ced_contribuyente = ?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, cedula);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Contribuyente c = new Contribuyente();
                    c.setIdContribuyente(rs.getInt("id_contribuyente"));
                    c.setCedContribuyente(rs.getString("ced_contribuyente"));
                    c.setNomContribuyente(rs.getString("nom_contribuyente"));
                    // ... otros campos si son necesarios
                    return c;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
