const listaCategorias = document.getElementById("listaCategorias");

const btnSaidas = document.getElementById("btnSaidas");
const btnEntradas = document.getElementById("btnEntradas");

const dataInicial = document.getElementById("dataInicial");
const dataFinal = document.getElementById("dataFinal");
const btnFiltrarPeriodo = document.getElementById("btnFiltrarPeriodo");

const periodoEntradas = document.getElementById("periodoEntradas");
const periodoSaidas = document.getElementById("periodoSaidas");
const periodoSaldo = document.getElementById("periodoSaldo");

const mensagemPeriodo = document.getElementById("mensagemPeriodo");

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

    fetch(`/relatorios/categorias?tipo=${tipo}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Erro ao buscar relatório por categoria.");
            }

            return response.json();
        })
        .then(dados => {

            listaCategorias.innerHTML = "";

            dados.forEach(categoria => {

                const nomeCategoria =
                    nomesCategorias[categoria.categoria] || categoria.categoria;

                listaCategorias.innerHTML += `
                    <tr>
                        <td>${nomeCategoria}</td>
                        <td>${formatarMoeda(categoria.total)}</td>
                    </tr>
                `;
            });

        })
        .catch(error => {
            console.error("Erro ao carregar relatório:", error);
        });
}

function filtrarPeriodo() {

    const inicio = dataInicial.value;
    const fim = dataFinal.value;

    if (!inicio || !fim) {
        mensagemPeriodo.textContent = "Informe as duas datas.";
        return;
    }

    if (inicio > fim) {
        mensagemPeriodo.textContent = "A data inicial não pode ser maior que a data final.";
        return;
    }

    mensagemPeriodo.textContent = "";

    fetch(`/relatorios/periodo?dataInicial=${inicio}&dataFinal=${fim}`)
        .then(response => {
            if (!response.ok) {
                throw new Error("Erro ao buscar relatório por período.");
            }

            return response.json();
        })
        .then(dados => {

            periodoEntradas.textContent = formatarMoeda(dados.entradas);
            periodoSaidas.textContent = formatarMoeda(dados.saidas);
            periodoSaldo.textContent = formatarMoeda(dados.total);

        })
        .catch(error => {
            console.error("Erro ao filtrar período:", error);
            mensagemPeriodo.textContent = "Não foi possível carregar o relatório.";
        });
}


btnSaidas.addEventListener("click", () => {
    carregarRelatorio("SAIDA");
});

btnEntradas.addEventListener("click", () => {
    carregarRelatorio("ENTRADA");
});

btnFiltrarPeriodo.addEventListener("click", filtrarPeriodo);


carregarRelatorio("SAIDA");