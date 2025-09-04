package com.seguimiento.controlador;

import com.google.gson.Gson;
import com.seguimiento.bean.Contribuyente;
import com.seguimiento.modelo.ModeloContribuyente;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ContribuyenteController extends HttpServlet {

    private final ModeloContribuyente modelo = new ModeloContribuyente();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }
        switch (accion) {
            case "listar":
                listarContribuyentesDataTables(request, response);
                break;
            case "insertar":
                insertarContribuyente(request, response);
                break;
            case "actualizar":
                actualizarContribuyente(request, response);
                break;
            case "eliminar":
                eliminarContribuyente(request, response);
                break;
            case "obtener":
                obtenerContribuyentePorId(request, response);
                break;
            case "buscarPorCedula":
                buscarContribuyentePorCedula(request, response);
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
    private void listarContribuyentesDataTables(HttpServletRequest request, HttpServletResponse response)
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

        List<Contribuyente> contribuyentes = modelo.listarContribuyentes(start, length, searchValue, orderColumn, orderDir);
        int totalRegistros = modelo.contarTotalContribuyentes();
        int registrosFiltrados = modelo.contarContribuyentesFiltrados(searchValue);

        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("draw", draw);
        jsonResponse.put("recordsTotal", totalRegistros);
        jsonResponse.put("recordsFiltered", registrosFiltrados);
        jsonResponse.put("data", contribuyentes);

        out.print(new Gson().toJson(jsonResponse));
        out.flush();
    }

// ================================
// INSERTAR
// ================================
    private void insertarContribuyente(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        Contribuyente c = construirContribuyenteDesdeRequest(request);
        boolean exito = modelo.insertar(c);
        String mensaje = exito ? "Contribuyente registrado correctamente" : "Error al registrar el contribuyente";

        enviarRespuestaJSON(response, exito, mensaje);
    }

// ================================
// ACTUALIZAR
// ================================
    private void actualizarContribuyente(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        Contribuyente c = construirContribuyenteDesdeRequest(request);
        c.setIdContribuyente(Integer.parseInt(request.getParameter("id_contribuyente")));

        boolean exito = modelo.actualizar(c);
        String mensaje = exito ? "Contribuyente actualizado correctamente" : "Error al actualizar el contribuyente";

        enviarRespuestaJSON(response, exito, mensaje);
    }

// ================================
// ELIMINAR
// ================================
    private void eliminarContribuyente(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        int idContribuyente = Integer.parseInt(request.getParameter("id_contribuyente"));
        boolean exito = modelo.eliminar(idContribuyente);
        String mensaje = exito ? "Contribuyente eliminado correctamente" : "Error al eliminar el contribuyente";

        enviarRespuestaJSON(response, exito, mensaje);
    }

// ================================
// OBTENER POR ID
// ================================
    private void obtenerContribuyentePorId(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int idContribuyente = Integer.parseInt(request.getParameter("id_contribuyente"));
        Contribuyente c = modelo.obtenerPorId(idContribuyente);

        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(c));
        out.flush();
    }

// ================================
// MÉTODOS AUXILIARES
// ================================
    private Contribuyente construirContribuyenteDesdeRequest(HttpServletRequest request) {
        Contribuyente c = new Contribuyente();
        c.setCedContribuyente(request.getParameter("ced_contribuyente"));
        c.setNomContribuyente(request.getParameter("nom_contribuyente"));
        c.setCelContribuyente(request.getParameter("cel_contribuyente"));
        c.setCorreoContribuyente(request.getParameter("correo_contribuyente"));
        c.setDirContribuyente(request.getParameter("dir_contribuyente"));
        c.setEstadoContribuyente(request.getParameter("estado_contribuyente"));
        return c;
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

    private void buscarContribuyentePorCedula(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String cedula = request.getParameter("ced_contribuyente");
        Contribuyente contribuyente = modelo.buscarPorCedula(cedula);

        Map<String, Object> jsonResponse = new HashMap<>();
        if (contribuyente != null) {
            jsonResponse.put("success", true);
            jsonResponse.put("data", contribuyente);
        } else {
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Contribuyente no encontrado");
        }

        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(jsonResponse));
        out.flush();
    }
}
