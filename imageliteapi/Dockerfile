# Container criado (Só tem o SO)

# Instalar Maven
FROM maven:3.9.5-eclipse-temurin-17 as builder
# Criar pasta /app no container
# O comandos abaixo serão executados dentro da pasta /app
WORKDIR /app
# Copiar os arquivos do projeto e por na pasta /app
COPY . .
# Fazer download de todas dependências
RUN mvn dependency:resolve
# Buildar a aplicação
RUN mvn clean package -DskipTests=true

# Instala o jdk (Nesse caso a imagem da Amazon)
FROM amazoncorretto:17
WORKDIR /app
# Pega a pasta app do projeto (builder) | Busca o arquivo .jar | Copia e cola em um novo arquivo .jar
COPY --from=builder ./app/target/*.jar ./application.jar
# Expôe a porta que vai rodar a aplicação dentro do container
EXPOSE 8080

# Comando que inicializa a aplicação (Por os comandos dentro do array)
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=production", "application.jar"]
