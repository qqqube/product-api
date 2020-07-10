#!/bin/sh

curl -XDELETE localhost:8080/product/clear
curl -XPOST localhost:8080/product/add -d name=wire -d quantity=5 -d paidPrice=2 -d sellingPrice=15
curl -XPOST localhost:8080/product/add -d name=wire -d quantity=5 -d paidPrice=3 -d sellingPrice=15

curl -XGET "localhost:8080/product/checkInventoryUpperBound?quantity=10"
printf "\n"
curl -XGET "localhost:8080/product/checkInventoryLowerBound?quantity=10"
printf "\n"
curl -XGET "localhost:8080/product/individualItemFilterLowerBound?profit=10"
printf "\n"
curl -XGET "localhost:8080/product/individualItemFilterUpperBound?profit=12"
printf "\n"
