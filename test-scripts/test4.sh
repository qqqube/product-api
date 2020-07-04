#!/bin/sh

curl localhost:8080/product/clear
curl localhost:8080/product/add -d name=wire -d quantity=5 -d paidPrice=2 -d sellingPrice=15
curl localhost:8080/product/add -d name=wire -d quantity=5 -d paidPrice=3 -d sellingPrice=15

curl localhost:8080/product/checkInventoryUpperBound -d quantity=10
printf "\n"
curl localhost:8080/product/checkInventoryLowerBound -d quantity=10
printf "\n"
curl localhost:8080/product/individualItemFilterLowerBound -d profit=10
printf "\n"
curl localhost:8080/product/individualItemFilterUpperBound -d profit=12
printf "\n"
