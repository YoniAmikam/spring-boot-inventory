# spring-boot-inventory
Spring-boot application with a REST controller, which expose a Swagger API catalog.
# Built With

he following technologies:

  - Java
  - Spring boot
  - Swagger
  - REST API/JSON
  - Docker
  
The application expose the following APIs:

  - List of the inventory items list (item no, name, amount, inventory code)

  - Read item details (by item no)

  - Withdrawal quantity of a specific item from stock

  - Deposit quantity of a specific item to stock

  - Add item to stock

  - Delete an item from stock

Data persisted on H2 DB using JPA.

The application pack and run from docker.

You can use swagger-ui to see the API and Postman to see the resposes and the inventory.

The docker image: https://hub.docker.com/repository/docker/yoniamikam/docker-spring-boot

Pull comamnd: docker pull yoniamikam/docker-spring-boot:v1.0
