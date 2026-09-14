# MentoAI — AGENTS.md

## 1. Projeto

MentoAI é um Copiloto Comercial Consultivo desenvolvido para o Challenge TOTVS.

O backend utiliza:

- Java 21
- Spring Boot
- Oracle
- JPA / Hibernate
- Flyway
- Azure OpenAI

A aplicação deve ser estruturada como um **monólito modular**, organizado pelos bounded contexts:

- `meeting` — clientes, reuniões e transcrições
- `analysis` — análises de IA, insights e sinais comerciais
- `alert` — alertas comerciais e controle de leitura
- `copilot` — conversas e perguntas contextualizadas
- `user` — usuários e perfis de acesso

A IA apoia decisões comerciais humanas. Ela não deve tomar decisões de negócio automaticamente.

---

## 2. Arquitetura

Utilizar DDD + Clean Architecture de forma pragmática.

Cada bounded context pode conter:

```text
<context>/
├── domain/
├── application/
├── infrastructure/
└── presentation/
```

Não criar diretórios vazios antecipadamente.

Criar estruturas somente quando existir código real que pertença a elas.

A direção conceitual das dependências deve ser:

```text
presentation
    ↓
application
    ↓
domain

infrastructure → implementa contratos necessários pelas camadas internas
```

Preservar a separação entre bounded contexts.

Não criar serviços centrais que concentrem responsabilidades de vários contextos.

---

## 3. Domain

O `domain` representa conceitos e regras de negócio.

Pode conter:

- entidades
- enums
- contratos de repository
- comportamentos de domínio independentes de framework

As entidades de domínio devem permanecer POJOs.

Não utilizar dentro do `domain`:

- Spring
- JPA
- Hibernate
- `@Entity`
- `@Table`
- `@Id`
- `@Repository`
- `@Service`
- Controllers
- DTOs HTTP
- JDBC
- tipos específicos do Oracle
- SDKs ou clientes da Azure OpenAI

Não alterar o Domain apenas para facilitar a persistência.

A infraestrutura deve se adaptar ao Domain, e não o contrário.

---

## 4. Persistência

Persistência pertence à camada `infrastructure`.

Estrutura preferencial quando necessária:

```text
infrastructure/
└── persistence/
    ├── entity/
    ├── repository/
    ├── adapter/
    └── mapper/
```

Entidades de domínio e entidades JPA possuem responsabilidades diferentes.

Exemplo:

```text
domain/entity/Cliente.java

infrastructure/persistence/entity/ClienteJpaEntity.java
```

As annotations JPA devem existir somente nas entidades de persistência.

Evitar relacionamentos JPA bidirecionais sem necessidade concreta.

Preferir que a entidade correspondente à tabela que possui a FK mantenha a referência.

Utilizar `FetchType.LAZY` nos relacionamentos sempre que adequado.

Persistir enums utilizando:

```java
@Enumerated(EnumType.STRING)
```

Não persistir enums por ordinal.

---

## 5. Repositories

Existem dois conceitos distintos de repository.

### Repository de domínio

Define o contrato necessário pela aplicação.

Exemplo:

```java
public interface ClienteRepository {

    Cliente salvar(Cliente cliente);

    Optional<Cliente> buscarPorId(Long id);
}
```

Não deve estender interfaces do Spring Data.

### Repository de infraestrutura

Pode utilizar Spring Data JPA.

Exemplo:

```java
public interface SpringDataClienteRepository
        extends JpaRepository<ClienteJpaEntity, Long> {
}
```

### Adapter

A Infrastructure deve implementar os contratos do Domain através de adapters.

Exemplo:

```java
@Repository
public class ClienteRepositoryAdapter
        implements ClienteRepository {
}
```

Não criar `BaseRepository` ou repositories genéricos sem uma necessidade concreta.

---

## 6. Application Services

Application Services representam casos de uso e orquestração.

Um Service pode concentrar múltiplas operações quando elas pertencem à mesma responsabilidade.

Exemplo aceitável:

```text
ClienteService

- criar
- buscarPorId
- listar
- atualizar
- alterarStatus
```

Não criar automaticamente:

- `UseCase`
- `UseCaseImpl`
- `Service` + `ServiceImpl`
- `Manager`
- `Facade`

quando existe apenas uma implementação e nenhuma necessidade concreta de abstração.

Preferir poucos services coesos em vez de uma classe por operação trivial.

Não criar God Services que atravessem bounded contexts.

Exemplo inadequado:

```text
MentoAIService
- criarCliente
- autenticarUsuario
- processarReuniao
- gerarAlerta
- perguntarCopiloto
- montarDashboard
```

Fluxos mais complexos podem possuir services específicos.

Exemplos:

- `ProcessarAnaliseService`
- `ConsolidarContextoClienteService`
- `PerguntarCopilotoService`
- `GerarSugestoesEstrategicasService`

Transações devem ficar preferencialmente na camada Application.

Utilizar `@Transactional` quando necessário.

---

## 7. REST API e DTOs

Controllers pertencem à camada `presentation`.

Controllers devem ser finos.

Fluxo esperado:

```text
HTTP Request
→ validação
→ Application Service
→ HTTP Response
```

Não implementar regras de negócio dentro de Controllers.

Não expor diretamente entidades do Domain pela API.

Utilizar DTOs específicos para entrada e saída.

Preferir Java `record` para DTOs simples.

Exemplo:

```java
public record CriarClienteRequest(
        @NotBlank String nome,
        @NotBlank String segmento,
        @NotBlank String porte
) {
}
```

Request DTOs podem utilizar Jakarta Bean Validation.

---

## 8. Mappers

Separar mapeamento de persistência de mapeamento HTTP.

Persistência:

```text
Domain ↔ JpaEntity
```

API:

```text
Request ↔ Domain ↔ Response
```

Preferir mapeadores manuais enquanto os mapeamentos forem simples.

Não adicionar MapStruct, ModelMapper ou bibliotecas semelhantes sem necessidade concreta.

---

## 9. Factories

Não criar uma Factory para cada entidade.

Preferir:

- construtores
- métodos factory estáticos
- métodos de domínio

Exemplo:

```java
AnaliseIA.pendente(reuniao);
```

Criar uma classe Factory separada somente quando a construção de um objeto possuir complexidade real.

Não adicionar Factories apenas para seguir um pattern.

---

## 10. Banco de dados e Flyway

O banco utilizado é Oracle.

Flyway é responsável pela criação e evolução do schema.

Migrations ficam em:

```text
src/main/resources/db/migration/
```

Formato:

```text
V1__create_initial_schema.sql
V2__add_example_field.sql
V3__create_example_index.sql
```

Depois que uma migration tiver sido aplicada e compartilhada, não alterá-la para representar uma nova mudança.

Criar uma nova migration.

Hibernate deve mapear e validar o banco, não controlar sua evolução.

Configuração esperada:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
    open-in-view: false
```

Não utilizar como estratégia compartilhada:

- `create`
- `create-drop`
- `update`

Flyway altera o schema.

Hibernate valida o schema.

---

## 11. Spring Boot

`MentoaiApiApplication.java` deve funcionar apenas como bootstrap da aplicação.

Formato esperado:

```java
@SpringBootApplication
public class MentoaiApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(MentoaiApiApplication.class, args);
    }
}
```

Não utilizar a classe principal para:

- criar entidades manualmente
- simular relacionamentos
- executar demonstrações
- imprimir dados de teste
- simular fluxos de negócio

Esses cenários devem ficar em testes.

---

## 12. Código compartilhado e erros

Código compartilhado deve permanecer pequeno e intencional.

Estrutura aceitável:

```text
shared/
├── config/
├── exception/
└── web/
```

Exemplos:

- `GlobalExceptionHandler`
- `ErrorResponse`
- `ResourceNotFoundException`
- `BusinessException`

Não transformar `shared` em depósito genérico.

Evitar sem necessidade:

- `Utils`
- `Helpers`
- `Managers`
- `BaseService`
- `BaseRepository`
- abstrações genéricas

Utilizar `@RestControllerAdvice` para tradução centralizada de exceptions para respostas HTTP.

Não espalhar `try/catch` genérico pelos Controllers.

Não retornar stack traces para consumidores da API.

---

## 13. IA e engenharia de contexto

Integrações com Azure OpenAI pertencem à Infrastructure.

O Domain não deve conhecer:

- Azure SDK
- API keys
- HTTP clients
- deployments/modelos específicos
- requests/responses específicos do provider

Fluxo conceitual do Copiloto:

```text
Pergunta
→ identificação de intenção
→ recuperação de contexto
→ construção do prompt
→ LLM
→ resposta
→ persistência
```

Para o MVP:

- preferir identificação de intenção simples e determinística
- permitir múltiplas intenções quando necessário
- controlar quantidade de contexto recuperado
- evitar histórico desnecessário
- evitar multiagentes
- não exigir RAG no MVP
- não exigir embeddings no MVP
- não exigir banco vetorial no MVP
- fundamentar respostas apenas no contexto recuperado

Não antecipar complexidade de IA sem necessidade concreta.

---

## 14. Simplicidade e escopo

Priorizar a solução mais simples que atenda corretamente ao requisito atual.

Não introduzir sem necessidade concreta:

- microservices
- CQRS
- event sourcing
- message brokers
- domain events complexos
- repositories genéricos
- services genéricos
- interfaces sem necessidade
- factories triviais
- mapeadores automáticos
- multiagentes
- banco vetorial

Patterns são ferramentas, não requisitos.

Antes de alterar código:

1. inspecionar a implementação existente
2. identificar o menor delta necessário
3. preservar código que já estiver correto
4. evitar refatorações não relacionadas

Não:

- atualizar dependências sem solicitação
- renomear classes ou pacotes apenas por preferência
- redesenhar módulos adjacentes sem necessidade
- corrigir problemas fora do escopo silenciosamente
- implementar arquitetura futura durante uma tarefa menor

Se encontrar um problema fora do escopo, reportar ao final em vez de expandir automaticamente a tarefa.

---

## 15. Fonte de verdade

Para regras de negócio e Domain, utilizar esta prioridade:

1. requisito explícito da tarefa atual
2. UML / modelo de domínio aprovado mais recente
3. backlogs e tasks atuais
4. código existente
5. documentação antiga

Para persistência:

1. migrations Flyway atuais
2. modelo lógico / DDL aprovado mais recente
3. documentação de Database
4. código de persistência existente

Se houver conflito relevante, não inventar uma reconciliação silenciosamente.

Preservar a fonte de maior prioridade e informar o conflito.

---

## 16. Estratégia de implementação

Preferir vertical slices em vez de implementar todas as camadas horizontalmente de uma vez.

Exemplo de primeiro fluxo:

```text
Domain
→ JpaEntity
→ PersistenceMapper
→ SpringDataRepository
→ RepositoryAdapter
→ ApplicationService
→ Request/Response DTO
→ Controller
→ Tests
```

Validar o primeiro padrão antes de replicá-lo em outros contextos.

Para a fase atual, priorizar:

```text
1. bootstrap/configuração
2. JPA + Oracle + Flyway
3. primeiro vertical slice funcional
4. clientes e usuários
5. reuniões e transcrições
6. pipeline de análise
7. alertas
8. memória contextual
9. copiloto
10. funcionalidades agregadas
```

Não antecipar etapas posteriores durante tarefas de infraestrutura básica.

---

## 17. Validação

Após alterações em código Java, executar os checks relevantes.

Windows:

```powershell
.\mvnw.cmd test
```

Unix/macOS:

```bash
./mvnw test
```

Quando apenas compilação for necessária:

Windows:

```powershell
.\mvnw.cmd compile
```

Unix/macOS:

```bash
./mvnw compile
```

Antes de finalizar uma tarefa:

- verificar compilação
- executar testes relevantes
- verificar imports não utilizados
- revisar os arquivos alterados
- verificar alterações acidentais fora do escopo
- informar os comandos de validação executados
- informar falhas ou limitações do ambiente

Nunca afirmar que testes passaram quando não foram executados com sucesso.

---

## 18. Relatório final do agente

Ao concluir uma tarefa relevante, responder de forma objetiva com:

### Arquivos alterados

Lista dos arquivos modificados.

### Arquivos criados

Lista dos arquivos novos.

### Principais mudanças

Resumo das alterações realizadas.

### Validação

Comandos executados e resultados.

### Pendências ou incompatibilidades

Problemas encontrados que ficaram fora do escopo.

Não alterar essas pendências silenciosamente.

---

## 19. Princípio final

O projeto deve priorizar:

- correção
- coesão
- baixo acoplamento
- simplicidade
- manutenção
- testabilidade
- viabilidade do MVP

Clean Architecture deve reduzir acoplamento.

DDD deve melhorar a representação do domínio.

Nenhum dos dois deve ser utilizado para justificar complexidade desnecessária.