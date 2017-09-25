FROM frolvlad/alpine-oraclejdk8:slim

# Supported environment variables
# Port of spring boot app (overrides application.properties/server.port)
ENV SERVER_PORT 9090
ENV ENDPOINT_URL http://localhost:3030/odfData

ADD oori-0.0.1-SNAPSHOT.jar oori.jar
ADD run.sh run.sh

RUN sh -c 'touch /oori.jar'
RUN sh -c 'chmod +x /run.sh'

ENTRYPOINT ["/run.sh"]