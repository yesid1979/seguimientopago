package com.seguimiento.modelo;

import com.seguimiento.bean.GestionJuridica;
import com.seguimiento.config.Conexion;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ModeloGestionJuridica {

    /* =============================================
       MÉTODOS PARA DATATABLES (SERVER-SIDE)
    ============================================= */
    public List<GestionJuridica> listarGestion(int start, int length, String searchValue, String orderColumn, String orderDir, int codPredio) {
        List<GestionJuridica> gestiones = new ArrayList<>();
        String[] columnas = {"g.id_gestion", "g.fecha_mandamiento", "g.num_proceso", "g.etapa_proceso", "g.valor_recuperado"};
        String columnaOrden = "g.id_gestion";

        if (orderColumn != null && !orderColumn.isEmpty()) {
            int colIndex = Integer.parseInt(orderColumn);
            if (colIndex >= 0 && colIndex < columnas.length) {
                columnaOrden = columnas[colIndex];
            }
        }

        String sql = "SELECT g.id_gestion, g.fecha_mandamiento, g.num_proceso, g.etapa_proceso, g.valor_recuperado "
                + "FROM gestion_juridica g "
                + "WHERE g.cod_predio = ? AND ("
                + "CAST(g.id_gestion AS TEXT) ILIKE ? OR "
                + "g.fecha_mandamiento ILIKE ? OR "
                + "g.num_proceso ILIKE ? OR "
                + "g.etapa_proceso ILIKE ? OR "
                + "CAST(g.valor_recuperado AS TEXT) ILIKE ?) "
                + "ORDER BY " + columnaOrden + " " + orderDir + " LIMIT ? OFFSET ?";

        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            String filtro = "%" + searchValue + "%";
            ps.setInt(1, codPredio);
            for (int i = 2; i <= 6; i++) {
                ps.setString(i, filtro);
            }
            ps.setInt(7, length);
            ps.setInt(8, start);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    GestionJuridica g = new GestionJuridica();
                    g.setIdGestion(rs.getInt("id_gestion"));
                    g.setFechaMandamiento(convertirFecha(rs.getString("fecha_mandamiento")));
                    g.setNumProceso(rs.getString("num_proceso"));
                    g.setEtapaProceso(rs.getString("etapa_proceso"));
                    g.setValorRecuperado(rs.getDouble("valor_recuperado"));
                    g.setCodPredio(codPredio);
                    gestiones.add(g);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gestiones;
    }

    public int contarTotalGestion(int codPredio) {
        String sql = "SELECT COUNT(*) FROM gestion_juridica WHERE cod_predio = ?";
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

    public int contarGestionFiltrada(String searchValue, int codPredio) {
        String sql = "SELECT COUNT(*) FROM gestion_juridica g "
                + "WHERE g.cod_predio = ? AND ("
                + "CAST(g.id_gestion AS TEXT) ILIKE ? OR "
                + "g.fecha_mandamiento ILIKE ? OR "
                + "g.num_proceso ILIKE ? OR "
                + "g.etapa_proceso ILIKE ? OR "
                + "CAST(g.valor_recuperado AS TEXT) ILIKE ?)";

        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            String filtro = "%" + searchValue + "%";
            ps.setInt(1, codPredio);
            for (int i = 2; i <= 6; i++) {
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
    public boolean insertarGestion(GestionJuridica gestion) {
        String sql = "INSERT INTO gestion_juridica (fecha_mandamiento, num_proceso, etapa_proceso, valor_recuperado, cod_predio) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, gestion.getFechaMandamiento());
            ps.setString(2, gestion.getNumProceso());
            ps.setString(3, gestion.getEtapaProceso());
            ps.setDouble(4, gestion.getValorRecuperado());
            if (gestion.getCodPredio() != null) {
                ps.setInt(5, gestion.getCodPredio());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarGestion(GestionJuridica gestion) {
        String sql = "UPDATE gestion_juridica SET fecha_mandamiento=?, num_proceso=?, etapa_proceso=?, valor_recuperado=?, cod_predio=? "
                + "WHERE id_gestion=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, gestion.getFechaMandamiento());
            ps.setString(2, gestion.getNumProceso());
            ps.setString(3, gestion.getEtapaProceso());
            ps.setDouble(4, gestion.getValorRecuperado());
            if (gestion.getCodPredio() != null) {
                ps.setInt(5, gestion.getCodPredio());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            ps.setInt(6, gestion.getIdGestion());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarGestion(int idGestion) {
        String sql = "DELETE FROM gestion_juridica WHERE id_gestion=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idGestion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public GestionJuridica obtenerGestionPorId(int idGestion) {
        GestionJuridica gestion = null;
        String sql = "SELECT id_gestion, fecha_mandamiento, num_proceso, etapa_proceso, valor_recuperado, cod_predio "
                + "FROM gestion_juridica WHERE id_gestion=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idGestion);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    gestion = new GestionJuridica();
                    gestion.setIdGestion(rs.getInt("id_gestion"));
                    gestion.setFechaMandamiento(rs.getString("fecha_mandamiento"));
                    gestion.setNumProceso(rs.getString("num_proceso"));
                    gestion.setEtapaProceso(rs.getString("etapa_proceso"));
                    gestion.setValorRecuperado(rs.getDouble("valor_recuperado"));
                    gestion.setCodPredio(rs.getInt("cod_predio"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gestion;
    }

    private String convertirFecha(String fechaBD) {
        try {
            DateTimeFormatter formatoBD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter formatoDeseado = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fecha = LocalDate.parse(fechaBD, formatoBD);
            return fecha.format(formatoDeseado);
        } catch (Exception e) {
            return fechaBD;
        }
    }
}
