# Overview

PLEASE don't judge me too harshly, this repo is only shared as a tool in case other people want to learn from my mistakes. Its only probably useful if it helps someone resolve similar errors, it should not be used as model to build off of. 

This Spring Boot service was created as a learning tool to help me better understand Spring Boot and the Redis streams (using the spring-data-redis package with Lettuce).

Critical components:
* Kotlin 1.4.32 with Java 11
* spring-boot-starter-data-redis 2.6.3 with spring-data-redis 2.6.1
* lettuce-core 6.1.6

### Helpful Commands

* Use redis-cli on the docker container `docker exec -it <CONTAINER ID> sh`
* Add to stream `XADD mystream * sensor-id 1234 temperature 14.0`
* Create a group for the stream `XGROUP CREATE mystream mygroup $`

Maven Commands
```bash
# To Construct the artifact.
mvn clean package

# To run the JAR we created in the previous mvn package.
java -jar target/StreamConsumerDemo2-0.0.1-SNAPSHOT.jar 
```


# Docker Compose

I used the following docker compose script to setup a local environment that had Redis with streams capability.
It also had redisinsight to service as a visual tool for interacting with the stream.

`docker-compose.yml`
```yaml
version: "3.9"
# https://docs.docker.com/compose/compose-file/compose-versioning/

services:

  redis:
    # Reference:
    #   https://hub.docker.com/_/redis
    hostname: redis
    image: "redis:alpine"
    ports:
      - "6379:6379"

  redisinsight:
    # Reference:
    #   https://docs.redis.com/latest/ri/installing/install-docker/
    #
    # REMEMBER - to connect to the redis database, use the host: "redis"
    image: "redislabs/redisinsight:latest"
    ports:
      - "8001:8001"

```