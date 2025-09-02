<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <title>Gestión de Contribuyentes</title>
        <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.min.css">
        <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>
        <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
    </head>
    <body>
        <h1>Listado de Contribuyentes</h1>

        <table id="tablaContribuyentes" class="display" style="width:100%">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Celular</th>
                    <th>Correo</th>
                    <th>Dirección</th>
                    <th>Estado</th>
                </tr>
            </thead>
        </table>

        <script>
            $(document).ready(function () {
                $('#tablaContribuyentes').DataTable({
                    "processing": true,
                    "serverSide": true,
                    "ajax": {
                        "url": "../ControladorContribuyente.do",
                        "type": "POST"
                    },
                    "columns": [
                        {"data": 0},
                        {"data": 1},
                        {"data": 2},
                        {"data": 3},
                        {"data": 4},
                        {"data": 5}
                    ],
                    "language": {
                        "url": "https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json"
                    }
                });
            });
        </script>
    </body>
</html>
