echo start sss tomcat container...
docker stop sss.tomcat
docker rm sss.tomcat
docker run \
-d \
-p 8390:8080 \
--name sss.tomcat \
dtheiler/sss.tomcat
echo sss tomcat container started