# 🚀 MentoAI - Copiloto Comercial Consultivo com IA

## Sobre o Projeto

O **MentoAI** é uma solução acadêmica desenvolvida para o Challenge TOTVS 2026 com o objetivo de transformar transcrições de reuniões comerciais em inteligência estratégica para apoio à tomada de decisão.

A proposta do projeto não é substituir vendedores, gestores ou sistemas CRM. O foco é atuar como um **copiloto comercial consultivo**, auxiliando líderes comerciais na identificação de riscos, oportunidades e informações relevantes extraídas de conversas realizadas com clientes.

O sistema foi modelado utilizando conceitos de **Domain-Driven Design (DDD)**, organizando o domínio em contextos independentes que representam áreas específicas do negócio.

O MVP foi projetado para demonstrar:

* Organização de domínio utilizando DDD;
* Modelagem orientada a objetos;
* Herança e polimorfismo;
* Relacionamentos entre entidades;
* Simulação de processamento de reuniões comerciais;
* Geração de análises, insights e alertas;
* Interação por linha de comando utilizando Scanner;
* Aplicação prática dos conceitos estudados na disciplina.

---

# Objetivo do Projeto

Empresas realizam diariamente diversas reuniões comerciais que geram informações valiosas sobre clientes, oportunidades de negócio e riscos de relacionamento.

Entretanto, essas informações normalmente ficam dispersas em anotações, e-mails ou transcrições, dificultando sua utilização estratégica.

O MentoAI busca centralizar esse conhecimento, transformando informações não estruturadas em análises consultivas capazes de apoiar gestores comerciais na tomada de decisão.

---

# Tecnologias Utilizadas

* Java 21
* Programação Orientada a Objetos
* Domain-Driven Design (DDD)
* Maven
* Scanner (entrada de dados)
* Estruturas de dados com Collections

---

# Arquitetura Baseada em DDD

O sistema foi organizado em Bounded Contexts que representam diferentes áreas do domínio comercial.

## User Context

Responsável pelos usuários do sistema.

### Entidades

* Usuario
* DiretorComercial
* ExecutivoComercial

### Conceitos Demonstrados

* Herança
* Polimorfismo
* Encapsulamento

---

## Meeting Context

Responsável pelas informações das reuniões comerciais.

### Entidades

* Cliente
* Reuniao
* Transcricao

### Responsabilidades

* Cadastro de clientes
* Registro de reuniões
* Armazenamento de transcrições
* Histórico de interações comerciais

---

## Analysis Context

Responsável pela interpretação das reuniões.

### Entidades

* AnaliseIA
* Insight

### Responsabilidades

* Geração de resumos executivos
* Identificação de riscos
* Identificação de oportunidades
* Classificação de insights

---

## Alert Context

Responsável pela representação de situações relevantes identificadas durante a análise.

### Entidade

* Alerta

### Responsabilidades

* Indicação de riscos comerciais
* Priorização de atenção do gestor
* Apoio à tomada de decisão

---

## Copilot Context

Representa a interação do usuário com o copiloto comercial.

### Entidades

* Chat
* PerguntaChat

### Responsabilidades

* Simular consultas ao copiloto
* Registrar perguntas
* Registrar respostas contextualizadas

---

# Funcionalidades Implementadas

## Cadastro de Usuários

O sistema permite cadastrar diferentes perfis de usuários:

* Diretor Comercial
* Executivo Comercial

Durante a visualização dos usuários cadastrados é demonstrado o uso de herança e polimorfismo através das subclasses de Usuario.

---

## Registro de Reuniões

O usuário pode registrar uma reunião comercial informando:

* Cliente
* Resumo da reunião

A partir dessas informações são criados:

* Cliente
* Reuniao
* Transcricao
* AnaliseIA

---

## Geração de Insights

O sistema realiza uma análise simplificada do conteúdo informado.

Exemplos:

### Risco Comercial

Caso o texto contenha termos como:

* cancelamento
* cancelar
* suporte

O sistema gera:

* Insight do tipo CHURN
* Alerta de prioridade ALTA

### Oportunidade Comercial

Caso o texto contenha termos como:

* interesse
* expansão
* novo módulo

O sistema gera:

* Insight do tipo OPORTUNIDADE

---

## Histórico de Reuniões

O sistema mantém as reuniões registradas durante a execução.

É possível visualizar:

* Dados da reunião
* Análise gerada
* Insights identificados

Essa funcionalidade demonstra o relacionamento entre os agregados do domínio.

---

## Simulação do Copiloto Comercial

Após o processamento da reunião, o sistema cria automaticamente uma interação com o Copiloto Comercial.

O objetivo é representar como o sistema poderá futuramente responder perguntas utilizando contexto comercial consolidado.

---

# Demonstração dos Conceitos da Disciplina

O projeto foi estruturado para evidenciar os principais conceitos solicitados.

## Programação Orientada a Objetos

* Classes e objetos
* Encapsulamento
* Herança
* Polimorfismo
* Associações entre entidades

## Domain-Driven Design

* Separação por Contextos
* Modelagem do domínio
* Entidades de negócio
* Linguagem Ubíqua

## Entrada e Saída de Dados

Utilização da classe Scanner para interação com o usuário através de menus em linha de comando.

## Collections

Utilização de:

* List<Usuario>
* List<Cliente>
* List<AnaliseIA>

para armazenamento e manipulação de dados durante a execução.

---

# Fluxo de Execução

```text
Cadastrar Usuário
        ↓
Registrar Reunião
        ↓
Criar Transcrição
        ↓
Gerar Análise
        ↓
Identificar Insights
        ↓
Gerar Alertas
        ↓
Registrar Histórico
        ↓
Consultar Copiloto
```

---

# Menu da Aplicação

```text
=== MENTO AI ===

1 - Cadastrar Usuário
2 - Visualizar Usuários
3 - Simular Reunião
4 - Visualizar Reuniões
0 - Sair
```

---

# Conclusão

O MentoAI demonstra a aplicação prática de Domain-Driven Design e Programação Orientada a Objetos em um cenário inspirado em problemas reais da área comercial.

A solução modela um copiloto comercial capaz de transformar informações de reuniões em conhecimento estruturado, servindo como base para futuras evoluções envolvendo inteligência artificial, análise contextual e integração com plataformas corporativas.
