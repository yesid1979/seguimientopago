package com.seguimiento.controlador;

import com.google.gson.Gson;
import com.seguimiento.bean.GestionJuridica;
import com.seguimiento.modelo.ModeloGestionJuridica;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GestionJuridicaController extends HttpServlet {

    private final ModeloGestionJuridica modelo = new ModeloGestionJuridica();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }
        switch (accion) {
            case "listar":
                listarGestion(request, response);
                break;
            case "insertar":
                insertarGestion(request, response);
                break;
            case "actualizar":
                actualizarGestion(request, response);
                break;
            case "eliminar":
                eliminarGestion(request, response);
                break;
            case "obtener":
                obtenerGestionPorId(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no válida");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    // ================================
    // LISTAR GESTIÓN JURÍDICA
    // ================================
    private void listarGestion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        int draw = Integer.parseInt(request.getParameter("draw"));
        int start = Integer.parseInt(request.getParameter("start"));
        int length = Integer.parseInt(request.getParameter("length"));
        String searchValue = request.getParameter("search[value]");
        String orderColumn = request.getParameter("order[0][column]");
        String orderDir = request.getParameter("order[0][dir]");
        int codPredio = Integer.parseInt(request.getParameter("cod_predio"));

        List<GestionJuridica> gestiones = modelo.listarGestion(start, length, searchValue, orderColumn, orderDir, codPredio);
        int totalRegistros = modelo.contarTotalGestion(codPredio);
        int registrosFiltrados = modelo.contarGestionFiltrada(searchValue, codPredio);

        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("draw", draw);
        jsonResponse.put("recordsTotal", totalRegistros);
        jsonResponse.put("recordsFiltered", registrosFiltrados);
        jsonResponse.put("data", gestiones);

        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(jsonResponse));
        out.flush();
    }

    // ================================
    // INSERTAR GESTIÓN JURÍDICA
    // ================================
    private void insertarGestion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");

        String fechaMandamiento = request.getParameter("fecha_mandamiento");
        String numProceso = request.getParameter("num_proceso");
        String etapaProceso = request.getParameter("etapa_proceso");
        String valorRecuperadoParam = request.getParameter("valor_recuperado");
        String codPredioParam = request.getParameter("cod_predio");

        GestionJuridica gestion = new GestionJuridica();
        try {
            gestion.setFechaMandamiento(fechaMandamiento);
            gestion.setNumProceso(numProceso);
            gestion.setEtapaProceso(etapaProceso);
            gestion.setValorRecuperado(valorRecuperadoParam != null ? Double.parseDouble(valorRecuperadoParam) : 0.0);
            gestion.setCodPredio(codPredioParam != null && !codPredioParam.isEmpty() ? Integer.parseInt(codPredioParam) : null);
        } catch (NumberFormatException e) {
            enviarRespuestaJSON(response, false, "Error en formato de datos: " + e.getMessage());
            return;
        }

        boolean exito = modelo.insertarGestion(gestion);
        enviarRespuestaJSON(response, exito, exito ? "La Gestión Jurídica registrada correctamente" : "Error al registrar la gestión");
    }

    // ================================
    // ACTUALIZAR GESTIÓN JURÍDICA
    // ================================
    private void actualizarGestion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");

        GestionJuridica gestion = construirGestionDesdeRequest(request);
        gestion.setIdGestion(Integer.parseInt(request.getParameter("id_gestion")));

        boolean exito = modelo.actualizarGestion(gestion);
        enviarRespuestaJSON(response, exito, exito ? "La Gestión Jurídica actualizada correctamente" : "Error al actualizar la gestión");
    }

    // ================================
    // ELIMINAR GESTIÓN JURÍDICA
    // ================================
    private void eliminarGestion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");

        int idGestion = Integer.parseInt(request.getParameter("id_gestion"));
        boolean exito = modelo.eliminarGestion(idGestion);

        enviarRespuestaJSON(response, exito, exito ? "La Gestión Jurídica eliminada correctamente" : "Error al eliminar la gestión");
    }

    // ================================
    // OBTENER GESTIÓN POR ID
    // ================================
    private void obtenerGestionPorId(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int idGestion = Integer.parseInt(request.getParameter("id_gestion"));
        GestionJuridica gestion = modelo.obtenerGestionPorId(idGestion);

        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(gestion));
        out.flush();
    }

    // ================================
    // MÉTODOS AUXILIARES
    // ================================
    private GestionJuridica construirGestionDesdeRequest(HttpServletRequest request) {
        GestionJuridica gestion = new GestionJuridica();
        gestion.setFechaMandamiento(request.getParameter("fecha_mandamiento"));
        gestion.setNumProceso(request.getParameter("num_proceso"));
        gestion.setEtapaProceso(request.getParameter("etapa_proceso"));
        gestion.setValorRecuperado(Double.parseDouble(request.getParameter("valor_recuperado")));
        String codPredioParam = request.getParameter("cod_predio");
        gestion.setCodPredio(codPredioParam != null && !codPredioParam.isEmpty() ? Integer.parseInt(codPredioParam) : null);
        return gestion;
    }

    private void enviarRespuestaJSON(HttpServletResponse response, boolean exito, String mensaje)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        Map<String, Object> json = new HashMap<>();
        json.put("success", exito);
        json.put("message", mensaje);
        out.print(new Gson().toJson(json));
        out.flush();
    }
}
