# Projeto Final: Gestão de Empréstimos com Quarkus

## Contexto

O objetivo deste projeto final é aplicar os conceitos vistos em aula construindo uma aplicação backend completa utilizando **Quarkus**. Considerando que já possuímos domínio local sobre clientes, o desafio deste projeto foca completamente no Microsserviço central de negócios: a **Gestão de Empréstimos e Parcelas**, guiado por um Documento de Requisitos (PRD).

## Microsserviço de Empréstimos e API de Avaliação (Taxas)

Para exercitar a construção modular baseada em microsserviços, adotaremos este paradigma:

- O microsserviço registrará internamente `clienteId` nas transações, atuando em banco de dados isolado da rotina de cadastro original.

- **Integração Externa Obrigatória**: A aplicação principal não deve estipular localmente taxas de juros internamente. É obrigatório criar um **MicroProfile REST Client** e consumir a API de Elegibilidade (`PROJETO_FINAL_taxas_api.yaml`) do ambiente mockup. Essa consulta antecede as regras de gravação, onde dados aceitos pelas regras de crédito no mercado retornam as avaliações (`Status 200`) ou indicam perfis não aprovados, resultando no cancelamento imediato da emissão (`Status 400`).

## Regras de Negócio e Cálculos SAC e PRICE

A responsabilidade de faturar e calcular a progressão monetária recai diretamente sobre a lógica presente na sua infraestrutura.
Ao reter os juros retornados pela comunicação cliente REST, sua camada de serviços atuará processando as simulações matemáticas:

- **Tabela SAC** (Valores de Amortização ficam estáticos e constantes, apenas a despesa de juros avança ao longo dos vencimentos mensais).

- **Tabela PRICE** (As prestações totais dos boletos mensais são matemáticas e fixas, o que oscila é a sua subdivisão entre amortização e juros).

Por fim, salve num movimento atômico o Empréstimo (raiz) agregando todas as suas `N` Parcelas atriadas por meio dos recursos de *Cascade* nas configurações da entidade.

## Critérios de Avaliação (Sistema de 100 Pontos)

### 1. Modelagem de Dados (10 pontos)

- **[10 pts]** Construção padronizada das entidades `Emprestimo` e `Parcela`, espelhando a declaração do PRD. Relacionamentos devem conter mapeamento ORM (Hibernate) contendo funcionalidade de `Cascade`.

### 2. Requisitos Funcionais (20 pontos)

- **[10 pts]** Criação base de requisição de Empréstimo: Integrar a chamada externa conectada com sucesso na geração em banco dos extratos.

- **[5 pts]** Listagem estrutural capaz de exibir os Empréstimos atrelados de cliente por ID.

- **[3 pts]** Endpoint simples que retorne lista com as Parcelas (carnê) que pertencem a um contrato.

- **[3 pts]** Endpoint extra capaz de capturar e inspecionar em detalhes e UUID os atributos isolados correspondentes à uma Parcela.

- **[4 pts]** Possibilidade de pagamento fictício, finalizando seu status para (`PAGA`).

### 3. Endpoints e Padrões REST (15 pontos)

- **[15 pts]** Atendimento Nível 2 na maturidade de Richardson. Mantenha os caminhos em plural, verbos operando coesos com e objetos JSON padronizados.

### 4. Validação (10 pontos)

- **[10 pts]** Mecanismos bloqueadores do Quarkus Hibernate Validator: Validação mandatória declarada via constraints nos requests (`valorTotal` engessado na barreira entre R$100.00 e R$10.000.000, tipos enumerados).

### 5. Media-Types HTTP (5 pontos)

- **[5 pts]** Configurações do Header travando processamentos a JSON puro (retornando as indicações corretas de requisição não suportada).

### 6. Centralização e Exceções (10 pontos)

- **[10 pts]** Criação isolada de `ExceptionMapper` de instâncias de erro lançadas em Runtime pelo Quarkus e seus validadores internos (ou falhas no HTTP client). Nenhuma view ou payload ao consumidor deve constar uma falha java na StackTrace original.

### 7. Banco de Dados e ORM (15 pontos)

- **[15 pts]** Executar gerência pela engrenagem **Hibernate com Panache Entity/Repository**, manipulando as faturas derivadas da persistência da aplicação local. Foco ao transacionamento único e consistente (salvando dezenas de tabelas relativas contidas integralmente no processo). Suporta nativo H2 Database.

### 8. Gestão e DI (5 pontos)

- **[5 pts]** Uso amplo de Injeção de Dependências nativas da tecnologia implementada na Stack (CDI).

### 9. Segurança via Perfil (10 pontos)

- **[10 pts]** Restrição mandatória no deleite do Quarkus framework (Security). Todo acesso sensível deve barrar requests baseando em autenticação prévia (e.g. métodos restritos por `@RolesAllowed("Gerente")`).

---

*Total Obrigatório: 100 pontos.*

## Desafios Extras Completos (Opcional)

- Expor requisições de listagem suportando paginação.

- Construir Profile no Quarkus exclusivo (`%prod`) parametrizando variáveis de banco que consigam ler variáveis de ambiente do PostgreSQL, superando dev e test com o database na memoria h2.

## Instruções de Entrega

1. O desenvolvimento deve observar fielmente os termos contidos na declaração funcional detalhada no arquivo [`PROJETO_FINAL_PRD.md`](PROJETO_FINAL_PRD.md).
2. As visões de projeto e topologia são tratadas no escopo do arquivo [`PROJETO_FINAL_architecture.md`](PROJETO_FINAL_architecture.md).
3. O contrato da API corporativa *exatamente como deve ser entregue* foi fornecido pre-construído em [`PROJETO_FINAL_emprestimos_api.yaml`](PROJETO_FINAL_emprestimos_api.yaml). Siga os endpoints ali informados rigorosamente.
4. A integração Client baseia-se na documentação Mockup da especificação [`PROJETO_FINAL_taxas_api.yaml`](PROJETO_FINAL_taxas_api.yaml).
