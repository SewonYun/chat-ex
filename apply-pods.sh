#!/bin/bash

kubectl delete deployment --all
kubectl delete service --all

kubectl apply -f zookeeper.yml
kubectl apply -f kafka-cluster.yml
kubectl apply -f datastore.yml
kubectl apply -f redis.yml
kubectl apply -f consumproducer.yml
kubectl apply -f front.yml

