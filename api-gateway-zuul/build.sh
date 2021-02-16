#!/usr/bin/env bash
mvn package

docker build -t hub.contains.com/micro-service/api-gateway-zuul:latest .

docker push hub.contains.com/micro-service/api-gateway-zuul:latest