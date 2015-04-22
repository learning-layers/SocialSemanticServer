echo starting SSS container ...
docker stop sss.sss
docker rm sss.sss
docker run \
-d \
-e "MYSQL_ROOT_USERNAME=sss" \
-e "MYSQL_ROOT_PASSWORD=sss" \
-e "SSS_HOST=host_with_out_protocol" \
-e "SSS_PORT=8390" \
-e "SSS_PORT_FOR_TOMCAT=8391" \
-e "SSS_MYSQL_HOST=host_with_out_protocol" \
-e "SSS_MYSQL_PORT=3333" \
-e "SSS_MYSQL_USERNAME=sss" \
-e "SSS_MYSQL_PASSWORD=sss" \
-e "SSS_MYSQL_SCHEME=sss" \
-e "SSS_AUTH_TYPE=csvFileAuth" \
-e "SSS_TETHYS_USER=sss_tethys_user" \
-e "SSS_TETHYS_PASSSWORD=sss_tethys_password" \
-e "SSS_TETHYS_LAS_USER=sss_las_user" \
-e "SSS_TETHYS_LAS_PASSWORD=sss_las_password" \
-e "SSS_TETHYS_OIDC_CONF_URI=https://api.learning-layers.eu/o/oauth2/.well-known/openid-configuration" \
-e "SSS_TETHYS_OIDC_USER_END_POINT_URI=https://api.learning-layers.eu/o/oauth2/userinfo" \
-p 8391:8390 \
--link sss.mysql:mysql \
--volumes-from sss.tomcat \
--name sss.sss \
dtheiler/sss.sss
echo done --> starting SSS container ...