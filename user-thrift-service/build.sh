#!/usr/bin/env bash

mvn package

docker build -t hub.contains.com/micro-service/user-service:latest .

docker push hub.contains.com/micro-service/user-service:latest