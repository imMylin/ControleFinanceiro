const parametros = new URLSearchParams(window.location.search);

const idMovimentacao = parametros.get("id");

const descricao = document.getElementById("descricao");
const tipo = document.getElementById("tipo");
const categoria = document.getElementById("categoria");
const valor = document.getElementById("valor");
const data = document.getElementById("data");

const mensagem = document.getElementById("mensagem");

function carregarMovimentacao() {

    fetch(`/movimentacoes/${idMovimentacao}`)
        .then(response => {

            if (!response.ok) {
                throw new Error("Movimentação não encontrada.");
            }

            return response.json();
        })
        .then(dados => {

            descricao.value = dados.descricao;
            tipo.value = dados.tipoMovimentacao;
            categoria.value = dados.categoriaGasto;
            valor.value = dados.valor;
            data.value = dados.data;

        })
        .catch(error => {

            console.error("Erro ao buscar movimentação:", error);

            mensagem.textContent = "Não foi possível carregar a movimentação.";

        });
}

carregarMovimentacao();

const formMovimentacao = document.getElementById("formMovimentacao");

formMovimentacao.addEventListener("submit", function(event) {

    event.preventDefault();

    const movimentacaoAtualizada = {
        descricao: descricao.value,
        tipoMovimentacao: tipo.value,
        categoriaGasto: categoria.value,
        valor: Number(valor.value),
        data: data.value
    };
    fetch(`/movimentacoes/${idMovimentacao}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(movimentacaoAtualizada)
    })
        .then(response => {

            if (!response.ok) {
                throw new Error("Erro ao atualizar movimentação.");
            }

            return response.json();
        })
        .then(dados => {

            mensagem.textContent = "Movimentação atualizada com sucesso!";

            setTimeout(() => {
                window.location.href = "movimentacoes.html";
            }, 1500);

        })
        .catch(error => {

            console.error("Erro ao atualizar:", error);

            mensagem.textContent = "Não foi possível atualizar a movimentação.";

        });

});

