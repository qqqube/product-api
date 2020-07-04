#!/bin/sh

curl localhost:8080/clear

curl localhost:8080/add -d name=wire -d quantity=5 -d paidPrice=2 -d sellingPrice=15
curl localhost:8080/add -d name=wire -d quantity=5 -d paidPrice=3 -d sellingPrice=15

curl localhost:8080/expectedProfitMax -d quantity=6
printf "\n"
curl localhost:8080/expectedProfitMin -d quantity=6
printf "\n"
