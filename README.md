# Positions

Import open positions from third-party APIs and perform searches over the data.

## Overview

This application consumes open positions from different **providers** (right now only Github is supported), and store them in a local database. 
Then, different **clients** that are doing recruitment can get the available positions from this dataset, and start to look for people.
Each **client** has its own **hunt**, which represents what they are looking for. For instance, a client A who is looking for a "Software Engineer" in "New York", has an active **hunt** with those parameters. 
Each client has its access token with a `client` role, and they are able to see only the positions matching their hunts. 

A security role `admin` is available to perform write operations, such creating new clients, adding new providers and importing positions from providers.

## Getting started

1. Package the application

    `mvn clean package`

2. Run the application

    `java -jar target/positions-1.0-SNAPSHOT.jar`

3. Import the data for test

    `./init.sh $(cat tokens/admin.token)`

4. Get the open positions

    `curl "http://localhost:8080/positions" -H "Authorization: Bearer <client-token>"`

    For instance, to get the positions for client "Free To Choose":

    `curl "http://localhost:8080/positions" -H "Authorization: Bearer $(cat tokens/free_to_choose.token)"`

    Or you can use swagger: http://localhost:8080/swagger-ui.html

    In that case, you need to add the authorization as `Bearer <token>`.

## Dependencies

* Spring Boot.
* H2 for SQL database.
* Spring JPA & Hibernate for ORM.
* Lucene for Full Text Search support.
* Swagger for API Documentation.
* Spring Security & JWT for authorization.

## Improvements for Production

* Support users & OAuth.
* Use a search engine like Solr or Elasticsearch instead of Lucene.
* Use a database like MySQL or PostgreSQL instead of H2.