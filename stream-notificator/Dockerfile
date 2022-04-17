FROM gradle:6.9.2-jdk11-alpine
COPY ./resource/project /project

RUN apk update \
  && apk add \
    bash \
    vim \
  && cd /project \
  && ./gradlew build

COPY ./resource/entrypoint.sh /entrypoint.sh
ENTRYPOINT [ "/entrypoint.sh" ]
CMD [ "-h" ]
