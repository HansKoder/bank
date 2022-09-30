# Title

Software for a Bank

# Description

Crud of customers

# technologies

Microservices

It made build with Eureka - Gateway - Spring Boot - Docker


# Install and Run the Project

You should execute the following script

```
sh start.sh
```


> This script 

You should export collection of postman

```
bank.postman_collection.json
```

Get IP of container

```

docker inspect -f '{{range .NetworkSettings.Networks}}{{.IPAddress}}{{end}}'  bank_gateway_1

```

Put this IP in the postman