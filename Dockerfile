FROM alpine:latest
LABEL MAINTAINER="Arkajyoti Nag(arka.imps@gmail.com)"
EXPOSE 3001
RUN apk update
RUN apk --no-cache add openjdk8
RUN apk --no-cache add maven
WORKDIR /demo-user-service
COPY target/user-service.jar /demo-user-service/target/user-service.jar
CMD ["java","-jar","/demo-user-service/target/user-service.jar"]

