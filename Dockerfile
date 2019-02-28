FROM java:8
EXPOSE 8080

VOLUME /tmp
ADD MK-II-1.0.0-release.jar /app.jar

#设置时区
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo 'Asia/Shanghai' >/etc/timezone \

RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-jar","/app.jar"]
