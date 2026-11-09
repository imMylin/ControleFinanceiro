const descricao = document.getElementById("descricao");
const tipo = document.getElementById("tipo");
const categoria = document.getElementById("categoria");
const valor = document.getElementById("valor");
const data = document.getElementById("data");

const formMovimentacao = document.getElementById("formMovimentacao");

formMovimentacao.addEventListener("submit", function(event) {

    event.preventDefault();

    const movimentacao = {
        descricao: descricao.value,
        tipoMovimentacao: tipo.value,
        categoriaGasto: categoria.value,
        valor: Number(valor.value),
        data: data.value
    };

    console.log(movimentacao);

});

