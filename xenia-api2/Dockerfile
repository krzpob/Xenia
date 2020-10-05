FROM openjdk:13-jdk-slim
VOLUME /tmp
ARG JAR_FILE
RUN addgroup --gid 504 xeniagroup
RUN adduser --home /home/xenia-api --uid 504 --ingroup xeniagroup --disabled-password --gecos "" xenia-api
RUN mkdir /var/log/xenia-api
RUN chown -R xenia-api /home/xenia-api /var/log/xenia-api
USER xenia-api
WORKDIR /home/xenia-api
EXPOSE 8080
ADD target/${JAR_FILE} /home/seashell-scylla/app.jar
ENTRYPOINT ["java" ,"-XX:+UseG1GC" ,"-Xmx1024m" \
,"-Djava.security.egd=file:/dev/./urandom ","-jar" ,"/home/xenia-api/app.jar" ]
