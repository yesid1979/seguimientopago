$(document).ready(function () {
    /* $('#cod_profesion').select2({
     theme: 'bootstrap-5',
     allowClear: true,
     width: '100%'// Usa el tema de Bootstrap 5
     });*/
    function cargarProfesionesP(selectedId) {
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
    let id = $("#id_usuario").val();
    $.ajax({
        url: '../ControladorUsuarios.do',
        type: 'POST',
        data: {accion: 'perfil', id_usuario: id},
        dataType: 'json',
        success: function (data) {
            $.when(
                    cargarProfesionesP(data.profesion ? data.profesion.idProfesion : '')
                    ).done(function () {
                $('#ced_usuario').val(data.cedUsuario);
                $('#nom_usuario').val(data.nomUsuario);
                $('#email_usuario').val(data.emailUsuario);
                $('#tel_usuario').val(data.telUsuario);
                $('#cel_usuario').val(data.celUsuario);
                $('#login_usuario').val(data.loginUsuario);
                $('#sexo_usuario').val(data.sexoUsuario);
                $('#accionUsuario').val('actualizarPerfil');
            });
        }
    });
    $('#formPerfil').on('submit', function (e) {
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
                        // $('#modalUsuario').modal('hide');
                        // tablaUsuarios.ajax.reload();
                    }
                });
            }
        });
    });
});