### Description
Coding challenge for Ultimate.io

###Prerequisites
1. Installed docker
2. Installed docker-compose

###Build and Run Reference
You can build and run application via `./buildAndUp.sh`

To build a Web App Docker Image use the following command

`docker build -t web-server-task .`

To build a Mongo Seeding Docker Image use the following command

`docker build -t mongo-seed ./mongo-seed`

To Up MongoDB service and Web App use docker-compose command

`docker-compose up`

####Swagger:
`http://localhost:9000/swagger-ui.html`

