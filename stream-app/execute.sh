#!/bin/bash

PROGNAME=$(basename $0)
VERSION="1.0"
IMAGE_NAME=stream-app:1.0
CONTAINER_NAME=stream-app
build_command=1
run_command=1
log_command=1
stop_command=1
console_command=1

function execute_build() {
  echo "execute_build"
  docker build \
    -t ${IMAGE_NAME} \
    .
}

function execute_run() {
  echo "execute_run"
  execute_stop
  docker run \
    -it \
    -d \
    --name ${CONTAINER_NAME} \
    ${IMAGE_NAME} ${@}
}

function execute_stop() {
  echo "execute_stop"
  container_count=`docker ps -a | grep ${CONTAINER_NAME} | grep -c ""`
  if [ -z ${container_count} ] ; then
    container_count=0
  fi
  if [ ${container_count} -ge 1 ] ; then
    echo "stopping container..."
    docker stop ${CONTAINER_NAME}
    docker rm -f ${CONTAINER_NAME}
    echo "finish to stop container"
  fi
}

function execute_log() {
  echo "execute_log"
  docker logs ${@} ${CONTAINER_NAME}
}

function execute_console() {
  echo "execute_console"
  docker exec -it ${CONTAINER_NAME} /bin/bash
}

function usage() {
  echo "Usage: ${PROGNAME} [OPTIONS]"
  echo "  this script is -."
  echo "Options:"
  echo "  -h, --help"
  echo "  -b, --build"
  echo "  -r, --run"
  echo "  -s, --stop"
  echo "  -c, --console"
  echo "  -v, --version"
}

for OPT in "${@}"
do
  case ${OPT} in
    -h | --help)
      usage
      exit 1
      ;;
    -b | --build)
     build_command=0
      ;;
    -r | --run)
      run_command=0
      ;;
    -s | --stop)
      stop_command=0
      ;;
    -l | --log)
      log_command=0
      ;;
    -c | --console)
      console_command=0
      ;;
    -v | --version)
      echo ${VERSION}
      exit 1
      ;;
    --*)
      shift 1
      param+=( "${OPT}" )
      ;;
    -- | -)
      shift 1
      param+=( "$@" )
      break
      ;;
    -*)
      echo "${PROGNAME}: illegal option -- '$(echo $1 | sed 's/^-*//')'" 1>$2
      exit 1
      ;;
    *)
      if [ [ ! -z "$1"] && [ ! "$1" =~ ^-+ ] ] ; then
        param+=( "$1" )
        shift 1
      fi
      ;;
  esac
done

if [ ${build_command} -eq 0 ] ; then
  execute_build
  exit 0
fi
if [ ${run_command} -eq 0 ] ; then
  execute_run ${param}
  exit 0
fi
if [ ${stop_command} -eq 0 ] ; then
  execute_stop
  exit 0
fi
if [ ${log_command} -eq 0 ] ; then
  execute_log ${param}
  exit 0
fi

if [ ${console_command} -eq 0 ] ; then
  execute_console
  exit 0
fi

usage
