<jsp:include page="./../header.jsp"/>
<jsp:include page="./../menu.jsp"/>
<div id="layoutSidenav_content">
    <main>
        <div class="container-fluid px-4">
            <h1 class="mt-4">Mis datos</h1>
            <ol class="breadcrumb mb-4">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/dashboard.jsp">Dashboard</a></li>
                <li class="breadcrumb-item active">Actualizar mi informacion</li>
            </ol>
            <div class="card mb-4">
                <div class="card-body descripcion-tabla">
                    En esta pantalla en donde se podra actualizar la informacion que se encuentra registrada en el sistema.
                </div>
            </div>
            <div class="card mb-4">
                <div class="card-header">
                    <h3>Informacion registrada</h3>
                </div>
                <div class="card-body">
                    <form id="formPerfil" enctype="multipart/form-data">
                        <input type="hidden" name="accion" id="accionUsuario">
                        <div class="row gx-3 gy-3">
                            <!-- FILA 1 -->
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="id_usuario" class="form-label">ID Usuario:</label>
                                    <input type="text" id="id_usuario" name="id_usuario" class="form-control" readonly value="${sessionScope.usuario.idUsuario}">
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="login_usuario" class="form-label">Usuario (login):</label>
                                    <input type="text" id="login_usuario" name="login_usuario" class="form-control" readonly>
                                </div>
                            </div>

                            <!-- FILA 2 -->
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="email_usuario" class="form-label">Correo:</label>
                                    <input type="email" id="email_usuario" name="email_usuario" class="form-control" readonly>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="ced_usuario" class="form-label">No. de documento:</label>
                                    <input type="text" id="ced_usuario" name="ced_usuario" class="form-control">
                                </div>
                            </div>

                            <!-- FILA 3 -->
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="nom_usuario" class="form-label">Nombre y apellidos:</label>
                                    <input type="text" id="nom_usuario" name="nom_usuario" class="form-control">
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="cel_usuario" class="form-label">No. de celular:</label>
                                    <input type="text" id="cel_usuario" name="cel_usuario" class="form-control">
                                </div>
                            </div>

                            <!-- FILA 4 -->
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="sexo_usuario" class="form-label">Sexo:</label>
                                    <select id="sexo_usuario" name="sexo_usuario" class="form-select">
                                        <option value="">Seleccione</option>
                                        <option value="M">Masculino</option>
                                        <option value="F">Femenino</option>
                                    </select>
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="cod_profesion" class="form-label">Profesión:</label>
                                    <select id="cod_profesion" name="cod_profesion" id="cod_profesion" class="form-select" style="width: 100%;"></select>
                                </div>
                            </div>

                            <!-- FILA 5 -->
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="password_usuario" class="form-label">Contraseña nueva:</label>
                                    <input type="password" id="password_usuario" name="password_usuario" class="form-control" placeholder="Dejar vacío para mantener la actual">
                                </div>
                            </div>
                            <div class="col-md-6">
                                <div class="mb-3">
                                    <label for="foto_usuario" class="form-label">Foto:</label>
                                    <input type="file" id="foto_usuario" name="foto_usuario" accept="image/*" class="form-control">
                                    <!-- preview opcional -->
                                    <div id="previewContainer" class="mt-2">
                                        <!-- Si quieres mostrar la foto actual, puedes poner aquí un <img> desde el servidor -->
                                        <!-- <img id="fotoPreview" src="uploads/usuario.jpg" class="img-thumbnail" style="max-width:150px;" /> -->
                                    </div>
                                </div>
                            </div>

                            <!-- BOTÓN -->
                            <div class="col-12 ">
                                <button type="submit" class="btn btn-success"><i class="fas fa-save"></i> Guardar</button>
                                <!--button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><i class="fas fa-times"></i> Cerrar</button!-->
                                <a href="${pageContext.request.contextPath}/dashboard.jsp" class="btn btn-secondary"><i class="fas fa-times"></i> Cerrar</a>
                            </div>
                        </div> <!-- row -->
                    </form>
                </div>
            </div>
        </div>
    </main>
    
    <jsp:include page="./../footer.jsp"/>
