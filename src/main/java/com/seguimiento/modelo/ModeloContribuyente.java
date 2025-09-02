package com.seguimiento.modelo;

import com.seguimiento.bean.Contribuyente;
import java.sql.*;
import com.seguimiento.config.Conexion;
import java.util.ArrayList;
import java.util.List;

public class ModeloContribuyente {

    // Obtener todos los contribuyentes (modo tradicional, no paginado)
    public List<Contribuyente> obtenerTodos() {
        List<Contribuyente> lista = new ArrayList<>();
        String sql = "SELECT id_contribuyente, nom_contribuyente, cel_contribuyente, "
                + "correo_contribuyente, dir_contribuyente, estado_contribuyente FROM contribuyente";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Contribuyente c = new Contribuyente();
                c.setIdContribuyente(rs.getInt("id_contribuyente"));
                c.setNomContribuyente(rs.getString("nom_contribuyente"));
                c.setCelContribuyente(rs.getString("cel_contribuyente"));
                c.setCorreoContribuyente(rs.getString("correo_contribuyente"));
                c.setDirContribuyente(rs.getString("dir_contribuyente"));
                c.setEstadoContribuyente(rs.getString("estado_contribuyente"));
                lista.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Contar total de registros
    public int contarTotal() {
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

    // Contar registros filtrados
    public int contarFiltrados(String search) {
        String sql = "SELECT COUNT(*) FROM contribuyente WHERE "
                + "CAST(id_contribuyente AS TEXT) ILIKE ? OR "
                + "nom_contribuyente ILIKE ? OR "
                + "cel_contribuyente ILIKE ? OR "
                + "correo_contribuyente ILIKE ? OR "
                + "dir_contribuyente ILIKE ? OR "
                + "estado_contribuyente ILIKE ?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            String searchPattern = "%" + search + "%";
            for (int i = 1; i <= 6; i++) {
                ps.setString(i, searchPattern);
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

    // Listar con soporte para DataTables (server-side)
    public List<Contribuyente> listarContribuyentes(int start, int length, String search, String orderColumn, String orderDir) {
        List<Contribuyente> lista = new ArrayList<>();

        String baseQuery = "SELECT id_contribuyente, nom_contribuyente, cel_contribuyente, correo_contribuyente, "
                + "dir_contribuyente, estado_contribuyente FROM contribuyente";
        String where = "";
        if (search != null && !search.trim().isEmpty()) {
            where = " WHERE CAST(id_contribuyente AS TEXT) ILIKE ? OR "
                    + "nom_contribuyente ILIKE ? OR "
                    + "cel_contribuyente ILIKE ? OR "
                    + "correo_contribuyente ILIKE ? OR "
                    + "dir_contribuyente ILIKE ? OR "
                    + "estado_contribuyente ILIKE ?";
        }

        String orderBy = " ORDER BY " + (orderColumn != null ? orderColumn : "id_contribuyente")
                + " " + (orderDir != null && orderDir.equalsIgnoreCase("desc") ? "DESC" : "ASC");

        String limit = " LIMIT ? OFFSET ?";

        String sql = baseQuery + where + orderBy + limit;

        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            int paramIndex = 1;
            if (!where.isEmpty()) {
                String searchPattern = "%" + search + "%";
                for (int i = 1; i <= 6; i++) {
                    ps.setString(paramIndex++, searchPattern);
                }
            }
            ps.setInt(paramIndex++, length);
            ps.setInt(paramIndex, start);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Contribuyente c = new Contribuyente();
                    c.setIdContribuyente(rs.getInt("id_contribuyente"));
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

    // Insertar un nuevo contribuyente
    public boolean insertar(Contribuyente c) {
        String sql = "INSERT INTO contribuyente (nom_contribuyente, cel_contribuyente, correo_contribuyente, "
                + "dir_contribuyente, estado_contribuyente) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNomContribuyente());
            ps.setString(2, c.getCelContribuyente());
            ps.setString(3, c.getCorreoContribuyente());
            ps.setString(4, c.getDirContribuyente());
            ps.setString(5, c.getEstadoContribuyente());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Actualizar contribuyente
    public boolean actualizar(Contribuyente c) {
        String sql = "UPDATE contribuyente SET nom_contribuyente = ?, cel_contribuyente = ?, "
                + "correo_contribuyente = ?, dir_contribuyente = ?, estado_contribuyente = ? "
                + "WHERE id_contribuyente = ?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, c.getNomContribuyente());
            ps.setString(2, c.getCelContribuyente());
            ps.setString(3, c.getCorreoContribuyente());
            ps.setString(4, c.getDirContribuyente());
            ps.setString(5, c.getEstadoContribuyente());
            ps.setInt(6, c.getIdContribuyente());
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
        String sql = "SELECT * FROM contribuyente WHERE id_contribuyente = ?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idContribuyente);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    c = new Contribuyente();
                    c.setIdContribuyente(rs.getInt("id_contribuyente"));
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
}
