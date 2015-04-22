#!/bin/bash

	mysql_install_db --user=mysql --datadir=/var/lib/mysql
       
       mysqld &
       sleep 5

	mysql -uroot -e "CREATE USER '$MYSQL_ROOT_USERNAME'@'%' IDENTIFIED BY '$MYSQL_ROOT_PASSWORD'"
	mysql -uroot -e "GRANT ALL PRIVILEGES ON *.* TO '$MYSQL_ROOT_USERNAME'@'%' WITH GRANT OPTION"
	mysql -uroot -e "FLUSH PRIVILEGES;"

       mysqladmin -uroot shutdown
       sleep 5

chown -R mysql:mysql /var/lib/mysql

exec "$@"