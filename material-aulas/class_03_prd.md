# Documento de Requisitos de Produto (PRD): Aula 03 - Integrações Externas (REST Clients)

## 1. Visão Geral
Esta aula introduz a comunicação entre serviços no contexto de aplicações web distribuídas. O objetivo principal é expandir as capacidades da nossa aplicação através da integração com APIs e serviços externos. A arquitetura exigirá a criação de clientes REST (REST Clients) declarativos, simplificando a comunicação e minimizando a complexidade tradicional de integrações B2B (Business-to-Business).

## 2. Domínio do Instrutor (Projeto Clientes)
O módulo de clientes será expandido para consultar bases de dados públicas. O objetivo é automatizar o preenchimento de endereços, reduzindo a necessidade de digitação manual e mitigando possíveis erros na entrada de dados por parte dos usuários.

### Histórias de Usuário (User Stories)

#### Story 1: Preenchimento Automático de Endereço por CEP
**Como** usuário do sistema de cadastro, **eu desejo** informar apenas o número do CEP do cliente, **para que** o sistema preencha automaticamente os campos de Logradouro e Bairro antes de salvar o registro.
* **Critérios de Aceite**:
  * A API de criação de clientes passará a exigir apenas o envio do `cep` e `numero`, tornando `logradouro` e `bairro` propriedades dinâmicas gerenciadas pelo servidor.
  * O sistema deverá implementar um cliente REST para consultar a API pública do ViaCEP.
  * Ao receber a resposta da API do ViaCEP, o sistema deve extrair os dados de logradouro e bairro, enriquecer o objeto do cliente com essas informações e salvar o registro completo no banco de dados local.

### Modelo de Dados e Especificação da API

#### Modelo de Dados (Cliente - Atualizado)
* **id**: Identificador único do cliente (Gerado automaticamente em formato UUID, retornado na response).
* **nome**: Nome completo do cliente (Obrigatório, String).
* **documento**: Identificador do cliente, como CPF ou CNPJ (Obrigatório, String).
* **cep**: Código de Endereçamento Postal (Obrigatório na requisição, String).
* **numero**: Número da residência (Obrigatório na requisição, String).
* **complemento**: Complemento do endereço (Opcional, String. Se não informado, retornará 'N/A').
* **logradouro**: Rua/Avenida do cliente (Preenchido automaticamente a partir do CEP, String).
* **bairro**: Bairro do cliente (Preenchido automaticamente a partir do CEP, String).

#### Especificação OpenAPI (Swagger)
A especificação consolidada das rotas e propriedades da API de Clientes (incluindo as alterações para suporte ao ViaCEP) encontra-se detalhada no arquivo: [`class_03_client_apis.yaml`](class_03_client_apis.yaml).

## 3. Domínio do Aluno (Microserviço de Empréstimos - Projeto Final)
Nesta etapa, o serviço de empréstimos será evoluído para depender de consultas a um sistema externo de análise de risco e crédito. O cálculo do empréstimo não dependerá mais de uma taxa enviada na requisição, mas sim da taxa de juros retornada por este serviço parceiro de avaliação (o *mock server* criado para a aula).

### Histórias de Usuário (User Stories)

#### Story 1: Integração com Serviço de Avaliação de Crédito (Mock Server)
**Como** sistema de gestão de empréstimos, **eu preciso** consultar uma API de avaliação de crédito em tempo real, **para** obter a taxa de juros correta estipulada para o cliente antes de gerar o contrato.
* **Critérios de Aceite**:
  * O projeto deve incluir bibliotecas de HTTP Client (ex: Quarkus REST Client) para habilitar chamadas externas de forma nativa e não-bloqueante.
  * Deve ser criada uma interface (*RestClient*) que mapeie devidamente o endpoint e o contrato disponibilizados pelo *mock server* externo.
  * A URL do serviço externo de avaliação deve ser parametrizada através de variáveis de ambiente/configuração no `application.properties`.

#### Story 2: Aplicação da Taxa de Crédito no Empréstimo
**Como** sistema de gestão de empréstimos, **eu devo** usar a taxa de juros retornada pelo sistema de crédito externo, **para garantir** que os cálculos das parcelas do empréstimo reflitam as condições reais aprovadas.
* **Critérios de Aceite**:
  * Ao receber uma nova solicitação de empréstimo, a aplicação deve consultar a API externa de análise de crédito.
  * O sistema deverá extrair a leitura da taxa repassada pela API e utilizá-la no cálculo das parcelas subsequentes, sejam elas no esquema SAC ou PRICE.
  * A contratação apenas será efetuada, calculada e gravada no banco de dados se houver comunicação bem-sucedida e devolução válida da API parceira.

### Modelo de Dados e Especificação da API (Empréstimos - Atualizado)
Nesta etapa, o cálculo passa a ser abastecido pela taxa coletada diretamente da API externa ao longo da requisição de criação.

#### Modelo de Dados (Empréstimo e Parcelas)
* **id**: Identificador único do contrato criado (Gerado automaticamente em formato UUID).
* **clienteId**: Identificador do cliente dono do financiamento (Obrigatório, UUID).
* **valorTotal**: Valor global emprestado (Obrigatório, Numérico, Decimal).
* **quantidadeParcelas**: Prazo do contrato em meses (Obrigatório, Inteiro).
* **tipoAmortizacao**: Sistema de amortização escolhido (Obrigatório, String predefinida em `SAC` ou `PRICE`).
* **status**: Estado do contrato (Retornado na response indicando `PENDENTE` ou `PAGO`).
* **taxaJurosMensal**: Taxa praticada no contrato (**Removida do payload de requisição**. Passará a ser definida internamente consultando o serviço de Elegibilidade e retornada nos dados do contrato).
* **parcelas**: A coleção referente aos boletos ou extratos de pagamento gerados no acordo.
  * *Campos de cada parcela*: 
    * `id` (UUID), `ordem` (Inteiro progressivo), `dataVencimento` (Data), `valorAmortizacao` (Decimal), `valorJuros` (Decimal), `valorPrestacao` (Decimal), `status` (String, PENDENTE/PAGA) e `saldoDevedor` (Decimal).

#### Especificação OpenAPI (Swagger)
A especificação refatorada indicando a remoção do envio explicito de taxa via requisição de cadastro (`POST /emprestimos`) encontra-se no arquivo: [`class_03_loan_apis.yaml`](class_03_loan_apis.yaml).
