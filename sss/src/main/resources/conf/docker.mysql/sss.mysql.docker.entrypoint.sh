#!/bin/bash

       sed -i "s#SSS_MYSQL_SCHEME#${SSS_MYSQL_SCHEME}#g" /sss.schema.sql &&

	mysql_install_db --user=mysql --datadir=/var/lib/mysql
       
       mysqld &
       sleep 5

	mysql -uroot -e "CREATE USER '$SSS_MYSQL_USERNAME'@'%' IDENTIFIED BY '$SSS_MYSQL_PASSWORD'"
	mysql -uroot -e "GRANT ALL PRIVILEGES ON *.* TO '$SSS_MYSQL_USERNAME'@'%' WITH GRANT OPTION"
	mysql -uroot -e "FLUSH PRIVILEGES;"
	mysql -uroot < "/sss.schema.sql"

       mysqladmin -uroot shutdown
       sleep 5

chown -R mysql:mysql /var/lib/mysql

exec "$@"