#!/bin/bash

PASSWORD=`head -c 12 /dev/random | base64`

# create database
mysql $1 -h mysql -Bse "CREATE DATABASE $3; create user $5; grant all on $3.* to '$5'@'%' identified by '$PASSWORD';"

echo "You can now connect to the MySQL container from linked containers by using:"
echo "mysql -u$5 -p$PASSWORD -hmysql"