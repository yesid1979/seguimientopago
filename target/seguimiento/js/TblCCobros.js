$(document).ready(function () {
    // ==============================
    // DATATABLE PRINCIPAL: PREDIOS CON COBROS
    // ==============================
    let tablaPredios = $('#tablaCobrosCo').DataTable({
        processing: true,
        serverSide: true,
        responsive: true,
        ajax: {
            url: '../ControladorPredios.do',
            type: 'POST',
            data: {accion: 'listarCobros'}
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
                        <button class="btn btn-info btn-sm ver-cobrosCob" data-id="${data.nroPredio}">
                            <i class="fas fa-eye"></i> Cobros
                        </button>
                    `;
                }
            }
        ],
        language: {
            url: 'https://cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json'
        }
    });
    $('#tablaCobrosCob').on('click', '.ver-cobrosCob', function () {
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

                // Asignar cod_predio para nuevos cobros
                $('#cod_predio_cobro').val(data.nroPredio);

                // Mostrar modal
                $('#modalCobrosDetallesCob').modal('show');

                // Cargar cobros relacionados
                if ($.fn.DataTable.isDataTable('#tablaCobrosDetalleCob')) {
                    $('#tablaCobrosDetalleCob').DataTable().destroy();
                }
                cargarCobros(data.nroPredio);
            }
        });
    });
    function cargarCobros(cod_predio) {
        $('#tablaCobrosDetalleCob').DataTable({
            processing: true,
            serverSide: true,
            responsive: true,
            ajax: {
                url: '../ControladorCobros.do',
                type: 'POST',
                data: {accion: 'listar', cod_predio: cod_predio}
            },
            columns: [
                {data: 'numIntentos'},
                {data: 'compromiso'},
                {data: 'fechaSeguimiento'},
                {data: 'valorAcordado', render: $.fn.dataTable.render.number('.', ',', 2, '$')}/*,
                {
                    data: null,
                    orderable: false,
                    render: function (data) {
                        return `
                            <button class="btn btn-danger btn-sm eliminar-cobro" data-id="${data.idCobro}">
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

