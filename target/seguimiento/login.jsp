<%@page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix = "c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix = "sql"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
        <link rel="icon" type="image/png" sizes="32x32" href="${pageContext.request.contextPath}/img/favicon-32x32.png">
        <title>Ingreso al sistema</title>
        <link href="css/styles.css" rel="stylesheet" />
        <script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <style>
            body {
                background-color: #ffffff !important;
            }
            .card-body form {
                padding: 2rem;
            }
            .form-control {
                border-radius: 0.5rem;
                border: 1.5px solid #ccc;
                padding: 1.1rem 1rem;
                font-size: 1.1rem;
                transition: border-color 0.3s ease, box-shadow 0.3s ease;
            }
            .form-control:focus {
                border-color: #007bff;
                box-shadow: 0 0 8px rgba(0, 123, 255, 0.5);
                outline: none;
            }
            .form-check-label {
                font-weight: 500;
            }
            .btn-primary {
                width: 100%;
                padding: 1rem 0;
                font-size: 1.1rem;
                border-radius: 0.5rem;
                transition: background-color 0.3s ease;
            }
            .btn-primary:hover {
                background-color: #0056b3;
            }
            .form-floating > label {
                padding-left: 1rem;
                font-size: 1.1rem;
            }
            .form-floating > input {
                height: 3.5rem;
                padding-left: 1rem;
                font-size: 1.1rem;
            }
            /* Diseño dividido con fondo blanco y ancho aumentado */
            .split-card {
                display: flex;
                overflow: hidden;
                border-radius: 0.5rem;
                box-shadow: 0 0 15px rgba(0,0,0,0.1);
                margin-top: 3rem;
                background-color: #ffffff;
                max-width: 900px; /* Ancho aumentado */
                margin-left: auto;
                margin-right: auto;
            }
            .split-left, .split-right {
                flex: 1;
                padding: 2.5rem;
            }
            .split-left {
                display: flex;
                flex-direction: column;
                justify-content: center;
                background-color: #f8f9fa; /* Fondo del recuadro del formulario */
            }
            .split-right {
                color: #444;
                display: flex;
                flex-direction: column;
                justify-content: center;
                align-items: center;
                text-align: center;
                background-color: #ffffff; /* Fondo en blanco */
            }
            .split-right h2 {
                font-weight: 700;
                font-size: 1.8rem;
            }
            .split-right i {
                font-size: 4rem;
                margin-bottom: 1rem;
                color: #007bff;
            }
            @media (max-width: 768px) {
                .split-card {
                    flex-direction: column;
                    max-width: 100%;
                }
                .split-right, .split-left {
                    padding: 2rem 1rem;
                }
            }
        </style>
    </head>
    <body>
        <div id="layoutAuthentication">
            <div id="layoutAuthentication_content">
                <main>
                    <div class="container">
                        <div class="row justify-content-center">
                            <div class="col-lg-12">
                                <div class="split-card">
                                    <div class="split-left">
                                        <h3 class="text-center font-weight-light mb-4">Ingreso al sistema</h3>
                                        <form id="loginForm"  method="post">
                                            <div class="mb-3 input-group">
                                                <span class="input-group-text"><i class="fas fa-user"></i></span>
                                                <input class="form-control" id="usernameOrEmail" name="usernameOrEmail" type="text" placeholder="Nombre de usuario o correo" aria-label="Nombre de usuario o correo" required/>
                                            </div>
                                            <div class="mb-3 input-group">
                                                <span class="input-group-text"><i class="fas fa-lock"></i></span>
                                                <input class="form-control" id="inputPassword" name="inputPassword" type="password" placeholder="Contraseña" aria-label="Contraseña" required/>
                                            </div>
                                            <div class="form-check mb-3">
                                                <input class="form-check-input" id="inputRememberPassword" type="checkbox" value="" />
                                                <label class="form-check-label" for="inputRememberPassword">Recordar contraseña</label>
                                            </div>
                                            <div class="d-flex align-items-center justify-content-between mt-4 mb-0">
                                                <button class="btn btn-primary" type="submit">Enviar</button>
                                            </div>
                                        </form>
                                        <div class="card-footer text-center py-3 mt-3">
                                            <div id="loginMessage" class="text-danger mt-3"></div>
                                            <!--div class="small"><a href="register.html">¿No tienes cuenta? Regístrate</a></div!-->
                                        </div>
                                    </div>
                                    <div class="split-right">
                                        <img src="${pageContext.request.contextPath}/img/logoCartera4.png" alt="Descripción de la imagen" width="320" />
                                        <h2>Bienvenido</h2>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
            <div id="layoutAuthentication_footer">
                <footer class="py-4 bg-light mt-auto">
                    <div class="container-fluid px-4">
                        <div class="d-flex align-items-center justify-content-between small">
                            <div class="text-muted">2025 Copyright &copy; Fundación Acción Social Humana</div>
                            <div>
                                <a href="#">Política de Privacidad</a>
                                &middot;
                                <a href="#">Términos y Condiciones</a>
                            </div>
                        </div>
                    </div>
                </footer>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
        <script src="js/scripts.js"></script>
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script>
            $(document).ready(function () {
                // Función para obtener parámetros de la URL
                function getParameterByName(name) {
                    const url = new URL(window.location.href);
                    return url.searchParams.get(name);
                }

                // Al cargar la página, verificar si hay un error
                // document.addEventListener('DOMContentLoaded', function () {
                const error = getParameterByName('error');
                const inputUsername = document.getElementById('inputUsername'); // Asegúrate de que el ID coincida con tu campo

                if (error === 'prohibido') {
                    Swal.fire({
                        icon: 'warning',
                        title: 'Acceso Denegado',
                        text: 'Usted no tiene permiso para acceder a esta página.',
                        confirmButtonText: 'Aceptar'
                    }).then(() => {
                        inputUsername.focus(); // Enfoca el campo después de cerrar la alerta
                    });
                } else if (error === 'expirada') {
                    Swal.fire({
                        icon: 'error',
                        title: 'Sesión expirada',
                        text: 'Su sesión ha expirado. Por favor inicie sesión nuevamente.',
                        confirmButtonText: 'Aceptar'
                    }).then(() => {
                        inputUsername.focus(); // Enfoca el campo después de cerrar la alerta
                    });
                }
                // });

                $("#loginForm").on("submit", function (e) {
                    e.preventDefault();
                    $.ajax({
                        url: "${pageContext.request.contextPath}/ControladorLogin.do",
                        type: "POST",
                        data: $(this).serialize(),
                        dataType: "json",
                        success: function (response) {
                            if (response.success) {
                                window.location.href = response.redirect;
                            } else {
                                $("#loginMessage").text(response.message);
                                $('#usernameOrEmail').val("");
                                $('#inputPassword').val("");
                                $('#usernameOrEmail').focus();
                            }
                        },
                        error: function () {
                            $("#loginMessage").text("Error de conexión con el servidor.");
                            $('#usernameOrEmail').val("");
                            $('#inputPassword').val("");
                            $('#usernameOrEmail').focus();
                        }
                    });
                });
            });
        </script>
    </body>
</html>
