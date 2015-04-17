echo run rest container
docker stop sss.rest
docker rm sss.rest
docker run -d -p 8390:8080 --name sss.rest ssss/sss.rest