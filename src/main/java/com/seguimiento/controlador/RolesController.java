package com.seguimiento.controlador;

import com.google.gson.Gson;
import com.seguimiento.bean.Rol;
import com.seguimiento.modelo.ModeloRoles;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RolesController extends HttpServlet {

    private final ModeloRoles modelo = new ModeloRoles();    
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }
        switch (accion) {
            case "listar":
                listarRolesDataTables(request, response);
                break;
            case "insertar":
                insertarRol(request, response);
                break;
            case "actualizar":
                actualizarRol(request, response);
                break;
            case "eliminar":
                eliminarRol(request, response);
                break;
            case "obtener":
                obtenerRolPorId(request, response);
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
    private void listarRolesDataTables(HttpServletRequest request, HttpServletResponse response)
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

        // Usar el modelo de roles
        List<Rol> roles = modelo.listarRoles(start, length, searchValue, orderColumn, orderDir);
        int totalRegistros = modelo.contarTotalRoles();
        int registrosFiltrados = modelo.contarRolesFiltrados(searchValue);

        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("draw", draw);
        jsonResponse.put("recordsTotal", totalRegistros);
        jsonResponse.put("recordsFiltered", registrosFiltrados);
        jsonResponse.put("data", roles);

        out.print(new Gson().toJson(jsonResponse));
        out.flush();
    }

    // ================================
    // INSERTAR
    // ================================
    private void insertarRol(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        Rol r = construirRolDesdeRequest(request);
        boolean exito = modelo.insertar(r);
        String mensaje = exito ? "Rol registrado correctamente" : "Error al registrar el rol";
        enviarRespuestaJSON(response, exito, mensaje);
    }

    // ================================
    // ACTUALIZAR
    // ================================
    private void actualizarRol(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        Rol r = construirRolDesdeRequest(request);
        r.setIdRol(Integer.parseInt(request.getParameter("id_rol")));
        boolean exito = modelo.actualizar(r);
        String mensaje = exito ? "Rol actualizado correctamente" : "Error al actualizar el rol";
        enviarRespuestaJSON(response, exito, mensaje);
    }

    // ================================
    // ELIMINAR
    // ================================
    private void eliminarRol(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        int idRol = Integer.parseInt(request.getParameter("id_rol"));
        boolean exito = modelo.eliminar(idRol);
        String mensaje = exito ? "Rol eliminado correctamente" : "Error al eliminar el rol";
        enviarRespuestaJSON(response, exito, mensaje);
    }

    // ================================
    // OBTENER POR ID
    // ================================
    private void obtenerRolPorId(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int idRol = Integer.parseInt(request.getParameter("id_rol"));
        Rol r = modelo.obtenerPorId(idRol);
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(r));
        out.flush();
    }

    // ================================
    // MÉTODOS AUXILIARES
    // ================================
    private Rol construirRolDesdeRequest(HttpServletRequest request) {
        Rol r = new Rol();
        r.setNomRol(request.getParameter("nombre_rol"));
        r.setDescRol(request.getParameter("descripcion_rol"));
        r.setEstadoRol(request.getParameter("estado_rol"));
        return r;
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
