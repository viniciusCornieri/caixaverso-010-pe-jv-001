# ADR 001: Camada de Service e Injeção de Dependência (CDI)

## Status

Proposto

## Contexto

No desenvolvimento de microsserviços com Quarkus, é comum que a lógica de negócio acabe "vazando" para os Controllers (JAX-RS). Precisamos de um padrão que facilite o teste unitário, promova o reuso e siga o Princípio de Responsabilidade Única (SOLID), sem a complexidade excessiva de gerenciar múltiplos arquivos de Use Cases para operações simples.

## Decisão

Adotaremos o padrão **Service** suportado pelo **CDI (Contexts and Dependency Injection)**.

1. **Service**: As regras de negócio e integrações serão encapsuladas em classes de serviço (ex: `EmprestimoService`), agrupando comportamentos relacionados de um mesmo domínio.
2. **CDI**: Utilizaremos o Quarkus para gerenciar o ciclo de vida dessas classes através de escopos como `@ApplicationScoped`.

## Consequências

- **Positivas**: Facilidade para testar a lógica de negócio isoladamente; código mais legível e centralizado por domínio; desenvolvimento mais ágil para projetos de médio porte.

- **Negativas**: Risco de criar "God Classes" se o serviço crescer demais (deve ser monitorado e quebrado se necessário).

## Guia de Implementação

- **Nomeação**: `NomeDoDominioService.java`

- **Escopo**: `@ApplicationScoped` por padrão.

- **Injeção**: Sempre preferir `@Inject` no Controller para chamar o Service.
