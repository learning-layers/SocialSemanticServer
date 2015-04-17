echo run mysql container
docker stop sss.mysql
docker rm sss.mysql
docker run -d -p 3333:3306 --name sss.mysql ssss/sss.mysql