FROM openjdk:8

LABEL authors="方糖科技"

LABEL maintainer="admin@asugar.cn"

EXPOSE 8080/tcp
EXPOSE 6379/tcp

ADD ruoyi-admin/target/ruoyi-admin.jar app.jar

CMD java -jar app.jar