echo start sss solr container...
docker stop sss.solr
docker rm sss.solr
docker run \
-t \
-i \
-p 9999:8983 \
--name sss.solr \
dtheiler/sss.solr /bin/bash
echo sss solr container started