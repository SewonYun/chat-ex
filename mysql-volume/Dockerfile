FROM mysql:latest

COPY . /mysql/initdb.d:/docker-entrypoint-initdb.d

ENV MYSQL_ROOT_PASSWORD=1Q2W3E4R_TEST
ENV MYSQL_DATABASE=chat_exc
ENV MYSQL_USER=chat_admin
ENV MYSQL_PASSWORD=1q2w3e4r

EXPOSE 3306
