// Inicializar DataTable para predios
$(document).ready(function () {
    // ================================
    // DATATABLE SERVER-SIDE
    // ================================
    let tablaNotificaciones = $('#tablaNotificaciones').DataTable({
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
                                <button class="btn btn-sm btn-outline-primary ver-detalles" data-id="${data.nroPredio}" title="Ver detalles">
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

    // Manejar click en botón de ver detalles
    $('#tablaNotificaciones').on('click', '.ver-detalles', function () {
        let nroPredio = $(this).data('id');
        cargarDetallesPredio(nroPredio);
        $('#modalPredioDetalles').modal('show');
    });

    // Manejar click en botón de agregar notificación
    $('#btnAgregarNotificacion').on('click', function () {
        $('#cod_predio_notificacion').val($('#detalle_id_predio').text());
        $('#modalNotificacion').modal('show');
    });

    // Inicializar DataTable para notificaciones
    $('#tablaNotificaciones2').DataTable({
        searching: false,
        ordering: false,
        paging: false,
        info: false
    });
});

// Función para cargar detalles del predio
function cargarDetallesPredio(nroPredio) {
    $.ajax({
        url: '../ControladorPredios.do',
        type: 'POST',
        data: {accion: 'obtener', nro_predio: nroPredio},
        dataType: 'json',
        success: function (predio) {
           // $('#cod_predio_notificacion1').val(nroPredio);
            $('#detalle_id_predio').text(predio.idPredio);
            $('#detalle_matricula').text(predio.matriculaPredio);
            $('#detalle_numrecibo').text(predio.numreciboPredio);
            //   $('#detalle_cedula_propietario').text(predio.ced_contribuyente);
            //   $('#detalle_propietario').text(predio.nom_contribuyente);

            // Si hay un contribuyente asociado, mostrar su cédula y nombre
            if (predio.contribuyente) {
                $('#detalle_cedula_propietario').text(predio.contribuyente.cedContribuyente);
                $('#detalle_propietario').text(predio.contribuyente.nomContribuyente);
            } else {
                $('#detalle_cedula_propietario').text('');
                $('#detalle_propietario').text('');
            }

            $('#detalle_direccion').text(predio.dirPredio);
            $('#detalle_vereda_barrio').text(predio.veredaBarrio);
            $('#detalle_valor_pendiente').text(
                    new Intl.NumberFormat('es-CO', {
                        style: 'currency',
                        currency: 'COP',
                        minimumFractionDigits: 0
                    }).format(predio.valorPendiente)
                    );
            $('#detalle_vigencia').text(predio.vigenciaPredio);

            // Cargar notificaciones del predio
            cargarNotificaciones(nroPredio);
        }
    });
}

// Función para cargar notificaciones
function cargarNotificaciones(codPredio) {
    $.ajax({
        url: '../ControladorNotificaciones.do',
        type: 'POST',
        data: {accion: 'listar', cod_predio: codPredio},
        dataType: 'json',
        success: function (notificaciones) {
            let tbody = $('#notificacionesBody');
            tbody.empty();

            if (notificaciones.length === 0) {
                tbody.append('<tr><td colspan="7" class="text-center">No hay notificaciones registradas</td></tr>');
            } else {
                notificaciones.forEach(notificacion => {
                    let fila = `
                                <tr>
                                    <td>${notificacion.fechaNotificacion}</td>
                                    <td>${notificacion.horaNotificacion}</td>
                                    <td>${notificacion.tipoNotificacion}</td>
                                    <td>${new Intl.NumberFormat('es-CO', {
                        style: 'currency',
                        currency: 'COP',
                        minimumFractionDigits: 0
                    }).format(notificacion.valorEnviado)}</td>
                                    <td>${notificacion.agenciaEnvio}</td>
                                    <td>${notificacion.estadoNotificacion}</td>
                                    <td>
                                        <button class="btn btn-sm btn-outline-danger eliminar-notificacion" data-id="${notificacion.idNotificacion}">
                                            <i class="fas fa-trash"></i> Eliminar
                                        </button>
                                    </td>
                                </tr>
                            `;
                    tbody.append(fila);
                });
            }
        }
    });
}

// Función para insertar notificación
$('#formNotificacion').on('submit', function(e) {
    e.preventDefault();
    
    // Validar que el campo cod_predio tenga un valor
    var codPredio = $('#cod_predio_notificacion').val();
    if (!codPredio || codPredio.trim() === '') {
        Swal.fire({
            icon: 'warning',
            title: 'Advertencia',
            text: 'Debes seleccionar un predio primero'
        });
        return;
    }
    
    // Crear objeto de notificación con los datos del formulario
    var notificacion = {
        cod_predio: parseInt(codPredio),  // Convertir explícitamente a número
        fecha_notificacion: $('#fecha_notificacion').val(),
        hora_notificacion: $('#hora_notificacion').val(),
        tipo_notificacion: $('#tipo_notificacion').val(),
        valor_enviado: parseFloat($('#valor_enviado_notif').val()) || 0,
        estado_notificacion: $('#estado_notificacion').val(),
        agencia_envio: $('#agencia_envio').val(),
        observacion: $('#observacion_notificacion').val()
    };

    // Enviar datos al servidor
    $.ajax({
        url: '../ControladorNotificaciones.do',
        type: 'POST',
        data: {
            accion: 'insertar',
            notificacion: notificacion
        },
        dataType: 'json',
        success: function(response) {
            if (response.exito) {
                // Cerrar modal
                $('#modalNotificacion').modal('hide');
                
                // Restablecer formulario
                $('#formNotificacion')[0].reset();
                
                // Mostrar mensaje de éxito
                Swal.fire({
                    icon: 'success',
                    title: '¡Éxito!',
                    text: 'Notificación agregada correctamente'
                });
                
                // Recargar notificaciones
                cargarNotificaciones(parseInt(codPredio));
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'No se pudo agregar la notificación'
                });
            }
        },
        error: function(xhr, status, error) {
            console.error(error);
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Error de conexión con el servidor'
            });
        }
    });
});

// Manejar eliminación de notificación
$('#tablaNotificaciones1').on('click', '.eliminar-notificacion', function () {
    let idNotificacion = $(this).data('id');
    Swal.fire({
        title: '¿Estás seguro?',
        text: "Se eliminará permanentemente esta notificación.",
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
                data: {accion: 'eliminar', id_notificacion: idNotificacion},
                dataType: 'json',
                success: function (respuesta) {
                    if (respuesta.exito) {
                        Swal.fire({
                            icon: 'success',
                            title: '¡Eliminado!',
                            text: 'La notificación ha sido eliminada.'
                        });
                        // Recargar notificaciones
                        let codPredio = $('#cod_predio_notificacion').val();
                        cargarNotificaciones(codPredio);
                    } else {
                        Swal.fire({
                            icon: 'error',
                            title: 'Error',
                            text: 'No se pudo eliminar la notificación.'
                        });
                    }
                }
            });
        }
    });
});