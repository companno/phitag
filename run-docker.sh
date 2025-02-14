#!/usr/bin/env sh

docker-compose -f docker/docker-compose.dev.yml up --remove-orphans &
docker run --net=host phitag/phitag
