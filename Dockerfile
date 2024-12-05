FROM eclipse-temurin:23-jre
WORKDIR /app

# Copiar o .jar da etapa de build
COPY target/tcc-0.0.1-SNAPSHOT.jar /app/app.jar

# Expor a porta da aplicação
EXPOSE 8080

# Comando para iniciar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
