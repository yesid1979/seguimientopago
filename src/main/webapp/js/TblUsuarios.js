// Inicializar DataTable con Server-Side
$(document).ready(function () {

    // ================================
    // FUNCIONES PARA CARGAR COMBOS
    // ================================
    function cargarProfesiones(selectedId) {
        const $select = $('#cod_profesion');
        $select.prop('disabled', true).empty().append('<option value="">Cargando...</option>');

        return $.ajax({
            url: '../ControladorUsuarios.do',
            type: 'POST',
            data: {accion: 'listarProfesiones'},
            dataType: 'json',
            success: function (response) {
                $select.empty().append('<option value="">Seleccione una opcion</option>');
                if (response.success && response.data) {
                    $.each(response.data, function (i, item) {
                        $select.append(new Option(item.nomProfesion, item.idProfesion));
                    });
                    if (selectedId)
                        $select.val(selectedId);
                }
            },
            complete: function () {
                $select.prop('disabled', false);
            }
        });
    }

    function cargarRoles(selectedId) {
        const $select = $('#cod_rol');
        $select.prop('disabled', true).empty().append('<option value="">Cargando...</option>');

        return $.ajax({
            url: '../ControladorUsuarios.do',
            type: 'POST',
            data: {accion: 'listarRoles'},
            dataType: 'json',
            success: function (response) {
                $select.empty().append('<option value="">Seleccione una opcion</option>');
                if (response.success && response.data) {
                    $.each(response.data, function (i, item) {
                        $select.append(new Option(item.nomRol, item.idRol));
                    });
                    if (selectedId)
                        $select.val(selectedId);
                }
            },
            complete: function () {
                $select.prop('disabled', false);
            }
        });
    }

    // ================================
    // DATATABLE SERVER-SIDE
    // ================================
    let tablaUsuarios = $('#tablaUsuarios').DataTable({
        processing: true,
        serverSide: true,
        responsive: true,
        ajax: {
            url: '../ControladorUsuarios.do',
            type: 'POST',
            data: {accion: 'listar'}
        },
        columns: [
            {data: 'idUsuario'},
            {data: 'cedUsuario'},
            {data: 'nomUsuario'},
            {data: 'emailUsuario'},
            {data: 'celUsuario'},
            {data: 'sexoUsuario'},
            {data: 'loginUsuario'},
            {data: 'rol.nomRol'},
            {data: 'estadoUsuario'},
            {
                data: null,
                orderable: false,
                render: function (data) {
                    return `
        <button class="btn btn-sm btn-outline-primary editar" data-id="${data.idUsuario}" title="Editar">
            <i class="fas fa-edit"></i>
        </button>
        <button class="btn btn-sm btn-outline-danger eliminar" data-id="${data.idUsuario}" title="Eliminar">
            <i class="fas fa-trash-alt"></i>
        </button>
    `;
                }
            }
        ],
        language: {
            url: 'https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json'
        }
    });

    // ================================
    // AGREGAR NUEVO USUARIO
    // ================================
    $('#btnAgregar').on('click', function () {
        $('#formUsuario')[0].reset();
        $('#accionUsuario').val('insertar');

        $.when(cargarProfesiones(), cargarRoles()).done(function () {
            $('#modalUsuario').modal('show');
        });
    });

    // ================================
    // EDITAR USUARIO
    // ================================
    $('#tablaUsuarios').on('click', '.editar', function () {
        let id = $(this).data('id');
        $.ajax({
            url: '../ControladorUsuarios.do',
            type: 'POST',
            data: {accion: 'obtener', id_usuario: id},
            dataType: 'json',
            success: function (data) {
                $.when(
                        cargarProfesiones(data.profesion ? data.profesion.idProfesion : ''),
                        cargarRoles(data.rol ? data.rol.idRol : '')
                        ).done(function () {
                    $('#id_usuario').val(data.idUsuario);
                    $('#ced_usuario').val(data.cedUsuario);
                    $('#nom_usuario').val(data.nomUsuario);
                    $('#email_usuario').val(data.emailUsuario);
                    $('#tel_usuario').val(data.telUsuario);
                    $('#cel_usuario').val(data.celUsuario);
                    $('#login_usuario').val(data.loginUsuario);
                    $('#sexo_usuario').val(data.sexoUsuario);
                    $('#accionUsuario').val('actualizar');
                    $('#modalUsuario').modal('show');
                });
            }
        });
    });

    // ================================
    // GUARDAR (INSERTAR/ACTUALIZAR)
    // ================================
    $('#formUsuario').on('submit', function (e) {
        e.preventDefault();
        $.ajax({
            url: '../ControladorUsuarios.do',
            type: 'POST',
            data: $(this).serialize(),
            dataType: 'json',
            success: function (response) {
                Swal.fire({
                    icon: response.success ? 'success' : 'error',
                    title: response.success ? '¡Éxito!' : '¡Error!',
                    text: response.message || 'Operación realizada'
                }).then(() => {
                    if (response.success) {
                        $('#modalUsuario').modal('hide');
                        tablaUsuarios.ajax.reload();
                    }
                });
            }
        });
    });

    // ================================
    // ELIMINAR
    // ================================
    $('#tablaUsuarios').on('click', '.eliminar', function () {
        let id = $(this).data('id');
        Swal.fire({
            title: '¿Estás seguro?',
            text: "¡Esta acción no se puede deshacer!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Sí, eliminar',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: '../ControladorUsuarios.do',
                    type: 'POST',
                    data: {accion: 'eliminar', id_usuario: id},
                    dataType: 'json',
                    success: function (response) {
                        Swal.fire({
                            icon: response.success ? 'success' : 'error',
                            title: response.success ? '¡Eliminado!' : '¡Error!',
                            text: response.message || 'Operación realizada'
                        }).then(() => {
                            if (response.success) {
                                tablaUsuarios.ajax.reload();
                            }
                        });
                    }
                });
            }
        });
    });
});
