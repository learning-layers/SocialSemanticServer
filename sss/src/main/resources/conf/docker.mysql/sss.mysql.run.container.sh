echo start sss mysql container...
docker stop sss.mysql
docker rm sss.mysql
docker run \
-d \
-e "SSS_MYSQL_USERNAME=mysql_username" \
-e "SSS_MYSQL_PASSWORD=mysql_password" \
-e "SSS_MYSQL_SCHEME=sss" \
-p 3333:3306 \
--name sss.mysql \
dtheiler/sss.mysql
echo started sss mysql container
