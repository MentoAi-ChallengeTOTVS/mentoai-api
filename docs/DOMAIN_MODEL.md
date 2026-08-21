# Modelo de Domínio

## 1. Objetivo

Este documento registra a estrutura técnica atual do Domain MentoAI.

Ele define:

- bounded context de cada entidade;
- atributos;
- enums;
- referências entre entidades;
- cardinalidades estruturais.

Regras específicas de uma feature devem ser definidas na própria tarefa quando não estiverem representadas estruturalmente neste modelo.

---

## 2. Visão geral

```text
meeting
├── Cliente
├── Reuniao
└── Transcricao

user
└── Usuario

analysis
├── AnaliseIA
├── Insight
└── SinalComercial

alert
├── Alerta
└── AlertaUsuario

copilot
├── Chat
└── PerguntaChat
```

---

## 3. Contexto `meeting`

### Cliente

Representa o cliente comercial utilizado como referência para o histórico de reuniões e inteligência contextual.

```text
Cliente
- id: Long
- nome: String
- segmento: String
- porte: String
- criacao: LocalDateTime
- status: Boolean
```

Referências estruturais:

```text
Cliente 1 ─── N Reuniao
```

A referência deve existir prioritariamente no lado dependente:

```java
Reuniao.cliente
```

Não é necessário manter `List<Reuniao>` em `Cliente` apenas para representar a cardinalidade.

`status` representa o estado ativo/inativo do cliente.

---

### Reuniao

Representa uma reunião comercial vinculada a um cliente e a um usuário responsável.

```text
Reuniao
- id: Long
- dataReuniao: LocalDateTime
- duracaoMinutos: Integer
- cliente: Cliente
- usuario: Usuario
- criacao: LocalDateTime
```

Referências:

```text
Cliente 1 ─── N Reuniao
Usuario 1 ─── N Reuniao
Reuniao 1 ─── 0..1 Transcricao
Reuniao 1 ─── 0..1 AnaliseIA
```

O estado do processamento de IA não pertence a `Reuniao`.

Ele pertence a `AnaliseIA`.

---

### Transcricao

Representa o conteúdo textual e os metadados do arquivo associado a uma reunião.

```text
Transcricao
- id: Long
- conteudo: String
- nomeArquivo: String
- formatoArquivo: String
- idioma: String
- reuniao: Reuniao
- criacao: LocalDateTime
```

Relacionamento:

```text
Reuniao 1 ─── 0..1 Transcricao
```

A referência é mantida em:

```java
Transcricao.reuniao
```

---

## 4. Contexto `user`

### Usuario

Representa um usuário da plataforma e seu perfil de acesso.

```text
Usuario
- id: Long
- nome: String
- email: String
- senha: String
- perfil: PerfilUsuario
- ativo: Boolean
- criacao: LocalDateTime
- atualizacao: LocalDateTime
```

Referências estruturais:

```text
Usuario 1 ─── N Reuniao
Usuario 1 ─── N Chat
Usuario 1 ─── N AlertaUsuario
```

Perfis são representados por `PerfilUsuario`.

Não utilizar subclasses como `DiretorComercial` ou `ExecutivoComercial` para representar perfil de acesso.

---

## 5. Contexto `analysis`

### AnaliseIA

Representa o processamento de IA relacionado a uma reunião.

```text
AnaliseIA
- id: Long
- reuniao: Reuniao
- resumoExecutivo: String
- sentimentoGeral: SentimentoGeral
- statusProcessamento: StatusProcessamento
- criacao: LocalDateTime
- iniciadoEm: LocalDateTime
- finalizadoEm: LocalDateTime
- mensagemErro: String
```

Campos que podem estar ausentes durante o processamento:

```text
resumoExecutivo
sentimentoGeral
iniciadoEm
finalizadoEm
mensagemErro
```

Relacionamentos:

```text
Reuniao 1 ─── 0..1 AnaliseIA
AnaliseIA 1 ─── N Insight
AnaliseIA 1 ─── N SinalComercial
```

O estado inicial esperado da análise é:

```text
PENDENTE
```

---

### Insight

Representa uma interpretação estratégica gerada a partir da análise.

```text
Insight
- id: Long
- analise: AnaliseIA
- tipo: TipoInsight
- descricao: String
- severidade: Severidade
- criacao: LocalDateTime
```

Relacionamento:

```text
AnaliseIA 1 ─── N Insight
```

`Insight` é diferente de `SinalComercial`.

Estruturalmente:

- `SinalComercial` registra um sinal objetivo identificado;
- `Insight` registra uma interpretação estratégica.

---

### SinalComercial

Representa um sinal comercial estruturado identificado durante a análise.

```text
SinalComercial
- id: Long
- analise: AnaliseIA
- tipo: TipoSinalComercial
- descricao: String
- evidencia: String
- relevancia: RelevanciaSinal
- criacao: LocalDateTime
```

Relacionamentos:

```text
AnaliseIA 1 ─── N SinalComercial
SinalComercial 1 ─── 0..1 Alerta
```

A evidência textual faz parte do próprio sinal.

---

## 6. Contexto `alert`

### Alerta

Representa um alerta originado por um sinal comercial.

```text
Alerta
- id: Long
- sinalComercial: SinalComercial
- prioridade: PrioridadeAlerta
- motivo: String
- criacao: LocalDateTime
```

Relacionamento:

```text
SinalComercial 1 ─── 0..1 Alerta
Alerta 1 ─── N AlertaUsuario
```

`Alerta` referencia `SinalComercial` diretamente.

Não modelar o alerta como dependente direto de `Cliente` ou `AnaliseIA` enquanto o modelo estrutural atual permanecer este.

---

### AlertaUsuario

Representa a associação de um alerta a um usuário e seu estado individual de leitura.

```text
AlertaUsuario
- id: Long
- alerta: Alerta
- usuario: Usuario
- lido: boolean
- lidoEm: LocalDateTime
```

Relacionamentos:

```text
Alerta 1 ─── N AlertaUsuario
Usuario 1 ─── N AlertaUsuario
```

`lidoEm` pode permanecer ausente enquanto o alerta não tiver sido lido.

Estado inicial esperado:

```text
lido = false
```

---

## 7. Contexto `copilot`

### Chat

Representa uma sessão de interação entre usuário e copiloto.

```text
Chat
- id: Long
- titulo: String
- usuario: Usuario
- criacao: LocalDateTime
```

Relacionamentos:

```text
Usuario 1 ─── N Chat
Chat 1 ─── N PerguntaChat
```

O contexto comercial utilizado pelo copiloto não precisa ser persistido diretamente dentro de `Chat`.

Ele pode ser recuperado dinamicamente durante o processamento das perguntas.

---

### PerguntaChat

Representa uma interação dentro de um chat.

```text
PerguntaChat
- id: Long
- chat: Chat
- pergunta: String
- resposta: String
- criacao: LocalDateTime
```

Relacionamento:

```text
Chat 1 ─── N PerguntaChat
```

`resposta` pode estar ausente durante a geração da resposta pelo copiloto.

---

## 8. Enumerações

### PerfilUsuario

```text
EXECUTIVO_COMERCIAL
DIRETOR_COMERCIAL
```

### StatusProcessamento

```text
PENDENTE
PROCESSANDO
PROCESSADA
ERRO
```

### PrioridadeAlerta

```text
BAIXA
MEDIA
ALTA
```

### TipoSinalComercial

```text
NECESSIDADE
DOR
OBJECAO
ORCAMENTO
PRAZO
MOMENTO_CLIENTE
CONCORRENCIA
OPORTUNIDADE
RISCO_CHURN
```

### RelevanciaSinal

```text
BAIXA
MEDIA
ALTA
```

### TipoInsight

```text
RISCO
OPORTUNIDADE
TENDENCIA
ESTRATEGICO
```

### SentimentoGeral

```text
POSITIVO
NEUTRO
NEGATIVO
```

### Severidade

```text
BAIXA
MEDIA
ALTA
```

---

## 9. Relacionamentos consolidados

```text
Cliente       1 ─── N    Reuniao
Usuario       1 ─── N    Reuniao

Reuniao       1 ─── 0..1 Transcricao
Reuniao       1 ─── 0..1 AnaliseIA

AnaliseIA     1 ─── N    Insight
AnaliseIA     1 ─── N    SinalComercial

SinalComercial 1 ── 0..1 Alerta

Alerta        1 ─── N    AlertaUsuario
Usuario       1 ─── N    AlertaUsuario

Usuario       1 ─── N    Chat
Chat          1 ─── N    PerguntaChat
```

---

## 10. Diretrizes de implementação do modelo

- Representar relacionamentos no Domain através de referências entre objetos.
- Não duplicar referência e ID da FK dentro da mesma entidade de Domain.
- Não criar coleções bidirecionais apenas para espelhar o banco.
- Não utilizar annotations JPA no Domain.
- Não representar enums de negócio como `String`.
- Não adicionar atributos estruturais sem alteração explícita do modelo aprovado.
