$(document).ready(function () {
    // Llamada AJAX para obtener los datos del dashboard

    $.ajax({
        url: './ControladorDashboard.do',
        type: 'POST',
        dataType: 'json',
        success: function (data) {
            // Actualizar los valores en las tarjetas
            $('#totalContribuyentes').text(data.dashboard.totalContribuyentes);
            $('#totalPredios').text(data.dashboard.totalPredios);
            $('#totalNotificados').text(data.dashboard.totalNotificados);
            $('#totalCoactivos').text(data.dashboard.totalCoactivos);

            // Renderizar gráficas
            renderValorPendientePorPredioChart(data.valorPendientePorPredio);
            renderPrediosPorEstadoChart(data.prediosPorEstado);
            renderPrediosPorVeredaBarrioChart(data.prediosPorVeredaBarrio);
            renderValorRecuperadoVsPendienteChart(data.valorRecuperadoVsPendiente);
            renderPrediosPorVigenciaChart(data.prediosPorVigencia);

        },
        error: function (xhr, status, error) {
            console.error("Error al cargar los datos del dashboard:", error);
        }
    });

    // Función para renderizar la gráfica de valor pendiente por predio
    function renderValorPendientePorPredioChart(data) {
        const labels = data.map(item => item[0].substring(0, 20) + "..."); // Acortar nombres largos
        const values = data.map(item => item[1]);

        const ctx = document.getElementById('valorPendientePorPredioChart').getContext('2d');
        new Chart(ctx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                        label: 'Valor Pendiente ($)',
                        data: values,
                        backgroundColor: 'rgba(54, 162, 235, 0.2)',
                        borderColor: 'rgba(54, 162, 235, 1)',
                        borderWidth: 2,
                        fill: true,
                        tension: 0.4 // Suaviza la línea
                    }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    tooltip: {
                        callbacks: {
                            label: function (context) {
                                return '$' + context.raw.toLocaleString();
                            }
                        }
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function (value) {
                                return '$' + value.toLocaleString();
                            }
                        }
                    }
                }
            }
        });
    }
    // Función para renderizar la gráfica de predios por estado
    function renderPrediosPorEstadoChart(data) {
        const labels = data.map(item => item[0]);
        const values = data.map(item => item[1]);
        const backgroundColors = [
            'rgba(255, 99, 132, 0.7)',
            'rgba(54, 162, 235, 0.7)',
            'rgba(255, 206, 86, 0.7)',
            'rgba(75, 192, 192, 0.7)',
            'rgba(153, 102, 255, 0.7)',
            'rgba(255, 159, 64, 0.7)'
        ];

        const ctx = document.getElementById('prediosPorEstadoChart').getContext('2d');
        new Chart(ctx, {
            type: 'doughnut',
            data: {
                labels: labels,
                datasets: [{
                        data: values,
                        backgroundColor: backgroundColors,
                        borderWidth: 1
                    }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'right',
                    },
                    tooltip: {
                        callbacks: {
                            label: function (context) {
                                const label = context.label || '';
                                const value = context.raw || 0;
                                const total = context.dataset.data.reduce((a, b) => a + b, 0);
                                const percentage = Math.round((value / total) * 100);
                                return `${label}: ${value} (${percentage}%)`;
                            }
                        }
                    }
                }
            }
        });
    }
    // Función para renderizar la gráfica de predios por vereda/barrio
    function renderPrediosPorVeredaBarrioChart(data) {
        const labels = data.map(item => item[0]);
        const values = data.map(item => item[1]);

        const ctx = document.getElementById('prediosPorVeredaBarrioChart').getContext('2d');
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                        label: 'Cantidad de Predios',
                        data: values,
                        backgroundColor: 'rgba(75, 192, 192, 0.7)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1
                    }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        display: false
                    },
                    tooltip: {
                        callbacks: {
                            label: function (context) {
                                return context.raw + ' predios';
                            }
                        }
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            stepSize: 1
                        }
                    }
                }
            }
        });
    }
    // Función para renderizar la gráfica de valor recuperado vs. pendiente
    function renderValorRecuperadoVsPendienteChart(data) {
        const labels = data.map(item => item[0].substring(0, 20) + "...");
        const valoresRecuperados = data.map(item => item[1]);
        const valoresPendientes = data.map(item => item[2]);

        const ctx = document.getElementById('valorRecuperadoVsPendienteChart').getContext('2d');
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [
                    {
                        label: 'Valor Recuperado ($)',
                        data: valoresRecuperados,
                        backgroundColor: 'rgba(75, 192, 192, 0.7)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1
                    },
                    {
                        label: 'Valor Pendiente ($)',
                        data: valoresPendientes,
                        backgroundColor: 'rgba(255, 99, 132, 0.7)',
                        borderColor: 'rgba(255, 99, 132, 1)',
                        borderWidth: 1
                    }
                ]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    tooltip: {
                        callbacks: {
                            label: function (context) {
                                return '$' + context.raw.toLocaleString();
                            }
                        }
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function (value) {
                                return '$' + value.toLocaleString();
                            }
                        }
                    }
                }
            }
        });
    }
    function renderPrediosPorVigenciaChart(data) {
        const labels = data.map(item => item[0]);
        const values = data.map(item => item[1]);

        const ctx = document.getElementById('prediosPorVigenciaChart').getContext('2d');
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                        label: 'Cantidad de Predios',
                        data: values,
                        backgroundColor: 'rgba(153, 102, 255, 0.7)',
                        borderColor: 'rgba(153, 102, 255, 1)',
                        borderWidth: 1
                    }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: {
                        display: true,
                        position: 'top',
                    },
                    tooltip: {
                        callbacks: {
                            label: function (context) {
                                return context.raw + ' predios';
                            }
                        }
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            stepSize: 1
                        }
                    }
                }
            }
        });
    }

});


