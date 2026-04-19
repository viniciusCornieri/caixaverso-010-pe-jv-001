# ADR 002: Isolamento de Endpoints e Proibição de Exposição de Objetos de Domínio

## Status

Proposto

## Contexto

Atualmente, as APIs estão sendo construídas. Ao retornar objetos de domínio diretamente nos endpoints, criamos um acoplamento forte entre a API externa e a estrutura interna dos dados. Na Aula 05, introduziremos a persistência de dados (datasource/Panache), e expor entidades de banco de dados diretamente nos endpoints é uma prática arriscada que pode expor dados sensíveis ou causar falhas de serialização.

## Decisão

Estabelecemos que **objetos de domínio (Entities/Models) nunca devem ser retornados diretamente pelos métodos dos Controllers (Resources)**.

1. **DTO (Data Transfer Objects)**: Toda comunicação externa (entrada e saída) deve ser feita exclusivamente através de DTOs.
2. **Mapeamento**: Os Use Cases ou Services devem ser responsáveis por converter objetos de domínio em DTOs antes de retorná-los para o Resource.
3. **Isolamento de Contrato**: Mudanças na estrutura do banco de dados ou no domínio interno não devem quebrar o contrato da API automaticamente.

## Consequências

- **Positivas**: Proteção da integridade do domínio; flexibilidade para evoluir o banco de dados sem quebrar a API; controle total sobre o que é serializado no JSON de resposta.

- **Negativas**: Necessidade de criar classes adicionais (DTOs) e lógica de mapeamento (boilerplate).

## Guia de Implementação

- **Pacote**: Os DTOs devem residir em um pacote específico de transporte (ex: `dto.response` e `dto.request`).

- **Prática**: Mesmo que o DTO seja idêntico ao objeto de domínio inicialmente, a separação deve ser mantida.

- **Preparação**: Este isolamento facilitará a integração com Hibernate Panache na Aula 05, onde as Entities terão anotações e comportamentos específicos de banco de dados.
