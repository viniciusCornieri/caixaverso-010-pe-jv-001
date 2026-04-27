# Documento de Requisitos de Produto (PRD): Aula 06 - Validação e Tratamento de Erros

## 1. Visão Geral

Nesta aula, o objetivo é garantir a integridade dos dados que entram no sistema e fornecer feedback claro e padronizado ao cliente da API em caso de falhas de negócio ou dados inconsistentes. O sistema deve barrar entradas inválidas precocemente e manter a estabilidade operacional, validando todos os fluxos na porta de entrada da aplicação.

## 2. Domínio do Instrutor (Projeto Clientes)

O projeto de clientes será atualizado para validar os dados de entrada e tratar exceções de negócio de forma global, garantindo que as respostas a erros sigam uma padronização rigorosa.

### Histórias de Usuário - Clientes

#### Story 1: Validação de Cadastro de Cliente

**Como** usuário do sistema de cadastro de clientes, **eu desejo** garantir que os dados obrigatórios do cliente sejam consistentes e íntegros no momento do cadastro, **para que** dados incorretos ou duplicados possam ser barrados.

* **Critérios de Aceite**:
  * O campo `nome` é obrigatório e deve possuir no mínimo 4 caracteres não vazios e no máximo 150 caracteres.
  * O campo `documento` (CPF/CNPJ) deve ser válido possuindo 11 ou 14 caracteres não vazios.
  * O campo `cep` é obrigatório e deve possuir 8 caracteres numéricos não vazios.
  * O campo `numero` é obrigatório e deve possuir no máximo 20 caracteres não vazios.
  * O campo `complemento` é opcional e deve possuir no máximo 50 caracteres não vazios.
  * A validação deve ocorrer antes do processamento das regras de negócio (rejeição prematura de dados mal formados).

#### Story 2: Prevenção de Duplicidade de Cadastro

**Como** usuário do sistema de cadastro de clientes, **eu desejo** garantir que o mesmo cliente não seja cadastrado mais de uma vez, **para que** a base de dados mantenha-se íntegra e sem redundâncias.

* **Critérios de Aceite**:
  * O sistema deve evitar a duplicação de cadastros, rejeitando a criação de um novo cliente caso o documento informado (CPF/CNPJ) já exista na base de dados.

#### Story 3: Padronização de Erros (Respostas do Sistema)

**Como** consumidor da API, **eu desejo** que qualquer erro de validação retorne um payload padronizado, **para que** eu consiga entender claramente qual campo falhou e o porquê.

* **Critérios de Aceite**:
  * O sistema deve explorar e manter a estrutura de resposta nativa do Quarkus (`ConstraintViolationException`).
  * A resposta de erro HTTP 400 deve conter `title` ("Constraint Violation"), `status` (400) e uma lista de `violations`.
  * Cada violação deve detalhar o `field` (campo problemático) e a `message` (motivo da falha).

## 3. Domínio do Aluno (Microserviço de Empréstimos)

Os alunos devem implementar restrições estritas no payload de contratação de empréstimo para garantir o rigor financeiro e a conformidade com as regras de avaliação de crédito e limites de capital do banco.

### Histórias de Usuário - Empréstimos

#### Story 1: Restrições de Contratação

**Como** analista de risco, **eu exijo** que todos os parâmetros vitais do empréstimo sejam validados rigorosamente em cada proposta, incluindo limites financeiros, prazo máximo e a efetiva identificação do solicitante.

* **Critérios de Aceite**:
  * O campo `clienteId` é obrigatório e deve representar um identificador único de conta válido (UUID);
  * O campo `valorTotal` deve estar restrito a solicitações entre R$ 100,00 e R$ 10.000.000,00.
  * O campo `quantidadeParcelas` deve estipular um prazo mínimo de 1 e máximo de 480 meses para quitação.
  * O campo `tipoAmortizacao` deve ser validado somente contra os sistemas matemáticos formalmente autorizados pela instituição (`SAC` ou `PRICE`).

#### Story 2: Automação e Validação de Fluxo Crítico (Opcional)

**Como** analista de qualidade (QA), **eu desejo** garantir que o fluxo de criação de empréstimo funcione de ponta a ponta de maneira robusta, cobrindo cenários de sucesso e assegurando a eficácia das barreiras de entrada contra falhas.

* **Critérios de Aceite**:
  * O sistema deve possuir scripts ou testes automatizados para validar integralmente a esteira de contratação de empréstimo em um ambiente controlado.
  * Enviar um payload inválido (por exemplo: um valor total estourando o limite ou omitir o `clienteId`) deve obrigatoriamente resultar no erro HTTP `400 Bad Request`, detalhando a violação de regra na saída.
  * Deve ser comprovado que a validação estrutural é capaz de barrar a execução antes do acionamento de qualquer integração com a API Externa de Risco.

## 4. Requisitos Não Funcionais

* **Feedback**: As mensagens de erro expostas pelas falhas de validação devem ser humanizadas e em português claro.
* **Testes de Resiliência**: A cobertura de automação de testes (Testes de Integração) deve abranger intencionalmente os casos limítrofes (Edge Cases), comprovando a tolerância do sistema a envios incorretos da parte do cliente da API.
