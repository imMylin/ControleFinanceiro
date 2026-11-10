const listaCategorias = document.getElementById("listaCategorias");

const btnSaidas = document.getElementById("btnSaidas");
const btnEntradas = document.getElementById("btnEntradas");

let tipoRelatorio = "SAIDA";
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


function carregarRelatorio(tipo) {

    tipoRelatorio = tipo;

    fetch(`/relatorios/categorias?tipo=${tipoRelatorio}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Erro ao buscar relatório.");
            }

            return response.json();
        })
        .then(dados => {


            listaCategorias.innerHTML = "";

            dados.forEach(categoria => {

                const nomeCategoria =
                    nomesCategorias[categoria.categoria] || categoria.categoria;

                const totalCategoria = formatarMoeda(categoria.total);

                listaCategorias.innerHTML += `
                    <tr>
                        <td>${nomeCategoria}</td>
                        <td>${totalCategoria}</td>
                    </tr>
                `;
            });


            const labels = dados.map(categoria =>
                nomesCategorias[categoria.categoria] || categoria.categoria
            );

            const valores = dados.map(categoria => categoria.total);


            if (grafico === null) {

                const elementoGrafico =
                    document.getElementById("graficoCategorias");

                grafico = new Chart(elementoGrafico, {
                    type: "doughnut",

                    data: {
                        labels: labels,

                        datasets: [{
                            label: "Total por categoria",
                            data: valores,
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
                        plugins: {
                            legend: {
                                position: "bottom"
                            }
                        }
                    }
                });

            } else {

                grafico.data.labels = labels;
                grafico.data.datasets[0].data = valores;

                grafico.update();
            }
        })
        .catch(error => {
            console.error("Erro ao carregar relatório:", error);
        });


    btnSaidas.classList.toggle("ativo", tipo === "SAIDA");
    btnEntradas.classList.toggle("ativo", tipo === "ENTRADA");
}


btnSaidas.addEventListener("click", () => {
    carregarRelatorio("SAIDA");
});

btnEntradas.addEventListener("click", () => {
    carregarRelatorio("ENTRADA");
});


carregarRelatorio("SAIDA");