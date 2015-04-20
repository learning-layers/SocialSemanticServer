echo start sss container...
docker stop sss.sss
docker rm sss.sss
docker run \
-d \
-e "SSS_MYSQL_HOST=db_host" \
-e "SSS_MYSQL_PORT=3333" \
-e "SSS_MYSQL_USERNAME=sss" \
-e "SSS_MYSQL_PASSWORD=sss" \
-e "SSS_MYSQL_SCHEME=sss" \
-e "SSS_AUTH_TYPE=csvFileAuth" \
-e "SSS_TETHYS_USER=tethys_user" \
-e "SSS_TETHYS_PASSSWORD=tethys_password" \
-e "SSS_TETHYS_LAS_USER=sss" \
-e "SSS_TETHYS_LAS_PASSWORD=sss" \
-e "SSS_TETHYS_OIDC_CONF_URI=https://api.learning-layers.eu/o/oauth2/.well-known/openid-configuration" \
-e "SSS_TETHYS_OIDC_USER_END_POINT_URI=https://api.learning-layers.eu/o/oauth2/userinfo" \
-p 8391:8390 \
--name sss.sss \
dtheiler/sss.sss
echo sss container started