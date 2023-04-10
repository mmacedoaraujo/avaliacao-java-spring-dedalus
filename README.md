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

Para acessar o swagger:

```sh
http://localhost:8080/user-api-swagger
```

![](https://user-images.githubusercontent.com/103322548/230923062-20423445-75f5-4029-b4ca-b7fd0192e54a.png)
