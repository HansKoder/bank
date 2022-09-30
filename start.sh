docker-compose down
docker rmi eureka:latest
docker rmi gateway:latest
docker rmi ms-customer:latest

mvn clean install -f ./eureka/pom.xml
mvn clean install -f ./gateway/pom.xml
mvn clean install -f ./ms-customers/pom.xml

docker build -t eureka:latest -f ./eureka/Dockerfile ./eureka
docker build -t gateway:latest -f ./gateway/Dockerfile ./gateway
docker build -t ms-customer:latest -f ./ms-customers/Dockerfile ./ms-customers

docker-compose up -d

echo "Docker compose up. Waiting to run migrations"
sleep 10

mvn flyway:migrate -f ./ms-customers/pom.xml

echo "Migrations completed. Process finished."