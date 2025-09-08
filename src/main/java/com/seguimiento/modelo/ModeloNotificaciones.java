package com.seguimiento.modelo;

import com.seguimiento.bean.Notificacion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.seguimiento.config.Conexion;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ModeloNotificaciones {

    /* =============================================
       MÉTODOS PARA DATATABLES (SERVER-SIDE)
    ============================================= */
    // Listar notificaciones por propiedad
    public List<Notificacion> listarNotificaciones(int start, int length, String searchValue, String orderColumn, String orderDir, int codPredio) {
        List<Notificacion> notificaciones = new ArrayList<>();
        String[] columnas = {"n.id_notificacion", "n.fecha_notificacion", "n.hora_notificacion", "n.tipo_notificacion",
            "n.valor_enviado", "n.estado_notificacion", "n.agencia_envio", "n.responsable_seguimiento"};
        String columnaOrden = "n.id_notificacion";

        if (orderColumn != null && !orderColumn.isEmpty()) {
            int colIndex = Integer.parseInt(orderColumn);
            if (colIndex >= 0 && colIndex < columnas.length) {
                columnaOrden = columnas[colIndex];
            }
        }

        String sql = "SELECT n.id_notificacion, n.fecha_notificacion, n.hora_notificacion, n.tipo_notificacion, "
                + "n.valor_enviado, n.estado_notificacion, n.agencia_envio, n.observacion,n.responsable_seguimiento "
                + "FROM notificaciones n "
                + "WHERE n.cod_predio = ? AND ("
                + "(CAST(n.id_notificacion AS TEXT) ILIKE ? OR "
                + "n.fecha_notificacion ILIKE ? OR "
                + "n.hora_notificacion ILIKE ? OR "
                + "n.tipo_notificacion ILIKE ? OR "
                + "CAST(n.valor_enviado AS TEXT) ILIKE ? OR "
                + "n.estado_notificacion ILIKE ? OR "
                + "n.agencia_envio ILIKE ? OR "
                + "n.responsable_seguimiento ILIKE ?)) "
                + "ORDER BY " + columnaOrden + " " + orderDir + " LIMIT ? OFFSET ?";

        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            String filtro = "%" + searchValue + "%";
            ps.setInt(1, codPredio);
            for (int i = 2; i <= 9; i++) {
                ps.setString(i, filtro);
            }
            ps.setInt(10, length);
            ps.setInt(11, start);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Notificacion n = new Notificacion();
                    n.setIdNotificacion(rs.getInt("id_notificacion"));
                    n.setFechaNotificacion(convertirFecha(rs.getString("fecha_notificacion")));
                    n.setHoraNotificacion(convertirHora12(rs.getString("hora_notificacion")));
                    n.setTipoNotificacion(rs.getString("tipo_notificacion"));
                    n.setValorEnviado(rs.getDouble("valor_enviado"));
                    n.setEstadoNotificacion(rs.getString("estado_notificacion"));
                    n.setAgenciaEnvio(rs.getString("agencia_envio"));
                    n.setObservacion(rs.getString("observacion"));
                    n.setResponsable(rs.getString("responsable_seguimiento"));
                    notificaciones.add(n);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notificaciones;
    }

    // Contar total de notificaciones por propiedad
    public int contarTotalNotificaciones(int codPredio) {
        String sql = "SELECT COUNT(*) FROM notificaciones WHERE cod_predio = ?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codPredio);
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

    // Contar notificaciones filtradas por propiedad
    public int contarNotificacionesFiltradas(String searchValue, int codPredio) {
        String sql = "SELECT COUNT(*) FROM notificaciones n "
                + "WHERE n.cod_predio = ? AND ("
                + "(CAST(n.id_notificacion AS TEXT) ILIKE ? OR "
                + "n.fecha_notificacion ILIKE ? OR "
                + "n.hora_notificacion ILIKE ? OR "
                + "n.tipo_notificacion ILIKE ? OR "
                + "CAST(n.valor_enviado AS TEXT) ILIKE ? OR "
                + "n.estado_notificacion ILIKE ? OR "
                + "n.agencia_envio ILIKE ? OR "
                + "n.responsable_seguimiento ILIKE ?))";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            String filtro = "%" + searchValue + "%";
            ps.setInt(1, codPredio);
            for (int i = 2; i <= 9; i++) {
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
    // Insertar notificación
    public boolean insertarNotificacion(Notificacion notificacion) {
        String sql = "INSERT INTO notificaciones (cod_predio, fecha_notificacion, hora_notificacion, "
                + "tipo_notificacion, valor_enviado, estado_notificacion, agencia_envio, observacion,responsable_seguimiento) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, notificacion.getCodPredio());
            ps.setString(2, notificacion.getFechaNotificacion());
            ps.setString(3, notificacion.getHoraNotificacion());
            ps.setString(4, notificacion.getTipoNotificacion());
            ps.setDouble(5, notificacion.getValorEnviado());
            ps.setString(6, notificacion.getEstadoNotificacion());
            ps.setString(7, notificacion.getAgenciaEnvio());
            ps.setString(8, notificacion.getObservacion());
            ps.setString(9, notificacion.getResponsable());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Actualizar notificación
    public boolean actualizarNotificacion(Notificacion notificacion) {
        String sql = "UPDATE notificaciones SET fecha_notificacion = ?, hora_notificacion = ?, "
                + "tipo_notificacion = ?, valor_enviado = ?, estado_notificacion = ?, "
                + "agencia_envio = ?, observacion = ?, responsable_seguimiento=? WHERE id_notificacion = ?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, notificacion.getFechaNotificacion());
            ps.setString(2, notificacion.getHoraNotificacion());
            ps.setString(3, notificacion.getTipoNotificacion());
            ps.setDouble(4, notificacion.getValorEnviado());
            ps.setString(5, notificacion.getEstadoNotificacion());
            ps.setString(6, notificacion.getAgenciaEnvio());
            ps.setString(7, notificacion.getObservacion());
            ps.setString(8, notificacion.getResponsable());
            ps.setInt(9, notificacion.getIdNotificacion());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar notificación
    public boolean eliminarNotificacion(int idNotificacion) {
        String sql = "DELETE FROM notificaciones WHERE id_notificacion = ?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idNotificacion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtener notificación por ID
    public Notificacion obtenerNotificacionPorId(int idNotificacion) {
        Notificacion notificacion = null;
        String sql = "SELECT id_notificacion, cod_predio, fecha_notificacion, hora_notificacion, "
                + "tipo_notificacion, valor_enviado, estado_notificacion, agencia_envio, observacion, responsable_seguimiento "
                + "FROM notificaciones WHERE id_notificacion = ?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idNotificacion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    notificacion = new Notificacion();
                    notificacion.setCodPredio(rs.getInt("cod_predio"));
                    notificacion.setFechaNotificacion(rs.getString("fecha_notificacion"));
                    notificacion.setHoraNotificacion(rs.getString("hora_notificacion"));
                    notificacion.setTipoNotificacion(rs.getString("tipo_notificacion"));
                    notificacion.setValorEnviado(rs.getDouble("valor_enviado"));
                    notificacion.setEstadoNotificacion(rs.getString("estado_notificacion"));
                    notificacion.setAgenciaEnvio(rs.getString("agencia_envio"));
                    notificacion.setObservacion(rs.getString("observacion"));
                    notificacion.setResponsable(rs.getString("responsable_seguimiento"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return notificacion;
    }

    private String convertirHora12(String hora24) {
        try {
            DateTimeFormatter formato24 = DateTimeFormatter.ofPattern("HH:mm");
            DateTimeFormatter formato12 = DateTimeFormatter.ofPattern("hh:mm a");
            LocalTime hora = LocalTime.parse(hora24, formato24);
            return hora.format(formato12);
        } catch (Exception e) {
            return hora24; // Si falla, devuelve tal cual
        }
    }

    private String convertirFecha(String fechaBD) {
        try {
            DateTimeFormatter formatoBD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter formatoDeseado = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fecha = LocalDate.parse(fechaBD, formatoBD);
            return fecha.format(formatoDeseado);
        } catch (Exception e) {
            return fechaBD; // Si falla, devuelve tal cual
        }
    }
}
