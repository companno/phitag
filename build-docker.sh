#!/usr/bin/env sh

docker-compose -f docker/docker-compose.dev.yml up --remove-orphans &
docker build --network=host -t phitag/phitag -f phitag.Dockerfile .
