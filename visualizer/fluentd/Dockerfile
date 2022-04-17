FROM fluentd:v1.14.0-debian-1.0

USER root

RUN gem update --system \
  && gem install fluent-plugin-influxdb \
  && gem install fluent-plugin-kafka

USER fluentd

COPY ./resource/fluent.conf /fluentd/etc/fluent.conf

COPY ./resource/entrypoint.sh /entrypoint.sh
ENTRYPOINT [ "/entrypoint.sh" ]
CMD [ "-h" ]
