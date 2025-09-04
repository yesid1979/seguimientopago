package com.seguimiento.modelo;

import com.seguimiento.bean.Contribuyente;
import com.seguimiento.bean.Predio;
import java.sql.*;
import com.seguimiento.config.Conexion;
import java.util.ArrayList;
import java.util.List;

public class ModeloPredios {

    /* =============================================
       MÉTODOS PARA DATATABLES (SERVER-SIDE)
    ============================================= */
    // Listar predios con soporte DataTables
    public List<Predio> listarPredios(int start, int length, String searchValue, String orderColumn, String orderDir) {
        List<Predio> lista = new ArrayList<>();
        String[] columnas = {
            "p.nro_predio", "p.id_predio", "p.matricula_predio", "p.vereda_barrio",
            "p.dir_predio", "p.numrecibo_predio", "c.nom_contribuyente", "c.ced_contribuyente", "p.vigencia_predio"
        };
        String columnaOrden = "p.nro_predio";
        if (orderColumn != null && !orderColumn.isEmpty()) {
            int colIndex = Integer.parseInt(orderColumn);
            if (colIndex >= 0 && colIndex < columnas.length) {
                columnaOrden = columnas[colIndex];
            }
        }

        // Contar el número de condiciones en el WHERE
        int numCondiciones = 9; // Actualiza este número según las condiciones que tengas

        String sql = "SELECT p.nro_predio, p.id_predio, p.numrecibo_predio, p.matricula_predio, "
                + "p.vereda_barrio, p.dir_predio, p.valor_pendiente, p.observacion, p.vigencia_predio, "
                + "p.valor_enviado, p.estado_predio, p.cod_contribuyente, "
                + "c.id_contribuyente, c.ced_contribuyente, c.nom_contribuyente "
                + "FROM predios p "
                + "LEFT JOIN contribuyente c ON p.cod_contribuyente = c.id_contribuyente "
                + "WHERE (CAST(p.nro_predio AS TEXT) ILIKE ? OR p.id_predio ILIKE ? OR p.matricula_predio ILIKE ? "
                + "OR p.vereda_barrio ILIKE ? OR p.dir_predio ILIKE ? OR p.numrecibo_predio ILIKE ? "
                + "OR COALESCE(c.nom_contribuyente, '') ILIKE ? OR c.ced_contribuyente ILIKE ? OR p.vigencia_predio ILIKE ?) "
                + "ORDER BY " + columnaOrden + " " + orderDir + " LIMIT ? OFFSET ?";

        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            String filtro = "%" + searchValue + "%";

            // Usar el número correcto de condiciones
            for (int i = 1; i <= numCondiciones; i++) {
                ps.setString(i, filtro);
            }

            // Establecer los parámetros de paginación
            ps.setInt(numCondiciones + 1, length);
            ps.setInt(numCondiciones + 2, start);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Predio p = new Predio();
                    p.setNroPredio(rs.getInt("nro_predio"));
                    p.setIdPredio(rs.getString("id_predio"));
                    p.setNumreciboPredio(rs.getString("numrecibo_predio"));
                    p.setMatriculaPredio(rs.getString("matricula_predio"));
                    p.setVeredaBarrio(rs.getString("vereda_barrio"));
                    p.setDirPredio(rs.getString("dir_predio"));
                    p.setValorPendiente(rs.getDouble("valor_pendiente"));
                    p.setObservacion(rs.getString("observacion"));
                    p.setVigenciaPredio(rs.getString("vigencia_predio"));
                    p.setValorEnviado(rs.getDouble("valor_enviado"));
                    p.setEstadoPredio(rs.getString("estado_predio"));

                    // Manejo del contribuyente (puede ser NULL)
                    Integer idContribuyente = rs.getObject("cod_contribuyente", Integer.class);
                    Contribuyente contribuyente = new Contribuyente();
                    if (idContribuyente != null) {
                        contribuyente.setIdContribuyente(idContribuyente);
                        contribuyente.setCedContribuyente(rs.getString("ced_contribuyente"));
                        contribuyente.setNomContribuyente(rs.getString("nom_contribuyente"));
                    } else {
                        // Si no hay contribuyente, establecemos valores por defecto
                        contribuyente.setIdContribuyente(0);
                        contribuyente.setCedContribuyente("");
                        contribuyente.setNomContribuyente("Sin asignar");
                    }
                    p.setContribuyente(contribuyente);

                    lista.add(p);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    // Contar total de predios
    public int contarTotalPredios() {
        String sql = "SELECT COUNT(*) FROM predios";
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

    // Contar predios filtrados
public int contarPrediosFiltrados(String searchValue) {
    // Contar el número de condiciones en el WHERE
    int numCondiciones = 9; // Debe ser el mismo número que en listarPredios
    
    String sql = "SELECT COUNT(*) FROM predios p "
            + "LEFT JOIN contribuyente c ON p.cod_contribuyente = c.id_contribuyente "
            + "WHERE (CAST(p.nro_predio AS TEXT) ILIKE ? OR p.id_predio ILIKE ? OR p.matricula_predio ILIKE ? "
            + "OR p.vereda_barrio ILIKE ? OR p.dir_predio ILIKE ? OR c.ced_contribuyente ILIKE ? "
            + "OR COALESCE(c.nom_contribuyente, '') ILIKE ? OR p.numrecibo_predio ILIKE ? OR p.vigencia_predio ILIKE ?)";
    
    try (Connection con = Conexion.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {
        String filtro = "%" + searchValue + "%";
        
        // Usar el número correcto de condiciones
        for (int i = 1; i <= numCondiciones; i++) {
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
    // Insertar predio
    public boolean insertar(Predio p) {
        String sql = "INSERT INTO predios (id_predio, numrecibo_predio, matricula_predio, vereda_barrio, dir_predio, "
                + "valor_pendiente, cod_contribuyente, observacion, vigencia_predio, valor_enviado, estado_predio) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getIdPredio());
            ps.setString(2, p.getNumreciboPredio());
            ps.setString(3, p.getMatriculaPredio());
            ps.setString(4, p.getVeredaBarrio());
            ps.setString(5, p.getDirPredio());
            ps.setDouble(6, p.getValorPendiente());

            // Manejo del contribuyente (puede ser NULL)
            if (p.getContribuyente() != null) {
                ps.setInt(7, p.getContribuyente().getIdContribuyente());
            } else {
                ps.setNull(7, java.sql.Types.INTEGER);
            }

            ps.setString(8, p.getObservacion());
            ps.setString(9, p.getVigenciaPredio());
            ps.setDouble(10, p.getValorEnviado());
            ps.setString(11, p.getEstadoPredio());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Actualizar predio
    public boolean actualizar(Predio p) {
        String sql = "UPDATE predios SET id_predio = ?, numrecibo_predio = ?, matricula_predio = ?, "
                + "vereda_barrio = ?, dir_predio = ?, valor_pendiente = ?, cod_contribuyente = ?, "
                + "observacion = ?, vigencia_predio = ?, valor_enviado = ?, estado_predio = ? "
                + "WHERE nro_predio = ?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getIdPredio());
            ps.setString(2, p.getNumreciboPredio());
            ps.setString(3, p.getMatriculaPredio());
            ps.setString(4, p.getVeredaBarrio());
            ps.setString(5, p.getDirPredio());
            ps.setDouble(6, p.getValorPendiente());

            // Manejo del contribuyente (puede ser NULL)
            if (p.getContribuyente() != null) {
                ps.setInt(7, p.getContribuyente().getIdContribuyente());
            } else {
                ps.setNull(7, java.sql.Types.INTEGER);
            }

            ps.setString(8, p.getObservacion());
            ps.setString(9, p.getVigenciaPredio());
            ps.setDouble(10, p.getValorEnviado());
            ps.setString(11, p.getEstadoPredio());
            ps.setInt(12, p.getNroPredio());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar predio
    public boolean eliminar(int nroPredio) {
        String sql = "DELETE FROM predios WHERE nro_predio = ?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nroPredio);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Obtener predio por ID
    public Predio obtenerPorId(int nroPredio) {
        Predio p = null;
        String sql = "SELECT p.nro_predio, p.id_predio, p.numrecibo_predio, p.matricula_predio, "
                + "p.vereda_barrio, p.dir_predio, p.valor_pendiente, p.observacion, p.vigencia_predio, "
                + "p.valor_enviado, p.estado_predio, p.cod_contribuyente, "
                + "c.id_contribuyente, c.ced_contribuyente, c.nom_contribuyente "
                + "FROM predios p "
                + "LEFT JOIN contribuyente c ON p.cod_contribuyente = c.id_contribuyente "
                + "WHERE p.nro_predio = ?";
        try (Connection con = Conexion.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, nroPredio);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    p = new Predio();
                    p.setNroPredio(rs.getInt("nro_predio"));
                    p.setIdPredio(rs.getString("id_predio"));
                    p.setNumreciboPredio(rs.getString("numrecibo_predio"));
                    p.setMatriculaPredio(rs.getString("matricula_predio"));
                    p.setVeredaBarrio(rs.getString("vereda_barrio"));
                    p.setDirPredio(rs.getString("dir_predio"));
                    p.setValorPendiente(rs.getDouble("valor_pendiente"));
                    p.setObservacion(rs.getString("observacion"));
                    p.setVigenciaPredio(rs.getString("vigencia_predio"));
                    p.setValorEnviado(rs.getDouble("valor_enviado"));
                    p.setEstadoPredio(rs.getString("estado_predio"));

                    // Manejo del contribuyente (puede ser NULL)
                    Integer idContribuyente = rs.getObject("cod_contribuyente", Integer.class);
                    if (idContribuyente != null) {
                        Contribuyente contribuyente = new Contribuyente();
                        contribuyente.setIdContribuyente(idContribuyente);
                        contribuyente.setCedContribuyente(rs.getString("ced_contribuyente"));
                        contribuyente.setNomContribuyente(rs.getString("nom_contribuyente"));
                        p.setContribuyente(contribuyente);
                    } else {
                        p.setContribuyente(null);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return p;
    }
}
