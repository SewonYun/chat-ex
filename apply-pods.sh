#!/bin/bash

kubectl delete deployment --all
kubectl delete service --all

kubectl apply -f zookeeper.yml
kubectl apply -f kafka-cluster.yml
kubectl apply -f producer.yml
kubectl apply -f consumer.yml

