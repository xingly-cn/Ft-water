FROM java:8
LABEL authors="方糖科技"
MAINTAINER 方糖<admin@asugar.cn>

ADD ruoyi-admin/target/ruoyi-admin.jar /app.jar

CMD java -jar app.jar