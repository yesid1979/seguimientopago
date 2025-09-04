package com.seguimiento.controlador;

import com.google.gson.Gson;
import com.seguimiento.bean.Contribuyente;
import com.seguimiento.bean.Predio;
import com.seguimiento.modelo.ModeloContribuyente;
import com.seguimiento.modelo.ModeloPredios;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PrediosController extends HttpServlet {

    private final ModeloPredios modelo = new ModeloPredios();
    private final ModeloContribuyente modeloContribuyente = new ModeloContribuyente();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }
        switch (accion) {
            case "listar":
                listarPrediosDataTables(request, response);
                break;
            case "insertar":
                insertarPredio(request, response);
                break;
            case "actualizar":
                actualizarPredio(request, response);
                break;
            case "eliminar":
                eliminarPredio(request, response);
                break;
            case "obtener":
                obtenerPredioPorId(request, response);
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
    // LISTAR PARA DATATABLES
    // ================================
    private void listarPrediosDataTables(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        int draw = Integer.parseInt(request.getParameter("draw"));
        int start = Integer.parseInt(request.getParameter("start"));
        int length = Integer.parseInt(request.getParameter("length"));
        String searchValue = request.getParameter("search[value]");
        String orderColumn = request.getParameter("order[0][column]");
        String orderDir = request.getParameter("order[0][dir]");

        List<Predio> predios = modelo.listarPredios(start, length, searchValue, orderColumn, orderDir);
        int totalRegistros = modelo.contarTotalPredios();
        int registrosFiltrados = modelo.contarPrediosFiltrados(searchValue);

        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("draw", draw);
        jsonResponse.put("recordsTotal", totalRegistros);
        jsonResponse.put("recordsFiltered", registrosFiltrados);
        jsonResponse.put("data", predios);

        out.print(new Gson().toJson(jsonResponse));
        out.flush();
    }

    // ================================
    // INSERTAR
    // ================================
    private void insertarPredio(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        Predio p = construirPredioDesdeRequest(request);
        boolean exito = modelo.insertar(p);
        String mensaje = exito ? "Predio registrado correctamente" : "Error al registrar el predio";

        enviarRespuestaJSON(response, exito, mensaje);
    }

    // ================================
    // ACTUALIZAR
    // ================================
    private void actualizarPredio(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        Predio p = construirPredioDesdeRequest(request);
        p.setNroPredio(Integer.parseInt(request.getParameter("nro_predio")));

        boolean exito = modelo.actualizar(p);
        String mensaje = exito ? "Predio actualizado correctamente" : "Error al actualizar el predio";

        enviarRespuestaJSON(response, exito, mensaje);
    }

    // ================================
    // ELIMINAR
    // ================================
    private void eliminarPredio(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        int nroPredio = Integer.parseInt(request.getParameter("nro_predio"));
        boolean exito = modelo.eliminar(nroPredio);
        String mensaje = exito ? "Predio eliminado correctamente" : "Error al eliminar el predio";

        enviarRespuestaJSON(response, exito, mensaje);
    }

    // ================================
    // OBTENER POR ID
    // ================================
    private void obtenerPredioPorId(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int nroPredio = Integer.parseInt(request.getParameter("nro_predio"));
        Predio p = modelo.obtenerPorId(nroPredio);

        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(p));
        out.flush();
    }

    // ================================
    // MÉTODOS AUXILIARES
    // ================================
    private Predio construirPredioDesdeRequest(HttpServletRequest request) {
        Predio p = new Predio();

        // Campos básicos del predio
        p.setIdPredio(request.getParameter("id_predio"));
        p.setNumreciboPredio(request.getParameter("numrecibo_predio"));
        p.setMatriculaPredio(request.getParameter("matricula_predio"));
        p.setVeredaBarrio(request.getParameter("vereda_barrio"));
        p.setDirPredio(request.getParameter("dir_predio"));
        p.setObservacion(request.getParameter("observacion"));
        p.setVigenciaPredio(request.getParameter("vigencia_predio"));
        p.setEstadoPredio(request.getParameter("estado_predio"));

        // Manejo de valores numéricos
        try {
            String valorPendiente = request.getParameter("valor_pendiente");
            if (valorPendiente != null && !valorPendiente.isEmpty()) {
                p.setValorPendiente(Double.parseDouble(valorPendiente));
            }
        } catch (NumberFormatException e) {
            p.setValorPendiente(0.0);
        }

        try {
            String valorEnviado = request.getParameter("valor_enviado");
            if (valorEnviado != null && !valorEnviado.isEmpty()) {
                p.setValorEnviado(Double.parseDouble(valorEnviado));
            }
        } catch (NumberFormatException e) {
            p.setValorEnviado(0.0);
        }

        // Manejo del contribuyente (llave foránea)
        String cedulaContribuyente = request.getParameter("ced_contribuyente");
        if (cedulaContribuyente != null && !cedulaContribuyente.trim().isEmpty()) {
            Contribuyente contribuyente = modeloContribuyente.buscarPorCedula(cedulaContribuyente);
            p.setContribuyente(contribuyente);
        } else {
            p.setContribuyente(null);
        }

        return p;
    }

    private void enviarRespuestaJSON(HttpServletResponse response, boolean exito, String mensaje)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Map<String, Object> json = new HashMap<>();
        json.put("success", exito);
        json.put("message", mensaje);
        out.print(new Gson().toJson(json));
        out.flush();
    }
}
