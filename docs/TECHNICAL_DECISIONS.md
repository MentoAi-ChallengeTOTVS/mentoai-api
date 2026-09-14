# Decisões Técnicas

## Objetivo

Este documento registra decisões arquiteturais estáveis do MentoAI.

Ele não substitui `AGENTS.md`.

`AGENTS.md` define como trabalhar no repositório.

Este arquivo registra **por que a arquitetura foi escolhida desta forma**.

---

# TD-001 — Monólito modular

**Status:** Aceita

## Contexto

O MentoAI possui diferentes áreas funcionais, mas o MVP não exige independência operacional, deploy separado ou escalabilidade individual por serviço.

## Decisão

Implementar uma única aplicação Spring Boot estruturada como monólito modular.

Bounded contexts:

```text
meeting
analysis
alert
copilot
user
```

## Consequências

- um único deploy;
- menor complexidade operacional;
- separação lógica por contexto;
- possibilidade futura de extrair módulos somente se houver necessidade concreta.

Microservices não fazem parte da arquitetura inicial.

---

# TD-002 — DDD e Clean Architecture pragmáticos

**Status:** Aceita

## Contexto

O projeto precisa de separação arquitetural, mas abstrações excessivas aumentariam o custo de implementação do MVP.

## Decisão

Utilizar princípios de DDD e Clean Architecture sem exigir todos os patterns associados.

Camadas internas:

```text
domain
application
infrastructure
presentation
```

A arquitetura deve crescer conforme o código real.

## Consequências

- separação clara de responsabilidades;
- menos boilerplate;
- nenhum pacote vazio criado apenas por padrão;
- patterns devem existir somente quando resolverem um problema concreto.

---

# TD-003 — Domain independente de frameworks

**Status:** Aceita

## Contexto

Entidades de negócio não devem depender de escolhas de persistência ou transporte.

## Decisão

Entidades Domain permanecem POJOs.

Não utilizar no Domain:

```text
Spring
JPA
Hibernate
Controllers
HTTP DTOs
Oracle-specific types
Azure OpenAI SDK
```

## Consequências

- Domain testável isoladamente;
- menor acoplamento tecnológico;
- mudanças de infraestrutura não exigem modificar entidades de negócio.

---

# TD-004 — Domain Entity separada de JPA Entity

**Status:** Aceita

## Contexto

Adicionar annotations e necessidades do ORM diretamente às entidades Domain criaria acoplamento entre modelo de negócio e persistência.

## Decisão

Utilizar classes separadas.

Exemplo:

```text
domain/entity/Cliente.java

infrastructure/persistence/entity/ClienteJpaEntity.java
```

Mapeadores convertem entre os modelos.

## Consequências

Positivas:

- separação mais clara;
- Domain sem JPA;
- liberdade de modelagem do banco.

Custo:

- mais classes;
- necessidade de persistence mappers.

Esse custo é aceito enquanto a separação continuar simples e explícita.

---

# TD-005 — Repository contract no Domain e adapter na Infrastructure

**Status:** Aceita

## Contexto

Application não deve depender diretamente de Spring Data.

## Decisão

Definir contratos de repository no Domain.

Infrastructure implementa esses contratos usando Spring Data JPA.

Fluxo:

```text
Application
→ Domain Repository
← Repository Adapter
→ Spring Data Repository
→ JPA
```

## Consequências

- Application não conhece `JpaRepository`;
- persistência pode ser substituída/testada por contrato;
- evita vazamento de JPA para camadas internas.

---

# TD-006 — Application Services coesos

**Status:** Aceita

## Contexto

Uma interface e uma implementação para cada operação simples aumentariam o número de classes sem benefício proporcional no MVP.

## Decisão

Application Services podem agrupar operações coesas.

Exemplo:

```text
ClienteService
├── criar
├── buscarPorId
├── listar
├── atualizar
└── alterarStatus
```

Não criar por padrão:

```text
CriarClienteUseCase
CriarClienteUseCaseImpl
ClienteService
ClienteServiceImpl
ClienteFacade
```

Fluxos complexos podem ganhar services dedicados.

## Consequências

- menor boilerplate;
- organização por responsabilidade;
- classes continuam limitadas ao bounded context.

God Services que cruzam contextos continuam proibidos.

---

# TD-007 — Controllers finos e DTOs explícitos

**Status:** Aceita

## Contexto

Expor entidades diretamente pela API acoplaria contratos HTTP ao Domain e à persistência.

## Decisão

Controllers apenas:

```text
recebem
validam
delegam
respondem
```

Utilizar request/response DTOs próprios.

Preferir Java `record` para DTOs simples.

## Consequências

- contrato HTTP independente;
- mudanças de Domain não vazam automaticamente para API;
- validações de entrada permanecem na borda.

---

# TD-008 — Flyway controla evolução do schema

**Status:** Aceita

## Contexto

Hibernate consegue criar/alterar schema, mas o projeto possui um modelo Oracle explícito e precisa de evolução reproduzível.

## Alternativas consideradas

### Hibernate `ddl-auto=update`

Vantagem:

- configuração inicial simples.

Desvantagens:

- evolução implícita;
- histórico de mudança menos controlado;
- maior risco de diferenças entre ambientes.

### Flyway

Vantagens:

- migrations versionadas;
- histórico explícito;
- banco reproduzível;
- alinhamento com o DDL controlado.

## Decisão

Flyway é responsável pela criação e evolução do schema.

Hibernate utiliza:

```text
ddl-auto=validate
```

## Consequências

Toda mudança estrutural do banco deve gerar uma nova migration.

---

# TD-009 — Oracle como banco relacional

**Status:** Aceita

## Contexto

O projeto acadêmico possui modelagem de Database Design direcionada ao Oracle.

## Decisão

Manter Oracle como banco relacional da aplicação.

## Consequências

- tipos e constraints físicos seguem Oracle;
- detalhes Oracle permanecem restritos à Infrastructure e migrations;
- Domain deve representar semântica, não tipos físicos.

Exemplo:

```text
Oracle NUMBER(1)
↔
Domain Boolean
```

quando o campo representar verdadeiro/falso.

---

# TD-010 — Relacionamentos JPA preferencialmente unidirecionais

**Status:** Aceita

## Contexto

Coleções bidirecionais aumentam complexidade do grafo de entidades, sincronização de lados e risco de carregamento desnecessário.

## Decisão

Mapear preferencialmente a relação no lado que contém a FK.

Exemplo:

```text
REUNIAO.CLIENTE_ID
→ ReuniaoJpaEntity.cliente
```

Não criar automaticamente:

```text
ClienteJpaEntity.reunioes
```

## Consequências

- grafos menores;
- menos efeitos colaterais;
- consultas reversas são implementadas por repositories quando necessárias.

---

# TD-011 — Enums persistidos como String

**Status:** Aceita

## Contexto

Valores de enums estão representados explicitamente por CHECK constraints no banco.

## Decisão

Persistir enums utilizando representação textual.

```java
@Enumerated(EnumType.STRING)
```

Não utilizar ordinal.

## Consequências

- banco legível;
- compatibilidade com constraints;
- reordenação de enum não altera significado persistido.

---

# TD-012 — Vertical slices para evolução da arquitetura

**Status:** Aceita

## Contexto

Implementar todas as JPA Entities, adapters, services e controllers simultaneamente aumentaria o risco de replicar uma arquitetura não validada.

## Decisão

Implementar primeiro um fluxo vertical completo.

Exemplo inicial:

```text
Cliente
→ JpaEntity
→ Mapper
→ Repository
→ Adapter
→ Service
→ DTO
→ Controller
→ Tests
```

Somente depois replicar o padrão.

## Consequências

- feedback arquitetural rápido;
- mudanças menores;
- menos refatoração em massa.

---

# TD-013 — Azure OpenAI como integração externa

**Status:** Aceita

## Contexto

O projeto utiliza LLM via API e não pretende treinar modelos próprios.

## Decisão

Azure OpenAI pertence à Infrastructure.

Domain e regras de aplicação não dependem de modelos específicos do provider.

## Consequências

- integração substituível;
- credenciais isoladas;
- Domain independente do fornecedor.

---

# TD-014 — Engenharia de contexto antes de RAG

**Status:** Aceita

## Contexto

Grande parte dos dados relevantes do MentoAI é estruturada e pode ser recuperada diretamente do banco relacional.

Adicionar embeddings, vector database e RAG no início aumentaria complexidade e custo.

## Decisão

No MVP, priorizar:

```text
IntentResolver
→ ContextResolver
→ consultas estruturadas
→ PromptBuilder
→ LLM
```

RAG/embeddings só devem ser adicionados se houver limitação concreta na recuperação estruturada.

## Consequências

- MVP mais simples;
- menor custo operacional;
- menor superfície de falha;
- arquitetura continua extensível para recuperação vetorial futura.

---

# TD-015 — Identificação de intenção simples no MVP

**Status:** Aceita

## Contexto

As intenções principais do copiloto são limitadas e conhecidas.

## Decisão

Começar com regras simples e determinísticas.

Intenções iniciais:

```text
CLIENT_RISK
CLIENT_OPPORTUNITY
CLIENT_SUMMARY
CLIENT_EVOLUTION
COMPETITOR_ANALYSIS
GENERAL_CONTEXT
```

Permitir múltiplas intenções quando necessário.

## Consequências

- comportamento previsível;
- implementação rápida;
- fácil depuração;
- classificação por LLM pode ser adicionada futuramente se regras se tornarem insuficientes.

---

# TD-016 — Sem abstrações genéricas prematuras

**Status:** Aceita

## Decisão

Não criar sem necessidade concreta:

```text
BaseService
BaseRepository
GenericService
GenericMapper
Manager
Facade
Factory trivial
CQRS
Event Sourcing
Microservices
Message Broker
Multi-agent architecture
```

## Consequência

A arquitetura deve permanecer orientada pelos problemas reais do projeto, não por quantidade de patterns implementados.
