$(document).ready(function () {
    let tablaPredios = $('#tablaTotNotificaciones').DataTable({
        processing: true,
        serverSide: true,
        responsive: true,
        ajax: {
            url: '../ControladorPredios.do',
            type: 'POST',
            data: {accion: 'listarNotificados'}
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
                        <button class="btn btn-info btn-sm ver-detallesNot" data-id="${data.nroPredio}">
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
    $('#tablaTotNotificaciones').on('click', '.ver-detallesNot', function () {
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
                if ($.fn.DataTable.isDataTable('#tablaNotificacionesNot2')) {
                    $('#tablaNotificacionesNot2').DataTable().destroy();
                }
                cargarNotificaciones(data.nroPredio);
            }
        });
    });
    function cargarNotificaciones(cod_predio) {
        $('#tablaNotificacionesNot2').DataTable({
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
                {data: 'estadoNotificacion'}/*,
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
                 }*/
            ],
            language: {
                url: 'https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json'
            }
        });
    }
});