#!/bin/sh

curl localhost:8080/product/clear
curl localhost:8080/product/add -d name=wire -d quantity=1 -d paidPrice=10 -d sellingPrice=15
curl localhost:8080/product/add -d name=wire -d quantity=1 -d paidPrice=11 -d sellingPrice=15
curl localhost:8080/product/averageSellingPriceProduct
#15.0
printf "\n"
curl localhost:8080/product/stdSellingPriceProduct
#0.0
printf "\n"
curl localhost:8080/product/averagePaidPriceProduct
#10.5
printf "\n"
curl localhost:8080/product/stdPaidPriceProduct
#0.5
printf "\n"
