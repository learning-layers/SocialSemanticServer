#!/bin/bash

trap 'exit' ERR
trap "echo TRAPed signal" HUP INT QUIT KILL TERM

cd /sss.app/
java -jar -Dlog4j.configuration=file:log4j.properties sss.jar

read

exec "$@"