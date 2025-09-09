package com.seguimiento.config;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter("/*")
public class FiltroLogin implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession sesion = req.getSession(false);
        String uri = req.getRequestURI();

        boolean esLogin = uri.endsWith("/login.jsp")
                || uri.endsWith("/ControladorUsuario.do")
                || uri.endsWith("/ControladorLogin.do")
                || uri.endsWith("/ControladorSalir.do");

        boolean esAdminRaiz = uri.equals(req.getContextPath() + "/");

        boolean esRecursoEstatico = uri.contains("/css/")
                || uri.contains("/js/")
                || uri.contains("/img/")
                || uri.contains("/assets/");

        if (esLogin || esAdminRaiz || esRecursoEstatico) {
            chain.doFilter(request, response);
            return;
        }

        // Caso 1: Sesión inexistente → expirada
        if (sesion == null) {
            res.sendRedirect(req.getContextPath() + "/login.jsp?error=expirada");
            return;
        }

        // Caso 2: Sesión existe pero usuario no autenticado → prohibido
        if (sesion.getAttribute("usuario") == null) {
            res.sendRedirect(req.getContextPath() + "/login.jsp?error=prohibido");
            return;
        }

        // Si quieres verificar roles, agrega aquí
        // Ejemplo:
        // String rol = (String) sesion.getAttribute("rol");
        // if (!"ADMIN".equals(rol) && uri.contains("/admin/")) {
        //     res.sendRedirect(req.getContextPath() + "/login.jsp?error=prohibido");
        //     return;
        // }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
