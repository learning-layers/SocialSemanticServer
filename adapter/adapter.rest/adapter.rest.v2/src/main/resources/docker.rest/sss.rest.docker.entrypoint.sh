#!/bin/bash

sed -i "s#SSS_HOST#${SSS_HOST}#g" /sss.adapter.rest.v2.conf.yaml &&
sed -i "s#SSS_PORT#${SSS_PORT}#g" /sss.adapter.rest.v2.conf.yaml &&

mv /sss.adapter.rest.v2.conf.yaml /usr/local/tomcat/conf/
mv /sss.adapter.rest.v2.war /usr/local/tomcat/webapps/

exec "$@"