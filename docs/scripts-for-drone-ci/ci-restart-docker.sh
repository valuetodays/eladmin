#!/bin/bash

# if place the code in .drone.yml : only restart docker, cannot start it up
# but if place in an sh file: it works all.

dc_file=/root/workbench_dir/git-repo/valuetodays-dockers/portal-quarkus/docker-compose.yml
# print result
docker compose -f ${dc_file} ps
log_text=$(docker compose -f ${dc_file} ps | grep 'portal-system-quarkus')
echo "log_text=[$log_text]"
if [ "$log_text" == "" ]; then
  echo "Service not running, starting..."
  docker compose -f ${dc_file} up -d
else
  echo "Service is running, restarting..."
  docker compose -f ${dc_file} restart
fi
# end
