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

            <!-- Descripción -->
            <div class="card mb-4">
                <div class="card-body descripcion-tabla">
                    Esta tabla muestra el listado de predios para hacerle notificaciones.
                    Aquí podrás consultar información relevante y administrar las notificaciones realizadas.
                </div>
            </div>

            <!-- Tabla de predios -->
            <div class="card mb-4">
                <div class="card-body">
                    <div class="table-responsive">
                        <table id="tablaNotificaciones" class="table table-striped table-bordered table-hover table-sm" style="width:100%">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Id Predio</th>
                                    <th>Matrícula</th>
                                    <th>No. de recibo</th>
                                    <th>Cédula propietario</th>
                                    <th>Propietario</th>
                                    <th>Dirección</th>
                                    <th>Vereda/Barrio</th>
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

    <!-- Modal Detalles del Predio -->
    <div class="modal fade" id="modalPredioDetalles" tabindex="-1" role="dialog" aria-labelledby="modalPredioDetallesLabel" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
        <div class="modal-dialog modal-xl" role="document">
            <div class="modal-content shadow-lg rounded-4 border-0">
                <div class="modal-header bg-primary text-white">
                    <h5 class="modal-title" id="modalPredioDetallesLabel">Detalles del Predio</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Cerrar">
                        <span aria-hidden="true"></span>
                    </button>
                </div>

                <div class="modal-body p-4">
                    <!-- Información básica del predio -->
                    <div class="row mb-4">
                        <div class="col-md-12">
                            <div class="section-card">
                            <h4>Datos del Predio</h4>
                            <hr>
                            <div class="row">
                                <div class="col-md-3">
                                    <p><strong>ID sistema:</strong> <span id="detalle_id"></span></p>
                                </div>
                                <div class="col-md-3">
                                    <p><strong>ID predio:</strong> <span id="detalle_id_predio"></span></p>
                                </div>
                                <div class="col-md-3">
                                    <p><strong>Matrícula:</strong> <span id="detalle_matricula"></span></p>
                                </div>
                                <div class="col-md-3">
                                    <p><strong>No. de recibo:</strong> <span id="detalle_numrecibo"></span></p>
                                </div>

                            </div>
                            <div class="row">
                                <div class="col-md-3">
                                    <p><strong>Cédula propietario:</strong> <span id="detalle_cedula_propietario"></span></p>
                                </div>
                                <div class="col-md-3">
                                    <p><strong>Propietario:</strong> <span id="detalle_propietario"></span></p>
                                </div>
                                <div class="col-md-3">
                                    <p><strong>Dirección:</strong> <span id="detalle_direccion"></span></p>
                                </div>
                                <div class="col-md-3">
                                    <p><strong>Vereda/Barrio:</strong> <span id="detalle_vereda_barrio"></span></p>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-3">
                                    <p><strong>Valor pendiente:</strong> <span id="detalle_valor_pendiente"></span></p>
                                </div>
                                <div class="col-md-3">
                                    <p><strong>Vigencia:</strong> <span id="detalle_vigencia"></span></p>
                                </div>
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
                                <button id="btnAgregarNotificacion" class="btn btn-success btn-sm">
                                    <i class="fas fa-plus"></i> Agregar Notificación
                                </button>
                            </div>

                            <!-- Tabla de notificaciones -->
                            <table id="tablaNotificaciones2" class="table table-striped table-bordered table-hover table-sm" style="width:100%">
                                <thead>
                                    <tr>
                                        <th>Fecha de notificación</th>
                                        <th>Hora</th>
                                        <th>Medio de notificación</th>
                                        <th>Valor enviado</th>
                                        <th>Agencia envío</th>
                                        <th>Responsable</th>
                                        <th>Estado actual</th>
                                        <th>Acciones</th>
                                    </tr>
                                </thead>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><i class="fas fa-times"></i> Cerrar</button>
                </div>
            </div>
        </div>
    </div>

<!-- Modal Agregar Notificación -->
<div class="modal fade" id="modalNotificacion" tabindex="-1" role="dialog" aria-labelledby="modalNotificacionLabel" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
    <div class="modal-dialog modal-lg" role="document"> <!-- CAMBIADO a modal-lg -->
        <div class="modal-content shadow-lg rounded-4 border-0" >
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title" id="modalNotificacionLabel">Agregar Notificación</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Cerrar">
                    <span aria-hidden="true"></span>
                </button>
            </div>

            <div class="modal-body p-4">
                <form id="formNotificacion">
                    <input type="hidden" name="accion" value="insertar">
                    <input type="hidden" name="cod_predio" id="cod_predio_notificacion">

                    <div class="row g-3">
                        <div class="col-md-6 mb-3">
                            <label for="fecha_notificacion" class="form-label">Fecha de Notificación</label>
                            <input type="date" name="fecha_notificacion" id="fecha_notificacion" class="form-control" required>
                        </div>

                        <div class="col-md-6 mb-3">
                            <label for="hora_notificacion" class="form-label">Hora</label>
                            <input type="time" name="hora_notificacion" id="hora_notificacion" class="form-control" required>
                        </div>

                        <div class="col-md-6 mb-3">
                            <label for="tipo_notificacion" class="form-label">Medio de Notificación</label>
                            <select name="tipo_notificacion" id="tipo_notificacion" class="form-select" required>
                                <option value="-1">Seleccione una opción</option>
                                <option value="Notificacion personal">Notificación personal</option>
                                <option value="Por correo certificado">Por correo certificado</option>
                                <option value="Aviso o Edicto">Aviso o Edicto</option>
                                <option value="Por publicacion en pagina web">Por publicación en página web</option>
                                <option value="Correo electrónico">Correo electrónico</option>
                                <option value="Otra">Otra</option>
                            </select>
                        </div>

                        <div class="col-md-6 mb-3">
                            <label for="valor_enviado" class="form-label">Valor Enviado</label>
                            <input type="number" name="valor_enviado" id="valor_enviado" class="form-control" step="0.01" min="0">
                        </div>

                        <div class="col-md-6 mb-3">
                            <label for="estado_notificacion" class="form-label">Estado actual</label>
                            <select name="estado_notificacion" id="estado_notificacion" class="form-select" required>
                                <option value="-1">Seleccione una opción</option>
                                <option value="Persuasivo">Persuasivo</option>
                                <option value="En Acuerdo ">En Acuerdo </option>
                                <option value="Coactivo">Coactivo</option>
                                <option value="Embargado">Embargado</option>
                                <option value="Pagado">Pagado</option>
                            </select>
                        </div>

                        <div class="col-md-6 mb-3">
                            <label for="agencia_envio" class="form-label">Agencia de Envío</label>
                            <input type="text" name="agencia_envio" id="agencia_envio" class="form-control" placeholder="Ej: Correos de Costa Rica">
                        </div>
                        <div class="col-md-12 mb-3">
                            <label for="responsable" class="form-label">Responsable del seguimiento</label>
                            <input type="text" name="responsable" id="responsable" class="form-control" placeholder="Funcionario encargado del caso">
                        </div>

                        <div class="col-md-12 mb-3">
                            <label for="observacion_notificacion" class="form-label">Observación</label>
                            <textarea name="observacion_notificacion" id="observacion_notificacion" class="form-control" rows="3"></textarea>
                        </div>
                    </div>
                </form>
            </div>

            <div class="modal-footer">
                <button type="submit" form="formNotificacion" class="btn btn-success"><i class="fas fa-save"></i> Guardar</button>
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><i class="fas fa-times"></i> Cancelar</button>
            </div>
        </div>
    </div>
</div>
<jsp:include page="./../footer.jsp"/>
<script src="${pageContext.request.contextPath}/js/TblNotificaciones.js" charset="UTF-8"></script>
