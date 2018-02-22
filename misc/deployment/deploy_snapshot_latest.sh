#!/bin/bash
# Loads latest docker image, stops old version and starts latest version

echo "### Start:       Pull latest docker image"
sudo docker pull butterfaces/showcase-snapshot
echo "### Finished:    Pull latest docker image"

echo "### Start:       Stopping actual running application"
sudo docker stop butterfaces-showcase-snapshot
echo "### Finished:    Stopping actual running application"

echo "### Start:       Removing old docker image"
sudo docker rm butterfaces-showcase-snapshot
echo "### Finished:    Removing old docker image"

echo "### Start:       Starting application"
sudo docker run -d --restart always -p 8085:8080 --name butterfaces-showcase-snapshot butterfaces/showcase-snapshot
echo "### Finished:    Removing application"