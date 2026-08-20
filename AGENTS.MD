# MentoAI — Instruções para agentes

## Contexto

MentoAI é um Copiloto Comercial Consultivo desenvolvido para o Challenge TOTVS.

O sistema transforma transcrições de reuniões comerciais em inteligência comercial contextualizada, permitindo identificar riscos, oportunidades, sinais comerciais, insights e histórico de clientes.

A IA apoia decisões humanas. Ela não toma decisões automaticamente.

## Prioridades do projeto

1. Manter o MVP viável e simples.
2. Preservar alinhamento entre Domain, UML e modelo lógico.
3. Evitar abstrações e infraestrutura prematuras.
4. Implementar apenas o necessário para as funcionalidades planejadas.
5. Não adicionar complexidade sem necessidade concreta.

## Fonte de verdade

Quando houver divergência entre código antigo, documentação antiga, backlog e modelagem atual, utilizar esta prioridade:

1. Modelagem UML e modelo lógico atuais explicitamente fornecidos na tarefa.
2. Estrutura e convenções atuais do repositório.
3. Backlogs e tasks atuais.
4. Código legado.
5. Documentação antiga.

Não inventar campos, entidades, enums ou relacionamentos apenas para satisfazer uma task se eles não existirem na modelagem atual.

Se houver conflito relevante, preservar a modelagem atual e informar o conflito ao final da tarefa.

## Arquitetura

O projeto é organizado pelos seguintes contextos:

* `meeting`
* `analysis`
* `alert`
* `copilot`
* `user`

Cada contexto pode possuir:

```text
<context>/
└── domain/
    ├── entity/
    ├── enums/
    └── repository/
```

Preservar a estrutura atual do projeto sempre que possível.

## Domain

O Domain deve permanecer independente de frameworks.

Pode conter:

* entidades;
* enums;
* interfaces de repositório já necessárias ao domínio;
* métodos simples de domínio.

As entidades podem conter:

* atributos;
* construtores;
* getters;
* setters;
* `toString()`;
* comportamentos simples que pertençam claramente ao domínio.

## Proibido no Domain

Não adicionar:

* JPA;
* Hibernate;
* `@Entity`;
* `@Table`;
* `@Id`;
* `@OneToMany`;
* `@ManyToOne`;
* Spring Data;
* Spring Services;
* Controllers;
* DTOs;
* Configurations;
* migrations;
* implementações de repository;
* integração com banco;
* Azure OpenAI;
* chamadas HTTP;
* filas;
* processamento assíncrono;
* infraestrutura;
* novas dependências apenas para simplificar entidades.

Não adicionar Lombok ou outra dependência sem solicitação explícita.

## Regras de alteração

Ao receber uma tarefa:

1. Inspecione primeiro o código existente.
2. Identifique o delta entre código atual e modelo solicitado.
3. Altere somente o necessário.
4. Não reescreva classes que já estejam corretas.
5. Preserve nomes de pacotes quando estiverem adequados.
6. Remova ou ajuste conceitos antigos somente quando conflitarem com o modelo atual.
7. Não crie arquitetura futura antecipadamente.
8. Não altere arquivos fora do escopo solicitado.

Interfaces de repository existentes podem ter imports ou tipos ajustados quando mudanças no Domain exigirem isso.

Não criar uma interface de repository para cada entidade apenas porque ela existe no modelo lógico.

## Relacionamentos

Representar relacionamentos no Domain por referências entre objetos quando isso fizer parte do modelo.

Não adicionar coleções bidirecionais apenas para imitar relacionamentos de banco de dados.

O modelo lógico deve orientar qual entidade mantém a referência.

## Regras para modelagem

Tipos Java preferenciais:

* identificadores: `Long`
* textos: `String`
* datas e horários: `LocalDateTime`
* valores lógicos: `Boolean` ou `boolean`, conforme semântica existente
* categorias fechadas: `enum`

Não representar enums como `String`.

Não adicionar IDs estrangeiros paralelos quando já existir referência à entidade.

Exemplo incorreto:

```java
private Long clienteId;
private Cliente cliente;
```

Preferir no Domain:

```java
private Cliente cliente;
```

A representação de `cliente_id` pertence futuramente à persistência.

## Escopo atual

O projeto encontra-se em evolução da modelagem de domínio.

Até instrução contrária, alterações no Domain NÃO devem iniciar implementação de:

* API REST;
* persistência;
* autenticação JWT;
* Azure OpenAI;
* processamento assíncrono;
* RAG;
* embeddings;
* banco vetorial;
* infraestrutura.

Essas responsabilidades serão implementadas em etapas posteriores.
