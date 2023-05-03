FROM openjdk:8

LABEL authors="方糖科技"

LABEL maintainer="admin@asugar.cn

LABEL version="1.0"

EXPOSE 8080/tcp

ADD ruoyi-admin/target/ruoyi-admin.jar /app.jar

CMD java -jar app.jar