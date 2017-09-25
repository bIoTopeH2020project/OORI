#!/bin/sh

# add data dump to O-MI node
curl -X POST -H "Content-Type: text/plain" --data-ascii "@brussels_bikes_omi_write.xml" "http://localhost:8080"

# send subscription for added data to OORI
curl -X POST -H "Content-Type: application/json" --data-ascii "@brussels_bikes_subscription_body.json" "http://localhost:9090/nodes"