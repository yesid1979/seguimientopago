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
                    Esta tabla muestra el listado de predios para hacer seguimiento a la gestion juridica.
                </div>
            </div>

            <!-- Tabla de predios -->
            <div class="card mb-4">
                <div class="card-body">
                    <div class="table-responsive">
                        <table id="tablaGestionJuridica" class="table table-striped table-bordered table-hover table-sm" style="width:100%">
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

    <!-- Modal Detalles del Predio con Cobros -->
    <div class="modal fade" id="modalGestionJuridicaDetalles" tabindex="-1" role="dialog" aria-labelledby="modalGestionJuridicaDetallesLabel" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
        <div class="modal-dialog modal-xl" role="document">
            <div class="modal-content shadow-lg rounded-4 border-0">
                <div class="modal-header bg-primary text-white">
                    <h5 class="modal-title" id="modalCobrosDetallesLabel">Detalles de Gestión Jurídica</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Cerrar">
                        <span aria-hidden="true"></span>
                    </button>
                </div>

                <div class="modal-body p-4">
                    <!-- Datos del predio -->
                    <div class="row mb-4">
                        <div class="col-md-12">
                            <div class="section-card">
                                <h4>Datos del Predio</h4>
                                <hr>
                                <div class="row">
                                    <div class="col-md-3"><p><strong>ID sistema:</strong> <span id="detalle_id"></span></p></div>
                                    <div class="col-md-3"><p><strong>ID predio:</strong> <span id="detalle_id_predio"></span></p></div>
                                    <div class="col-md-3"><p><strong>Matrícula:</strong> <span id="detalle_matricula"></span></p></div>
                                    <div class="col-md-3"><p><strong>No. de recibo:</strong> <span id="detalle_numrecibo"></span></p></div>
                                </div>
                                <div class="row">
                                    <div class="col-md-3"><p><strong>Cédula propietario:</strong> <span id="detalle_cedula_propietario"></span></p></div>
                                    <div class="col-md-3"><p><strong>Propietario:</strong> <span id="detalle_propietario"></span></p></div>
                                    <div class="col-md-3"><p><strong>Dirección:</strong> <span id="detalle_direccion"></span></p></div>
                                    <div class="col-md-3"><p><strong>Vereda/Barrio:</strong> <span id="detalle_vereda_barrio"></span></p></div>
                                </div>
                                <div class="row">
                                    <div class="col-md-3"><p><strong>Valor pendiente:</strong> <span id="detalle_valor_pendiente"></span></p></div>
                                    <div class="col-md-3"><p><strong>Vigencia:</strong> <span id="detalle_vigencia"></span></p></div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Cobros administrativos -->
                    <div class="row">
                        <div class="col-md-12">
                            <h4>Gestión Jurídica</h4>
                            <hr>
                            <div class="mb-3">
                                <button id="btnAgregarGestion" class="btn btn-success btn-sm">
                                    <i class="fas fa-plus"></i> Agregar gestion jurídica
                                </button>
                            </div>

                            <!-- Tabla de cobros -->
                            <table id="tablaGestionJurDetalle" class="table table-striped table-bordered table-hover table-sm" style="width:100%">
                                <thead>
                                    <tr>
                                        <th>Fecha mandamiento de pago</th>
                                        <th>No. de proceso de coactivo</th>
                                        <th>Etapa del proceso</th>
                                        <th>Valor recuperado vía embargo</th>
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

    <!-- Modal Agregar Cobro -->
    <div class="modal fade" id="modalGestionJuridicas" tabindex="-1" role="dialog" aria-labelledby="modalGestionJuridicasLabel" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content shadow-lg rounded-4 border-0">
                <div class="modal-header bg-primary text-white">
                    <h5 class="modal-title" id="modalCobroLabel">Agregar Gestion Juridica</h5>
                    <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Cerrar">
                        <span aria-hidden="true"></span>
                    </button>
                </div>

                <div class="modal-body p-4">
                    <form id="formGestionJuridica">
                        <input type="hidden" name="accion" value="insertar">
                        <input type="hidden" name="cod_predio" id="cod_predio_gestion">

                        <div class="row g-3">
                            <div class="col-md-6 mb-3">
                                <label for="fecha_mandamiento" class="form-label">Fecha de Seguimiento</label>
                                <input type="date" name="fecha_mandamiento" id="fecha_mandamiento" class="form-control" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="num_proceso" class="form-label">No de Proceso de coactivo</label>
                                <input type="text" name="num_proceso" id="num_proceso" class="form-control" required>
                            </div>
                            <div class="col-md-12 mb-3">
                                <label for="etapa_proceso" class="form-label">Etapa del Proceso</label>
                                <select name="etapa_proceso" id="etapa_proceso" class="form-select" required>
                                    <option value="-1">Seleccione una opción</option>
                                    <option value="Mandamiento">Mandamiento</option>
                                    <option value="Embargo">Embargo</option>
                                    <option value="Remate">Remate</option>
                                    <option value="Archivo">Archivo</option>
                                    <option value="Otra">Otra</option>
                                </select>
                            </div>

                            <div class="col-md-6 mb-3">
                                <label for="valor_recuperado" class="form-label">Valor Recuperado vía Embargo</label>
                                <input type="number" name="valor_recuperado" id="valor_recuperado" class="form-control" step="0.01" min="0">
                            </div>
                        </div>
                    </form>
                </div>

                <div class="modal-footer">
                    <button type="submit" form="formGestionJuridica" class="btn btn-success">
                        <i class="fas fa-save"></i> Guardar
                    </button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><i class="fas fa-times"></i> Cancelar</button>
                </div>
            </div>
        </div>
    </div>

    <jsp:include page="./../footer.jsp"/>
    <script src="${pageContext.request.contextPath}/js/TblGestionJuridica.js" charset="UTF-8"></script>
</div>
