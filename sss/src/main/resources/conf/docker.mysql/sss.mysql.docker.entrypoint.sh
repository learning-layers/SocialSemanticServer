#!/bin/bash

	mysql_install_db --user=mysql --datadir=/var/lib/mysql
       
       mysqld &
       sleep 5

	mysql -uroot -e "CREATE USER 'sss'@'%' IDENTIFIED BY 'sss'"
	mysql -uroot -e "GRANT ALL PRIVILEGES ON *.* TO 'sss'@'%' WITH GRANT OPTION"
	mysql -uroot -e "FLUSH PRIVILEGES;"
	mysql -uroot < "/sss.schema.sql"

       mysqladmin -uroot shutdown
       sleep 5

chown -R mysql:mysql /var/lib/mysql

exec "$@"