# Componente csb-management-seller

Componente responsável pelo CRUD de vendedores.

## Informações da aplicação

> Aplicação foi feita utilizando gradle 8, Java 17, Spring 3, Junit-Jupiter e Postgresql como banco de dados.

> Foi utilizado um script SQL para criar a tabelas e sequências utilizadas ao iniciar o projeto.

### Comando úteis

Buildar:
`gradlew clean build`

Rodar Testes:
`gradlew test`

## Iniciando a aplicação

Existe um docker-compose responsável por criar o banco de dados da aplicação.

Para iniciar o banco executar o comando na raiz do projeto:

`docker-compose up --build -d`

Para iniciar a aplicação há a possibilidade de executar pelo próprio INTELLIJ
ou pelo comando `gradlew run`

## Finalizando aplicação

Para terminar o container de banco de dados executar o comando:
`docker-compose down` na raiz do diretório do projeto.
