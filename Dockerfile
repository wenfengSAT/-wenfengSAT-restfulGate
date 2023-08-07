FROM openjdk:11

MAINTAINER "wenfengSAT@163.com"

ENV LANG C.UTF-8
ENV LANGUAGE en_US:en
ENV LC_ALL C.UTF-8
ENV TZ Asia/Shanghai

ARG WORK_DIR=/data

WORKDIR ${WORK_DIR}
COPY ./target/*.jar  ${WORK_DIR}/
ADD ./bin/server.sh ${WORK_DIR}/bin/server.sh

ENTRYPOINT ["bash","/data/bin/server.sh"]