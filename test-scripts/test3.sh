#!/bin/sh

curl localhost:8080/product/clear

curl localhost:8080/product/add -d name=wire -d quantity=5 -d paidPrice=2 -d sellingPrice=15
curl localhost:8080/product/add -d name=wire -d quantity=5 -d paidPrice=3 -d sellingPrice=15

curl localhost:8080/product/expectedProfitMax -d quantity=6
printf "\n"
curl localhost:8080/product/expectedProfitMin -d quantity=6
printf "\n"
