package com.seguimiento.modelo;

import com.seguimiento.bean.Cobro;
import com.seguimiento.config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ModeloCobros {

    /* =============================================
       MÉTODOS PARA DATATABLES (SERVER-SIDE)
    ============================================= */
    public List<Cobro> listarCobros(int start, int length, String searchValue, String orderColumn, String orderDir, int codPredio) {
        List<Cobro> cobros = new ArrayList<>();
        String[] columnas = {"c.id_cobro", "c.num_intentos", "c.compromiso", "c.fecha_seguimiento", "c.valor_acordado"};
        String columnaOrden = "c.id_cobro";

        if (orderColumn != null && !orderColumn.isEmpty()) {
            int colIndex = Integer.parseInt(orderColumn);
            if (colIndex >= 0 && colIndex < columnas.length) {
                columnaOrden = columnas[colIndex];
            }
        }

        String sql = "SELECT c.id_cobro, c.num_intentos, c.compromiso, c.fecha_seguimiento, c.valor_acordado "
                + "FROM cobro c "
                + "WHERE c.cod_predio = ? AND ("
                + "CAST(c.id_cobro AS TEXT) ILIKE ? OR "
                + "CAST(c.num_intentos AS TEXT) ILIKE ? OR "
                + "c.compromiso ILIKE ? OR "
                + "c.fecha_seguimiento ILIKE ? OR "
                + "CAST(c.valor_acordado AS TEXT) ILIKE ?) "
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
                    Cobro c = new Cobro();
                    c.setIdCobro(rs.getInt("id_cobro"));
                    c.setNumIntentos(rs.getInt("num_intentos"));
                    c.setCompromiso(rs.getString("compromiso"));
                    c.setFechaSeguimiento(convertirFecha(rs.getString("fecha_seguimiento")));
                    c.setValorAcordado(rs.getDouble("valor_acordado"));
                    c.setCodPredio(codPredio);
                    cobros.add(c);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cobros;
    }

    public int contarTotalCobros(int codPredio) {
        String sql = "SELECT COUNT(*) FROM cobro WHERE cod_predio = ?";
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

    public int contarCobrosFiltrados(String searchValue, int codPredio) {
        String sql = "SELECT COUNT(*) FROM cobro c "
                + "WHERE c.cod_predio = ? AND ("
                + "CAST(c.id_cobro AS TEXT) ILIKE ? OR "
                + "CAST(c.num_intentos AS TEXT) ILIKE ? OR "
                + "c.compromiso ILIKE ? OR "
                + "c.fecha_seguimiento ILIKE ? OR "
                + "CAST(c.valor_acordado AS TEXT) ILIKE ?)";

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
    public boolean insertarCobro(Cobro cobro) {
        String sql = "INSERT INTO cobro (num_intentos, compromiso, fecha_seguimiento, valor_acordado, cod_predio) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cobro.getNumIntentos());
            ps.setString(2, cobro.getCompromiso());
            ps.setString(3, cobro.getFechaSeguimiento());
            ps.setDouble(4, cobro.getValorAcordado());
            if (cobro.getCodPredio() != null) {
                ps.setInt(5, cobro.getCodPredio());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarCobro(Cobro cobro) {
        String sql = "UPDATE cobro SET num_intentos=?, compromiso=?, fecha_seguimiento=?, valor_acordado=?, cod_predio=? "
                + "WHERE id_cobro=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cobro.getNumIntentos());
            ps.setString(2, cobro.getCompromiso());
            ps.setString(3, cobro.getFechaSeguimiento());
            ps.setDouble(4, cobro.getValorAcordado());
            if (cobro.getCodPredio() != null) {
                ps.setInt(5, cobro.getCodPredio());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }
            ps.setInt(6, cobro.getIdCobro());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarCobro(int idCobro) {
        String sql = "DELETE FROM cobro WHERE id_cobro=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCobro);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Cobro obtenerCobroPorId(int idCobro) {
        Cobro cobro = null;
        String sql = "SELECT id_cobro, num_intentos, compromiso, fecha_seguimiento, valor_acordado, cod_predio "
                + "FROM cobro WHERE id_cobro=?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idCobro);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cobro = new Cobro();
                    cobro.setIdCobro(rs.getInt("id_cobro"));
                    cobro.setNumIntentos(rs.getInt("num_intentos"));
                    cobro.setCompromiso(rs.getString("compromiso"));
                    cobro.setFechaSeguimiento(rs.getString("fecha_seguimiento"));
                    cobro.setValorAcordado(rs.getDouble("valor_acordado"));
                    cobro.setCodPredio(rs.getInt("cod_predio"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cobro;
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
