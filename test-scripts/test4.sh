#!/bin/sh

curl localhost:8080/clear
curl localhost:8080/add -d name=wire -d quantity=5 -d paidPrice=2 -d sellingPrice=15
curl localhost:8080/add -d name=wire -d quantity=5 -d paidPrice=3 -d sellingPrice=15

curl localhost:8080/checkInventoryUpperBound -d quantity=10
printf "\n"
curl localhost:8080/checkInventoryLowerBound -d quantity=10
printf "\n"
curl localhost:8080/individualItemFilterLowerBound -d profit=10
printf "\n"
curl localhost:8080/individualItemFilterUpperBound -d profit=12
printf "\n"
