<jsp:include page="./../header.jsp"/>
<jsp:include page="./../menu.jsp"/>
<div id="layoutSidenav_content">
    <main>
        <div class="container-fluid px-4">
            <h1 class="mt-4">Contribuyentes</h1>
            <ol class="breadcrumb mb-4">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard.jsp">Dashboard</a></li>
                <li class="breadcrumb-item active">Listado de contribuyentes</li>
            </ol>
            <div class="card mb-4">
                <div class="card-body descripcion-tabla">
                    Esta tabla muestra el listado de contribuyentes registrados en el sistema.
                    Aquí podrás consultar información relevante como nombre y apellidos, número de celular, correo electrónico, dirección y estado actual de cada contribuyente.
                </div>
            </div>
            <div class="card mb-4">
                <div class="card-header">
                    <a href="${pageContext.request.contextPath}/ControladorConcepto.do?accion=nuevo" class="btn btn-primary" title="Nueva concepto"><i class="fa fa-plus-circle"></i> Nuevo</a>
                    <button class="btn btn-danger btn-raised pull-right" id="btn_modal" data-toggle="modal" data-target="#myModal" title="Eliminar registros" style="float: right;"><i class="fas fa-trash-alt"></i> Eliminar</button>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table id="TblClientes" class="table table-striped table-bordered table-hover table-sm" style="width:100%" >
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
        </div>
    </main>
    <jsp:include page="./../footer.jsp"/>
