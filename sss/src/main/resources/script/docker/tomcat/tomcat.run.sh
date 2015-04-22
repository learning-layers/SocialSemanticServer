echo starting SSS Tomcat container ...
docker stop sss.tomcat
docker rm sss.tomcat
docker run \
-d \
-p 8390:8080 \
--name sss.tomcat \
dtheiler/sss.tomcat
echo done --> starting SSS Tomcat container ...
