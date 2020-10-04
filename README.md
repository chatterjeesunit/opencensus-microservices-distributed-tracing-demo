# Distributed Tracing in Spring Boot Microservices using OpenCensus Agent(with Jaeger or Zipkin)

## Demo App Architecture
![Architecture](/resources/OpenCensusSpringBootDemo.png)


## Jaeger / Zipkin Setup
 * A `docker-compose.yml` file is provided that will set up the Jaeger and Zipkin.
 * Both the tools can be run together, and application can send traces to both of them.
 * Access Jaeger UI on - http://localhost:16686/search
 * Acces Zipkin UI on - http://localhost:9411/zipkin/
<br>

## Open Census Agent Setup
 * The `docker-compose.yml` also contains setup for OpenCensus Agent
 * The configuration for OpenCensus Agent is located at - `/resources/docker/ocagent-config.yaml`
 * It is configured to recieve OpenCensus Traces from application, and export traces to Zipkin and Jaeger tools.
<br>


## Running the application

##### Pre-requisite
 - Docker
 - Java 11
 - Gradle
 
##### First Time Setup
Do this if you are starting the application for first time
    
   `rm -rf ~/data/opencensus-pgres-13`
    
   `mkdir -p ~/data/opencensus-pgres-13`
 
 
   
##### Run the application
 
 - Build
    
    `./gradlew clean build --console=plain`
    
 - Run
 
    `docker-compose up -d --build`
    
   
## Initial Data Creation
Connect to PostGres (username: `pgadmin`, password: `pgadmin#123`), and run the sql scripts in `resources/dummy-data-scripts` folder to create the dummy data for this application.

 * `Customer-DummyData.sql`  - Run on `customerdb`
 * `Orders-DummyData.sql`    - Run on `orderdb`
 * `Products-DummyData.sql`  - Run on `productdb`


## PostGres Admin
To connect to postgres from your host machine, you can install `pgAdmin`

`docker run --name postgres-admin -p 5000:80 -e "PGADMIN_DEFAULT_EMAIL=admin@dba.com" -e "PGADMIN_DEFAULT_PASSWORD=admin" -d dpage/pgadmin4`

Connect to pgAdmin at http://localhost:5000, and login using the admin credentials provided in docker run command.




## SAMPLE REST END POINTS

 * Get all customers
 ```http://localhost/api/v1/bff/customers?pageNum=0&pageSize=10```

 * Get All Orders for a customer 
  ```http://localhost/api/v1/bff/customer/5c0e7fe0-f062-11ea-9298-0242c0a88003/orders```
 

## PENDING WORK
 * Enable SQL Tracing