const listaMovimentacoes = document.getElementById("listaMovimentacoes");

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
function editarMovimentacao(id) {

    window.location.href = `editar-movimentacao.html?id=${id}`;

}

function carregarMovimentacoes() {

    fetch("/movimentacoes")
        .then(response => {
            if (!response.ok) {
                throw new Error("Erro ao buscar movimentações.");
            }

            return response.json();
        })
        .then(dados => {

            listaMovimentacoes.innerHTML = "";

            dados.forEach(movimentacao => {

                const valorFormatado = formatarMoeda(movimentacao.valor);

                const dataFormatada = new Date(
                    `${movimentacao.data}T12:00:00`
                ).toLocaleDateString("pt-BR");

                const classeTipo =
                    movimentacao.tipoMovimentacao === "ENTRADA"
                        ? "entrada"
                        : "saida";

                listaMovimentacoes.innerHTML += `
                    <tr>
                        <td>${movimentacao.descricao}</td>
                        <td>${nomesCategorias[movimentacao.categoriaGasto] || movimentacao.categoriaGasto}</td>
                        <td class="${classeTipo}">${valorFormatado}</td>
                        <td>${dataFormatada}</td>
                        <td class="acoes">
                            <button
                                class="btn-editar"
                                onclick="editarMovimentacao(${movimentacao.id})"
                                title="Editar movimentação">
                                <i data-lucide="pencil"></i>
                            </button>
                        
                            <button
                                class="btn-excluir"
                                onclick="excluirMovimentacao(${movimentacao.id})"
                                title="Excluir movimentação">
                                <i data-lucide="trash-2"></i>
                            </button>
                        </td>
                    </tr>
                `;
            });
            lucide.createIcons();
        })
        .catch(error => {
            console.error("Erro ao buscar movimentações:", error);
        });
}


function excluirMovimentacao(id) {

    const confirmar = confirm("Deseja realmente excluir esta movimentação?");

    if (!confirmar) {
        return;
    }

    fetch(`/movimentacoes/${id}`, {
        method: "DELETE"
    })
        .then(response => {

            if (!response.ok) {
                throw new Error("Não foi possível excluir a movimentação.");
            }

            alert("Movimentação excluída com sucesso!");

            carregarMovimentacoes();

        })
        .catch(error => {
            console.error("Erro ao excluir movimentação:", error);
            alert("Ocorreu um erro ao tentar excluir a movimentação.");
        });
}


carregarMovimentacoes();
