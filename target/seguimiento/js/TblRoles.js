// TblRoles.js - Gestión de Roles con DataTables Server-Side
$(document).ready(function() {
    // Inicializar DataTable con Server-Side para Roles
    let tablaRoles = $('#tablaRoles').DataTable({
        processing: true,
        serverSide: true,
        responsive: true,
        ajax: {
            url: '../ControladorRol.do',
            type: 'POST',
            data: { accion: 'listar' }
        },
        columns: [
            { data: 'idRol' },
            { data: 'nomRol' },
            { data: 'descRol' },
            { 
                data: 'estadoRol',
                render: function (data, type, row) {
                    if (type === 'display' || type === 'filter') {
                        return data === 'Activo' 
                            ? '<span class="badge bg-success">Activo</span>' 
                            : '<span class="badge bg-secondary">Inactivo</span>';
                    }
                    return data; // para orden y búsqueda usa el valor original
                }
            },
            {
                data: null,
                orderable: false,
                render: function(data) {
                    return `
                        <button class="btn btn-sm btn-outline-primary editar" data-id="${data.idRol}" title="Editar">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-danger eliminar" data-id="${data.idRol}" title="Eliminar">
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
    // AGREGAR NUEVO ROL
    // ================================
    $('#btnAgregarRol').on('click', function () {
        $('#formRol')[0].reset();
        $('#accionRol').val('insertar');
        $('#modalRol').modal('show');
    });

    // Cuando el modal termine de mostrarse, poner el focus
    $('#modalRol').on('shown.bs.modal', function () {
        $('#nombre_rol').trigger('focus');
    });

    // ================================
    // EDITAR ROL
    // ================================
    $('#tablaRoles').on('click', '.editar', function () {
        let id = $(this).data('id');
        $.ajax({
            url: '../ControladorRol.do',
            type: 'POST',
            data: {accion: 'obtener', id_rol: id},
            dataType: 'json',
            success: function (data) {
                $('#id_rol').val(data.idRol);
                $('#nombre_rol').val(data.nomRol);
                $('#descripcion_rol').val(data.descRol);
                $('#estado_rol').val(data.estadoRol);
                $('#accionRol').val('actualizar');
                $('#modalRol').modal('show');
            }
        });
    });

    // ================================
    // GUARDAR (INSERTAR/ACTUALIZAR)
    // ================================
    $('#formRol').on('submit', function (e) {
        e.preventDefault();
        $.ajax({
            url: '../ControladorRol.do',
            type: 'POST',
            data: $(this).serialize() + '&accion=' + $('#accionRol').val(),
            dataType: 'json',
            success: function (response) {
                Swal.fire({
                    icon: response.success ? 'success' : 'error',
                    title: response.success ? '¡Éxito!' : '¡Error!',
                    text: response.message || 'Operación realizada'
                }).then(() => {
                    if (response.success) {
                        $('#modalRol').modal('hide');
                        tablaRoles.ajax.reload();
                    }
                });
            }
        });
    });

    // ================================
    // ELIMINAR
    // ================================
    $('#tablaRoles').on('click', '.eliminar', function () {
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
                    url: '../ControladorRol.do',
                    type: 'POST',
                    data: {accion: 'eliminar', id_rol: id},
                    dataType: 'json',
                    success: function (response) {
                        Swal.fire({
                            icon: response.success ? 'success' : 'error',
                            title: response.success ? '¡Eliminado!' : '¡Error!',
                            text: response.message || 'Operación realizada'
                        }).then(() => {
                            if (response.success) {
                                tablaRoles.ajax.reload();
                            }
                        });
                    }
                });
            }
        });
    });
});


