<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="./../header.jsp"/>
<jsp:include page="./../menu.jsp"/>
<div id="layoutSidenav_content">
    <main>
        <div class="container-fluid px-4">
            <h1 class="mt-4">Predios</h1>
            <ol class="breadcrumb mb-4">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard.jsp">Dashboard</a></li>
                <li class="breadcrumb-item active">Listado de predios</li>
            </ol>
            <div class="card mb-4">
                <div class="card-body descripcion-tabla">
                    Esta tabla muestra el listado de predios registrados en el sistema.
                    Aquí podrás consultar información relevante como matrícula, ubicación, valor pendiente, contribuyente asociado y estado actual de cada predio.
                </div>
            </div>
            <div class="card mb-4">
                <div class="card-header">
                    <!--button id="btnAgregarPredio" class="btn btn-primary"><i class="fas fa-home"></i> Nuevo predio</button!-->
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table id="tablaNotificaciones" class="table table-striped table-bordered table-hover table-sm" style="width:100%" >
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Id Predio</th>
                                    <th>Matricula</th>
                                    <th>No. de recibo</th>
                                    <th>Cedula propietario</th>
                                    <th>Propietario</th>
                                    <th>Direccion</th>
                                    <th>Vereda o barrio</th>
                                    <th>Valor pendiente</th>
                                    <th>Vigencia</th>
                                    <th>Acciones</th>
                                </tr>
                            </thead>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <!-- Modal para detalles del predio y notificaciones -->
    <div class="modal fade" id="modalPredioDetalles" tabindex="-1" role="dialog" aria-labelledby="modalPredioDetallesLabel" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
        <div class="modal-dialog modal-xl" role="document">
            <div class="modal-content">
                <div class="modal-header bg-primary text-white">
                    <h5 class="modal-title" id="modalPredioDetallesLabel">Detalles del Predio</h5>
                    <button type="button" class="close text-white" data-bs-dismiss="modal" aria-label="Cerrar">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>

                <div class="modal-body">
                    <!-- Información básica del predio -->
                    <div class="row mb-4">
                        <div class="col-md-12">
                            <h4>Datos del Predio</h4>
                            <hr>
                            <div class="row">
                                <div class="col-md-3">
                                    <p><strong>ID:</strong> <span id="detalle_id_predio"></span></p>
                                </div>
                                <div class="col-md-3">
                                    <p><strong>Matrícula:</strong> <span id="detalle_matricula"></span></p>
                                </div>
                                <div class="col-md-3">
                                    <p><strong>No. de recibo:</strong> <span id="detalle_numrecibo"></span></p>
                                </div>
                                <div class="col-md-3">
                                    <p><strong>Cédula propietario:</strong> <span id="detalle_cedula_propietario"></span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-6">
                                    <p><strong>Propietario:</strong> <span id="detalle_propietario"></span></p>
                                </div>
                                <div class="col-md-6">
                                    <p><strong>Dirección:</strong> <span id="detalle_direccion"></span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-4">
                                    <p><strong>Vereda/Barrio:</strong> <span id="detalle_vereda_barrio"></span></p>
                                </div>
                                <div class="col-md-4">
                                    <p><strong>Valor pendiente:</strong> <span id="detalle_valor_pendiente"></span></p>
                                </div>
                                <div class="col-md-4">
                                    <p><strong>Vigencia:</strong> <span id="detalle_vigencia"></span></p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Sección de Notificaciones -->
                    <div class="row">
                        <div class="col-md-12">
                            <h4>Notificaciones Administrativas</h4>
                            <hr>
                            <div class="mb-3">
                                <button id="btnAgregarNotificacion" class="btn btn-success btn-sm"><i class="fas fa-plus"></i> Agregar Notificación</button>
                            </div>

                            <!-- Tabla de notificaciones -->
                            <table id="tablaNotificaciones2" class="table table-striped table-bordered table-hover table-sm">
                                <thead>
                                    <tr>
                                        <th>Fecha</th>
                                        <th>Hora</th>
                                        <th>Tipo</th>
                                        <th>Valor Enviado</th>
                                        <th>Agencia Envío</th>
                                        <th>Estado</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                                <tbody id="notificacionesBody">
                                    <!-- Las notificaciones se cargarán dinámicamente aquí -->
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Modal para agregar/editar notificación -->
    <div class="modal fade" id="modalNotificacion" tabindex="-1" role="dialog" aria-labelledby="modalNotificacionLabel" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
        <div class="modal-dialog modal-md" role="document">
            <div class="modal-content">
                <div class="modal-header bg-primary text-white">
                    <h5 class="modal-title" id="modalNotificacionLabel">Agregar Notificación</h5>
                    <button type="button" class="close text-white" data-bs-dismiss="modal" aria-label="Cerrar">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>

                <div class="modal-body">
                    <form id="formNotificacion">
                        <input type="hidden" name="accion" id="accionNotificacion" value="insertar">
                        <input type="text" name="cod_predio_notificacion" id="cod_predio_notificacion">

                        <div class="mb-3">
                            <label for="fecha_notificacion">Fecha</label>
                            <input type="date" name="fecha_notificacion" id="fecha_notificacion" class="form-control" required>
                        </div>

                        <div class="mb-3">
                            <label for="hora_notificacion">Hora</label>
                            <input type="time" name="hora_notificacion" id="hora_notificacion" class="form-control" required>
                        </div>

                        <div class="mb-3">
                            <label for="tipo_notificacion">Tipo de Notificación</label>
                            <select name="tipo_notificacion" id="tipo_notificacion" class="form-control" required>
                                <option value="">Seleccione...</option>
                                <option value="Pago recibido">Pago recibido</option>
                                <option value="Aviso vencimiento">Aviso vencimiento</option>
                                <option value="Recordatorio pago">Recordatorio pago</option>
                                <option value="Actualización de datos">Actualización de datos</option>
                                <option value="Notificación general">Notificación general</option>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label for="valor_enviado_notif">Valor Enviado</label>
                            <input type="number" name="valor_enviado_notif" id="valor_enviado_notif" class="form-control" step="0.01" min="0">
                        </div>

                        <div class="mb-3">
                            <label for="estado_notificacion">Estado</label>
                            <select name="estado_notificacion" id="estado_notificacion" class="form-control" required>
                                <option value="Pendiente">Pendiente</option>
                                <option value="Enviado">Enviado</option>
                                <option value="Fallido">Fallido</option>
                                <option value="Cancelado">Cancelado</option>
                            </select>
                        </div>

                        <div class="mb-3">
                            <label for="agencia_envio">Agencia de Envío</label>
                            <input type="text" name="agencia_envio" id="agencia_envio" class="form-control" placeholder="Ej: Correos de Costa Rica">
                        </div>

                        <div class="mb-3">
                            <label for="observacion_notificacion">Observación</label>
                            <textarea name="observacion_notificacion" id="observacion_notificacion" class="form-control" rows="3"></textarea>
                        </div>
                    </form>
                </div>

                <div class="modal-footer">
                    <button type="submit" form="formNotificacion" class="btn btn-success"><i class="fas fa-save"></i> Guardar</button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="./../footer.jsp"/>