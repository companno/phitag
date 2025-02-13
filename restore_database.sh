#!/usr/bin/env sh

cat "$1" | docker exec -i phitag-postgres psql -d phitag -U dev -p 5432
