# using open jdk
# https://github.com/docker-library/openjdk/tree/master/8-jdk

# with memory settings are using container limits
# https://developers.redhat.com/blog/2017/03/14/java-inside-docker/

FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
ADD target/${JAR_FILE} app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS="-Djava.net.preferIPv4Stack=true -XX:+PrintFlagsFinal -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app.jar" ]