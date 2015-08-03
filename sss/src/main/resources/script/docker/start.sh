#!/bin/bash

# SSS Docker deployment script including MySQL data, MySQL server, Tomcat Server, SSS MySQL schema, SSS Tomcat config, SSS Tomcat REST API, SSS Java application
# make sure to remove priviously created containers first
# or delete ALL existing containers with:
docker rm -f $(docker ps -a -q);

MYSQL_ROOT_USERNAME="root"
MYSQL_ROOT_PASSWORD="sss";
SSS_MYSQL_USERNAME="sss";
SSS_MYSQL_SCHEME="sss";
LAYERS_API_URI="https://api.learning-layers.eu";

SSS_HOST=$(ifconfig  eth0 | awk '/inet addr/{print substr($2,6)}');
SSS_PORT="8390";
SSS_PORT_FOR_TOMCAT="8391";
SSS_MYSQL_HOST=$(ifconfig  eth0 | awk '/inet addr/{print substr($2,6)}');
SSS_MYSQL_PORT="3307";
SSS_AUTH_TYPE="csvFileAuth";
SSS_TETHYS_USER="SSSUser";
SSS_TETHYS_PASSSWORD="f74UH~X#WVQ";
SSS_TETHYS_LAS_USER="sss";
SSS_TETHYS_LAS_PASSWORD="ssstest";
SSS_TETHYS_OIDC_CONF_URI="$LAYERS_API_URI/o/oauth2/.well-known/openid-configuration";
SSS_TETHYS_OIDC_USER_END_POINT_URI="$LAYERS_API_URI/o/oauth2/userinfo";

echo "Start SSS MySQL data volume"
docker run --name sss.mysql.data dtheiler/sss.mysql.data
echo " -> done"
echo ""

echo "Start SSS MySQL server"
docker run -d -p 3307:3306 -e "MYSQL_ROOT_PASSWORD=$MYSQL_ROOT_PASSWORD" --volumes-from sss.mysql.data --name sss.mysql dtheiler/sss.mysql
echo " -> done"
echo ""

echo "Wait 8 seconds for MySql"
echo ""
sleep 8

echo "Start SSS Tomcat server"
docker run -d -p 8081:8080 --name sss.tomcat dtheiler/sss.tomcat
echo " -> done"
echo ""

echo "Create SSS database and user"
SSS_MYSQL_PASSWORD=`docker run --link sss.mysql:mysql dtheiler/sss.mysql.create -p$MYSQL_ROOT_PASSWORD --new-database $SSS_MYSQL_SCHEME --new-user $SSS_MYSQL_USERNAME | grep "mysql" | awk '{split($0,a," "); print a[3]}' | cut -c3-`
echo " -> done"
echo ""

echo "Start SSS"
docker run -d \
-e "SSS_HOST=$SSS_HOST" \
-e "SSS_PORT=$SSS_PORT" \
-e "SSS_PORT_FOR_TOMCAT=$SSS_PORT_FOR_TOMCAT" \
-e "SSS_MYSQL_HOST=$SSS_MYSQL_HOST" \
-e "SSS_MYSQL_PORT=$SSS_MYSQL_PORT" \
-e "SSS_MYSQL_USERNAME=$SSS_MYSQL_USERNAME" \
-e "SSS_MYSQL_PASSWORD=$SSS_MYSQL_PASSWORD" \
-e "SSS_MYSQL_SCHEME=$SSS_MYSQL_SCHEME" \
-e "SSS_AUTH_TYPE=$SSS_AUTH_TYPE" \
-e "SSS_TETHYS_USER=$SSS_TETHYS_USER" \
-e "SSS_TETHYS_PASSSWORD=$SSS_TETHYS_PASSSWORD" \
-e "SSS_TETHYS_LAS_USER=$SSS_TETHYS_LAS_USER" \
-e "SSS_TETHYS_LAS_PASSWORD=$SSS_TETHYS_LAS_PASSWORD" \
-e "SSS_TETHYS_OIDC_CONF_URI=$SSS_TETHYS_OIDC_CONF_URI" \
-e "SSS_TETHYS_OIDC_USER_END_POINT_URI=$SSS_TETHYS_OIDC_USER_END_POINT_URI" \
-p 8391:8390 --link sss.mysql:mysql --volumes-from sss.tomcat --volumes-from sss.mysql.data --name sss.sss dtheiler/sss.sss
echo " -> done"
echo ""