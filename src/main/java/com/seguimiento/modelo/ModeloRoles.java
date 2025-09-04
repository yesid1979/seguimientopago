package com.seguimiento.modelo;

import com.seguimiento.bean.Rol;
import com.seguimiento.config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModeloRoles {

    /* =============================================
       MÉTODOS PARA DATATABLES (SERVER-SIDE)
    ============================================= */
    // Listar roles con soporte DataTables
    public List<Rol> listarRoles(int start, int length, String searchValue, String orderColumn, String orderDir) {
        List<Rol> lista = new ArrayList<>();
        String[] columnas = {
            "r.id_rol", "r.nom_rol", "r.desc_rol", "r.estado_rol"
        };
        String columnaOrden = "r.id_rol";
        if (orderColumn != null && !orderColumn.isEmpty()) {
            int colIndex = Integer.parseInt(orderColumn);
            if (colIndex >= 0 && colIndex < columnas.length) {
                columnaOrden = columnas[colIndex];
            }
        }
        String sql = "SELECT r.id_rol, r.nom_rol, r.desc_rol, r.estado_rol "
                + "FROM roles r "
                + "WHERE (CAST(r.id_rol AS TEXT) ILIKE ? OR r.nom_rol ILIKE ? OR r.desc_rol ILIKE ? OR r.estado_rol ILIKE ?) "
                + "ORDER BY " + columnaOrden + " " + orderDir + " LIMIT ? OFFSET ?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            String filtro = "%" + searchValue + "%";
            for (int i = 1; i <= 4; i++) {
                ps.setString(i, filtro);
            }
            ps.setInt(5, length);
            ps.setInt(6, start);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Rol r = new Rol();
                    r.setIdRol(rs.getInt("id_rol"));
                    r.setNomRol(rs.getString("nom_rol"));
                    r.setDescRol(rs.getString("desc_rol"));
                    r.setEstadoRol(rs.getString("estado_rol"));
                    lista.add(r);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Contar total de roles
    public int contarTotalRoles() {
        String sql = "SELECT COUNT(*) FROM roles";
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

    // Contar roles filtrados
    public int contarRolesFiltrados(String searchValue) {
        String sql = "SELECT COUNT(*) FROM roles r "
                + "WHERE (CAST(r.id_rol AS TEXT) ILIKE ? OR r.nom_rol ILIKE ? OR r.desc_rol ILIKE ? OR r.estado_rol ILIKE ?)";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            String filtro = "%" + searchValue + "%";
            for (int i = 1; i <= 4; i++) {
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
    // Insertar rol
    public boolean insertar(Rol r) {
        String sql = "INSERT INTO roles (nom_rol, desc_rol, estado_rol) VALUES (?, ?, ?)";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, r.getNomRol());
            ps.setString(2, r.getDescRol());
            ps.setString(3, r.getEstadoRol());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Actualizar rol
    public boolean actualizar(Rol r) {
        String sql = "UPDATE roles SET nom_rol = ?, desc_rol = ?, estado_rol = ? WHERE id_rol = ?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, r.getNomRol());
            ps.setString(2, r.getDescRol());
            ps.setString(3, r.getEstadoRol());
            ps.setInt(4, r.getIdRol());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar rol
    public boolean eliminar(int idRol) {
        String sql = "DELETE FROM roles WHERE id_rol = ?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idRol);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtener rol por ID
    public Rol obtenerPorId(int idRol) {
        Rol r = null;
        String sql = "SELECT id_rol, nom_rol, desc_rol, estado_rol FROM roles WHERE id_rol = ?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idRol);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    r = new Rol();
                    r.setIdRol(rs.getInt("id_rol"));
                    r.setNomRol(rs.getString("nom_rol"));
                    r.setDescRol(rs.getString("desc_rol"));
                    r.setEstadoRol(rs.getString("estado_rol"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return r;
    }
}
