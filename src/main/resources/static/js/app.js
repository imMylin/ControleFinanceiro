const descricao = document.getElementById("descricao");
const tipo = document.getElementById("tipo");
const categoria = document.getElementById("categoria");
const valor = document.getElementById("valor");
const data = document.getElementById("data");

const formMovimentacao = document.getElementById("formMovimentacao");

const mensagem = document.getElementById("mensagem");

formMovimentacao.addEventListener("submit", function(event) {

    event.preventDefault();

    const movimentacao = {
        descricao: descricao.value,
        tipoMovimentacao: tipo.value,
        categoriaGasto: categoria.value,
        valor: Number(valor.value),
        data: data.value
    };

    fetch("/movimentacoes", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(movimentacao)
    })
        .then(response => {

            return response.json().then(dados => {

                if (response.ok) {
                    mensagem.textContent = "Movimentação cadastrada com sucesso!";

                    mensagem.classList.remove("mensagem-erro");
                    mensagem.classList.add("mensagem-sucesso");

                    formMovimentacao.reset();
                    setTimeout(() => {
                        window.location.href = "movimentacoes.html";
                    }, 3000);
                } else {
                    mensagem.textContent = dados.mensagem;

                    mensagem.classList.remove("mensagem-sucesso");
                    mensagem.classList.add("mensagem-erro");
                }

            });

        })
        .catch(error => {
            console.log("Falha na comunicação com o servidor!");
            console.error(error);
        });

});

