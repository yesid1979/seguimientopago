package com.seguimiento.controlador;

import com.google.gson.Gson;
import com.seguimiento.bean.Cobro;
import com.seguimiento.modelo.ModeloCobros;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CobrosController extends HttpServlet {

    private final ModeloCobros modelo = new ModeloCobros();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }
        switch (accion) {
            case "listar":
                listarCobros(request, response);
                break;
            case "insertar":
                insertarCobro(request, response);
                break;
            case "actualizar":
                actualizarCobro(request, response);
                break;
            case "eliminar":
                eliminarCobro(request, response);
                break;
            case "obtener":
                obtenerCobroPorId(request, response);
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
    // LISTAR COBROS
    // ================================
    private void listarCobros(HttpServletRequest request, HttpServletResponse response)
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

        List<Cobro> cobros = modelo.listarCobros(start, length, searchValue, orderColumn, orderDir, codPredio);
        int totalRegistros = modelo.contarTotalCobros(codPredio);
        int registrosFiltrados = modelo.contarCobrosFiltrados(searchValue, codPredio);

        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("draw", draw);
        jsonResponse.put("recordsTotal", totalRegistros);
        jsonResponse.put("recordsFiltered", registrosFiltrados);
        jsonResponse.put("data", cobros);

        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(jsonResponse));
        out.flush();
    }

// ================================
    // INSERTAR COBRO
    // ================================
    private void insertarCobro(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");

        String numIntentosParam = request.getParameter("num_intentos");
        String compromiso = request.getParameter("compromiso");
        String fechaSeguimiento = request.getParameter("fecha_seguimiento");
        String valorAcordadoParam = request.getParameter("valor_acordado");
        String codPredioParam = request.getParameter("cod_predio");

        Cobro cobro = new Cobro();
        try {
            cobro.setNumIntentos(numIntentosParam != null ? Integer.parseInt(numIntentosParam) : 0);
            cobro.setCompromiso(compromiso);
            cobro.setFechaSeguimiento(fechaSeguimiento);
            cobro.setValorAcordado(valorAcordadoParam != null ? Double.parseDouble(valorAcordadoParam) : 0.0);
            cobro.setCodPredio(codPredioParam != null && !codPredioParam.isEmpty() ? Integer.parseInt(codPredioParam) : null);
        } catch (NumberFormatException e) {
            enviarRespuestaJSON(response, false, "Error en formato de datos: " + e.getMessage());
            return;
        }

        boolean exito = modelo.insertarCobro(cobro);
        enviarRespuestaJSON(response, exito, exito ? "Cobro registrado correctamente" : "Error al registrar el cobro");
    }

    // ================================
    // ACTUALIZAR COBRO
    // ================================
    private void actualizarCobro(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");

        Cobro cobro = construirCobroDesdeRequest(request);
        cobro.setIdCobro(Integer.parseInt(request.getParameter("id_cobro")));

        boolean exito = modelo.actualizarCobro(cobro);
        enviarRespuestaJSON(response, exito, exito ? "Cobro actualizado correctamente" : "Error al actualizar el cobro");
    }

    // ================================
    // ELIMINAR COBRO
    // ================================
    private void eliminarCobro(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");

        int idCobro = Integer.parseInt(request.getParameter("id_cobro"));
        boolean exito = modelo.eliminarCobro(idCobro);

        enviarRespuestaJSON(response, exito, exito ? "Cobro eliminado correctamente" : "Error al eliminar el cobro");
    }

    // ================================
    // OBTENER COBRO POR ID
    // ================================
    private void obtenerCobroPorId(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int idCobro = Integer.parseInt(request.getParameter("id_cobro"));
        Cobro cobro = modelo.obtenerCobroPorId(idCobro);

        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(cobro));
        out.flush();
    }

    // ================================
    // MÉTODOS AUXILIARES
    // ================================
    private Cobro construirCobroDesdeRequest(HttpServletRequest request) {
        Cobro cobro = new Cobro();
        cobro.setNumIntentos(Integer.parseInt(request.getParameter("num_intentos")));
        cobro.setCompromiso(request.getParameter("compromiso"));
        cobro.setFechaSeguimiento(request.getParameter("fecha_seguimiento"));
        cobro.setValorAcordado(Double.parseDouble(request.getParameter("valor_acordado")));
        String codPredioParam = request.getParameter("cod_predio");
        cobro.setCodPredio(codPredioParam != null && !codPredioParam.isEmpty() ? Integer.parseInt(codPredioParam) : null);
        return cobro;
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
