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
                    <button id="btnAgregarPredio" class="btn btn-primary"><i class="fas fa-home"></i> Nuevo predio</button>
                </div>
                <div class="card-body">
                    <div class="table-responsive">
                        <table id="tablaPredios" class="table table-striped table-bordered table-hover table-sm" style="width:100%" >
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

<!-- Modal para insertar/editar predio -->
<div class="modal fade" id="modalPredio" tabindex="-1" role="dialog" aria-labelledby="modalPredioLabel" aria-hidden="true" data-bs-backdrop="static" data-bs-keyboard="false">
    <div class="modal-dialog modal-xl" role="document">
        <div class="modal-content">
            <form id="formPredio">
                <!-- Campos ocultos -->
                <input type="hidden" name="accion" id="accionPredio" value="insertar">
                <input type="hidden" name="nro_predio" id="nro_predio">
                
                <div class="modal-header bg-primary text-white">
                    <h5 class="modal-title" id="modalPredioLabel">Gestión de Predio</h5>
                    <button type="button" class="close text-white" data-bs-dismiss="modal" aria-label="Cerrar">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-4">
                            <label for="id_predio">ID Predio</label>
                            <input type="text" name="id_predio" id="id_predio" class="form-control" required>
                        </div>
                        <div class="col-md-4">
                            <label for="matricula_predio">Matrícula</label>
                            <input type="text" name="matricula_predio" id="matricula_predio" class="form-control" required>
                        </div>
                        <div class="col-md-4">
                            <label for="numrecibo_predio">No. de recibo</label>
                            <input type="text" name="numrecibo_predio" id="numrecibo_predio" class="form-control">
                        </div>
                    </div>
                    
                    <div class="row mt-3">
                        <div class="col-md-6">
                            <label for="vereda_barrio">Vereda/Barrio</label>
                            <input type="text" name="vereda_barrio" id="vereda_barrio" class="form-control" required>
                        </div>
                        <div class="col-md-6">
                            <label for="dir_predio">Dirección</label>
                            <input type="text" name="dir_predio" id="dir_predio" class="form-control" required>
                        </div>
                    </div>
                    
                    <div class="row mt-3">
                        <div class="col-md-4">
                            <label for="ced_contribuyente">Cédula del Contribuyente</label>
                            <input type="text" name="ced_contribuyente" id="ced_contribuyente" class="form-control" required>
                        </div>
                        <div class="col-md-8">
                            <label for="nom_contribuyente">Nombre del Contribuyente</label>
                            <input type="text" name="nom_contribuyente" id="nom_contribuyente" class="form-control" readonly>
                        </div>
                    </div>
                    
                    <div class="row mt-3">
                        <div class="col-md-4">
                            <label for="valor_pendiente">Valor Pendiente</label>
                            <input type="number" name="valor_pendiente" id="valor_pendiente" class="form-control" step="0.01" min="0">
                        </div>
                        <div class="col-md-4">
                            <label for="valor_enviado">Valor Enviado</label>
                            <input type="number" name="valor_enviado" id="valor_enviado" class="form-control" step="0.01" min="0">
                        </div>
                        <div class="col-md-4">
                            <label for="vigencia_predio">Vigencia</label>
                            <input type="text" name="vigencia_predio" id="vigencia_predio" class="form-control">
                        </div>
                    </div>
                    
                    <div class="row mt-3">
                        <div class="col-md-12">
                            <label for="observacion">Observación</label>
                            <textarea name="observacion" id="observacion" class="form-control" rows="2"></textarea>
                        </div>
                    </div>
                    
                    <div class="row mt-3">
                        <div class="col-md-4">
                            <label for="estado_predio">Estado</label>
                            <select name="estado_predio" id="estado_predio" class="form-control" required>
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