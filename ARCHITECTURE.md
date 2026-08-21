# Arquitetura do MentoAI

## 1. Objetivo

Este documento descreve a arquitetura técnica alvo do backend MentoAI.

O objetivo é manter o sistema simples o suficiente para um MVP acadêmico, mas com separação clara entre:

- domínio;
- casos de uso;
- persistência;
- API;
- integrações externas;
- inteligência artificial.

A arquitetura não deve ser utilizada para justificar abstrações sem necessidade concreta.

---

## 2. Estilo arquitetural

O backend é um **monólito modular**.

Existe uma única aplicação Spring Boot e um único processo de deploy, mas o código é dividido internamente por bounded contexts.

```text
MentoAI API
│
├── meeting
├── analysis
├── alert
├── copilot
└── user
```

Não utilizar microservices no MVP.

A separação entre os módulos é lógica e arquitetural, não operacional.

---

## 3. Bounded Contexts

### `meeting`

Responsável por:

- clientes;
- reuniões;
- transcrições;
- associação entre reunião, cliente e usuário responsável.

Entidades principais:

- `Cliente`
- `Reuniao`
- `Transcricao`

### `analysis`

Responsável por representar os resultados estruturados do processamento de reuniões.

Entidades principais:

- `AnaliseIA`
- `Insight`
- `SinalComercial`

### `alert`

Responsável por alertas derivados de sinais comerciais e pelo controle de leitura por usuário.

Entidades principais:

- `Alerta`
- `AlertaUsuario`

### `copilot`

Responsável pelas sessões de conversa e pelo histórico de perguntas e respostas do copiloto.

Entidades principais:

- `Chat`
- `PerguntaChat`

### `user`

Responsável pelos usuários da plataforma e seus perfis de acesso.

Entidade principal:

- `Usuario`

---

## 4. Organização interna dos módulos

Estrutura preferencial:

```text
<context>/
├── domain/
│   ├── entity/
│   ├── enums/
│   └── repository/
│
├── application/
│   └── service/
│
├── infrastructure/
│   ├── persistence/
│   │   ├── entity/
│   │   ├── repository/
│   │   ├── adapter/
│   │   └── mapper/
│   └── <integrações externas quando necessárias>/
│
└── presentation/
    └── rest/
        ├── controller/
        ├── request/
        ├── response/
        └── mapper/
```

Não criar pacotes vazios antecipadamente.

A estrutura deve crescer conforme a implementação real.

---

## 5. Direção das dependências

A dependência conceitual entre camadas deve seguir:

```text
Presentation
     ↓
Application
     ↓
Domain
```

`Infrastructure` fornece implementações concretas para contratos necessários pelas camadas internas.

Exemplo:

```text
ClienteController
      ↓
ClienteService
      ↓
ClienteRepository
      ↑
ClienteRepositoryAdapter
      ↓
SpringDataClienteRepository
      ↓
Hibernate / JPA
      ↓
Oracle
```

### Regras

- Domain não depende de Spring.
- Domain não depende de JPA/Hibernate.
- Application não depende de Spring Data repositories diretamente.
- Presentation não acessa banco diretamente.
- Controllers não contêm regras de negócio.
- Infrastructure pode depender de tecnologias externas.
- Integrações externas devem ficar fora do Domain.

---

## 6. Domain

O Domain contém a representação dos conceitos de negócio.

Elementos permitidos:

- entidades;
- enums;
- contratos de repository;
- comportamentos independentes de framework.

As entidades do Domain são POJOs.

Exemplo:

```text
meeting/domain/entity/Cliente.java
```

O modelo estrutural completo está em:

```text
docs/DOMAIN_MODEL.md
```

---

## 7. Application

Application representa casos de uso e orquestração.

Não existe obrigação de criar uma classe por caso de uso trivial.

Um Application Service pode concentrar operações coesas.

Exemplo:

```text
ClienteService
├── criar
├── buscarPorId
├── listar
├── atualizar
└── alterarStatus
```

Fluxos com responsabilidades próprias podem possuir serviços dedicados:

```text
ProcessarAnaliseService
ConsolidarContextoClienteService
PerguntarCopilotoService
```

Transações devem ser controladas preferencialmente nessa camada.

---

## 8. Infrastructure

Infrastructure contém detalhes técnicos.

Exemplos:

- JPA;
- Hibernate;
- Spring Data;
- Oracle;
- Flyway;
- Azure OpenAI;
- clientes HTTP;
- adapters;
- mapeamento de persistência.

### Separação Domain/JPA

O projeto utiliza entidades separadas:

```text
domain/entity/Cliente.java

infrastructure/persistence/entity/ClienteJpaEntity.java
```

A JPA Entity representa a estrutura persistida.

A Domain Entity representa o conceito de domínio.

A conversão deve ser feita por mapper de persistência.

---

## 9. Presentation

A camada Presentation expõe a API REST.

Fluxo esperado:

```text
HTTP Request
→ Controller
→ validação
→ Application Service
→ Response DTO
→ HTTP Response
```

Não expor entidades de Domain ou JPA diretamente como contrato HTTP.

DTOs simples devem preferir Java `record`.

Jakarta Bean Validation pode ser utilizado nos DTOs de entrada.

---

## 10. Persistência

Fluxo de persistência:

```text
Application
    ↓
Domain Repository
    ↑
Repository Adapter
    ↓
Spring Data Repository
    ↓
JPA / Hibernate
    ↓
Oracle
```

### Repositories

O Domain define contratos.

A Infrastructure implementa esses contratos.

Exemplo:

```java
public interface ClienteRepository {
    Cliente salvar(Cliente cliente);
    Optional<Cliente> buscarPorId(Long id);
}
```

Infrastructure:

```java
public interface SpringDataClienteRepository
        extends JpaRepository<ClienteJpaEntity, Long> {
}
```

Adapter:

```java
@Repository
public class ClienteRepositoryAdapter
        implements ClienteRepository {
}
```

---

## 11. Relacionamentos JPA

Evitar coleções bidirecionais sem necessidade concreta.

Preferir mapear a relação na entidade correspondente à tabela que contém a FK.

Exemplo:

```text
REUNIAO.CLIENTE_ID
```

é representado em `ReuniaoJpaEntity` como uma referência para `ClienteJpaEntity`.

Não é obrigatório criar uma coleção de reuniões dentro de `ClienteJpaEntity`.

Relacionamentos devem utilizar `FetchType.LAZY` quando adequado.

Enums persistidos devem utilizar `EnumType.STRING`.

---

## 12. Banco e migrations

Oracle é o banco relacional alvo.

Responsabilidades:

```text
Flyway
→ cria e evolui schema

Hibernate
→ mapeia e valida schema
```

Configuração esperada:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: validate
    open-in-view: false
```

Detalhes do modelo físico estão em:

```text
docs/DATABASE.md
```

---

## 13. Arquitetura de IA

Azure OpenAI é uma integração externa e pertence à Infrastructure.

O Domain não deve conhecer:

- SDK da Azure;
- deployment de modelo;
- API key;
- detalhes HTTP;
- modelos específicos do provider.

Existem dois fluxos principais de IA:

```text
Transcrição
→ processamento
→ resultado estruturado
→ memória comercial
```

e:

```text
Pergunta
→ identificação de intenção
→ recuperação de contexto
→ construção do prompt
→ LLM
→ resposta
```

A especificação técnica detalhada está em:

```text
docs/AI_ARCHITECTURE.md
```

---

## 14. Código compartilhado

Código transversal deve ser reduzido.

Estrutura aceitável:

```text
shared/
├── config/
├── exception/
└── web/
```

Evitar transformar `shared` em um depósito genérico.

Não criar abstrações como `BaseService`, `BaseRepository`, `Utils` ou `Manager` sem necessidade concreta.

---

## 15. Estratégia de evolução

Preferir vertical slices.

O primeiro slice deve estabelecer o padrão arquitetural das próximas implementações.

Exemplo:

```text
Cliente
│
├── Domain
├── JpaEntity
├── PersistenceMapper
├── SpringDataRepository
├── RepositoryAdapter
├── ApplicationService
├── DTOs
├── Controller
└── Tests
```

Após validar o padrão, replicá-lo somente onde fizer sentido.

---

## 16. Princípios

A arquitetura deve otimizar:

- coesão;
- baixo acoplamento;
- testabilidade;
- manutenção;
- simplicidade;
- viabilidade do MVP.

Não introduzir prematuramente:

- microservices;
- CQRS;
- event sourcing;
- message brokers;
- abstrações genéricas;
- multiagentes;
- banco vetorial.

Este documento descreve a arquitetura alvo. Caso o código atual ainda esteja em transição, novas alterações devem aproximar o projeto deste modelo sem refatorações fora de escopo.
