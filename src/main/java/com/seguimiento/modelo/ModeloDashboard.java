package com.seguimiento.modelo;

import com.seguimiento.bean.Dashboard;
import com.seguimiento.config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ModeloDashboard {

    public Dashboard obtenerDatosDashboard() {
        Dashboard dashboard = new Dashboard();

        dashboard.setTotalContribuyentes(getTotalContribuyentes());
        dashboard.setTotalPredios(getTotalPredios());
        dashboard.setTotalNotificados(getTotalNotificados());
        dashboard.setTotalCoactivos(getTotalCoactivos());

        return dashboard;
    }

    private int getTotalContribuyentes() {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM contribuyente";

        try (Connection connection = Conexion.getConexion();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                total = resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    private int getTotalPredios() {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM predios";

        try (Connection connection = Conexion.getConexion();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                total = resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    private int getTotalNotificados() {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM notificaciones";

        try (Connection connection = Conexion.getConexion();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                total = resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }

    private int getTotalCoactivos() {
        int total = 0;
        String sql = "SELECT COUNT(*) FROM gestion_juridica";

        try (Connection connection = Conexion.getConexion();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                total = resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return total;
    }
    // Obtener datos para la gráfica de valor pendiente por predio

    public List<Object[]> getValorPendientePorPredio() {
        List<Object[]> datos = new ArrayList<>();
        String sql = "SELECT dir_predio, valor_pendiente FROM predios ORDER BY valor_pendiente DESC LIMIT 10";

        try (Connection connection = Conexion.getConexion();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Object[] fila = new Object[2];
                fila[0] = resultSet.getString("dir_predio");
                fila[1] = resultSet.getDouble("valor_pendiente");
                datos.add(fila);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datos;
    }

// Obtener datos para la gráfica de predios por estado
    public List<Object[]> getPrediosPorEstado() {
        List<Object[]> datos = new ArrayList<>();
        String sql = "SELECT estado_predio, COUNT(*) as total FROM predios GROUP BY estado_predio";

        try (Connection connection = Conexion.getConexion();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Object[] fila = new Object[2];
                fila[0] = resultSet.getString("estado_predio");
                fila[1] = resultSet.getInt("total");
                datos.add(fila);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datos;
    }

// Obtener datos para la gráfica de predios por vereda/barrio
    public List<Object[]> getPrediosPorVeredaBarrio() {
        List<Object[]> datos = new ArrayList<>();
        String sql = "SELECT vereda_barrio, COUNT(*) as total FROM predios GROUP BY vereda_barrio";

        try (Connection connection = Conexion.getConexion();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Object[] fila = new Object[2];
                fila[0] = resultSet.getString("vereda_barrio");
                fila[1] = resultSet.getInt("total");
                datos.add(fila);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datos;
    }

// Obtener datos para la gráfica de valor recuperado vs. pendiente
    public List<Object[]> getValorRecuperadoVsPendiente() {
        List<Object[]> datos = new ArrayList<>();
        String sql = "SELECT p.dir_predio, "
                + "COALESCE(SUM(r.valor_pago), 0) as valor_recuperado, "
                + "p.valor_pendiente "
                + "FROM predios p "
                + "LEFT JOIN resultados r ON p.nro_predio = r.cod_predio "
                + "GROUP BY p.nro_predio, p.dir_predio, p.valor_pendiente";

        try (Connection connection = Conexion.getConexion();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Object[] fila = new Object[3];
                fila[0] = resultSet.getString("dir_predio");
                fila[1] = resultSet.getDouble("valor_recuperado");
                fila[2] = resultSet.getDouble("valor_pendiente");
                datos.add(fila);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datos;
    }
// Obtener datos para la gráfica de predios por vigencia

    public List<Object[]> getPrediosPorVigencia() {
        List<Object[]> datos = new ArrayList<>();
        String sql = "SELECT vigencia_predio, COUNT(*) as total FROM predios GROUP BY vigencia_predio ORDER BY vigencia_predio";

        try (Connection connection = Conexion.getConexion();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Object[] fila = new Object[2];
                fila[0] = resultSet.getString("vigencia_predio");
                fila[1] = resultSet.getInt("total");
                datos.add(fila);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datos;
    }

}
