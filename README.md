# MentoAI API

Backend do **MentoAI — Copiloto Comercial Consultivo**, desenvolvido para o Challenge TOTVS.

O sistema transforma transcrições de reuniões comerciais em inteligência contextualizada para apoiar líderes e executivos comerciais na identificação de riscos, oportunidades, histórico e contexto de clientes.

A IA atua como mecanismo de apoio. O sistema não toma decisões comerciais de forma autônoma.

## Stack técnica

Arquitetura alvo do backend:

- Java 21
- Spring Boot
- Spring MVC
- Jakarta Bean Validation
- Spring Data JPA
- Hibernate
- Oracle Database
- Flyway
- Azure OpenAI

## Arquitetura

O backend é estruturado como um **monólito modular**, organizado pelos bounded contexts:

- `meeting`
- `analysis`
- `alert`
- `copilot`
- `user`

Cada contexto segue, quando necessário, a separação:

```text
domain
application
infrastructure
presentation
```

A abordagem combina DDD e Clean Architecture de forma pragmática, priorizando baixo acoplamento e simplicidade compatível com o MVP.

Veja [ARCHITECTURE.md](docs/ARCHITECTURE.md).

## Documentação técnica

- [Arquitetura](docs/ARCHITECTURE.md)
- [Modelo de Domínio](docs/DOMAIN_MODEL.md)
- [Banco de Dados](docs/DATABASE.md)
- [Arquitetura de IA](docs/AI_ARCHITECTURE.md)
- [Decisões Técnicas](docs/TECHNICAL_DECISIONS.md)

## Princípio de implementação

O projeto deve evoluir preferencialmente por **vertical slices**, implementando um fluxo completo antes de replicar o padrão para os demais contextos.

Exemplo:

```text
Domain
→ JpaEntity
→ PersistenceMapper
→ SpringDataRepository
→ RepositoryAdapter
→ ApplicationService
→ DTOs
→ Controller
→ Tests
```

## Fonte operacional para agentes

As regras de trabalho, escopo, validação e convenções para agentes de código ficam em `AGENTS.md`.

Este README apresenta o projeto. Ele não substitui `AGENTS.md` nem a documentação técnica específica.
