package com.seguimiento.modelo;

import com.seguimiento.bean.Usuario;
import com.seguimiento.bean.Profesion;
import com.seguimiento.bean.Rol;
import com.seguimiento.config.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ModeloUsuarios {

    /* =============================================
       MÉTODOS PARA DATATABLES (SERVER-SIDE)
    ============================================= */
// Listar usuarios con soporte DataTables
    public List<Usuario> listarUsuarios(int start, int length, String searchValue, String orderColumn, String orderDir) {
        List<Usuario> lista = new ArrayList<>();

        String[] columnas = {
            "u.id_usuario", "u.ced_usuario", "u.nom_usuario", "u.email_usuario",
            "p.nom_profesion", "r.nom_rol", "u.estado_usuario"
        };
        String columnaOrden = "u.id_usuario";
        if (orderColumn != null && !orderColumn.isEmpty()) {
            int colIndex = Integer.parseInt(orderColumn);
            if (colIndex >= 0 && colIndex < columnas.length) {
                columnaOrden = columnas[colIndex];
            }
        }

        String sql = "SELECT u.id_usuario, u.ced_usuario, u.nom_usuario, u.tel_usuario, u.cel_usuario, "
                + "u.sexo_usuario, u.dir_usuario, u.nac_usuario, u.email_usuario, u.estado_usuario, "
                + "u.login_usuario, u.password_usuario, u.tp_usuario, u.foto_usuario, "
                + "p.id_profesion, p.nom_profesion, r.id_rol, r.nom_rol "
                + "FROM usuarios u "
                + "LEFT JOIN profesiones p ON u.cod_profesion = p.id_profesion "
                + "LEFT JOIN roles r ON u.cod_rol = r.id_rol "
                + "WHERE (CAST(u.id_usuario AS TEXT) ILIKE ? OR u.ced_usuario ILIKE ? OR u.nom_usuario ILIKE ? "
                + "OR u.email_usuario ILIKE ? OR p.nom_profesion ILIKE ? OR r.nom_rol ILIKE ?) "
                + "ORDER BY " + columnaOrden + " " + orderDir + " LIMIT ? OFFSET ?";

        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            String filtro = "%" + searchValue + "%";
            for (int i = 1; i <= 6; i++) {
                ps.setString(i, filtro);
            }
            ps.setInt(7, length);
            ps.setInt(8, start);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Usuario u = new Usuario();
                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setCedUsuario(rs.getString("ced_usuario"));
                    u.setNomUsuario(rs.getString("nom_usuario"));
                    u.setTelUsuario(rs.getString("tel_usuario"));
                    u.setCelUsuario(rs.getString("cel_usuario"));
                    u.setSexoUsuario(rs.getString("sexo_usuario"));
                    u.setDirUsuario(rs.getString("dir_usuario"));
                    u.setNacUsuario(rs.getString("nac_usuario"));
                    u.setEmailUsuario(rs.getString("email_usuario"));
                    u.setEstadoUsuario(rs.getString("estado_usuario"));
                    u.setLoginUsuario(rs.getString("login_usuario"));
                    u.setPasswordUsuario(rs.getString("password_usuario"));
                    u.setTpUsuario(rs.getString("tp_usuario"));
                    u.setFotoUsuario(rs.getString("foto_usuario"));

                    // Validación de profesión (puede ser NULL)
                    Integer idProfesion = rs.getObject("id_profesion", Integer.class);
                    if (idProfesion != null) {
                        Profesion profesion = new Profesion();
                        profesion.setIdProfesion(idProfesion);
                        profesion.setNomProfesion(rs.getString("nom_profesion"));
                        u.setProfesion(profesion);
                    } else {
                        u.setProfesion(null);
                    }

                    // Validación de rol (por si no existe)
                    Integer idRol = rs.getObject("id_rol", Integer.class);
                    if (idRol != null) {
                        Rol rol = new Rol();
                        rol.setIdRol(idRol);
                        rol.setNomRol(rs.getString("nom_rol"));
                        u.setRol(rol);
                    } else {
                        u.setRol(null);
                    }

                    lista.add(u);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Contar total de usuarios
    public int contarTotalUsuarios() {
        String sql = "SELECT COUNT(*) FROM usuarios";
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

    // Contar usuarios filtrados
    public int contarUsuariosFiltrados(String searchValue) {
        String sql = "SELECT COUNT(*) FROM usuarios u "
                + "LEFT JOIN profesiones p ON u.cod_profesion = p.id_profesion "
                + "LEFT JOIN roles r ON u.cod_rol = r.id_rol "
                + "WHERE (CAST(u.id_usuario AS TEXT) ILIKE ? OR u.ced_usuario ILIKE ? OR u.nom_usuario ILIKE ? "
                + "OR u.email_usuario ILIKE ? OR p.nom_profesion ILIKE ? OR r.nom_rol ILIKE ?)";

        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            String filtro = "%" + searchValue + "%";
            for (int i = 1; i <= 6; i++) {
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
    // Insertar usuario
    public boolean insertar(Usuario u) {
        String sql = "INSERT INTO usuarios (ced_usuario, nom_usuario, tel_usuario, cel_usuario, sexo_usuario, dir_usuario, nac_usuario, email_usuario, estado_usuario, login_usuario, password_usuario, tp_usuario, foto_usuario, cod_profesion, cod_rol) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getCedUsuario());
            ps.setString(2, u.getNomUsuario());
            ps.setString(3, u.getTelUsuario());
            ps.setString(4, u.getCelUsuario());
            ps.setString(5, u.getSexoUsuario());
            ps.setString(6, u.getDirUsuario());
            ps.setString(7, u.getNacUsuario());
            ps.setString(8, u.getEmailUsuario());
            ps.setString(9, u.getEstadoUsuario());
            ps.setString(10, u.getLoginUsuario());
            ps.setString(11, u.getPasswordUsuario());
            ps.setString(12, u.getTpUsuario());
            ps.setString(13, u.getFotoUsuario());

            // Profesión opcional
            if (u.getProfesion() != null && u.getProfesion().getIdProfesion() != 0) {
                ps.setInt(14, u.getProfesion().getIdProfesion());
            } else {
                ps.setNull(14, java.sql.Types.INTEGER);
            }

            // Rol (obligatorio, asumo que siempre tiene valor)
            ps.setInt(15, u.getRol().getIdRol());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

// Actualizar usuario
    public boolean actualizar(Usuario u) {
        String sql = "UPDATE usuarios SET ced_usuario = ?, nom_usuario = ?, tel_usuario = ?, cel_usuario = ?, sexo_usuario = ?, dir_usuario = ?, nac_usuario = ?, email_usuario = ?, estado_usuario = ?, login_usuario = ?, password_usuario = ?, tp_usuario = ?, foto_usuario = ?, cod_profesion = ?, cod_rol = ? "
                + "WHERE id_usuario = ?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, u.getCedUsuario());
            ps.setString(2, u.getNomUsuario());
            ps.setString(3, u.getTelUsuario());
            ps.setString(4, u.getCelUsuario());
            ps.setString(5, u.getSexoUsuario());
            ps.setString(6, u.getDirUsuario());
            ps.setString(7, u.getNacUsuario());
            ps.setString(8, u.getEmailUsuario());
            ps.setString(9, u.getEstadoUsuario());
            ps.setString(10, u.getLoginUsuario());
            ps.setString(11, u.getPasswordUsuario());
            ps.setString(12, u.getTpUsuario());
            ps.setString(13, u.getFotoUsuario());

            // Profesión opcional
            if (u.getProfesion() != null && u.getProfesion().getIdProfesion() != 0) {
                ps.setInt(14, u.getProfesion().getIdProfesion());
            } else {
                ps.setNull(14, java.sql.Types.INTEGER);
            }

            // Rol (obligatorio, asumo que siempre tiene valor)
            ps.setInt(15, u.getRol().getIdRol());

            // ID del usuario (WHERE)
            ps.setInt(16, u.getIdUsuario());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar usuario
    public boolean eliminar(int idUsuario) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtener usuario por ID
    public Usuario obtenerPorId(int idUsuario) {
        Usuario u = null;
        String sql = "SELECT u.id_usuario, u.ced_usuario, u.nom_usuario, u.tel_usuario, u.cel_usuario, "
                + "u.sexo_usuario, u.dir_usuario, u.nac_usuario, u.email_usuario, u.estado_usuario, "
                + "u.login_usuario, u.password_usuario, u.tp_usuario, u.foto_usuario, "
                + "p.id_profesion, p.nom_profesion, r.id_rol, r.nom_rol "
                + "FROM usuarios u "
                + "LEFT JOIN profesiones p ON u.cod_profesion = p.id_profesion "
                + "LEFT JOIN roles r ON u.cod_rol = r.id_rol "
                + "WHERE u.id_usuario = ?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    u = new Usuario();
                    u.setIdUsuario(rs.getInt("id_usuario"));
                    u.setCedUsuario(rs.getString("ced_usuario"));
                    u.setNomUsuario(rs.getString("nom_usuario"));
                    u.setTelUsuario(rs.getString("tel_usuario"));
                    u.setCelUsuario(rs.getString("cel_usuario"));
                    u.setSexoUsuario(rs.getString("sexo_usuario"));
                    u.setDirUsuario(rs.getString("dir_usuario"));
                    u.setNacUsuario(rs.getString("nac_usuario"));
                    u.setEmailUsuario(rs.getString("email_usuario"));
                    u.setEstadoUsuario(rs.getString("estado_usuario"));
                    u.setLoginUsuario(rs.getString("login_usuario"));
                    u.setPasswordUsuario(rs.getString("password_usuario"));
                    u.setTpUsuario(rs.getString("tp_usuario"));
                    u.setFotoUsuario(rs.getString("foto_usuario"));

                    Profesion profesion = new Profesion();
                    profesion.setIdProfesion(rs.getInt("id_profesion"));
                    profesion.setNomProfesion(rs.getString("nom_profesion"));
                    u.setProfesion(profesion);

                    Rol rol = new Rol();
                    rol.setIdRol(rs.getInt("id_rol"));
                    rol.setNomRol(rs.getString("nom_rol"));
                    u.setRol(rol);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return u;
    }

    public List<Profesion> listarProfesiones() {
        List<Profesion> lista = new ArrayList<>();
        String sql = "SELECT id_profesion, nom_profesion FROM profesiones ORDER BY nom_profesion ASC";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Profesion p = new Profesion();
                p.setIdProfesion(rs.getInt("id_profesion"));
                p.setNomProfesion(rs.getString("nom_profesion"));
                lista.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<Rol> listarRoles() {
        List<Rol> lista = new ArrayList<>();
        String sql = "SELECT id_rol, nom_rol FROM roles ORDER BY nom_rol ASC";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Rol r = new Rol();
                r.setIdRol(rs.getInt("id_rol"));
                r.setNomRol(rs.getString("nom_rol"));
                lista.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

}
