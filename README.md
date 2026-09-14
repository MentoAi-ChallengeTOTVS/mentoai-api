# MentoAI — Copiloto Comercial com Inteligência Artificial

O MentoAI é um copiloto comercial consultivo que transforma transcrições de reuniões em inteligência estruturada. Ele associa reuniões ao histórico do cliente, identifica sinais comerciais e gera resumo executivo e insights.

A inteligência artificial apoia a análise e a tomada de decisão humana; o MentoAI não toma decisões comerciais autonomamente.

## Integrantes

| Integrante | RM |
|---|---:|
| Pedro Henrique dos Santos | RM564188 |
| Pedro Cunha Coutinho | RM562191 |
| Breno Henrique Bortoloti Santos | RM562856 |
| Thomaz Vasconcelos Mendes | RM564805 |
| Nicolas Tetsuo Kimura | RM565377 |

- Turma: **2ESPX**
- Equipe: **Equipe 1**
- **Challenge FIAP 2026 em parceria com a TOTVS**

## Estado atual da Sprint 3

### Entregue no backend

- autenticação por e-mail e senha, com JWT;
- gestão de usuários e clientes;
- consulta de reuniões e transcrições;
- upload de transcrição `.txt`, com criação da reunião, transcrição e análise;
- processamento assíncrono após o commit do upload;
- integração com a API Google Gemini;
- geração e persistência de resumo executivo, sentimento, insights e sinais comerciais;
- consulta de análises e da fila de processamento;
- geração automática e consulta de alertas, além da marcação como lido;
- consolidação assíncrona da memória contextual do cliente;
- envio de feedback por e-mail.

A fila é local, mantida pelo executor assíncrono da aplicação. Não é uma fila externa ou durável e não possui retomada automática após reinicialização.

### Funcionalidades em evolução

- **Copiloto contextual:** possui entidades, persistência e services, mas o `ChatController` ainda não expõe operações HTTP.
- **Dashboard executivo agregado:** a tela usa mocks.
- **Busca Global:** existe como componente visual, sem endpoint dedicado.
- **Visão 360° completa:** cadastro, reuniões e memória contextual existem, mas a experiência permanece parcial.
- **Alertas no frontend:** a consulta é real, porém o estado de leitura tem integração parcial devido à diferença entre o ID listado e o ID esperado pela operação de leitura.

## Como o projeto atende à entrega de Java

| Exigência da disciplina | Implementação no MentoAI |
|---|---|
| Model | Entidades de domínio e entidades JPA específicas da infraestrutura. |
| DAO | Domain Repository, `RepositoryAdapter` e Spring Data Repository. |
| Service | Application Services com casos de uso, regras e transações. |
| CRUD | Criação, consulta, listagem, atualização e alteração de status em Cliente e Usuário. |
| API REST | Controllers Spring MVC em `/api/v1/...`, com DTOs de entrada e saída. |
| Validações | Jakarta Bean Validation e regras nos Services. |
| Exceções | Exceções compartilhadas e `GlobalExceptionHandler`. |
| Banco | Oracle Database, JPA/Hibernate e Flyway. |
| Boas práticas | DDD, responsabilidades separadas, DTOs, mappers, injeção de dependências e transações. |

### DAO x Repository

A estrutura tradicional da disciplina é:

```text
Controller → Service → DAO → Banco
```

O MentoAI utiliza:

```text
Controller → Application Service → Domain Repository
→ Repository Adapter → Spring Data Repository → Oracle Database
```

Não existe uma classe `ClienteDAO`, mas a responsabilidade de acesso a dados não foi removida. O contrato `ClienteRepository` é implementado pelo `ClienteRepositoryAdapter`, que delega ao `SpringDataClienteRepository`. O adapter cumpre, neste contexto, a responsabilidade de persistência atribuída ao DAO tradicional.

Repository e DAO não são literalmente o mesmo padrão. O Repository mantém o domínio desacoplado da tecnologia de persistência e acompanha a arquitetura DDD adotada:

```text
ClienteController → ClienteService → ClienteRepository
→ ClienteRepositoryAdapter → SpringDataClienteRepository → Oracle Database
```

## CRUD e exclusão lógica

Cliente possui operações para criar, listar com filtros e paginação, buscar por ID, atualizar, alterar status e consultar reuniões vinculadas. Usuário possui criação, listagem paginada, consulta, atualização e alteração do campo `ativo`.

Não há endpoint de exclusão física para Cliente ou Usuário. Em determinadas entidades, a operação equivalente ao Delete do CRUD é realizada por alteração de status, preservando histórico e relacionamentos do banco.

## Arquitetura do backend

O backend é um monólito modular organizado por bounded contexts:

```text
presentation    → Controllers REST e contratos HTTP
application     → Services e casos de uso
domain          → entidades, enums e interfaces de Repository
infrastructure  → JPA, adapters, mappers e integrações externas
shared          → configurações, segurança e tratamento transversal
```

Contextos existentes:

- `meeting` — clientes, reuniões e transcrições;
- `analysis` — análises, insights e sinais comerciais;
- `alert` — alertas e leitura por usuário;
- `copilot` — chats e perguntas, ainda sem API funcional;
- `user` — autenticação, usuários e perfis;
- `feedback` — recebimento e encaminhamento de feedback.

`shared` é a área transversal, não um bounded context de negócio.

## Stack

| Área | Tecnologias confirmadas |
|---|---|
| Backend | Java 21, Spring Boot 4.0.6, Spring MVC, Spring Security/JWT, Bean Validation, Spring Data JPA, Hibernate e Flyway |
| Banco | Oracle Database |
| IA | Google Gemini API |
| Frontend | Next.js 16.3.2, React 19.2.8, TypeScript 5 e Tailwind CSS 4 |
| Infraestrutura | Docker, Docker Compose e Oracle Cloud |

## Estrutura do pacote de avaliação

A entrega deve ser extraída mantendo os três projetos como diretórios irmãos:

```text
MentoAI-Sprint3/
├── mentoai-api/
├── mentoai-frontend/
└── mentoai-infra/
```

Essa é a organização do pacote entregue, não uma estrutura interna do repositório da API. O `mentoai-infra/docker-compose.yaml` usa os caminhos relativos `../mentoai-api` e `../mentoai-frontend`; portanto, a estrutura deve ser preservada.

## Pré-requisitos para avaliar a entrega

- Docker Desktop instalado e em execução;
- conexão com a internet.

O Oracle já está hospedado na Oracle Cloud. A execução via Docker não exige Oracle, Java, Maven, Node.js ou npm instalados localmente.

## Configuração fornecida na entrega

O diretório `mentoai-infra` contém `.env`, wallet Oracle em `wallet/` e `docker-compose.yaml`, já configurados para a avaliação. O professor não precisa alterá-los. Este README não exibe credenciais, chaves ou segredos.

O Compose encaminha ao backend:

- `DB_URL`, `DB_USERNAME` e `DB_PASSWORD`;
- `GMAIL_USERNAME`, `GMAIL_PASSWORD` e `FEEDBACK_RECIPIENT_EMAIL`;
- `GEMINI_API_KEY` e `GROQ_API_KEY`;
- `CORS_ALLOWED_ORIGINS`.

`BACKEND_PORT` e `FRONTEND_PORT` controlam as portas externas, com padrões `8080` e `3000`. O build do frontend recebe `NEXT_PUBLIC_API_URL`, e seu container recebe `API_INTERNAL_URL`.

## Oracle Cloud e Wallet

O Oracle Database não roda em container nesta entrega. O backend conecta ao banco remoto usando as variáveis de banco. A wallet de `mentoai-infra/wallet` é montada em `/app/wallet` no container do backend, em modo somente leitura (`ro`), conforme o Compose.

Não é necessário criar outra wallet nem instalar Oracle local para avaliar a Sprint 3.

## Gemini / Google AI Studio

`GEMINI_API_KEY` é passada ao backend pelo Compose e já estará preenchida no `.env` da avaliação. A integração processa a transcrição e produz resumo executivo, sentimento, insights, sinais comerciais e evidências. Depois da análise, a mesma abstração de IA apoia a consolidação do contexto do cliente. Não é necessário criar uma chave nova.

## Como executar

1. Extraia o ZIP preservando a estrutura dos três projetos.
2. Abra o Docker Desktop.
3. Abra um terminal em `mentoai-infra`.
4. Execute:

```bash
docker compose up --build
```

5. Aguarde backend e frontend iniciarem.
6. Acesse:

- Frontend: <http://localhost:3000>
- Backend: <http://localhost:8080>

Para encerrar:

```bash
docker compose down
```

## O que o Docker Compose inicia

- frontend Next.js;
- backend Spring Boot.

O Oracle é remoto e não sobe no Compose.

## Fluxo recomendado para avaliação

1. Acesse o frontend.
2. Faça login com um usuário disponibilizado pela equipe no ambiente de avaliação.
3. Consulte ou cadastre um cliente.
4. Acesse **Nova Reunião**.
5. Selecione o cliente e informe data, horário e duração.
6. Envie uma transcrição `.txt` UTF-8, não vazia e com até 1 MiB.
7. Confirme o envio e abra a fila.
8. Acompanhe os estados `PENDENTE` e `PROCESSANDO`.
9. Ao chegar a `PROCESSADA`, abra o detalhe da reunião.
10. Consulte resumo executivo, insights, sinais comerciais e evidências.

Nesta Sprint, o formulário envia temporariamente `usuarioId = 1`; esse usuário deve existir no banco do pacote. Credenciais não são publicadas no README e devem ser fornecidas pela equipe em meio seguro.

## Endpoints principais

Exceto a autenticação, as rotas exigem JWT. A gestão de usuários exige perfil `DIRETOR_COMERCIAL`.

| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/v1/auth/login` | Autentica e retorna JWT. |
| `POST` / `GET` | `/api/v1/usuarios` | Cria ou lista usuários. |
| `GET` / `PUT` | `/api/v1/usuarios/{id}` | Consulta ou atualiza um usuário. |
| `PATCH` | `/api/v1/usuarios/{id}/status` | Altera o status do usuário. |
| `POST` / `GET` | `/api/v1/clientes` | Cria ou lista clientes. |
| `GET` / `PUT` | `/api/v1/clientes/{id}` | Consulta ou atualiza um cliente. |
| `PATCH` | `/api/v1/clientes/{id}/status` | Altera o status do cliente. |
| `GET` | `/api/v1/clientes/{id}/reunioes` | Lista reuniões do cliente. |
| `GET` | `/api/v1/reunioes` | Lista reuniões. |
| `GET` | `/api/v1/reunioes/{id}` | Consulta uma reunião. |
| `GET` | `/api/v1/reunioes/{id}/transcricao` | Consulta sua transcrição. |
| `GET` | `/api/v1/transcricoes` | Lista transcrições. |
| `GET` | `/api/v1/transcricoes/{id}` | Consulta uma transcrição. |
| `POST` | `/api/v1/transcricoes/upload` | Envia arquivo e inicia a análise. |
| `GET` | `/api/v1/analises/{id}` | Consulta uma análise. |
| `GET` | `/api/v1/analises/reuniao/{reuniaoId}` | Consulta por reunião. |
| `GET` | `/api/v1/analises/fila` | Consulta fila e finalizados. |
| `GET` | `/api/v1/alertas` | Lista alertas. |
| `GET` | `/api/v1/alertas/{id}` | Consulta um alerta. |
| `PATCH` | `/api/v1/alertas/{id}/lido` | Marca o vínculo de alerta como lido. |

Não existem endpoints funcionais de Dashboard, Busca Global ou Copiloto nesta Sprint.

## Integração com o frontend e mocks

O frontend concentra o acesso a dados em `src/services`, permitindo substituir mocks por endpoints sem reestruturar toda a UI. Login, clientes, usuários, reuniões, upload, fila, detalhe da análise e consulta de alertas usam a API real.

Dashboard e Copiloto ainda usam mocks. A Visão 360° permanece parcial, e a leitura de alertas possui a limitação descrita anteriormente. Portanto, o frontend ainda não está 100% integrado.

## Principal fluxo funcional desta Sprint

```text
Upload
→ criação de reunião, transcrição e análise PENDENTE
→ commit e evento assíncrono
→ status PROCESSANDO
→ Gemini e validação da resposta
→ persistência de resumo, sentimento, insights e sinais
→ geração de alertas elegíveis
→ status PROCESSADA
→ consolidação da memória contextual
→ consulta do resultado
```

Uma falha principal pode marcar a análise como `ERRO`. Falha posterior no contexto não desfaz a análise concluída.

## Testes

Há testes unitários e de integração com JUnit, Mockito e Spring. A suíte completa inicializa o contexto e depende de `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` e Oracle acessível, com Flyway habilitado.

O teste Oracle de resumos recentes utiliza `ORACLE_TEST_URL`, `ORACLE_TEST_USERNAME` e `ORACLE_TEST_PASSWORD`; sem `ORACLE_TEST_URL`, ele é ignorado. Assim, não há execução obrigatória da suíte pelo professor sem configuração adicional. No pacote Docker, o foco é o fluxo funcional.

## Observações finais da Sprint 3

Este README descreve o estado da Sprint 3. Algumas experiências agregadas e o Copiloto permanecem em evolução. O foco é demonstrar arquitetura Java, persistência, API REST e o fluxo principal de análise.