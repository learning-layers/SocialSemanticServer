echo start knowbrain container...
docker stop sss.knowbrain
docker rm sss.knowbrain
docker run \
-d \
-e "SSS_CLIENT_SIDE_LIB_PATH=http://kcs-evolution.know.know-center.at:8390/sssclientside" \
--volumes-from sss.tomcat \
--name sss.knowbrain \
dtheiler/sss.knowbrain
echo knowbrain container started