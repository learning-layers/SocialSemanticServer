#!/bin/bash

mv /sss.adapter.rest.v2.conf.yaml /usr/local/tomcat/conf/
mv /sss.adapter.rest.v2.war /usr/local/tomcat/webapps/

exec "$@"