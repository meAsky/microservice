#!/usr/bin/env bash
docker build -t hub.contains.com/micro-service/message-service:latest .

docker push hub.contains.com/micro-service/message-service:latest