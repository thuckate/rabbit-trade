##### create mysql from docker
docker run -p 3306:3306 --name  basicauth -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=basicauth -d mysql
