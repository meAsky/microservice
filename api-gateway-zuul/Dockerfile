FROM hub.contains.com/micro-service/openjdk:8-jre
MAINTAINER xxx xxx@contains.com

COPY target/api-gateway-zuul-1.0-SNAPSHOT.jar /api-gateway-zuul.jar

ENTRYPOINT ["java","-jar","/api-gateway-zuul.jar"]
