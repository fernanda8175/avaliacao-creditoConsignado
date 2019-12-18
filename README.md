# Avaliação - Crédito Consignado

## Introdução
Este projeto foi idealizado para atender ao Desafio proposto para oportunidade de TI no banco Itaú.  
O Microserviço valida as regras de contratação de crédito consignado para aposentados e pensionistas.
​
Tecnologias utilizadas neste projeto:
 - Java 8 - Framework Spring Boot; 
 - Apache Maven;
 - Docker;
 - MongoDB;
 
## Pré-Requisitos do Projeto
- JDK, que pode ser baixado diretamente do site da [Oracle](https://www.oracle.com/technetwork/java/javase/downloads/index.html);  
- Apache Maven previamente configurado em seu computador. Caso precise de instalação, acesse a pagina oficial do [Maven](https://maven.apache.org/install.html) para realizar a instalação;
- Docker instalado em seu computador. Se necessário, faça o download na pagina oficial do [Docker](https://www.docker.com/products/docker-desktop);
- É necessário conexão com a internet para download das imagens diretamente do Dockerhub;
- O Projeto foi desenvolvimento utilizando Eclipse versão 4.12.0, disponível na [eclipse.org](https://www.eclipse.org/downloads/packages/release/2019-06/r/eclipse-ide-eclipse-committers);
- Versão do Spring 2.0.4, informações no site oficial [spring.io](https://spring.io/blog/2018/07/30/spring-boot-2-0-4-available-now);
​
## Instalação do Projeto
Todo o projeto foi desenvolvimento pensando em escalabilidade. Devido essa decisão, tanto a aplicação, como o banco de dados, são containers de imagens docker.  
Os passos a seguir são cronologicamente dependentes, e ao final, a aplicação estará disponível para receber requisições HTTP.
​
#### Clone do repositório:
Execute os passos a seguir:
- Abra um prompt de comando (Terminal, Powershell, Git Bash, etc) e execute o comando:  
  - `git clone https://github.com/fernanda8175/avaliacao-creditoConsignado.git`  
- A seguir, vamos acessar o diretório do projeto. Execute:  
  - `cd avaliacao-creditoConsignado/consignado/`  
  
Todos os comandos daqui para frente devem ser executados no prompt de comando que deve estar com o diretório do repositório como pasta de trabalho, acessado pelos comandos acima. 
​
#### Criando uma rede virtual no Docker
Para que nossa aplicação consiga conversar com o banco de dados, iremos criar uma rede virtual dentro do ambiente Docker.
Para tal ação, execute o comando abaixo no terminal:  
- `docker network create itau`
​
#### Instalação do Banco de Dados
Para esta aplicação foi escolhido utilizar o [MongoDB](https://www.mongodb.com/), que é um banco de dados noSQL. Desta forma conseguimos garantir alta disponibilidade e performance em ambientes distribuidos.
​
- Baixe a imagem oficial do mongodb do dockerhub, através do comando abaixo:
  - `docker pull mongo`  
- Ao final, vamos inicializar o nosso container de mongodb utilizando a rede virtual criada anteriormente:
  - `docker run --name mongo_itau -d -p 27017:27017 --network=itau mongo`  

Com esses passos finalizados, um container de mongodb estará em execução em seu computador.  
É possível verificar se o container está ativo através do comando abaixo:  
- `docker ps`
​
#### Build da aplicação via Maven
- Através do Maven, conseguimos compilar o projeto Java. Para tal ação, execute o comando abaixo:
  - `mvn clean package`
- Com o fim da compilação, precisamos criar uma imagem Docker com os arquivos referentes a nossa aplicação. Isso é possível executando o comando:  
  - `docker build -t consulta_consignado:1.0 -f Dockerfile .`
  
#### Iniciando a aplicação
Temos o banco de dados funcionando e a aplicação compilada dentro de uma imagem docker. Agora precisamos criar um container baseado na imagem docker da aplicação para conseguir utiliza-la.  
O Container será criado com base na imagem do OpenJDK ([openjdk:8-jdk-alpine](https://hub.docker.com/layers/openjdk/library/openjdk/8-jdk-alpine/images/sha256-210ecd2595991799526a62a7099718b149e3bbefdb49764cc2a450048e0dd4c0))
​
- Vamos executar o comando abaixo para iniciar a aplicação:
  - `docker run --name consulta_consignado_app -d -p 8080:8080 --network=itau consulta_consignado:1.0`  

Executando o comando abaixo é possível identificar dois containers ativos, um de mongodb e outro com a aplicação *consulta_consignado_app*.    
- `docker ps`
​
#### Acessando a aplicação
Com os containers ativos, a aplicação está pronta para receber requisições HTTP na porta 8080. Podemos utilizar a aplicação para testes através de ferramentas como o [Postman](https://www.getpostman.com/downloads/).  
Exemplos de requests:
​
##### POST:
- Requisição  
  - `URL [POST]: localhost:8080/api/loan/contract`  
  - ```
    Body:  
    {
        “cpf”: “40133618846",
        “valorContratado”: 800,
        “parcelas”: 5,
        “diaVencimento”: 15,
        “dataContratacao”: “2019-12-18”,
        “aposentadoria”: “1234”
    }
​
- Retorno
  - ```
    {
        “id”: “5dfa3633637e2f2f8dde18fa”,
        “cpf”: “40133618846",
        “valorContratado”: 800,
        “parcelas”: 5,
        “diaVencimento”: 15,
        “dataContratacao”: “2019-12-18T00:00:00.000+0000”,
        “fimContrato”: “2020-02-18T00:00:00.000+0000”,
        “aposentadoria”: 1234
    }
​
##### GET
- Requisição
  - `URL [GET]: localhost:8080/api/client/40133618846`
- Retorno
  - ```
    {
         “id”: “5dfa3561637e2f2f8dde18f7",
         “cpf”: “39016914803",
         “nome”: “Fernanda”,
         “margemContratacao”: 10,
         “aposentadoria”: 8531629,
         “dataAutorizacao”: “2020-06-15T01:00:00.000+0000",
         “dataNascimento”: “1993-02-19T00:00:00.000+0000",
         “dataDespacho”: “2019-12-18T00:00:00.000+0000"
    }
