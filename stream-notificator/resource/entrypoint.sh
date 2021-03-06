#!/bin/bash

PROGNAME=$(basename $0)
VERSION="1.0"
maintenance_mode=1
start_server=1

function run() {
  # add logic here
  cd /project
  ./gradlew runStream
}

function usage() {
  echo "Usage: ${PROGNAME} [OPTIONS]"
  echo "  this script is -."
  echo "Options:"
  echo "  -h, --help"
  echo "  -m, --maintenance"
  echo "  -s, --server"
  echo "  -v, --version"
}

for OPT in "${@}"
do
  case ${OPT} in
    -h | --help)
      usage
      exit 1
      ;;
    -m | --maintenance)
      maintenance_mode=0
      ;;
    -s | --server)
      start_server=0
      ;;
    -v | --version)
      echo ${VERSION}
      exit 1
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

if [ ${maintenance_mode} -eq 0 ] ; then
  tail -f /dev/null
  exit 1
fi

if [ ${start_server} -eq 0 ] ; then
  run
  exit 1
fi

usage
