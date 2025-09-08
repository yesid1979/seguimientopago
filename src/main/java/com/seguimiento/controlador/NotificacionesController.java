package com.seguimiento.controlador;

import com.google.gson.Gson;
import com.seguimiento.bean.Notificacion;
import com.seguimiento.modelo.ModeloNotificaciones;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NotificacionesController extends HttpServlet {

    private final ModeloNotificaciones modelo = new ModeloNotificaciones();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listar";
        }
        switch (accion) {
            case "listar":
                listarNotificaciones(request, response);
                break;
            case "insertar":
                insertarNotificacion(request, response);
                break;
            case "actualizar":
                actualizarNotificacion(request, response);
                break;
            case "eliminar":
                eliminarNotificacion(request, response);
                break;
            case "obtener":
                obtenerNotificacionPorId(request, response);
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
    // LISTAR NOTIFICACIONES
    // ================================
    private void listarNotificaciones(HttpServletRequest request, HttpServletResponse response)
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
        int codPredio = Integer.parseInt(request.getParameter("cod_predio"));

        List<Notificacion> notificaciones = modelo.listarNotificaciones(start, length, searchValue, orderColumn, orderDir, codPredio);
        int totalRegistros = modelo.contarTotalNotificaciones(codPredio);
        int registrosFiltrados = modelo.contarNotificacionesFiltradas(searchValue, codPredio);

        Map<String, Object> jsonResponse = new HashMap<>();
        jsonResponse.put("draw", draw);
        jsonResponse.put("recordsTotal", totalRegistros);
        jsonResponse.put("recordsFiltered", registrosFiltrados);
        jsonResponse.put("data", notificaciones);

        out.print(new Gson().toJson(jsonResponse));
        out.flush();
    }

    // ================================
    // INSERTAR NOTIFICACIÓN
    // ================================
    private void insertarNotificacion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");

        // Obtener parámetros del request
        String fechaNotificacion = request.getParameter("fecha_notificacion");
        String horaNotificacion = request.getParameter("hora_notificacion");
        String tipoNotificacion = request.getParameter("tipo_notificacion");
        String valorEnviadoParam = request.getParameter("valor_enviado");
        String estadoNotificacion = request.getParameter("estado_notificacion");
        String agenciaEnvio = request.getParameter("agencia_envio");
        String observacion = request.getParameter("observacion_notificacion");
        String responsables = request.getParameter("responsable");
        //  String codPredioParam = request.getParameter("cod_predio");
        //  System.out.println("Envio de codigo: "+codPredioParam);
        // Manejar el parámetro cod_predio de forma segura
        String codPredioParam = request.getParameter("cod_predio");
        int codPredio = 0;

        if (codPredioParam != null && !codPredioParam.trim().isEmpty()) {
            try {
                codPredio = Integer.parseInt(codPredioParam);
                System.out.println("Envio de codigo: " + codPredio);
            } catch (NumberFormatException e) {
                // Loggear el error pero continuar con el proceso
                System.err.println("Error al parsear cod_predio: " + e.getMessage());
            }
        }

        // Validar que tengamos los datos mínimos requeridos
        if (fechaNotificacion == null || horaNotificacion == null
                || tipoNotificacion == null || estadoNotificacion == null) {
            enviarRespuestaJSON(response, false, "Faltan datos obligatorios");
            return;
        }

        // Preparar el objeto Notificacion
        Notificacion notificacion = new Notificacion();
        notificacion.setFechaNotificacion(fechaNotificacion);
        notificacion.setHoraNotificacion(horaNotificacion);
        notificacion.setTipoNotificacion(tipoNotificacion);
        notificacion.setEstadoNotificacion(estadoNotificacion);
        notificacion.setAgenciaEnvio(agenciaEnvio);
        notificacion.setObservacion(observacion);
        notificacion.setResponsable(responsables);

        // Manejar valor_enviado de forma segura
        double valorEnviado = 0.0;
        try {
            valorEnviado = Double.parseDouble(valorEnviadoParam);
        } catch (NumberFormatException | NullPointerException e) {
            // Si falla, usar valor por defecto
            System.err.println("Error al parsear valor_enviado: " + e.getMessage());
        }
        notificacion.setValorEnviado(valorEnviado);

        // Asignar cod_predio si está disponible
        if (codPredio != 0) {
            notificacion.setCodPredio(codPredio);
        }

        // Procesar la inserción
        boolean exito = modelo.insertarNotificacion(notificacion);
        String mensaje = exito ? "Notificación registrada correctamente" : "Error al registrar la notificación";
        enviarRespuestaJSON(response, exito, mensaje);
    }

    // ================================
    // ACTUALIZAR NOTIFICACIÓN
    // ================================
    private void actualizarNotificacion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        Notificacion notificacion = construirNotificacionDesdeRequest(request);
        notificacion.setIdNotificacion(Integer.parseInt(request.getParameter("id_notificacion")));
        boolean exito = modelo.actualizarNotificacion(notificacion);
        String mensaje = exito ? "Notificación actualizada correctamente" : "Error al actualizar la notificación";
        enviarRespuestaJSON(response, exito, mensaje);
    }

    // ================================
    // ELIMINAR NOTIFICACIÓN
    // ================================
    private void eliminarNotificacion(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        int idNotificacion = Integer.parseInt(request.getParameter("id_notificacion"));
        boolean exito = modelo.eliminarNotificacion(idNotificacion);
        String mensaje = exito ? "Notificación eliminada correctamente" : "Error al eliminar la notificación";
        enviarRespuestaJSON(response, exito, mensaje);
    }

    // ================================
    // OBTENER NOTIFICACIÓN POR ID
    // ================================
    private void obtenerNotificacionPorId(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int idNotificacion = Integer.parseInt(request.getParameter("id_notificacion"));
        Notificacion notificacion = modelo.obtenerNotificacionPorId(idNotificacion);
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(new Gson().toJson(notificacion));
        out.flush();
    }

    // ================================
    // MÉTODOS AUXILIARES
    // ================================
    private Notificacion construirNotificacionDesdeRequest(HttpServletRequest request) {
        Notificacion notificacion = new Notificacion();
        notificacion.setIdNotificacion(Integer.parseInt(request.getParameter("id_notificacion")));
        notificacion.setCodPredio(Integer.parseInt(request.getParameter("cod_predio")));
        notificacion.setFechaNotificacion(request.getParameter("fecha_notificacion"));
        notificacion.setHoraNotificacion(request.getParameter("hora_notificacion"));
        notificacion.setTipoNotificacion(request.getParameter("tipo_notificacion"));
        notificacion.setValorEnviado(Double.parseDouble(request.getParameter("valor_enviado")));
        notificacion.setEstadoNotificacion(request.getParameter("estado_notificacion"));
        notificacion.setAgenciaEnvio(request.getParameter("agencia_envio"));
        notificacion.setObservacion(request.getParameter("observacion_notificacion"));
        notificacion.setResponsable(request.getParameter("responsable"));
        return notificacion;
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
