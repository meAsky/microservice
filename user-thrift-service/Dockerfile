FROM hub.contains.com/micro-service/openjdk:8-jre
MAINTAINER xxx xxx@contains.com

COPY target/user-thrift-service-1.0-SNAPSHOT.jar /user-service.jar

ENTRYPOINT ["java","-jar","/user-service.jar"]
