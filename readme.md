# Projeto Prático – Mensageria em Java  
**Software Concorrente e Distribuído**  
Elias Batista Ferreira  


**Participantes**

202201697 - JOÃO VITOR ALVES DOS REIS

202201691 - GABRIEL BORGES GARCIA 

202201706 - MATEUS HENRIQUE GANDI DE OLIVEIRA 


## Visão Geral do Projeto  
Sistema de e-commerce com arquitetura baseada em mensageria (Kafka) para gerenciamento assíncrono de pedidos, validação de estoque e notificações.  

### Fluxo e comunicação
![fluxo](https://github.com/user-attachments/assets/a7b1daaf-d777-4def-b075-82ec25ce96e6)

### Componentes Principais  

1. **Orders Service (Spring Boot - Porta 8079)**  
   - Recebe pedidos via POST (`/orders`).  
   - Gera UUID e timestamp para cada pedido.  
   - Publica pedidos no tópico `orders` do Kafka.  

2. **Inventory Service**  
   - Consome mensagens do tópico `orders`.  
   - Valida disponibilidade de estoque (PostgreSQL).  
   - Publica resultado (sucesso/falha) no tópico `inventory-events`.  

3. **Notification Service (Porta 8081)**  
   - Consome eventos de `inventory-events`.  
   - Envia e-mail com status do pedido (estoque válido ou insuficiente).  
   - Registra notificações no console.  

4. **Frontend**  
   - Interface para adicionar produtos ao carrinho.  
   - Envia pedidos ao **Orders Service**.  

### Tecnologias aplicadas

- Java 24
- Maven 3.9.9
- Spring Boot 3.5.3
- Kafka 2.13
- Node 23
- React.js 20.3.10


## Requisitos Funcionais Atendidos  

| ID     | Descrição |  
|--------|-----------|  
| RF-1   | Criação dos tópicos Kafka: `orders` e `inventory-events`. |  
| RF-2   | API REST em `Order-Service` para receber pedidos (UUID + timestamp + itens). |  
| RF-3   | `Inventory-Service` processa mensagens em ordem e publica resultado. |  
| RF-4   | `Notification-Service` dispara e-mails e logs no console. |  


## Requisitos Não Funcionais Atendidos

### 1. Escalabilidade  
- **Broker como mediador**: Gerencia mensagens entre múltiplas instâncias dos nossos serviços.  
- **Balanceamento de carga**: Partições permitem que operações de consumo ocorram em paralelo.
- **Resiliência**: Mensagens persistem mesmo com falhas, desde que configurado para isso.

### 2. Tolerância a Falhas  
- *Persistência*: Mensagens persistem mesmo com falhas no envio, desde que configurado para isso.
  
    [Exemplo] *Queda do Inventory Service*: O Kafka retém mensagens não processadas até o serviço voltar, evitando perda de pedidos.  

### 3. Idempotência  
- O uso do Kafka garante que operações repetidas não alteram o resultado após a primeira execução. A indepotência é aplicada sobre o produtor para torna-lo confiável.  
- Com ele existe a garantia de que não haverá duplicações na mesma partição.
- Cada requisição que envia lotes de eventos ao Kafka terá um identificador único.
- O broker ao receber novamente a requisição já processada com sucesso, apenas devolve o ack (confirmação que parte de um mecanismo maior que controla como os produtores recebem confirmações dos brokers sobre o recebimento e persistência das mensagens), sem persistir novamente e assim evitando a duplicação.
- Sem o produtor idempotente, o broker receberia uma requisição como se fosse uma nova, cadastrando-a novamente no lote de eventos, causando duplicação.


## Execução

**Para rodar o front**
-  cd ./front
-  npm i
-  npm run start

**Rodar o back**
- cd ./back (orders, inventory e notification)
- mvn spring-boot:run

**CRIAR CLUSTER**
- KAFKA_CLUSTER_ID="$(bin/kafka-storage.sh random-uuid)"

**FORMATAR DIRETORIO DE LOGS**
- bin/kafka-storage.sh format -t $KAFKA_CLUSTER_ID -c config/kraft/server.properties

**RODAR SERVIDOR**
- bin/kafka-server-start.sh config/kraft/server.properties

**CRIAR TOPICOS**
- bin/kafka-topics.sh --create --topic orders --bootstrap-server localhost:9092
- bin/kafka-topics.sh --create --topic inventory-events --bootstrap-server localhost:9092

**INICIAR TERMINAL COMO PRODUTOR**
- bin/kafka-console-producer.sh --topic orders --bootstrap-server localhost:9092
- bin/kafka-console-producer.sh --topic inventory-events --bootstrap-server localhost:9092

**INICIAR TERMINAL COMO CONSUMIDOR**
- bin/kafka-console-consumer.sh --topic orders --from-beginning --bootstrap-server localhost:9092
- bin/kafka-console-consumer.sh --topic inventory-events --from-beginning --bootstrap-server localhost:9092

## Como testar
- **Front** Adicionar itens ao carrinho e confirmar pedido
- **Email** Verificar e-mail recebido ou logs no console (Email de destino mockado)

## ScreenShots
![Imagem do WhatsApp de 2025-06-21 à(s) 15 11 19_9e6726b0](https://github.com/user-attachments/assets/1cec6665-dd9c-49d7-9555-2719313f22d1)
![Imagem do WhatsApp de 2025-06-21 à(s) 15 11 19_2bfa4fb9](https://github.com/user-attachments/assets/c2d2a87c-b267-4720-888c-e948c9257822)
![Captura de tela 2025-06-21 151733](https://github.com/user-attachments/assets/2aac1276-5d24-4772-adc6-b0418aae399d)

