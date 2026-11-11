
const totalEntradas = document.getElementById("totalEntradas");
const totalSaidas = document.getElementById("totalSaidas");
const saldoAtual = document.getElementById("saldoAtual");

let grafico = null;

const nomesCategorias = {
    ALIMENTACAO: "Alimentação",
    TRANSPORTE: "Transporte",
    MORADIA: "Moradia",
    SAUDE: "Saúde",
    EDUCACAO: "Educação",
    LAZER: "Lazer",
    ASSINATURAS: "Assinaturas",
    COMPRAS: "Compras",
    OUTROS: "Outros"
};

function formatarMoeda(valor) {
    return new Intl.NumberFormat("pt-BR", {
        style: "currency",
        currency: "BRL"
    }).format(valor);
}

function carregarResumo() {

    fetch("/relatorios/resumo")
        .then(response => {
            if (!response.ok) {
                throw new Error("Erro ao buscar resumo financeiro.");
            }

            return response.json();
        })
        .then(dados => {

            totalEntradas.textContent =
                formatarMoeda(dados.totalEntradas);

            totalSaidas.textContent =
                formatarMoeda(dados.totalSaidas);

            saldoAtual.textContent =
                formatarMoeda(dados.saldo);

        })
        .catch(error => {
            console.error("Erro ao carregar resumo:", error);
        });
}

function carregarGrafico() {

    fetch("/relatorios/categorias?tipo=SAIDA")
        .then(response => {
            if (!response.ok) {
                throw new Error("Erro ao buscar relatório.");
            }

            return response.json();
        })
        .then(dados => {

            const labels = dados.map(categoria =>
                nomesCategorias[categoria.categoria] ||
                categoria.categoria
            );

            const valores = dados.map(categoria =>
                Number(categoria.total)
            );

            const elementoGrafico =
                document.getElementById("graficoCategorias");

            Chart.register(ChartDataLabels);

            if (grafico !== null) {
                grafico.destroy();
            }

            grafico = new Chart(elementoGrafico, {

                type: "doughnut",

                data: {
                    labels: labels,

                    datasets: [{
                        label: "Total por categoria",
                        data: valores,

                        cutout: "65%",
                        radius: "85%",

                        backgroundColor: [
                            "#2563eb",
                            "#16a34a",
                            "#f59e0b",
                            "#dc2626",
                            "#9333ea",
                            "#0891b2",
                            "#64748b",
                            "#db2777",
                            "#854d0e"
                        ],

                        borderWidth: 2
                    }]
                },

                options: {
                    responsive: true,
                    maintainAspectRatio: false,

                    layout: {
                        padding: {
                            top: 10,
                            bottom: 15,
                            left: 10,
                            right: 10
                        }
                    },

                    plugins: {

                        legend: {
                            position: "bottom",
                            align: "center",
                            labels: {
                                color: "#BDBDBD",
                                padding: 15,
                                usePointStyle: true,
                                pointStyle: "circle",
                                font: {
                                    size: 15
                                }
                            }
                        },

                        tooltip: {
                            backgroundColor: "rgba(25, 25, 29, 0.92)",
                            titleColor: "#FFFFFF",
                            bodyColor: "#D4D4D4",
                            borderColor: "rgba(255, 255, 255, 0.22)",
                            borderWidth: 1,
                            padding: 12,
                            cornerRadius: 10,
                            displayColors: true,
                            titleFont: {
                                weight: "bold"
                            }
                        },

                        datalabels: {

                            color: "#ffffff",

                            font: {
                                weight: "bold",
                                size: 12
                            },

                            formatter: (valor, contexto) => {

                                const valores =
                                    contexto.dataset.data;

                                const total = valores.reduce(
                                    (soma, atual) => soma + atual,
                                    0
                                );

                                if (total === 0) {
                                    return "";
                                }

                                const porcentagem =
                                    (valor / total) * 100;

                                return porcentagem.toFixed(1) + "%";
                            }
                        }
                    }
                }
            });

        })
        .catch(error => {
            console.error("Erro ao carregar gráfico:", error);
        });
}

carregarResumo();
carregarGrafico();
