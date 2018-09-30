FROM java:8-jre-alpine
ARG JAR_FILE
COPY ${JAR_FILE} /root
WORKDIR /root
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","${JAR_FILE}"]
