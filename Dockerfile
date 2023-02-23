FROM openjdk:17
EXPOSE 8080
ADD target/dockertestproject.jar dockertestproject.jar
ENTRYPOINT ["java","-jar","/dockertestproject.jar"]