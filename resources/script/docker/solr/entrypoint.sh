#!/bin/bash

echo "starting Solr ..."
mkdir /opt/solr/server/solr/sss/
mkdir /opt/solr/server/solr/sss/conf/
cp -r /solr/core/. /opt/solr/server/solr/sss/conf/
/opt/solr/bin/solr -e techproducts
exec "$@"