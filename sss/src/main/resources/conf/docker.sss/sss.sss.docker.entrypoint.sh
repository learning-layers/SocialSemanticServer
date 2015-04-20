#!/bin/bash

trap 'exit' ERR
trap "echo TRAPed signal" HUP INT QUIT KILL TERM

cd /sss.app/

sed -i "s#SSS_MYSQL_HOST#${SSS_MYSQL_HOST}#g" /sss.app/sss.conf.yaml &&
sed -i "s#SSS_MYSQL_PORT#${SSS_MYSQL_PORT}#g" /sss.app/sss.conf.yaml &&
sed -i "s#SSS_MYSQL_USERNAME#${SSS_MYSQL_USERNAME}#g" /sss.app/sss.conf.yaml &&
sed -i "s#SSS_MYSQL_PASSWORD#${SSS_MYSQL_PASSWORD}#g" /sss.app/sss.conf.yaml &&
sed -i "s#SSS_MYSQL_SCHEME#${SSS_MYSQL_SCHEME}#g" /sss.app/sss.conf.yaml &&

sed -i "s#SSS_AUTH_TYPE#${SSS_AUTH_TYPE}#g" /sss.app/sss.conf.yaml &&

sed -i "s#SSS_TETHYS_USER#${SSS_TETHYS_USER}#g" /sss.app/sss.conf.yaml &&
sed -i "s#SSS_TETHYS_PASSWORD#${SSS_TETHYS_PASSWORD}#g" /sss.app/sss.conf.yaml &&
sed -i "s#SSS_TETHYS_LAS_USER#${SSS_TETHYS_LAS_USER}#g" /sss.app/sss.conf.yaml &&
sed -i "s#SSS_TETHYS_LAS_PASSWORD#${SSS_TETHYS_LAS_PASSWORD}#g" /sss.app/sss.conf.yaml &&
sed -i "s#SSS_TETHYS_OIDC_CONF_URI#${SSS_TETHYS_OIDC_CONF_URI}#g" /sss.app/sss.conf.yaml &&
sed -i "s#SSS_TETHYS_OIDC_USER_END_POINT_URI#${SSS_TETHYS_OIDC_USER_END_POINT_URI}#g" /sss.app/sss.conf.yaml &&

java -jar -Dlog4j.configuration=file:log4j.properties sss.jar

read

exec "$@"