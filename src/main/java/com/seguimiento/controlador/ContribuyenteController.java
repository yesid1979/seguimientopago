package com.seguimiento.controlador;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.seguimiento.bean.Contribuyente;
import com.seguimiento.modelo.ModeloContribuyente;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ContribuyenteController extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        int draw = Integer.parseInt(request.getParameter("draw"));
        int start = Integer.parseInt(request.getParameter("start"));
        int length = Integer.parseInt(request.getParameter("length"));
        String search = request.getParameter("search[value]");
        String orderColumnIndex = request.getParameter("order[0][column]");
        String orderDir = request.getParameter("order[0][dir]");

        // Mapeo de índice de columna a nombre real en BD
        String[] columnas = {"id_contribuyente", "nom_contribuyente", "cel_contribuyente", "correo_contribuyente", "dir_contribuyente", "estado_contribuyente"};
        String orderColumn = columnas[Integer.parseInt(orderColumnIndex)];
        ModeloContribuyente modelo = new ModeloContribuyente();
        JsonObject jsonResponse = new JsonObject();
        int totalRecords = modelo.contarTotal();
        int filteredRecords = modelo.contarFiltrados(search);
        List<Contribuyente> lista = modelo.listarContribuyentes(start, length, search, orderColumn, orderDir);
        JsonArray data = new JsonArray();
        for (Contribuyente c : lista) {
            JsonArray row = new JsonArray();
            row.add(c.getIdContribuyente());
            row.add(c.getNomContribuyente());
            row.add(c.getCelContribuyente());
            row.add(c.getCorreoContribuyente());
            row.add(c.getDirContribuyente());
            row.add(c.getEstadoContribuyente());
            data.add(row);
        }
        jsonResponse.addProperty("draw", draw);
        jsonResponse.addProperty("recordsTotal", totalRecords);
        jsonResponse.addProperty("recordsFiltered", filteredRecords);
        jsonResponse.add("data", data);
        PrintWriter out = response.getWriter();
        out.print(jsonResponse.toString());
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
