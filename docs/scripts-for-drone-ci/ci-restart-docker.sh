#!/bin/bash

# if place the code in .drone.yml : only restart docker, cannot start it up
# but if place in an sh file: it works all.
dc_file=/root/workbench_dir/git-repo/valuetodays-dockers/eladmin/docker-compose.yml
log_text=$(docker compose -f ${dc_file} ps | grep 'java')
echo "log_text=[$log_text]"
if [ "$log_text" == "" ]; then
  docker compose -f ${dc_file} up -d
else
  docker compose -f ${dc_file} restart
fi
# end
