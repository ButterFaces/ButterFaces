FROM frolvlad/alpine-oraclejdk8:slim
VOLUME /tmp
ADD showcase-swarm.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS="-Djava.net.preferIPv4Stack=true"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app.jar" ]