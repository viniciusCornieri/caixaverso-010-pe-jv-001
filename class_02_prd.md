# Documento de Requisitos de Produto (PRD): Aula 02 - Criação de Controllers e APIs no Quarkus (JAX-RS)

## 1. Visão Geral
Nesta aula estabelecemos a base de entrada e saída de dados das aplicações construindo os primeiros Controllers utilizando as anotações do JAX-RS. A meta principal é entender os verbos do protocolo HTTP, os códigos de retorno e o fluxo de requisições e respostas convertendo objetos Java para JSON usando Jackson e Lombok.

## 2. Domínio do Instrutor (Projeto Clientes)
O domínio de Clientes funcionará como a prova de conceito para demonstrar a criação e mapeamento de Controllers.

### Histórias de Usuário (User Stories)

#### Story 1: Gerenciamento em Memória
**Como** Gerente, **eu desejo** manter uma lista em memória dos clientes cadastrados, **para que** possamos testar as operações sem ainda nos preocuparmos com um banco de dados real.
* **Critérios de Aceite**: A lista deve ser manipulada pelo Controller. A lista servirá de fonte temporária.

#### Story 2: Adição de clientes
**Como** Gerente, **eu desejo** introduzir novos clientes no sistema, **para que** seus cadastros operacionais sejam efetivados na plataforma.
* **Critérios de Aceite**: 
  * O sistema deve disponibilizar um canal receptivo para processar os dados cadastrais originados de fontes limpas.
  * A aplicação avaliará a estrutura repassada e em seguida persistirá os registros temporariamente em base na memória.
  * Ao finalizar o cadastro com sucesso, o sistema atestará a criação efetiva devolvendo ao usuário a ficha consolidada.

#### Story 3: Consulta e Listagem Total
**Como** Gerente, **eu desejo** buscar todos os clientes cadastrados ou filtrá-los unicamente pelo documento, **para que** possa recuperar os dados operacionais exatos de uma ocorrência.
* **Critérios de Aceite**:
  * O sistema deve expor uma funcionalidade que permita listar todos os clientes da base para o gerente.
  * Deve ser possível também fazer a filtragem para retornar a ficha de um cliente específico de forma isolada referenciando apenas a identificação de seu documento.
  * Caso o documento fornecido na busca individual não exista, o sistema tem o dever de proteger a usabilidade retornando um informativo não destrutivo apontando explicitamente que tal recurso não foi encontrado.

#### Story 4: Simulador de Autarquia Regulatória de Taxas
**Como** Arquiteto de Software, **eu desejo** prover internamente ao painel do Cliente um simulador externo de aprovação e precificação de índices de taxas, **para que** o microserviço paralelo de Empréstimos possa consumi-lo de forma independente simulando o mundo real.
* **Critérios de Aceite**:
  * O ecossistema exporá um novo recurso que se comportará fidedignamente como a interface da Agência Reguladora de Taxas de Juros Externa (respondendo na rota acordada do documento de arquitetura).
  * O simulador, ao ser consultado enviando o formato e corpo descritos na especificação de negócio, aprovará e responderá artificialmente com o repasso de taxas de mercado em valores variando aleatoriamente entre 9% a 16%.

### Modelo de Dados e Especificação da API

#### Modelo de Dados (Cliente)
* **id**: Identificador de sistema do cliente (Gerado automaticamente em formato UUID, retornado na response).
* **nome**: Nome completo do cliente (Obrigatório, String).
* **documento**: Identificador único do cliente (Obrigatório, String).

#### Especificação OpenAPI (Swagger)
A especificação consolidada das rotas e contratos da API de Clientes encontra-se centralizada no arquivo separado: [`class_02_client_apis.yaml`](class_02_client_apis.yaml).

## 3. Domínio do Aluno (Microserviço de Empréstimos - Projeto Final)
Na etapa dois, o objetivo primário é inicializar a infraestrutura do microserviço de empréstimos e implementar um pequeno conjunto de operações elementares (CRUD), permitindo adicionar, remover e listar intenções de contratos acoplados a clientes da corporação.

### Histórias de Usuário (User Stories)

#### Story 1: Inserção do Contrato de Empréstimo e Cálculo de Parcelas
**Como** Atendente Financeiro, **eu desejo** registrar organicamente novas propostas de financiamento na nossa malha contábil estipulando no ato qual será a amortização e a correção aplicada, **para que** o sistema realize os cálculos matemáticos do parcelamento estipulado (nas tabelas SAC ou PRICE) automaticamente e encerre o vínculo.
* **Critérios de Aceite**:
  * A aplicação proverá caminho receptivo onde novas intenções constarão itens contábeis mandatórios (como o valor principal, a identificação do dono - `clienteId`, e o sistema a ser usado `SAC/PRICE`).
  * A arquitetura efetuará a lógica local e consolidará todas as parcelas originárias do cálculo de forma transparente.
  * *Observação:* Apenas nesta etapa inicial/Aula 02, o ecossistema presumirá que a numeração da `taxa de juros` necessária seja enviada em conjunto na entrada pela operadora. (Isto será refatorado na Aula 03 consumindo o simulador de autarquia via rede).
  * Terminada a ingestão de informações vitais, o subsistema responderá repassando atesto acompanhado do contrato e parcelas geradas.

#### Story 2: Listagem Total do Histórico de Prestações
**Como** Auditor Central de Risco, **eu desejo** obter a listagem de todos os requerimentos de crédito alocados pelo cliente na base, **para que** nós da central recuperemos a métrica absoluta da intenção exposta pelo interessado nos painéis gerenciais.
* **Critérios de Aceite**:
  * Definição de ponte de captura visual provida para emitir relatórios de massa.
  * O interessado que evocar apropriadamente este roteamento conseguirá puxar sob corpo unificado todos os empréstimos (juntamente com seu respectivo cronograma de parcelas) cadastrados provisoriamente para avaliação direta e imediata. 

#### Story 3: Cancelamento de Contrato de Empréstimo
**Como** Gerente, **eu desejo** poder excluir o registro de um empréstimo previamente cadastrado, **para que** eu possa corrigir rapidamente lançamentos indevidos ou propostas fraudulentas antes que o contrato prossiga.
* **Critérios de Aceite**:
  * O sistema deve expor um endpoint estruturado (via verbo `DELETE`) na rota de Empréstimos.
  * A identificação e consequente exclusão ocorrerá baseada unicamente no ID (UUID) do contrato fornecido.
  * A operação removerá o contrato e todo seu catálogo de parcelas da plataforma estrutural em memória, respondendo sem conteúdo no formato apropriado (Ex: `HTTP 204`).

### Modelo de Dados e Especificação da API (Empréstimos)
Para o projeto simulado nesta etapa o aplicativo fará o cálculo manual das tabelas através do seu algoritmo em memória.

#### Modelo de Dados (Empréstimo e Parcelas)
* **id**: Identificador único do contrato criado (Gerado automaticamente em formato UUID).
* **clienteId**: Identificador do cliente dono do financiamento (Obrigatório, UUID).
* **valorTotal**: Valor global emprestado originalmente (Obrigatório, Numérico, Decimal).
* **quantidadeParcelas**: Prazo cronológico do contrato em meses (Obrigatório, Inteiro).
* **tipoAmortizacao**: Sistema de amortização escolhido (Obrigatório, String predefinida em `SAC` ou `PRICE`).
* **status**: Progresso do contrato global (Retornado na response indicando `PENDENTE` ou `PAGO`).
* **taxaJurosMensal**: Taxa fixada na negociação (Numérico Inteiro).
* **parcelas**: Coleção de extratos gerada durante o cálculo inicial, compondo a liquidação mês a mês (Somente saída).
  * *Campos de cada parcela*: 
    * `id` (UUID), 
    * `ordem` (Inteiro incremental),
    * `dataVencimento` (Data), 
    * `valorAmortizacao` (Decimal), 
    * `valorJuros` (Decimal), 
    * `valorPrestacao` (Decimal),
    * `status` (String, PENDENTE/PAGA),
    * `saldoDevedor` (Decimal).

#### Especificação OpenAPI (Swagger)
A modelagem destas rotas do Domínio de Empréstimos encontra-se no arquivo dedicado: [`class_02_loan_apis.yaml`](class_02_loan_apis.yaml).
