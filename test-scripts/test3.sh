#!/bin/sh

curl -XDELETE localhost:8080/product/clear

curl -XPOST localhost:8080/product/add -d name=wire -d quantity=5 -d paidPrice=2 -d sellingPrice=15
curl -XPOST localhost:8080/product/add -d name=wire -d quantity=5 -d paidPrice=3 -d sellingPrice=15

curl -XGET "localhost:8080/product/expectedProfitMax?quantity=6"
printf "\n"
curl -XGET "localhost:8080/product/expectedProfitMin?quantity=6"
printf "\n"
