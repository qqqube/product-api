#!/bin/sh

curl localhost:8080/clear
curl localhost:8080/add -d name=wire -d quantity=1 -d paidPrice=10 -d sellingPrice=15
curl localhost:8080/add -d name=wire -d quantity=1 -d paidPrice=11 -d sellingPrice=15
curl localhost:8080/averageSellingPriceProduct
#15.0
printf "\n"
curl localhost:8080/stdSellingPriceProduct
#0.0
printf "\n"
curl localhost:8080/averagePaidPriceProduct
#10.5
printf "\n"
curl localhost:8080/stdPaidPriceProduct
#0.5
printf "\n"
