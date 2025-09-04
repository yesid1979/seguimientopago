<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="./../header.jsp"/>
<jsp:include page="./../menu.jsp"/>
<div id="layoutSidenav_content">
    <main>
        <div class="container-fluid px-4">
            <h1 class="mt-4">Contribuyente</h1>
            <ol class="breadcrumb mb-4">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard.jsp">Dashboard</a></li>
                <li class="breadcrumb-item active">Listado de contribuyente</li>
            </ol>
            <div class="card mb-4">
                <div class="card-body descripcion-tabla">
                    Esta tabla muestra el listado de contribuyentes registrados en el sistema.
                    Aquí podrás consultar información relevante como nombre y apellidos, número de celular, correo electrónico, dirección y estado actual de cada contribuyente.
                </div>
            </div>
            <div class="card mb-4">
                <div class="card-header">
                    <button id="btnAgregarContribuyente" class="btn btn-primary"><i class="fa fa-plus-circle"></i> Nuevo contribuyente</button>
                    <!--button class="btn btn-danger btn-raised pull-right" id="btn_modal" data-toggle="modal" data-target="#myModal" title="Eliminar registros" style="float: right;"><i class="fas fa-trash-alt"></i> Eliminar</button!-->
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table id="tablaContribuyentes" class="table table-striped table-bordered table-hover table-sm" style="width:100%" >
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>No. de cédula</th>
                                    <th>Nombre y Apellidos</th>
                                    <th>No. de teléfono</th>
                                    <th>E-Mail</th>
                                    <th>Dirección</th>
                                    <th>Estado</th>
                                    <th>Acciones</th>
                                </tr>

                            </thead>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </main>
    <!-- Modal para insertar/editar contribuyente -->
    <div class="modal fade" id="modalContribuyente" tabindex="-1" role="dialog" aria-labelledby="modalContribuyenteLabel" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
        <div class="modal-dialog modal-xl" role="document">
            <div class="modal-content">
                <form id="formContribuyente">
                    <!-- Campos ocultos -->
                    <input type="hidden" name="accion" id="accionContribuyente" value="insertar">
                    <input type="hidden" name="id_contribuyente" id="id_contribuyente">

                    <div class="modal-header bg-primary text-white">
                        <h5 class="modal-title" id="modalContribuyenteLabel">Gestión de Contribuyente</h5>
                        <button type="button" class="close text-white" data-bs-dismiss="modal" aria-label="Cerrar">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>

                    <div class="modal-body">
                        <div class="row">
                            <div class="col-md-4">
                                <label for="ced_contribuyente">No. de cédula</label>
                                <input type="text" name="ced_contribuyente" id="ced_contribuyente" class="form-control" required>
                            </div>
                            <div class="col-md-4">
                                <label for="nom_contribuyente">Nombres y apellidos</label>
                                <input type="text" name="nom_contribuyente" id="nom_contribuyente" class="form-control" required>
                            </div>
                            <div class="col-md-4">
                                <label for="correo_contribuyente">E-Mail</label>
                                <input type="email" name="correo_contribuyente" id="correo_contribuyente" class="form-control">
                            </div>
                        </div>

                        <div class="row mt-3">
                            <div class="col-md-4">
                                <label for="cel_contribuyente">No. de celular</label>
                                <input type="text" name="cel_contribuyente" id="cel_contribuyente" class="form-control">
                            </div>
                            <div class="col-md-8">
                                <label for="dir_contribuyente">Dirección</label>
                                <input type="text" name="dir_contribuyente" id="dir_contribuyente" class="form-control">
                            </div>
                        </div>

                        <div class="row mt-3">
                            <div class="col-md-4">
                                <label for="estado_contribuyente">Estado</label>
                                <select name="estado_contribuyente" id="estado_contribuyente" class="form-control" required>
                                    <option value="Activo">Activo</option>
                                    <option value="Inactivo">Inactivo</option>
                                </select>
                            </div>
                        </div>
                    </div>

                    <div class="modal-footer">
                        <button type="submit" class="btn btn-success"><i class="fas fa-save"></i> Guardar</button>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><i class="fas fa-times"></i> Cerrar</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <jsp:include page="./../footer.jsp"/>