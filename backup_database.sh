#!/usr/bin/env sh

docker exec -t phitag-postgres pg_dumpall -U dev -p 5432 > dump_`date +%Y-%m-%d"_"%H_%M_%S`.sql
