# Commerce

**A study project focused on Java, Spring Boot, Microservices and asynchronous communication.**

O Commerce é um projeto de estudo que consiste no desenvolvimento de uma aplicação de e-commerce baseada em arquitetura de microsserviços.

O objetivo é explorar conceitos de desenvolvimento back-end, comunicação assíncrona, persistência de dados e integração entre serviços, utilizando tecnologias do ecossistema Java.

O projeto possui dois microsserviços independentes: gerenciamento de pedidos e gerenciamento de estoque. O processamento de pagamentos será simulado dentro do serviço de pedidos.

> **Project status:** In development

---

## 1. Objetivos

- Aplicar conceitos de arquitetura de microsserviços.
- Desenvolver APIs REST com Spring Boot.
- Implementar comunicação assíncrona utilizando RabbitMQ.
- Trabalhar com persistência independente por serviço.
- Aplicar conceitos de transações e consistência em sistemas distribuídos.
- Implementar testes unitários e de integração.
- Explorar containerização utilizando Docker.

## 2. Arquitetura

A aplicação será dividida em dois microsserviços, cada um responsável por um domínio específico do sistema.

```text
                    CLIENT
                      |
                      v
                +-----------+
                |  ms-order |
                +-----------+
                      |
                      | RabbitMQ
                      |
                      v
              +----------------+
              |  ms-inventory |
              +----------------+
```

### Microsserviços

| Serviço | Responsabilidade |
|---|---|
| `ms-order` | Gerenciamento de pedidos, itens, valores, status e simulação de pagamentos. |
| `ms-inventory` | Cadastro de produtos, consulta de disponibilidade, reserva e liberação de estoque. |

### Comunicação entre serviços

A comunicação será realizada por meio do RabbitMQ, utilizando troca de mensagens e eventos para desacoplar os microsserviços.

Cada serviço possuirá seu próprio banco de dados, evitando dependências diretas entre as estruturas de persistência.

```text
ms-order       -> order_db
ms-inventory   -> inventory_db
```

## 3. Tecnologias

| Tecnologia | Utilização |
|---|---|
| Java 21 | Linguagem principal |
| Spring Boot | Desenvolvimento dos microsserviços |
| Spring Web | Construção das APIs REST |
| Spring Data JPA | Persistência de dados |
| PostgreSQL | Banco de dados relacional |
| RabbitMQ | Mensageria e comunicação assíncrona |
| Spring AMQP | Integração com RabbitMQ |
| Docker | Containerização |
| Docker Compose | Orquestração dos containers |
| Flyway | Versionamento do banco de dados |
| Bean Validation | Validação de dados |
| Lombok | Redução de código boilerplate |
| JUnit | Testes automatizados |
| Mockito | Simulação de dependências nos testes |
| Testcontainers | Testes com infraestrutura em containers |
| Maven | Gerenciamento de dependências e build |
| Git / GitHub | Versionamento e colaboração |

## 4. Funcionalidades

### Gerenciamento de estoque

- [ ] Cadastro de produtos.
- [ ] Consulta de produtos.
- [ ] Consulta de disponibilidade.
- [ ] Atualização de quantidade.
- [ ] Reserva de estoque.
- [ ] Liberação de estoque.
- [ ] Confirmação de baixa de estoque.

### Gerenciamento de pedidos

- [ ] Criação de pedidos.
- [ ] Adição de itens.
- [ ] Cálculo de subtotal.
- [ ] Cálculo do valor total.
- [ ] Consulta de pedidos.
- [ ] Controle de status.
- [ ] Histórico de alterações.
- [ ] Simulação de pagamento.
- [ ] Confirmação de pedidos.
- [ ] Cancelamento de pedidos.

### Integração

- [ ] Publicação e consumo de eventos RabbitMQ.
- [ ] Solicitação de reserva de estoque.
- [ ] Tratamento de reserva aprovada ou recusada.
- [ ] Liberação de estoque em caso de falha no pagamento.
- [ ] Tratamento básico de mensagens duplicadas e falhas.

## 5. Fluxo de compra

O fluxo principal de compra será dividido entre os dois microsserviços.

```text
1. Cliente solicita um pedido
          |
          v
2. ms-order cria o pedido
          |
          v
3. Publica OrderCreated
          |
          v
4. ms-inventory recebe o evento
          |
          v
5. Verifica disponibilidade
          |
          v
6. Reserva o estoque
          |
          v
7. Publica StockReserved
          |
          v
8. ms-order recebe a confirmação
          |
          v
9. Simula o pagamento
          |
          v
10. Confirma o pedido
          |
          v
11. Publica OrderConfirmed
```

### Tratamento de falhas

**Estoque insuficiente:**

O serviço de estoque informa a indisponibilidade e o serviço de pedidos realiza o cancelamento.

**Pagamento recusado:**

O serviço de pedidos cancela a operação e solicita ao serviço de estoque a liberação da reserva realizada anteriormente.

## 6. Eventos RabbitMQ

Os microsserviços utilizarão eventos para representar as principais operações do fluxo de negócio.

| Evento | Descrição |
|---|---|
| `OrderCreated` | Pedido criado. |
| `StockReservationRequested` | Solicitação de reserva de estoque. |
| `StockReserved` | Reserva de estoque aprovada. |
| `StockRejected` | Reserva recusada. |
| `StockReleased` | Estoque liberado. |
| `OrderConfirmed` | Pedido confirmado. |
| `OrderCancelled` | Pedido cancelado. |

## 7. Modelo de domínio

### Product

Responsável pela representação dos produtos disponíveis no estoque.

| Campo | Descrição |
|---|---|
| `id` | Identificador do produto |
| `name` | Nome |
| `description` | Descrição |
| `price` | Preço |
| `quantity` | Quantidade disponível |
| `active` | Situação do produto |
| `createdAt` | Data de criação |
| `updatedAt` | Data de atualização |

### Order

Representa um pedido realizado pelo cliente.

| Campo | Descrição |
|---|---|
| `id` | Identificador do pedido |
| `status` | Situação atual |
| `totalAmount` | Valor total |
| `createdAt` | Data de criação |
| `updatedAt` | Data de atualização |

### OrderItem

Representa os produtos associados a um pedido.

| Campo | Descrição |
|---|---|
| `id` | Identificador do item |
| `orderId` | Identificador do pedido |
| `productId` | Identificador do produto |
| `productName` | Nome do produto no momento da compra |
| `quantity` | Quantidade solicitada |
| `unitPrice` | Preço unitário no momento da compra |
| `subtotal` | Valor total do item |

O preço unitário será registrado no momento da criação do pedido, preservando o valor histórico da compra mesmo que o preço do produto seja alterado posteriormente.

## 8. Status do pedido

O pedido terá um ciclo de vida controlado por estados.

```text
CREATED
   |
   v
WAITING_STOCK
   |
   v
STOCK_RESERVED
   |
   v
PAYMENT_APPROVED
   |
   v
CONFIRMED
```

Em situações de falha, o pedido poderá seguir para cancelamento.

Estados principais:

- `CREATED`
- `WAITING_STOCK`
- `STOCK_RESERVED`
- `PAYMENT_APPROVED`
- `CONFIRMED`
- `PAYMENT_FAILED`
- `CANCELLED`

As transições serão controladas para impedir operações inválidas, como confirmar um pedido já cancelado.

## 9. Regras de negócio

- Um pedido deve possuir pelo menos um item.
- A quantidade solicitada deve ser maior que zero.
- O preço do produto deve ser válido e positivo.
- Não será permitido reservar uma quantidade superior à disponibilidade.
- Um pedido confirmado não poderá retornar ao estado inicial.
- Um pedido cancelado não poderá ser confirmado.
- Uma reserva não poderá ser liberada mais de uma vez.
- Um pagamento aprovado não deverá ser processado novamente.
- Cada microsserviço deverá acessar exclusivamente seu próprio banco de dados.

## 10. Estrutura do projeto

Estrutura planejada para o repositório:

```text
commerce/
│
├── ms-order/
│   ├── src/
│   └── pom.xml
│
├── ms-inventory/
│   ├── src/
│   └── pom.xml
│
├── docs/
│   ├── architecture.md
│   ├── business-rules.md
│   ├── events.md
│   └── diagrams/
│
├── docker-compose.yml
├── .env.example
├── .gitignore
└── README.md
```

## 11. Execução do projeto

A execução completa será disponibilizada após a configuração dos microsserviços e da infraestrutura.

Os serviços de infraestrutura serão executados utilizando Docker Compose.

```bash
# Iniciar os containers
docker compose up -d

# Verificar os containers
docker compose ps

# Consultar logs
docker compose logs -f

# Encerrar os containers
docker compose down
```

As configurações de conexão, credenciais e portas serão disponibilizadas por meio de variáveis de ambiente.

Consulte o arquivo `.env.example` para conhecer as configurações necessárias, sem armazenar credenciais reais no repositório.

## 12. Roadmap de desenvolvimento

| Fase | Descrição | Status |
|---|---|---|
| 01 | Planejamento e definição da arquitetura | Em andamento |
| 02 | Desenvolvimento do `ms-inventory` | Pendente |
| 03 | Desenvolvimento do `ms-order` | Pendente |
| 04 | Containerização e PostgreSQL | Pendente |
| 05 | Configuração do RabbitMQ | Pendente |
| 06 | Integração entre os microsserviços | Pendente |
| 07 | Implementação do pagamento simulado | Pendente |
| 08 | Tratamento de falhas | Pendente |
| 09 | Testes unitários e de integração | Pendente |
| 10 | Documentação e finalização | Pendente |

## 13. Melhorias futuras

Após a conclusão do fluxo principal, poderão ser explorados outros conceitos:

- Autenticação e autorização com JWT.
- API Gateway.
- Microsserviço de usuários.
- Serviço independente de pagamentos.
- Serviço de notificações.
- Carrinho de compras.
- Cupons de desconto.
- Expiração de reservas.
- Outbox Pattern.
- Saga Pattern.
- Circuit Breaker.
- Cache.
- Observabilidade.
- Deploy em nuvem.

Essas funcionalidades não fazem parte do escopo inicial.

## 14. Objetivo de conclusão

O projeto será considerado concluído quando os dois microsserviços estiverem funcionando de maneira independente e integrados por meio do RabbitMQ, permitindo executar o fluxo completo de compra.

O resultado esperado é uma aplicação capaz de cadastrar produtos, criar pedidos, reservar estoque, simular pagamentos, confirmar compras e tratar cenários básicos de falha.

---

**Desenvolvido por Vagner Souza Junior**

Projeto desenvolvido para fins de estudo e prática de desenvolvimento back-end com Java e arquitetura de microsserviços.