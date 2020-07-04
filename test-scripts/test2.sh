#!/bin/sh

curl localhost:8080/product/clear

curl localhost:8080/product/add -d name=wire -d quantity=5 -d paidPrice=10 -d sellingPrice=15
curl localhost:8080/product/add -d name=wire -d quantity=4 -d paidPrice=6 -d sellingPrice=15
curl localhost:8080/product/add -d name=wire -d quantity=2 -d paidPrice=9 -d sellingPrice=15
curl localhost:8080/product/listIdByProfitName -d name="wire"
printf "\n"
curl localhost:8080/product/add -d name=wire -d quantity=1 -d paidPrice=7 -d sellingPrice=15
curl localhost:8080/product/add -d name=wire -d quantity=10 -d paidPrice=14 -d sellingPrice=15
curl localhost:8080/product/listIdByProfitName -d name="wire"
printf "\n"
