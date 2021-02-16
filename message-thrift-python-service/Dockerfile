FROM hub.contains.com/micro-service/python-base:latest
MAINTAINER xxx xxx@contains.com

ENV PYTHONPATH /
COPY message /message

ENTRYPOINT ["python", "/message/message_service.py"]