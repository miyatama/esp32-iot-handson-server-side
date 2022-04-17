FROM debian:buster-slim

RUN apt update -y \
  && apt upgrade -y \
  && apt install -y \
    apt-transport-https \
    wget \
  && wget https://dl.grafana.com/oss/release/grafana-8.4.4.linux-amd64.tar.gz \
  && tar -zxvf grafana-8.4.4.linux-amd64.tar.gz \
  && mv grafana-8.4.4 grafana \
  && mkdir /var/lib/grafana \
  && mkdir /grafana/data \
  && addgroup grafana \
  && adduser -q --ingroup grafana grafana \
  && chown -R grafana:grafana /grafana


USER grafana
WORKDIR /grafana

COPY ./resource/entrypoint.sh /entrypoint.sh
ENTRYPOINT [ "/entrypoint.sh" ]
CMD [ "-h" ]
