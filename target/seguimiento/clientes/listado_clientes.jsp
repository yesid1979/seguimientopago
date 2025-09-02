<%@ include file="./../header.jsp" %>
<%@ include file="./../menu.jsp" %>
<div id="layoutSidenav_content">
    <main>
        <div class="container-fluid px-4">
            <h1 class="mt-4">Contribuyentes</h1>
            <ol class="breadcrumb mb-4">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard.jsp">Dashboard</a></li>
                <li class="breadcrumb-item active">Listado de contribuyentes</li>
            </ol>
            <div class="card mb-4">
                <div class="card-body">
                    DataTables is a third party plugin that is used to generate the demo table below. For more information about DataTables, please visit the
                    <a target="_blank" href="https://datatables.net/">official DataTables documentation</a>
                    .
                </div>
            </div>
            <div class="card mb-4">
                <div class="card-header">
                    <i class="fas fa-table me-1"></i>
                    Listado de contribuyentes
                </div>
                <div class="card-body">
                    <table id="TblClientes" class="table table-striped table-bordered" style="width:100%" >
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Nombre y apellidos</th>
                                <th>No. de celular</th>
                                <th>E-Mail</th>
                                <th>Dirección</th>
                                <th>Estado</th>
                            </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </div>
    </main>
    <%@ include file="./../footer.jsp" %>
