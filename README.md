# Overview #

The Docker Compose plugin is a XL Deploy plugin that adds capability for managing and deploying  Docker Compose files.
It manages to deploy `docker-compose`files directly or to import them to turn them into `Docker.Images`.

# CI status #

[![Build Status][xld-docker-compose-plugin-travis-image]][xld-docker-compose-plugin-travis-url]

[xld-docker-compose-plugin-travis-image]: https://travis-ci.org/xebialabs-community/xld-docker-compose-plugin.svg?branch=master
[xld-docker-compose-plugin-travis-url]: https://travis-ci.org/xebialabs-community/xld-docker-compose-plugin

# Installation #

Place the plugin XLDP file into your `SERVER_HOME/plugins` directory.

Dependencies:

* XL Deploy 6.0+
* XL Deploy Docker plugin 6.2.0+

# Sample Applications #

* XL Deploy Docker Sample Application https://github.com/bmoussaud/xld-petclinic-docker
* XL Deploy Docker MicroService Sample Application https://github.com/bmoussaud/xld-micropet-docker

# Deployable vs. Container Table  #

The following table describes which deployable / container combinations are possible.

| Deployables | Containers | Generated Deployed |
|-------------|------------|--------------------|
| docker.ComposeFile | docker.Machine | docker.ComposedContainers |


# Deployed Actions Table  #

The following table describes the effect a deployed has on its container.

| Deployed | Create | Destroy | Modify |
|----------|--------|---------|--------|
| docker.ComposeFile| `docker-compose up`| `docker-compose stop && docker-compose rm`  | `docker-compose stop && docker-compose rm` and `docker-compose up` |


(*) the `docker.RunContainer` generates the 'create' and the 'start' steps and sorts them based on the links between the containers.

# Docker Compose File Importer #

`docker-compose`  is a great tool but it looks like a black-box. The Docker Compose file importer allows to push `docker-compose`YAML file and to turn these information into `docker.Images` defined in the plugin.


