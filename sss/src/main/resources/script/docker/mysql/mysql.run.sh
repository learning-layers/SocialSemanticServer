echo starting SSS MySQL container ...
docker stop sss.mysql
docker rm sss.mysql
docker run \
-d \
-e "MYSQL_ROOT_USERNAME=sss" \
-e "MYSQL_ROOT_PASSWORD=sss" \
-p 3333:3306 \
--name sss.mysql \
dtheiler/sss.mysql
echo done --> starting SSS MySQL container
