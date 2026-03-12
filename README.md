# Sistema de Agendamento de Consultas

API REST para agendamento de consultas entre pacientes e médicos, com autenticação JWT, validações de horário e notificações por e-mail.

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.x
- Spring Security + JWT
- Spring Data JPA + PostgreSQL
- JavaMailSender
- Docker
- Swagger (SpringDoc OpenAPI)
- BCrypt

## Endpoints Principais
Auth

POST /auth/registrar - Cadastro de usuário (enviar role = PACIENTE ou MEDICO)

POST /auth/login - Autenticação (retorna token JWT)

### Consultas (requer token)
POST /consultas/agendar - Agenda nova consulta (paciente)

PUT /consultas/{id}/cancelar - Cancela consulta (paciente)

GET /consultas/paciente - Lista consultas do paciente logado

GET /consultas/medico/disponiveis?dataHora=... - Lista médicos disponíveis em determinada data/hora

## Estrutura do Banco
Tabelas:

usuario (base)

paciente (estende usuario)

medico (estende usuario)

consulta

## Regras de Validação
Email único

Senha criptografada com BCrypt

Consultas: médico não pode ter duas consultas no mesmo horário

Cancelamento: apenas consultas agendadas podem ser canceladas

## Melhorias Futuras
Testes automatizados

Paginação nas listagens

Lembretes automáticos por e-mail

Disponibilidade configurável por médico


## Funcionalidades

- Cadastro de pacientes e médicos
- Autenticação com JWT (dois perfis: PACIENTE e MEDICO)
- Agendamento de consultas (somente pacientes autenticados)
- Cancelamento de consultas (somente o paciente que agendou)
- Listagem de consultas por paciente/medico
- Verificação de disponibilidade de médicos por horário
- Regras de negócio:
  - Horário de funcionamento: segunda a sexta, 8h às 18h
  - Não permitir agendamento em horário já ocupado
  - Data futura obrigatória
- Envio de e-mail ao agendar/cancelar consulta

## Como Executar

### Pré-requisitos

- Docker e Docker Compose
- Java 17+
- Maven

### Passos

1. Clone o repositório
2. Inicie o banco PostgreSQL com Docker:
   ```bash
   docker-compose up -d