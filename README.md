# Avaliação Java e Spring Framework

Repositório contendo projeto desenvolvido a partir dos seguintes critérios:

![](https://user-images.githubusercontent.com/103322548/230923864-ce241d49-ea47-44b2-9f2d-b63591408b57.png)

## Como rodar o projeto

### Windows:

Clone o projeto com em alguma pasta de sua preferência com o seguinte comando:

```sh
git clone git@github.com:mmacedoaraujo/avaliacao-java-spring-dedalus.git
```

Em seguida importe o projeto em alguma IDE da sua preferência e execute as classes abaixo, em sua respectiva ordem:

```sh
com.mmacedoaraujo.discovery.DiscoveryApplication

com.mmacedoaraujo.gateway.GatewayApplication
```

Importante lembrar que as duas classes à seguir necessitam de uma váriavel de ambiente CLOUDAMQP_URL, contendo um URL para um servidor CloudAMQP. Exemplo: amqps://nslkctjt:***@jackal.rmq.cloudamqp.com/nslkctjt

```sh
com.mmacedoaraujo.userapi.UserApiApplication

com.mmacedoaraujo.userapirabbitmqconsumer.UserApiRabbitmqConsumerApplication
```

### Docker:

Clone o projeto com em alguma pasta de sua preferência com o seguinte comando:

```sh
git clone git@github.com:mmacedoaraujo/avaliacao-java-spring-dedalus.git
```

Na pasta raiz do projeto, abra o terminal e execute:

```sh
docker-compose up
```

## Como usar a aplicação

Após se certificar de que todos os quatro serviços estão rodando, basta usar o endereço do gateway para realizar requisições:

```sh
http://localhost:8080
```

### Endpoints da aplicação

Para acessar o Swagger:

```sh
http://localhost:8080/user-api-swagger
```

![](https://user-images.githubusercontent.com/103322548/230923062-20423445-75f5-4029-b4ca-b7fd0192e54a.png)

Para acessar o dashboard Eureka:

```sh
http://localhost:8080/eureka
```

![](https://user-images.githubusercontent.com/103322548/230933506-e3afd7f1-6ec8-461a-a959-262dc1549705.png)

Alguns endpoints irão necessitar de autenticação básica, basta usar:

```sh
username: admin
password: admin
```

## Mensageria

No projeto, adicionei uma implementação básica de mensageria com o RabbitMQ. Configurei o serviço user-api como "producer" e user-api-rabbitmq-consumer como "consumer", onde sempre que um novo usuário for cadastrado na api de usários, a entidade salva será enviada para a fila e consumida no serviço configurado como consumer, gerando apenas um 'System.out.println' com as informações, como exemplificado na imagem abaixo: 

![](https://user-images.githubusercontent.com/103322548/230941777-a96b0f70-819f-42e2-94e9-1f7e0c68d603.png)

## Tecnologias utilizadas

* Java 17
* Spring Boot
* Spring Cloud Gateway
* Spring Security
* Spring Cache
* AspectJ
* RabbitMQ
* Eureka Server and Client
* H2 Database
* Lombok
* MapStruct
* JUnit
* Swagger
* Docker
* Jib




