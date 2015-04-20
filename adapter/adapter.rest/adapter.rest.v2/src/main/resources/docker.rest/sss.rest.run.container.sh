echo start sss rest container...
docker stop sss.rest
docker rm sss.rest
docker run \
-d \
-e "SSS_HOST=sss_host" \
-e "SSS_PORT=8391" \
-p 8084:8080 \
--name sss.rest \
dtheiler/sss.rest
echo sss rest container started