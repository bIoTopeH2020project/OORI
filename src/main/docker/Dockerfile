FROM frolvlad/alpine-oraclejdk8:slim

# Supported environment variables
# Port of spring boot app (overrides application.properties/server.port)
ENV SERVER_PORT 8080
ENV BASE_URL=http://biotope.iais.fraunhofer.de/

ADD oorcs-0.0.1-SNAPSHOT.jar oorcs.jar
ADD run.sh run.sh

RUN sh -c 'touch /oorcs.jar'
RUN sh -c 'chmod +x /run.sh'

ENTRYPOINT ["/run.sh"]