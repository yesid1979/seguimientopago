// Inicializar DataTable con Server-Side para Contribuyentes
$(document).ready(function () {
    // ================================
    // DATATABLE SERVER-SIDE
    // ================================
    let tablaContribuyentes = $('#tablaContribuyentes').DataTable({
        processing: true,
        serverSide: true,
        responsive: true,
        ajax: {
            url: '../ControladorContribuyente.do',
            type: 'POST',
            data: {accion: 'listar'}
        },
        columns: [
            {data: 'idContribuyente'},
            {data: 'cedContribuyente'},
            {data: 'nomContribuyente'},
            {data: 'celContribuyente'},
            {data: 'correoContribuyente'},
            {data: 'dirContribuyente'},
            {data: 'estadoContribuyente'},
            {
                data: null,
                orderable: false,
                render: function (data) {
                    return `
                        <button class="btn btn-sm btn-outline-primary editar" data-id="${data.idContribuyente}" title="Editar">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-danger eliminar" data-id="${data.idContribuyente}" title="Eliminar">
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
    // AGREGAR NUEVO CONTRIBUYENTE
    // ================================
    $('#btnAgregarContribuyente').on('click', function () {
        $('#formContribuyente')[0].reset();
        $('#accionContribuyente').val('insertar');
        $('#modalContribuyente').modal('show');
        
    });

    // ================================
    // EDITAR CONTRIBUYENTE
    // ================================
    $('#tablaContribuyentes').on('click', '.editar', function () {
        let id = $(this).data('id');
        $.ajax({
            url: '../ControladorContribuyente.do',
            type: 'POST',
            data: {accion: 'obtener', id_contribuyente: id},
            dataType: 'json',
            success: function (data) {
                $('#id_contribuyente').val(data.idContribuyente);
                $('#ced_contribuyente').val(data.cedContribuyente);
                $('#nom_contribuyente').val(data.nomContribuyente);
                $('#cel_contribuyente').val(data.celContribuyente);
                $('#correo_contribuyente').val(data.correoContribuyente);
                $('#dir_contribuyente').val(data.dirContribuyente);
                $('#estado_contribuyente').val(data.estadoContribuyente);
                $('#accionContribuyente').val('actualizar');
                $('#modalContribuyente').modal('show');
            }
        });
    });

    // ================================
    // GUARDAR (INSERTAR/ACTUALIZAR)
    // ================================
    $('#formContribuyente').on('submit', function (e) {
        e.preventDefault();
        $.ajax({
            url: '../ControladorContribuyente.do',
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
                        $('#modalContribuyente').modal('hide');
                        tablaContribuyentes.ajax.reload();
                    }
                });
            }
        });
    });

    // ================================
    // ELIMINAR
    // ================================
    $('#tablaContribuyentes').on('click', '.eliminar', function () {
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
                    url: '../ControladorContribuyente.do',
                    type: 'POST',
                    data: {accion: 'eliminar', id_contribuyente: id},
                    dataType: 'json',
                    success: function (response) {
                        Swal.fire({
                            icon: response.success ? 'success' : 'error',
                            title: response.success ? '¡Eliminado!' : '¡Error!',
                            text: response.message || 'Operación realizada'
                        }).then(() => {
                            if (response.success) {
                                tablaContribuyentes.ajax.reload();
                            }
                        });
                    }
                });
            }
        });
    });
});