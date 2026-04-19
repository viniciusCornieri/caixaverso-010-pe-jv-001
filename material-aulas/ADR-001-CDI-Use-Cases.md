# ADR 001: Arquitetura Baseada em Use Cases e Injeção de Dependência (CDI)

## Status

Proposto

## Contexto

No desenvolvimento de microsserviços com Quarkus, é comum que a lógica de negócio acabe "vazando" para os Controllers (JAX-RS) ou se tornando Services gigantescos e difíceis de manter. Precisamos de um padrão que facilite o teste unitário e siga o Princípio de Responsabilidade Única (SOLID).

## Decisão

Adotaremos o padrão **Use Case** (ou Interactor) suportado pelo **CDI (Contexts and Dependency Injection)**.

1. **Use Case**: Cada ação de negócio (ex: `CalcularTaxaEmprestimo`) será uma classe única.
2. **CDI**: Utilizaremos o Quarkus para gerenciar o ciclo de vida dessas classes através de escopos como `@ApplicationScoped`.

## Consequências

- **Positivas**: Facilidade para testar a lógica de negócio isoladamente; código mais legível e desacoplado; conformidade com Clean Architecture.

- **Negativas**: Aumento no número de arquivos no projeto.

## Guia de Implementação

- **Nomeação**: `NomeDaAcaoUseCase.java`

- **Escopo**: `@ApplicationScoped` por padrão.

- **Injeção**: Sempre preferir `@Inject` no Controller para chamar o Use Case.
