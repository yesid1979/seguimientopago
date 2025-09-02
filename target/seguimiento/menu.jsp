<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="layoutSidenav_nav">
    <nav class="sb-sidenav accordion sb-sidenav-light" id="sidenavAccordion">
        <div class="sb-sidenav-menu">
            <div class="nav">
                <div class="sb-sidenav-menu-heading">Inicio</div>
                <a class="nav-link" href="${pageContext.request.contextPath}/dashboard.jsp">
                    <div class="sb-nav-link-icon"><i class="fas fa-tachometer-alt"></i></div>
                    Dashboard
                </a>
                <div class="sb-sidenav-menu-heading">Datos maestros</div>
                <a class="nav-link collapsed" href="#" data-bs-toggle="collapse" data-bs-target="#collapseLayouts" aria-expanded="false" aria-controls="collapseLayouts">
                    <div class="sb-nav-link-icon"><i class="fas fa-columns"></i></div>
                    Registro de 
                    <div class="sb-sidenav-collapse-arrow"><i class="fas fa-angle-down"></i></div>
                </a>
                <div class="collapse" id="collapseLayouts" aria-labelledby="headingOne" data-bs-parent="#sidenavAccordion">
                    <nav class="sb-sidenav-menu-nested nav">
                        <a class="nav-link" href="${pageContext.request.contextPath}/clientes/listado_clientes.jsp">Contribuyentes</a>
                        <a class="nav-link" href="layout-sidenav-light.html">Predios</a>
                    </nav>
                </div>
                <a class="nav-link" href="notificaciones.jsp">
                    <div class="sb-nav-link-icon"><i class="fas fa-envelope"></i></div>
                    Notificaciones
                </a>
                <a class="nav-link" href="cobros.jsp">
                    <div class="sb-nav-link-icon"><i class="fas fa-money-bill"></i></div>
                    Cobros
                </a>
            </div>
        </div>
        <div class="sb-sidenav-footer">
            <div class="small">Conectado como:</div>
            Administrador
        </div>
    </nav>
</div>
