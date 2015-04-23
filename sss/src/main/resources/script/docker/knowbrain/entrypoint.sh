#!/bin/bash

echo "extracting KnowBrain package ..."
cd ./
unzip /knowbrain.zip
echo "done --> extracting KnowBrain package"

echo "setting SSS client side lib path for KnowBrain ..."
cd /knowbrain/
sed -i "s#SSS_CLIENT_SIDE_LIB_PATH#${SSS_CLIENT_SIDE_LIB_PATH}#g" ./index.html
echo "done --> setting SSS client side lib path for KnowBrain ..."

echo "deploying KnowBrain to Tomcat ..."
cd ..
cp -r ./knowbrain /usr/local/tomcat/webapps/
echo "done --> deploying KnowBrain to Tomcat ..."

exec "$@"