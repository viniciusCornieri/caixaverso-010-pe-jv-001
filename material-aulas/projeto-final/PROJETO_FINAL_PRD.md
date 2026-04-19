# Documento de Requisitos de Produto (PRD): Microsserviço de Empréstimos

## 1. Visão Geral

O Microsserviço de Empréstimos (Loans) é a aplicação prática central do projeto. Ele atua como um sistema de gestão financeira desenvolvido em Quarkus. Este sistema é responsável pelo cálculo das Amortizações (Tabelas SAC e PRICE), comunicando-se unicamente com APIs de terceiros para validar a elegibilidade do cliente e a taxa de juros a ser aplicada.

**Entrega Final Esperada**: Uma API REST (Nível 2 de Richardson) implementada com Quarkus, utilizando Bean Validation, Hibernate Panache, Tratamento de Exceções Dedicado, MicroProfile REST Client e Quarkus Security.

## 2. Metas & Alinhamento com as Rubricas

* Implementar um CRUD profissional de operações financeiras.

* Aplicar os princípios de injeção de dependências do Quarkus.

* Parametrizar restrições complexas via Bean Validation.

* Desenvolver a lógica dos sistemas de amortização SAC e PRICE.

* Persistir dados utilizando Hibernate Panache (H2 ou PostgreSQL).

* Restringir ações críticas aplicando Roles de segurança (ex: Gerente).

## 3. Requisitos de Dados e Restrições de Negócio

* **Emprestimo**:

  * `id`: Identificador único do empréstimo.
  * `clienteId`: Identificador do cliente solicitante (Obrigatório, não pode ser nulo ou vazio).
  * `valorTotal`: Valor global do empréstimo (Obrigatório. Mínimo de R$ 100,00 e máximo de R$ 10.000.000,00).
  * `quantidadeParcelas`: Prazo de pagamento em meses (Obrigatório. Entre 1 e 480 meses).
  * `tipoAmortizacao`: Sistema de amortização escolhido (Obrigatório. Restrito a `SAC` ou `PRICE`).
  * `status`: Estado atual do contrato (`PENDENTE`, `PAGO`).

* **Parcela**:

  * `id`: Identificador único da fatura/parcela.
  * `ordem`: Mês correspondente à parcela (ex: 1 para o mês um) (Obrigatório).
  * `dataVencimento`: Data limite de pagamento (Obrigatório).
  * `valorAmortizacao`: Fração do valor que efetivamente abate a dívida original (Obrigatório).
  * `valorJuros`: Custo da taxa de juros aplicada sobre o saldo devedor restante (Obrigatório).
  * `valorPrestacao`: Valor final que será pago pelo cliente naquele mês (Amortização + Juros) (Obrigatório).
  * `status`: Situação do pagamento da parcela (`PENDENTE` ou `PAGA`).

## 4. Histórias de Usuário (User Stories)

### Story 1: Integração com Serviço de Avaliação de Crédito

**Como** sistema de gestão de empréstimos, **eu preciso** consultar a API externa de análise de crédito antes de processar qualquer solicitação local, **para que** eu aplique a taxa de juros correta e bloqueie solicitações de clientes não autorizados.

* **Critérios de Aceite**:

  * O sistema utiliza o Microprofile REST Client para consultar a API externa (conforme `PROJETO_FINAL_taxas_api`).
  * Respostas HTTP 400 da API externa indicam cliente inelegível e devem interromper o cadastro acionando o erro correspondente.
  * Respostas HTTP 200 da API externa retornam a taxa de juros aplicável, que deve ser capturada e utilizada nos cálculos das faturas.

### Story 2: Criação do Contrato de Empréstimo

**Como** cliente, **eu desejo** enviar minha solicitação com os dados do empréstimo pretendido, **para que** o sistema simule e registre meu contrato em banco de dados.

* **Critérios de Aceite**:

  * O endpoint HTTP POST em `/emprestimos` deve receber obrigatoriamente no payload o valor principal, o `clienteId`, a **quantidade de parcelas (meses)** e o sistema de amortização (`SAC/PRICE`). Sem o prazo (parcelas), o cálculo não pode ser realizado.
  * O sistema consulta internamente a taxa com o serviço parceiro.
  * A aplicação efetua o cálculo (SAC/PRICE) e persiste o contrato em cascata com suas parcelas, retornando o `Status 201 Created`.

### Story 3: Proteção de Deleção Exclusiva

**Como** gerente do sistema, **eu exijo** que o cancelamento de contratos e deleção de dados críticos seja restrito, **para evitar** exclusões indevidas do histórico financeiro.

* **Critérios de Aceite**:

  * A rota DELETE da entidade Empréstimo deve estar protegida utilizando anotações de segurança (Quarkus Security).
  * Apenas usuários autenticados com o *Role* `Gerente` podem executar a operação.
  * Usuários sem privilégios recebem `Status 403 Forbidden`.

### Story 4: Validação Estrita de Dados de Entrada

**Como** cliente da API, **eu preciso** receber mensagens de erro padronizadas quando informo dados violando as regras de negócio, **para que** eu possa corrigir os campos específicos rapidamente.

* **Critérios de Aceite**:

  * Entradas inválidas (como prazos acima de 480 meses ou tipo de amortização inexistente) devem ser interceptadas via Bean Validation.
  * A resposta de erro padrão deve retornar `Status 400 Bad Request` contendo um JSON detalhado que lista objetivamente qual coluna e qual regra não foram cumpridas.

### Story 5: Listagem Pessoal de Contratos

**Como** cliente autenticado, **eu desejo** listar todos os empréstimos registrados com o meu identificador, **para que** eu tenha visibilidade das minhas pendências atuais.

* **Critérios de Aceite**:

  * O sistema deve oferecer um endpoint GET parametrizado para recuperar os empréstimos, seus valores globais e tipo selecionado.

### Story 6: Visualização da Fatura (Carnê)

**Como** cliente, **eu desejo** visualizar todo o planejamento atrelado ao meu empréstimo na forma de parcelas individuais, **para que** eu saiba as evoluções das pendências com exatidão mensal.

* **Critérios de Aceite**:

  * Endpoints RESTful adicionais devem permitir expandir uma parcela pelo ID ou recuperar toda a lista das faturas com seus valores consolidados de prestação, juros e amortizações.
