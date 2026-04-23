# Documento de Requisitos de Produto (PRD): Aula 05 - Persistência com Panache

## 1. Visão Geral

Nesta aula, o objetivo é evoluir o Microsserviço de Empréstimos para que os dados deixem de ser voláteis e passem a ser persistidos em um banco de dados relacional. Utilizaremos o **Hibernate Panache** para simplificar o mapeamento objeto-relacional (ORM) e as operações de CRUD.

## 2. Domínio do Instrutor (Projeto Clientes)

O projeto de clientes será atualizado para persistir dados de `Cliente` e `Endereco` utilizando o padrão **Active Record (PanacheEntity)**.

### Histórias de Usuário - Clientes

#### Story 1: Persistência de Cadastro com Endereço

**Como** sistema de cadastro, **eu desejo** que os dados do cliente e seu endereço (obtido via ViaCEP) sejam gravados permanentemente, **para que** as informações não sejam perdidas ao reiniciar o servidor.

* **Critérios de Aceite**:

  * As entidades `Cliente` e `Endereco` devem estender `PanacheEntity`.
  * O relacionamento entre `Cliente` e `Endereco` deve ser `@OneToOne` com persistência em cascata.
  * O uso de métodos estáticos do Panache (ex: `Cliente.persist()`) deve ser demonstrado no Service.

## 3. Domínio do Aluno (Microserviço de Empréstimos - Projeto Final)

Os alunos devem implementar a persistência do fluxo de empréstimos e suas faturas utilizando o padrão **Repository (PanacheRepository)**.

### Histórias de Usuário - Empréstimos

#### Story 1: Registro Permanente de Contratos de Empréstimo

**Como** operador financeiro, **eu preciso** que cada empréstimo aprovado seja gravado no banco de dados, **para que** possamos realizar auditorias e cobranças futuras.

* **Critérios de Aceite**:

  * A entidade `Emprestimo` deve ser mapeada com `@Entity`.
  * Deve existir um `EmprestimoRepository` que implemente `PanacheRepository<Emprestimo>`.
  * A persistência deve ocorrer dentro de um contexto transacional (`@Transactional`).

#### Story 2: Gerenciamento de Parcelas em Cascata

**Como** sistema financeiro, **eu devo** gravar todas as parcelas calculadas junto com o contrato de empréstimo em uma única transação, **para garantir** a integridade dos dados.

* **Critérios de Aceite**:

  * O relacionamento entre `Emprestimo` e `Parcela` deve ser `@OneToMany`.
  * Deve ser configurado `cascade = CascadeType.ALL` e `orphanRemoval = true`.
  * Ao salvar o objeto `Emprestimo`, a lista de `Parcela` deve ser persistida automaticamente.

## 4. Requisitos Não Funcionais e Padrões

* **Banco de Dados**: Suporte a H2 (em memória para dev) e PostgreSQL (via Docker para demonstração).

* **Camadas**: A persistência deve ser injetada nos **Services**, mantendo o Controller limpo.

* **Estratégia de ID**: Utilizar geração automática de ID do banco, mas discutir a possibilidade de IDs customizados (como ULID/UUID) conforme aulas anteriores.

## 5. Especificação da API (YAML)

As rotas permanecem as mesmas das aulas anteriores, mas agora o comportamento esperado é a persistência real dos dados.
