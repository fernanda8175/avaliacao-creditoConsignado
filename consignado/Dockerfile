FROM openjdk:8-jdk-alpine

RUN apk update && apk upgrade

RUN mkdir -p /appl/bin

ADD target/consignado-0.0.1-SNAPSHOT.jar /appl/bin/app.jar

ENTRYPOINT ["java","-Dspring.data.mongodb.uri=mongodb://mongo_itau:27017/itau_consignado", "-jar", "/appl/bin/app.jar"]