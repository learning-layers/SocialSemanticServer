echo run sss container
docker stop sss.sss
docker rm sss.sss
docker run -p 8391:8390 --name sss.sss ssss/sss.sss