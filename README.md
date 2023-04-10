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

## Logging

Utilizei o AspectJ para realizar logging no serviço de usuários, fazendo logging das camadas controller e service.

Ao salvar um novo usuário temos o seguinte retorno:

![](https://user-images.githubusercontent.com/103322548/230944345-c676bd9e-b054-4ac8-988f-d1940b348c00.png)

Caso haja alguma exceção na camada de serviços teremos:

![](https://user-images.githubusercontent.com/103322548/230944349-bdbf8d7c-7a28-4461-b085-424efa2f447f.png)

Implementação do logging ao acionar um método na camada de controller:

![](https://user-images.githubusercontent.com/103322548/230944336-906939a3-5a64-492c-91b1-7ef68480f103.png)

Implementação do logging do retorno da requisição na camada de controller:

![](https://user-images.githubusercontent.com/103322548/230944338-35644525-496c-4f84-b212-70320116efbe.png)

Implementação do logging de exceções na camada de serviços: 

![](https://user-images.githubusercontent.com/103322548/230944340-b4848dc3-8f14-41c5-a846-a3e1c737a765.png)

Implementação do logging ao acionar métodos na camada de serviços: 

![](https://user-images.githubusercontent.com/103322548/230944342-5cceb3c2-399e-4ab2-b4c4-84ff97d51c8d.png)


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




