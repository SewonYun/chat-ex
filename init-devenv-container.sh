#!/bin/bash

# docker container for what push image to local registry 
docker run -d -p 5252:5000 --name registry registry:2
docker restart registry

source container-image-builder.sh
