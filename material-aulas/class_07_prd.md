# Documento de Requisitos de Produto (PRD): Aula 07 - Segurança

## 1. Visão Geral

Nesta aula, o foco é a proteção dos recursos da API. Implementaremos autenticação e autorização básica utilizando **Quarkus Security (Properties)**.
**Dependência necessária:** `quarkus-elytron-security-properties`

> **IMPORTANTE:** Todos os endpoints da aplicação (tanto de clientes quanto de empréstimos) deverão exigir, no mínimo, um usuário autenticado. Nenhum endpoint deve permanecer com acesso público/anônimo.

## 2. Domínio do Instrutor (Projeto Clientes)

Demonstração de como proteger endpoints de consulta de clientes.

### Histórias de Usuário - Clientes

#### Story 1: Autenticação e Autorização de Operadores

**Como** administrador do sistema, **eu desejo** que apenas usuários com o perfil de `Gerente` possam listar todos os clientes cadastrados.

* **Critérios de Aceite**:
  * Adicionar a extensão `quarkus-elytron-security-properties`.
  * Endpoint `GET /clientes` deve exigir autenticação e o papel de `Gerente`.
  * Configurar usuários, senhas e perfis via `application.properties` (para fins de demonstração rápida).

## 3. Domínio do Aluno (Microserviço de Empréstimos)

Proteção de operações críticas de negócio.

### Histórias de Usuário - Empréstimos

#### Story 1: Autorização Baseada em Perfis (RBAC)

**Como** gestor financeiro, **eu exijo** que apenas usuários com o perfil `Gerente` possam deletar ou cancelar contratos de empréstimo.

* **Critérios de Aceite**:
  * Endpoint `DELETE /emprestimos/{id}` protegido com `@RolesAllowed("Gerente")`.
  * Usuários com perfil `Usuario` devem conseguir apenas criar e listar seus empréstimos.

## 4. Requisitos Não Funcionais

* **Segurança**: Validação de autenticação básica e controle de acesso via Roles (RBAC).
