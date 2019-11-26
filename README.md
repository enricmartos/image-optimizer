# image-optimizer

## Description

This REST API application is a image manipulation tool which allows you to resize images and convert documents to images.

## Personal goals

- To implement a REST API using RESTEasy
- To build the service layer integrated with an external software (ImageMagick)
- To understand configuration, deployment and execution of JBoss applications

## Core technologies

*Back-end*
- RESTEasy (implementation of JAX-RS specification)
- RESTEasy CDI (Context and Dependency Injection)

*Dependency management tool*
- Gradle

*IDE*
- IntellIJ Idea

*Containerization*
- Docker-compose

## Build setup

### With Docker

- Clone this repo to your local machine. Docker-compose version must above 1.18 (Upgrade docker-compose to the latest version)[https://stackoverflow.com/questions/49839028/how-to-upgrade-docker-compose-to-latest-version]. Don't forget to restart your shell after performing all the steps.
```
# Start docker-compose

$ docker-compose up
```


## Usage

- Postman or any other API tester must be already installed in your machine. Otherwise, you will have to install them. You can perform the requests below in order to test the application. They belong to *Image optimizer* collection, which can be easily imported to Postman with [this](https://www.getpostman.com/collections/72e499bc286888be53d9) shareable link. 

| Action | HTTP request method | Endpoint | Header param | Body example (form-data) |
| ------------- | ------------- | ------------- | ------------- | ------------- |
| Resize image to a given width and height | POST  | **/api/image/resize** | apiKey | selectedFile, width, height |
| Convert each page/slide of a document to an image | POST  | **/api/image/docToImages** | apiKey | selectedFile |


