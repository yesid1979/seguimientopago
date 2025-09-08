$(document).ready(function () {
    // ==============================
    // DATATABLE PRINCIPAL: PREDIOS
    // ==============================
    let tablaPredios = $('#tablaNotificaciones').DataTable({
        processing: true,
        serverSide: true,
        responsive: true,
        ajax: {
            url: '../ControladorPredios.do',
            type: 'POST',
            data: {accion: 'listar'}
        },
        columns: [
            {data: 'nroPredio'},
            {data: 'idPredio'},
            {data: 'matriculaPredio'},
            {data: 'numreciboPredio'},
            {data: 'contribuyente.cedContribuyente'},
            {data: 'contribuyente.nomContribuyente'},
            {data: 'dirPredio'},
            {data: 'veredaBarrio'},
            {
                data: 'valorPendiente',
                render: $.fn.dataTable.render.number('.', ',', 0, '$')
            },
            {data: 'vigenciaPredio'},
            {
                data: null,
                orderable: false,
                render: function (data, type, row) {
                    return `
                        <button class="btn btn-info btn-sm ver-detalles" data-id="${data.nroPredio}">
                            <i class="fas fa-eye"></i> Ver
                        </button>
                    `;
                }
            }
        ],
        language: {
            url: 'https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json'
        }
    });

    // ==============================
    // VER DETALLES DEL PREDIO
    // ==============================
    $('#tablaNotificaciones').on('click', '.ver-detalles', function () {
        let id = $(this).data('id');
        $.ajax({
            url: '../ControladorPredios.do',
            type: 'POST',
            data: {accion: 'obtener', nro_predio: id},
            dataType: 'json',
            success: function (data) {
                // Rellenar datos en el modal
                $('#detalle_id').text(data.nroPredio);
                $('#detalle_id_predio').text(data.idPredio);
                $('#detalle_matricula').text(data.matriculaPredio);
                $('#detalle_numrecibo').text(data.numreciboPredio);
                $('#detalle_cedula_propietario').text(data.contribuyente.cedContribuyente);
                $('#detalle_propietario').text(data.contribuyente.nomContribuyente);
                $('#detalle_direccion').text(data.dirPredio);
                $('#detalle_vereda_barrio').text(data.veredaBarrio);
                $('#detalle_valor_pendiente').text(new Intl.NumberFormat('es-CO').format(data.valorPendiente));
                $('#detalle_vigencia').text(data.vigenciaPredio);

                // Asignar cod_predio para nuevas notificaciones
                $('#cod_predio_notificacion').val(data.nroPredio);
                // Mostrar modal
                $('#modalPredioDetalles').modal('show');

                // Cargar notificaciones relacionadas
                if ($.fn.DataTable.isDataTable('#tablaNotificaciones2')) {
                    $('#tablaNotificaciones2').DataTable().destroy();
                }
                cargarNotificaciones(data.nroPredio);
            }
        });
    });

    // ==============================
    // DATATABLE SECUNDARIA: NOTIFICACIONES
    // ==============================
    function cargarNotificaciones(cod_predio) {
        $('#tablaNotificaciones2').DataTable({
            processing: true,
            serverSide: true,
            responsive: true,
            ajax: {
                url: '../ControladorNotificaciones.do',
                type: 'POST',
                data: {accion: 'listar', cod_predio: cod_predio}
            },
            columns: [
                {data: 'fechaNotificacion'},
                {data: 'horaNotificacion'},
                {data: 'tipoNotificacion'},
                {data: 'valorEnviado', render: $.fn.dataTable.render.number('.', ',', 0, '$')},
                {data: 'agenciaEnvio'},
                {data: 'responsable'},
                {data: 'estadoNotificacion'},
                {
                    data: null,
                    orderable: false,
                    render: function (data, type, row) {
                        return `
                            <button class="btn btn-danger btn-sm eliminar-notificacion" data-id="${data.idNotificacion}">
                                <i class="fas fa-trash"></i>
                            </button>
                        `;
                    }
                }
            ],
            language: {
                url: 'https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json'
            }
        });
    }

    // ==============================
    // ELIMINAR NOTIFICACIÓN
    // ==============================
    $('#tablaNotificaciones2').on('click', '.eliminar-notificacion', function () {
        let id = $(this).data('id');

        Swal.fire({
            title: '¿Está seguro?',
            text: "Esta acción no se puede deshacer",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Sí, eliminar',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    url: '../ControladorNotificaciones.do',
                    type: 'POST',
                    data: {accion: 'eliminar', id_notificacion: id},
                    success: function (response) {
                        Swal.fire({
                            icon: response.success ? 'success' : 'error',
                            title: response.message,
                            showConfirmButton: true,
                            confirmButtonText: 'Aceptar'
                        });

                        // Recargar la tabla
                        $('#tablaNotificaciones2').DataTable().ajax.reload();
                    },
                    error: function () {
                        Swal.fire({
                            icon: 'error',
                            title: 'Error',
                            text: 'Hubo un problema al eliminar la notificación.'
                        });
                    }
                });
            }
        });
    });


    // ==============================
    // AGREGAR NOTIFICACIÓN
    // ==============================
    $('#btnAgregarNotificacion').on('click', function () {
        //  $('#formNotificacion')[0].reset();
        $('#modalNotificacion').modal('show');
    });

    $('#formNotificacion').on('submit', function (e) {
        e.preventDefault();

        $.ajax({
            url: '../ControladorNotificaciones.do',
            type: 'POST',
            data: $(this).serialize(),
            success: function (response) {
                Swal.fire({
                    icon: response.success ? 'success' : 'error',
                    title: response.message,
                    showConfirmButton: true,
                    confirmButtonText: 'Aceptar'
                });

                if (response.success) {
                    $('#modalNotificacion').modal('hide');
                    $('#tablaNotificaciones2').DataTable().ajax.reload();
                    $('#formNotificacion')[0].reset(); // Limpia el formulario
                }
            },
            error: function () {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'Hubo un problema al agregar la notificación.'
                });
            }
        });
    });
});
