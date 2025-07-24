#!/bin/bash

# set variable
WORKSPACE_PATH=/drone/src

WORKBENCH_DIR=/root/workbench_dir
WORKBENCH_BAK_DIR=${WORKBENCH_DIR}'/backup'
WORKBENCH_DEPLOY_DIR=${WORKBENCH_DIR}'/deploy'
APP_JAR_NAME=eladmin-tmp-system-runner.jar
APP_JAR_DIR=${WORKSPACE_PATH}/eladmin-system/target

echo start on $(date "+%Y-%m-%d %H:%M:%S")
shell_directory=$(cd `dirname $0`; pwd)
echo ${shell_directory}
echo current_dir=$(pwd)

timeStampStr=$(date "+%Y-%m-%d_%H_%M_%S")
APP_JAR_BAK=${WORKBENCH_BAK_DIR}'/'${APP_JAR_NAME}'.'${timeStampStr}

if [ -f "${WORKBENCH_DEPLOY_DIR}/${APP_JAR_NAME}" ]; then
    mv ${WORKBENCH_DEPLOY_DIR}/${APP_JAR_NAME} ${APP_JAR_BAK}
fi

cp ${APP_JAR_DIR}/${APP_JAR_NAME} ${WORKBENCH_DEPLOY_DIR}/

echo restarted ${APP_JAR_DIR}/${APP_JAR_NAME}
# end
