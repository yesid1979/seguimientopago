package com.seguimiento.controlador;

import com.google.gson.Gson;
import com.seguimiento.bean.Dashboard;
import com.seguimiento.modelo.ModeloDashboard;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DashboardController extends HttpServlet {

    private final ModeloDashboard modelo = new ModeloDashboard();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Dashboard dashboard = modelo.obtenerDatosDashboard();
        List<Object[]> valorPendientePorPredio = modelo.getValorPendientePorPredio();
        List<Object[]> prediosPorEstado = modelo.getPrediosPorEstado();
        List<Object[]> prediosPorVeredaBarrio = modelo.getPrediosPorVeredaBarrio();
        List<Object[]> valorRecuperadoVsPendiente = modelo.getValorRecuperadoVsPendiente();
        List<Object[]> prediosPorVigencia = modelo.getPrediosPorVigencia();
        // Convertir el objeto Dashboard a JSON
        Gson gson = new Gson();
        // Crear un objeto para contener todos los datos
        Map<String, Object> data = new HashMap<>();
        data.put("dashboard", dashboard);
        data.put("valorPendientePorPredio", valorPendientePorPredio);
        data.put("prediosPorEstado", prediosPorEstado);
        data.put("prediosPorVeredaBarrio", prediosPorVeredaBarrio);
        data.put("valorRecuperadoVsPendiente", valorRecuperadoVsPendiente);
        data.put("prediosPorVigencia", prediosPorVigencia);

        // Convertir el objeto a JSON
        String json = gson.toJson(data);

        // Configurar la respuesta como JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
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
