<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="./../header.jsp"/>
<jsp:include page="./../menu.jsp"/>
<div id="layoutSidenav_content">
    <main>
        <div class="container-fluid px-4">
            <h1 class="mt-4">Usuarios</h1>
            <ol class="breadcrumb mb-4">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard.jsp">Dashboard</a></li>
                <li class="breadcrumb-item active">Listado de usuarios</li>
            </ol>
            <div class="card mb-4">
                <div class="card-body descripcion-tabla">
                    Esta tabla muestra el listado de contribuyentes registrados en el sistema.
                    Aquí podrás consultar información relevante como nombre y apellidos, número de celular, correo electrónico, dirección y estado actual de cada contribuyente.
                </div>
            </div>
            <div class="card mb-4">
                <div class="card-header">
                    <button id="btnAgregar" class="btn btn-primary"><i class="fa fa-plus-circle"></i> Nuevo usuario</button>
                    <!--button class="btn btn-danger btn-raised pull-right" id="btn_modal" data-toggle="modal" data-target="#myModal" title="Eliminar registros" style="float: right;"><i class="fas fa-trash-alt"></i> Eliminar</button!-->
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table id="tablaUsuarios" class="table table-striped table-bordered table-hover table-sm" style="width:100%" >
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>No. de cédula</th>
                                    <th>Nombres y apellidos</th>
                                    <th>E-Mail</th>
                                    <th>No. de celular</th>
                                    <th>Sexo</th>
                                    <th>Usuario</th>
                                    <th>Tipo de usuario</th>
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
<!-- Modal para insertar/editar usuario -->
<div class="modal fade" id="modalUsuario" tabindex="-1" role="dialog" aria-labelledby="modalUsuarioLabel" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
    <div class="modal-dialog modal-xl" role="document">
        <div class="modal-content">
            <form id="formUsuario">
                <!-- Campos ocultos -->
                <input type="hidden" name="accion" id="accionUsuario" value="insertar">
                <input type="hidden" name="id_usuario" id="id_usuario">

                <div class="modal-header bg-primary text-white">
                    <h5 class="modal-title" id="modalUsuarioLabel">Gestión de Usuario</h5>
                    <button type="button" class="close text-white" data-bs-dismiss="modal" aria-label="Cerrar">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>

                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-4">
                            <label for="ced_usuario">No. de cédula</label>
                            <input type="text" name="ced_usuario" id="ced_usuario" class="form-control" required>
                        </div>

                        <div class="col-md-4">
                            <label for="nom_usuario">Nombres y apellidos</label>
                            <input type="text" name="nom_usuario" id="nom_usuario" class="form-control" required>
                        </div>

                        <div class="col-md-4">
                            <label for="email_usuario">E-Mail</label>
                            <input type="email" name="email_usuario" id="email_usuario" class="form-control" required>
                        </div>
                    </div>

                    <div class="row mt-3">
                        <div class="col-md-4">
                            <label for="tel_usuario">No. de teléfono</label>
                            <input type="text" name="tel_usuario" id="tel_usuario" class="form-control">
                        </div>

                        <div class="col-md-4">
                            <label for="cel_usuario">No. de celular</label>
                            <input type="text" name="cel_usuario" id="cel_usuario" class="form-control">
                        </div>

                        <div class="col-md-4">
                            <label for="sexo_usuario">Sexo</label>
                            <select name="sexo_usuario" id="sexo_usuario" class="form-control">
                                <option value="">Seleccione</option>
                                <option value="Masculino">Masculino</option>
                                <option value="Femenino">Femenino</option>
                            </select>
                        </div>
                    </div>

                    <div class="row mt-3">
                        <div class="col-md-4">
                            <label for="cod_profesion">Profesión</label>
                            <select name="cod_profesion" id="cod_profesion" class="form-control">
                                <option value="">Seleccione una opcion</option>
                            </select>
                        </div>

                        <div class="col-md-4">
                            <label for="cod_rol">Tipo de usuario</label>
                            <select name="cod_rol" id="cod_rol" class="form-control" required>
                                <option value="">Seleccione una opcion</option>
                            </select>
                        </div>

                        <div class="col-md-4">
                            <label for="estado_usuario">Estado</label>
                            <select name="estado_usuario" id="estado_usuario" class="form-control" required>
                                <option value="Activo">Activo</option>
                                <option value="Inactivo">Inactivo</option>
                            </select>
                        </div>
                    </div>

                    <div class="row mt-3">
                        <div class="col-md-6">
                            <label for="login_usuario">Usuario (login)</label>
                            <input type="text" name="login_usuario" id="login_usuario" class="form-control" required>
                        </div>

                        <div class="col-md-6">
                            <label for="password_usuario">Contraseña</label>
                            <input type="password" name="password_usuario" id="password_usuario" class="form-control" required>
                        </div>
                    </div>

                    <!--div class="row mt-3">
                        <div class="col-md-6">
                            <label for="foto_usuario">Foto (URL)</label>
                            <input type="text" name="foto_usuario" id="foto_usuario" class="form-control">
                        </div>
                    </div!-->
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