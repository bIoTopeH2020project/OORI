#!/bin/sh

echo "Starting Spring boot app"

# Set a server port, default is 8080
: ${SERVER_PORT=8080}

# Set a sparql repository endpoint url, default is empty, i.e. fallback to in-memory repository
: ${ENDPOINT_URL=""}

ARGS="-Djava.security.egd=file:/dev/./urandom -Dserver.port=${SERVER_PORT} -Dendpoint.url=${ENDPOINT_URL}"

# Add proxy args
if [ ! -z "$PROXY_HOST" ]; then
    ARGS="${ARGS} -Dhttp.proxyHost=${PROXY_HOST} -Dhttp.proxyPort=${PROXY_PORT-3128}"
    if [ ! -z "$PROXY_USER" ]; then
        ARGS="${ARGS} -Dhttp.proxyUser=${PROXY_USER}"
    fi
    if [ ! -z "$PROXY_PASS" ]; then
        ARGS="${ARGS} -Dhttp.proxyPassword=${PROXY_PASS}"
    fi
fi

# Enable debugging
ARGS="${ARGS} -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005"

echo "ARGS=${ARGS}"

exec java ${ARGS} -jar /oorcs.jar