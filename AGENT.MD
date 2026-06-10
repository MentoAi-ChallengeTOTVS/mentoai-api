# AGENTS.md

## Visão Geral

Este projeto é um Copiloto Comercial Consultivo baseado em IA desenvolvido para o Challenge TOTVS.

O objetivo do sistema é transformar transcrições de reuniões comerciais em inteligência estratégica para apoiar líderes comerciais na identificação de riscos, oportunidades e evolução do relacionamento com clientes.

A IA não toma decisões automaticamente.

Seu papel é apoiar análises humanas através da geração de resumos, insights, alertas e consultas contextualizadas.

---

# Objetivos do MVP

O MVP deve permitir:

* Cadastro de clientes
* Registro de reuniões
* Armazenamento de transcrições
* Geração de análises
* Geração de insights
* Geração de alertas
* Histórico comercial consolidado
* Chat consultivo contextualizado

---

# Fora do Escopo

Não implementar:

* Multiagentes
* Treinamento de modelos
* Fine tuning
* CRM completo
* Tomada automática de decisão
* Sistemas autônomos
* Workflows complexos
* Integrações externas reais

---

# Arquitetura

O projeto utiliza Domain Driven Design (DDD).

Cada Bounded Context possui sua própria estrutura.

Estrutura padrão:

meeting/
├── domain/
│   ├── entity/
│   ├── enums/
│   └── repository/
└── service/

analysis/
├── domain/
│   ├── entity/
│   ├── enums/
│   └── repository/
└── service/

communication/
├── domain/
│   ├── entity/
│   └── repository/
└── service/

user/
├── domain/
│   ├── entity/
│   ├── enums/
│   └── repository/
└── service/

alert/
├── domain/
│   ├── entity/
│   ├── enums/
│   └── repository/
└── service/

---

# Regras de Implementação

## Domain

Contém apenas:

* Entidades
* Enums
* Interfaces de repositório

Entidades devem conter:

* Atributos
* Construtores
* Getters e Setters
* Métodos de domínio
* toString()

Não utilizar anotações JPA neste momento.

Não utilizar frameworks dentro do domínio.

---

## Repository

Os repositórios devem ser apenas interfaces.

Exemplo:

public interface ClienteRepository {
}

Não implementar persistência nesta fase.

---

## Service

Os serviços podem existir apenas como placeholders.

Exemplo:

public class ClienteService {
}

Não implementar regras complexas nesta fase.

---

# Bounded Contexts

## Meeting Context

Responsável por:

* Clientes
* Reuniões
* Transcrições

Entidades:

* Cliente
* Reuniao
* Transcricao

---

## Analysis Context

Responsável por:

* Análises geradas pela IA
* Insights

Entidades:

* AnaliseIA
* Insight

---

## Alert Context

Responsável por:

* Alertas comerciais

Entidades:

* Alerta

---

## Communication Context

Responsável por:

* Conversas com o copiloto

Entidades:

* Chat
* PerguntaCopiloto

---

## User Context

Responsável por:

* Usuários da plataforma

Entidades:

* Usuario

---

# Modelo Atual

Cliente
└── Reuniao
├── Transcricao
└── AnaliseIA
├── Insight
└── Alerta

Usuario
└── Chat
└── PerguntaCopiloto

---

# Enums

PerfilUsuario

* DIRETOR_COMERCIAL
* EXECUTIVO_COMERCIAL

StatusProcessamento

* PENDENTE
* PROCESSANDO
* PROCESSADA
* ERRO

TipoInsight

* CHURN
* OPORTUNIDADE
* CONCORRENTE
* SATISFACAO

PrioridadeAlerta

* BAIXA
* MEDIA
* ALTA

SentimentoGeral

* POSITIVO
* NEUTRO
* NEGATIVO

Severidade

* BAIXA
* MEDIA
* ALTA

---

# Diretrizes para IA

Ao gerar código:

* Priorizar simplicidade
* Não criar infraestrutura desnecessária
* Não criar DTOs
* Não criar Controllers
* Não criar Configurations
* Não criar JPA
* Não criar integrações externas

Focar exclusivamente em:

* Modelagem OO
* Entidades
* Relacionamentos
* Encapsulamento
* Regras básicas de domínio

A implementação atual visa atender os requisitos acadêmicos da Sprint antes da evolução para uma API completa.
