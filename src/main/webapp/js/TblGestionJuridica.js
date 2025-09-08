$(document).ready(function () {
    // ==============================
    // DATATABLE PRINCIPAL: PREDIOS CON GESTIÓN JURÍDICA
    // ==============================
    let tablaPredios = $('#tablaGestionJuridica').DataTable({
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
                render: function (data) {
                    return `
                        <button class="btn btn-info btn-sm ver-gestion" data-id="${data.nroPredio}">
                            <i class="fas fa-balance-scale"></i> Gestión jurídica
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
    // VER DETALLES DE GESTIÓN JURÍDICA DEL PREDIO (modal)
    // ==============================
    $('#tablaGestionJuridica').on('click', '.ver-gestion', function () {
        let id = $(this).data('id');

        $.ajax({
            url: '../ControladorPredios.do',
            type: 'POST',
            data: {accion: 'obtener', nro_predio: id},
            dataType: 'json',
            success: function (data) {
                // Rellenar datos del predio en el modal (ajusta ids si difieren)
                $('#detalle_id').text(data.nroPredio || '');
                $('#detalle_id_predio').text(data.idPredio || '');
                $('#detalle_matricula').text(data.matriculaPredio || '');
                $('#detalle_numrecibo').text(data.numreciboPredio || '');
                $('#detalle_cedula_propietario').text((data.contribuyente && data.contribuyente.cedContribuyente) || '');
                $('#detalle_propietario').text((data.contribuyente && data.contribuyente.nomContribuyente) || '');
                $('#detalle_direccion').text(data.dirPredio || '');
                $('#detalle_vereda_barrio').text(data.veredaBarrio || '');
                $('#detalle_valor_pendiente').text(data.valorPendiente != null ? new Intl.NumberFormat('es-CO').format(data.valorPendiente) : '');
                $('#detalle_vigencia').text(data.vigenciaPredio || '');

                // Asignar cod_predio para nuevas gestiones jurídicas
                $('#cod_predio_gestion').val(data.nroPredio);

                // Mostrar modal de detalles
                $('#modalGestionJuridicaDetalles').modal('show');

                // Cargar la tabla secundaria de gestión jurídica
                if ($.fn.DataTable.isDataTable('#tablaGestionJurDetalle')) {
                    $('#tablaGestionJurDetalle').DataTable().destroy();
                }
                cargarGestionJuridica(data.nroPredio);
            },
            error: function (xhr) {
                console.error('Error al obtener predio:', xhr.responseText);
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'No se pudo cargar los datos del predio.'
                });
            }
        });
    });

    // ==============================
    // DATATABLE SECUNDARIA: GESTIÓN JURÍDICA
    // ==============================
    function cargarGestionJuridica(cod_predio) {
        $('#tablaGestionJurDetalle').DataTable({
            processing: true,
            serverSide: true,
            responsive: true,
            ajax: {
                url: '../ControladorGestionJuridica.do',
                type: 'POST',
                data: {accion: 'listar', cod_predio: cod_predio}
            },
            columns: [
                {data: 'fechaMandamiento'}, // dd/MM/yyyy si lo formatea el backend
                {data: 'numProceso'},
                {data: 'etapaProceso'},
                {data: 'valorRecuperado', render: $.fn.dataTable.render.number('.', ',', 2, '$')},
                {
                    data: null,
                    orderable: false,
                    render: function (data) {
                        return `
                            <button class="btn btn-danger btn-sm eliminar-gestion" data-id="${data.idGestion}">
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
    // ELIMINAR GESTIÓN JURÍDICA
    // ==============================
    $('#tablaGestionJurDetalle').on('click', '.eliminar-gestion', function () {
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
                    url: '../ControladorGestionJuridica.do',
                    type: 'POST',
                    data: {accion: 'eliminar', id_gestion: id},
                    success: function (response) {
                        // response debe ser { success: true/false, message: "..." }
                        Swal.fire({
                            icon: response.success ? 'success' : 'error',
                            title: response.message,
                            showConfirmButton: true,
                            confirmButtonText: 'Aceptar'
                        });

                        // Recargar la tabla secundaria
                        $('#tablaGestionJurDetalle').DataTable().ajax.reload();
                    },
                    error: function () {
                        Swal.fire({
                            icon: 'error',
                            title: 'Error',
                            text: 'Hubo un problema al eliminar la gestión jurídica.'
                        });
                    }
                });
            }
        });
    });

    // ==============================
    // AGREGAR GESTIÓN JURÍDICA
    // ==============================
    $('#btnAgregarGestion').on('click', function () {
        $('#formGestionJuridica')[0].reset();
        $('#modalGestionJuridicas').modal('show');
    });

    $('#formGestionJuridica').on('submit', function (e) {
        e.preventDefault();

        $.ajax({
            url: '../ControladorGestionJuridica.do',
            type: 'POST',
            data: $(this).serialize(), // Asegúrate que el form tenga <input name="accion" value="insertar">
            success: function (response) {
                Swal.fire({
                    icon: response.success ? 'success' : 'error',
                    title: response.message,
                    showConfirmButton: true,
                    confirmButtonText: 'Aceptar'
                });

                if (response.success) {
                    $('#modalGestionJuridicas').modal('hide');
                    $('#tablaGestionJurDetalle').DataTable().ajax.reload();
                    $('#formGestionJuridica')[0].reset();
                }
            },
            error: function () {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'Hubo un problema al agregar la gestión jurídica.'
                });
            }
        });
    });
});


