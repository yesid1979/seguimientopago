// Inicializar DataTable con Server-Side para Predios
$(document).ready(function () {
    // ================================
    // FUNCIÓN PARA BUSCAR CONTRIBUYENTE POR CÉDULA
    // ================================
    $("#id_predio").focus();
    function buscarContribuyentePorCedula(cedula) {
        return $.ajax({
            url: '../ControladorContribuyente.do',
            type: 'POST',
            data: {accion: 'buscarPorCedula', ced_contribuyente: cedula},
            dataType: 'json'
        });
    }

    // ================================
    // DATATABLE SERVER-SIDE
    // ================================
    let tablaPredios = $('#tablaPredios').DataTable({
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
                render: function (data, type, row) {
                    if (type === 'display' || type === 'filter') {
                        return new Intl.NumberFormat('es-CO', {
                            style: 'currency',
                            currency: 'COP',
                            minimumFractionDigits: 0
                        }).format(data);
                    }
                    return data; // para orden y búsqueda usa el valor original
                }
            },
            {data: 'vigenciaPredio'},
            {
                data: null,
                orderable: false,
                render: function (data) {
                    return `
                        <button class="btn btn-sm btn-outline-primary editar" data-id="${data.nroPredio}" title="Editar">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button class="btn btn-sm btn-outline-danger eliminar" data-id="${data.nroPredio}" title="Eliminar">
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
    // AGREGAR NUEVO PREDIO
    // ================================
    $('#btnAgregarPredio').on('click', function () {
        $('#formPredio')[0].reset();
        $('#accionPredio').val('insertar');
        // Limpiar campos de contribuyente
        $('#ced_contribuyente').val('');
        $('#nom_contribuyente').val('');
        $('#modalPredio').modal('show');
    });

// Cuando el modal termine de mostrarse, poner el focus
    $('#modalPredio').on('shown.bs.modal', function () {
        $('#id_predio').trigger('focus');
    });

    // ================================
    // EDITAR PREDIO
    // ================================
    $('#tablaPredios').on('click', '.editar', function () {
        let id = $(this).data('id');
        $.ajax({
            url: '../ControladorPredios.do',
            type: 'POST',
            data: {accion: 'obtener', nro_predio: id},
            dataType: 'json',
            success: function (data) {
                $('#nro_predio').val(data.nroPredio);
                $('#id_predio').val(data.idPredio);
                $('#numrecibo_predio').val(data.numreciboPredio);
                $('#matricula_predio').val(data.matriculaPredio);
                $('#vereda_barrio').val(data.veredaBarrio);
                $('#dir_predio').val(data.dirPredio);
                $('#valor_pendiente').val(data.valorPendiente);
                $('#observacion').val(data.observacion);
                $('#vigencia_predio').val(data.vigenciaPredio);
                $('#valor_enviado').val(data.valorEnviado);
                $('#estado_predio').val(data.estadoPredio);

                // Si hay un contribuyente asociado, mostrar su cédula y nombre
                if (data.contribuyente) {
                    $('#ced_contribuyente').val(data.contribuyente.cedContribuyente);
                    $('#nom_contribuyente').val(data.contribuyente.nomContribuyente);
                } else {
                    $('#ced_contribuyente').val('');
                    $('#nom_contribuyente').val('');
                }

                $('#accionPredio').val('actualizar');
                $('#modalPredio').modal('show');
            }
        });
    });

    // ================================
    // BUSCAR CONTRIBUYENTE AL CAMBIAR LA CÉDULA
    // ================================
    $('#ced_contribuyente').on('blur', function () {
        let cedula = $(this).val().trim();
        if (cedula !== '') {
            buscarContribuyentePorCedula(cedula)
                    .done(function (response) {
                        if (response.success && response.data) {
                            $('#nom_contribuyente').val(response.data.nomContribuyente);
                        } else {
                            $('#nom_contribuyente').val('Contribuyente no encontrado');
                            // Opcional: limpiar el campo de cédula o mostrar un mensaje
                        }
                    })
                    .fail(function () {
                        $('#nom_contribuyente').val('Error al buscar');
                    });
        } else {
            $('#nom_contribuyente').val('');
        }
    });

    // ================================
    // GUARDAR (INSERTAR/ACTUALIZAR)
    // ================================
    $('#formPredio').on('submit', function (e) {
        e.preventDefault();
        $.ajax({
            url: '../ControladorPredios.do',
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
                        $('#modalPredio').modal('hide');
                        tablaPredios.ajax.reload();
                    }
                });
            }
        });
    });

    // ================================
    // ELIMINAR
    // ================================
    $('#tablaPredios').on('click', '.eliminar', function () {
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
                    url: '../ControladorPredios.do',
                    type: 'POST',
                    data: {accion: 'eliminar', nro_predio: id},
                    dataType: 'json',
                    success: function (response) {
                        Swal.fire({
                            icon: response.success ? 'success' : 'error',
                            title: response.success ? '¡Eliminado!' : '¡Error!',
                            text: response.message || 'Operación realizada'
                        }).then(() => {
                            if (response.success) {
                                tablaPredios.ajax.reload();
                            }
                        });
                    }
                });
            }
        });
    });
});

