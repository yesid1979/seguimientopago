<jsp:include page="./../header.jsp"/>
<jsp:include page="./../menu.jsp"/>
<div id="layoutSidenav_content">
    <main>
        <div class="container-fluid px-4">
            <h1 class="mt-4">Roles</h1>
            <ol class="breadcrumb mb-4">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard.jsp">Dashboard</a></li>
                <li class="breadcrumb-item active">Listado de roles</li>
            </ol>
            <div class="card mb-4">
                <div class="card-body descripcion-tabla">
                    Esta tabla muestra el listado de roles definidos en el sistema.
                    Aquí podrás consultar información relevante como el nombre del rol, descripción y estado actual de cada rol.
                </div>
            </div>
            <div class="card mb-4">
                <div class="card-header">
                    <button id="btnAgregarRol" class="btn btn-primary"><i class="fa fa-plus-circle"></i> Nuevo rol</button>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table id="tablaRoles" class="table table-striped table-bordered table-hover table-sm" style="width:100%" >
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Nombre del rol</th>
                                    <th>Descripción</th>
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
    <!-- Modal para insertar/editar rol -->
    <div class="modal fade" id="modalRol" tabindex="-1" role="dialog" aria-labelledby="modalRolLabel" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content shadow-lg rounded-4 border-0">
                <form id="formRol">
                    <!-- Campos ocultos -->
                    <input type="hidden" name="accion" id="accionRol" value="insertar">
                    <input type="hidden" name="id_rol" id="id_rol">
                    <div class="modal-header bg-primary text-white">
                        <h5 class="modal-title" id="modalRolLabel">Gestión de Roles</h5>
                        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Cerrar">
                            <span aria-hidden="true"></span>
                        </button>
                    </div>
                    <div class="modal-body p-4">
                        <div class="row g-3">
                            <div class="col-md-6">
                                <label for="nombre_rol" class="form-label">Nombre del rol</label>
                                <input type="text" name="nombre_rol" id="nombre_rol" class="form-control" required>
                            </div>
                            <div class="col-md-6">
                                <label for="estado_rol" class="form-label">Estado</label>
                                <select name="estado_rol" id="estado_rol" class="form-select" required>
                                    <option value="Activo">Activo</option>
                                    <option value="Inactivo">Inactivo</option>
                                </select>
                            </div>
                        </div>
                        <div class="row mt-3">
                            <div class="col-md-12">
                                <label for="descripcion_rol" class="form-label">Descripción</label>
                                <textarea name="descripcion_rol" id="descripcion_rol" class="form-control" rows="4"></textarea>
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