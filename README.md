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

## Upload e consulta da análise

`POST /api/v1/transcricoes/upload` mantém o envio multipart e retorna **HTTP 202 Accepted** com os IDs criados e o estado inicial:

```json
{
  "reuniaoId": 1,
  "transcricaoId": 1,
  "analiseId": 1,
  "status": "PENDENTE"
}
```

`UploadAnaliseService` abre a transação; `UploadTranscricaoService` participa por propagação `REQUIRED`. Reunião, transcrição e análise são commitadas na conclusão da chamada ao orquestrador. O evento `AnaliseSolicitadaEvent`, contendo apenas `analiseId`, é publicado nessa transação e entregue ao listener somente em `AFTER_COMMIT`. Um rollback não dispara o processamento.

O listener usa `@Async("analysisExecutor")` e delega ao processamento existente, sem aguardar o Gemini na thread HTTP:

```text
Upload → PENDENTE + evento → COMMIT → submissão async → HTTP 202
Background → PROCESSANDO → Gemini → PROCESSADA ou ERRO
```

O início e a conclusão/falha mantêm suas transações próprias; a geração da IA ocorre sem transação aberta. Resumo, sentimento, insights e sinais são persistidos atomicamente na finalização. Uma falha nessa etapa não desfaz o upload.

As consultas somente leitura são:

- `GET /api/v1/analises/{id}`;
- `GET /api/v1/analises/reuniao/{reuniaoId}`.

Retornam `200`, ou `404` quando a análise não existe. A resposta preserva os campos da análise, incluindo `statusProcessamento`, resumo, sentimento, datas e mensagem de erro. Em `PROCESSADA`, inclui `insights` e `sinaisComerciais` persistidos; nos demais estados, essas coleções são arrays vazios, nunca `null`.

O frontend pode consultar enquanto o estado for `PENDENTE` ou `PROCESSANDO` e parar em `PROCESSADA` ou `ERRO`. O `PENDENTE` do POST é o estado inicial criado: o background pode começar antes de o cliente receber a resposta, e o primeiro GET já pode observar um estado mais avançado, inclusive final. Não há polling no servidor, endpoint público para alterar status ou retomada automática.

### Executor e limitações do MVP

O pool `analysisExecutor` é fixo (`corePoolSize = maxPoolSize`) e usa threads `analysis-*`. As propriedades de `application.yml` são:

- `mentoai.analysis.async.pool-size`: padrão conservador de `2`, substituível por `ANALYSIS_ASYNC_POOL_SIZE`;
- `mentoai.analysis.async.queue-capacity`: padrão conservador de `50`, substituível por `ANALYSIS_ASYNC_QUEUE_CAPACITY`.

Ambos os valores devem ser positivos. Em Docker, variáveis personalizadas precisam ser passadas ao ambiente do container; apenas declará-las no `.env` do Compose não as encaminha automaticamente. Alterações Java ou no YAML empacotado exigem reconstruir a imagem.

Ao saturar, o executor rejeita a submissão com `AbortPolicy`, sem executar a tarefa na thread HTTP. A rejeição após o commit é registrada pelos logs do Spring; o POST mantém `202` e a análise rejeitada fica `PENDENTE`, sem nova tentativa automática. Exceções não tratadas no background também são registradas pelo Spring; as falhas tratadas pelo processador mantêm a mensagem segura no banco.

A fila é local e volátil. Quedas, reinícios ou falhas ao registrar `ERRO` podem deixar análises em `PENDENTE`/`PROCESSANDO` indefinidamente. Não há garantia de retomada ou execução durável, fila externa, scheduler ou retry. Logs de exceção completos devem ser usados com dados fictícios no diagnóstico local, pois podem conter informações sensíveis.

## Fonte operacional para agentes

As regras de trabalho, escopo, validação e convenções para agentes de código ficam em `AGENTS.md`.

Este README apresenta o projeto. Ele não substitui `AGENTS.md` nem a documentação técnica específica.
