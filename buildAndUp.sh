#!/bin/bash

docker build -t web-server-task .

docker build -t mongo-seed ./mongo-seed

docker-compose up