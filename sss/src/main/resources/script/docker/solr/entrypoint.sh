#!/bin/bash

echo "starting Solr ..."
mkdir /opt/solr/server/solr/dieter/
mkdir /opt/solr/server/solr/dieter/conf/
cp -r /solr/core/. /opt/solr/server/solr/dieter/conf/
/opt/solr/bin/solr -e techproducts  -f
exec "$@"