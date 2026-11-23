# No.8: WAR をビルドし、外部 Tomcat 11 に ROOT として配置する
FROM maven:3.10-eclipse-temurin-17 AS build

WORKDIR /build
COPY app/pom.xml app/pom.xml
COPY app/src app/src
RUN mvn -f app/pom.xml package -DskipTests

FROM tomcat:11.0-jdk17-temurin

COPY --from=build /build/app/target/mira.war ${CATALINA_HOME}/webapps/ROOT.war