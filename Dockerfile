# --- Etapa 1: Compilação (Build) ---
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /build

# 1. Copia o pom.xml primeiro para aproveitar o cache das dependências do Maven
COPY pom.xml .
RUN mvn dependency:go-offline

# 2. Copia o código-fonte e compila gerando o arquivo .jar
COPY src ./src
RUN mvn clean package -Dmaven.test.skip=true

# --- Etapa 2: Execução ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia o .jar gerado na etapa anterior para a imagem final
COPY --from=builder /build/target/*.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Executa a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
