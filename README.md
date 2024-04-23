# Componente csb-management-seller

Componente responsável pelo CRUD de vendedores.
Internamente possui um servidor mock para simular serviço de filiais onde os vendedores são.
A cada request de criação ou atualização é verificado no serviço mock se a filial existe.

## Informações da aplicação principal (vendedores)

> Aplicação foi feita utilizando gradle 8, Java 17, Spring 3, Junit-Jupiter e Postgresql como banco de dados.
> Segue a arquitetura em camadas controller/service/repository.
> Foi utilizado um script SQL para criar a tabelas e sequências utilizadas ao iniciar o projeto.

> Aplicação roda na porta 8080 - http://localhost:8080

> **Para facilitar o uso para testes da aplicação foi adicionado o swagger** - http://localhost:8080/swagger-ui/index.html

> Para observabilidade da api foi utilizado:
> - Spring Actuator (monitoramento, healthchek e métricas)
> - Micrometer (métricas e para trace nos logs)
> - Prometheus (para guardar métricas temporais) - http://localhost:9090
> - Zipkin (para log distribuído entre cliente e servidor) - http://localhost:9411/zipkin/

### Comando úteis

Buildar:
`gradlew :app:clean :app:build`

Rodar Testes:
`gradlew :app:test`

## Informações sobre aplicação mock

> Aplicação foi feita utilizando gradle 8, Java 17, Spring 3, Junit-Jupiter

> Tem por finalidade ser uma aplicação que retorna dados prefinidos sobre filiais.

> Para observabilidade da api foi utilizado:
> - Spring Actuator
> - Micrometer
> - Zipkin (recebe o traceId da request do cliente)

> Aplicação roda na porta 8081 - http://localhost:8081

> **Para facilitar o uso para testes da aplicação foi adicionado o swagger** - http://localhost:8081/swagger-ui/index.html

### Comando úteis

Buildar:
`gradlew :mock-branchoffice-api:clean :mock-branchoffice-api:build`

Rodar Testes:
`gradlew :mock-branchoffice-api:test`

## Iniciando a aplicação

Existe um docker-compose responsável por criar o banco de dados da aplicação.

Para iniciar o banco executar o comando na raiz do projeto:

`docker-compose up --build -d`

Para iniciar a aplicação há a possibilidade de executar pelo próprio INTELLIJ
ou pelo comando `gradlew :app:run` e `gradlew :mock-branchoffice-api:run` para aplicação mock.