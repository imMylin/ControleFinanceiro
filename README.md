Aplicação web para controle financeiro pessoal, desenvolvida com Java e Spring Boot para o Back-End e HTML, JavaScript e CSS para o Front-End. 
O projeto permite registrar entradas e saídas, acompanhar o saldo e consultar informações por meio de um dashboard e relatórios.

### Funcionalidades:

- Cadastro de movimentações financeiras.
- Listagem e exclusão de movimentações.
- Classificação das movimentações como entrada ou saída.
- Organização por categorias de gastos.
- Dashboard com resumo de entradas, saídas e saldo.
- Gráfico para visualizar os gastos por categoria.
- Relatórios por categoria, com opção de consultar entradas ou saídas.
- Relatório por período, com filtro por data inicial e final.
- Validação dos dados recebidos pela API.
- Tratamento global de exceções para respostas de erro mais consistentes.


### Tecnologias utilizadas

Backend
- Java 21
- Spring Boot 4.1.1
- Spring Web
- Spring Data JPA
- Bean Validation
- Hibernate
- Maven

Frontend
- HTML5
- CSS3
- JavaScript
- Chart.js

Banco de dados
- MySQL

### Observação:

O frontend está integrado à aplicação Spring Boot, dentro de src/main/resources/static.

### Como executar

### Pré-requisitos

Antes de executar o projeto, tenha instalado:
- JDK 21.
- MySQL.
- Maven

1. Clone o repositório (git clone https://github.com/imMylin/controlefinanceiro.git)
2. Entre na pasta: controlefinanceiro
3. Crie o banco de dados (CREATE DATABASE financas;)
4. Configure a conexão com o banco
   
   -Abra src/main/resources/application.properties e configure a conexão com o seu MySQL substituindo os campos (SEU_USUARIO) e (SUA_SENHA) pelos dados reais de seu usuário e senha do mysql

5. Execute a aplicação
6. Acesse o sistema (http://localhost:8080/)
   

### Objetivo do projeto:

Este projeto foi desenvolvido como prática de desenvolvimento de interface web construída com HTML, CSS e JavaScript e a integração com o back-end que estava sendo estudado separadamente

O desenvolvimento permitiu aplicar conceitos de desenvolvimento web com construção, estilização e implementação de funcionalidades, além de possibilitar a integração com banco de dados e um back-end construído a parte.
