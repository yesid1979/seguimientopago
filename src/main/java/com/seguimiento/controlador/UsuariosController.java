package com.seguimiento.controlador;

import com.google.gson.Gson;
import com.seguimiento.bean.Profesion;
import com.seguimiento.bean.Rol;
import com.seguimiento.bean.Usuario;
import com.seguimiento.modelo.ModeloUsuarios;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

public class UsuariosController extends HttpServlet {

    private final ModeloUsuarios modelo = new ModeloUsuarios();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }

        switch (accion) {
            case "listar":
                listarUsuariosDataTables(request, response);
                break;
            case "insertar":
                insertarUsuario(request, response);
                break;
            case "actualizar":
                actualizarUsuario(request, response);
                break;
            case "eliminar":
                eliminarUsuario(request, response);
                break;
            case "obtener":
                obtenerUsuarioPorId(request, response);
                break;
            case "listarProfesiones": // NUEVO
                listarProfesiones(request, response);
                break;
            case "listarRoles": // NUEVO
                listarRoles(request, response);
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
    private void listarUsuariosDataTables(HttpServletRequest request, HttpServletResponse response)
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

        List<Usuario> usuarios = modelo.listarUsuarios(start, length, searchValue, orderColumn, orderDir);
        int totalRegistros = modelo.contarTotalUsuarios();
        int registrosFiltrados = modelo.contarUsuariosFiltrados(searchValue);

        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("draw", draw);
        jsonResponse.put("recordsTotal", totalRegistros);
        jsonResponse.put("recordsFiltered", registrosFiltrados);
        jsonResponse.put("data", usuarios);

        out.print(new Gson().toJson(jsonResponse));
        out.flush();
    }

    // ================================
    // INSERTAR
    // ================================
    private void insertarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        System.out.println("llego");
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        Usuario u = construirUsuarioDesdeRequest(request);
        boolean exito = modelo.insertar(u);
        String mensaje = exito ? "Usuario registrado correctamente" : "Error al registrar el usuario";
        enviarRespuestaJSON(response, exito, mensaje);
    }

    // ================================
    // ACTUALIZAR
    // ================================
    private void actualizarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        Usuario u = construirUsuarioDesdeRequest(request);
        u.setIdUsuario(Integer.parseInt(request.getParameter("id_usuario")));
        boolean exito = modelo.actualizar(u);
        String mensaje = exito ? "Usuario actualizado correctamente" : "Error al actualizar el usuario";
        enviarRespuestaJSON(response, exito, mensaje);
    }

    // ================================
    // ELIMINAR
    // ================================
    private void eliminarUsuario(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        int idUsuario = Integer.parseInt(request.getParameter("id_usuario"));
        boolean exito = modelo.eliminar(idUsuario);
        String mensaje = exito ? "Usuario eliminado correctamente" : "Error al eliminar el usuario";
        enviarRespuestaJSON(response, exito, mensaje);
    }

    // ================================
    // OBTENER POR ID
    // ================================
    private void obtenerUsuarioPorId(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int idUsuario = Integer.parseInt(request.getParameter("id_usuario"));
        Usuario u = modelo.obtenerPorId(idUsuario);
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(u));
        out.flush();
    }

    // ================================
    // MÉTODOS AUXILIARES
    // ================================
    private Usuario construirUsuarioDesdeRequest(HttpServletRequest request) {
        Usuario u = new Usuario();
        u.setCedUsuario(request.getParameter("ced_usuario"));
        u.setNomUsuario(request.getParameter("nom_usuario"));
        u.setTelUsuario(request.getParameter("tel_usuario"));
        u.setCelUsuario(request.getParameter("cel_usuario"));
        u.setSexoUsuario(request.getParameter("sexo_usuario"));
        u.setEmailUsuario(request.getParameter("email_usuario"));
        u.setEstadoUsuario(request.getParameter("estado_usuario"));
        u.setLoginUsuario(request.getParameter("login_usuario"));
        u.setTpUsuario(request.getParameter("tp_usuario"));
        u.setFotoUsuario(request.getParameter("foto_usuario"));

        // Profesión opcional
        String codProfesion = request.getParameter("cod_profesion");
        if (codProfesion != null && !codProfesion.trim().isEmpty()) {
            Profesion profesion = new Profesion();
            profesion.setIdProfesion(Integer.parseInt(codProfesion));
            u.setProfesion(profesion);
        } else {
            u.setProfesion(null);
        }

        // Rol obligatorio
        String codRol = request.getParameter("cod_rol");
        if (codRol != null && !codRol.trim().isEmpty()) {
            Rol rol = new Rol();
            rol.setIdRol(Integer.parseInt(codRol));
            u.setRol(rol);
        }

        // Contraseña: codificar solo si se envía
        String password = request.getParameter("password_usuario");
        if (password != null && !password.trim().isEmpty()) {
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            u.setPasswordUsuario(hashedPassword);
        } else {
            // Si es actualización y no envía contraseña nueva, conserva la anterior
            String idUsuario = request.getParameter("id_usuario");
            if (idUsuario != null && !idUsuario.trim().isEmpty()) {
                Usuario usuarioExistente = modelo.obtenerPorId(Integer.parseInt(idUsuario));
                if (usuarioExistente != null) {
                    u.setPasswordUsuario(usuarioExistente.getPasswordUsuario());
                }
            }
        }

        return u;
    }

    /*  private void enviarRespuestaJSON(HttpServletResponse response, boolean exito)
            throws IOException {
        Usuario u = new Usuario();
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print("{\"success\":" + exito + "}");
        out.flush();
    }*/
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

    private void listarProfesiones(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        List<Profesion> profesiones = modelo.listarProfesiones();
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("data", profesiones);
        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(json));
        out.flush();
    }

    private void listarRoles(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        List<Rol> roles = modelo.listarRoles();
        Map<String, Object> json = new HashMap<>();
        json.put("success", true);
        json.put("data", roles);
        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(json));
        out.flush();
    }
}
