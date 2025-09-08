package com.seguimiento.controlador;

import com.google.gson.Gson;
import com.seguimiento.bean.Usuario;
import com.seguimiento.modelo.ModeloUsuarios;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.mindrot.jbcrypt.BCrypt;

public class LoginController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ModeloUsuarios modelo = new ModeloUsuarios();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");

        String usernameOrEmail = request.getParameter("usernameOrEmail");
        String password = request.getParameter("inputPassword");

        Map<String, Object> json = new HashMap<>();

        Usuario usuario = modelo.buscarPorLoginOEmail(usernameOrEmail);

        if (usuario == null) {
            json.put("success", false);
            json.put("message", "Usuario o correo no registrado.");
        } else if (!"Activo".equalsIgnoreCase(usuario.getEstadoUsuario())) {
            json.put("success", false);
            json.put("message", "El usuario se encuentra inactivo.");
        } else if (!BCrypt.checkpw(password, usuario.getPasswordUsuario())) {
            json.put("success", false);
            json.put("message", "Usuario o contraseña incorrectos.");
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuario);
            json.put("success", true);
            json.put("redirect", "dashboard.jsp");
         //   json.put("message", "Acceso concedido.");
        }

        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(json));
        out.flush();
    
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

}
