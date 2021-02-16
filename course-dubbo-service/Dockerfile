FROM hub.contains.com/micro-service/openjdk:8-jre
MAINTAINER xxx xxx@contains.com

COPY target/course-dubbo-service-1.0-SNAPSHOT.jar /course-service.jar

ENTRYPOINT ["java","-jar","/course-service.jar"]
