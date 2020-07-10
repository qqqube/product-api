#!/bin/sh

curl -XDELETE localhost:8080/product/clear
curl -XPOST localhost:8080/product/add -d name=wire -d quantity=5 -d paidPrice=2 -d sellingPrice=15
curl -XPOST localhost:8080/product/add -d name=wire -d quantity=5 -d paidPrice=3 -d sellingPrice=15
curl -XPOST localhost:8080/product/makePurchaseId -d id=1 -d quantity=4
curl -XGET "localhost:8080/product/totalProfit"
#52
printf "\n"
curl -XPOST localhost:8080/product/makePurchaseId -d id=1 -d quantity=1
curl -XGET "localhost:8080/product/totalProfit"
#65
printf "\n"
curl -XGET "localhost:8080/product/averageProfitPerPurchase"
#32.5
printf "\n"
curl -XGET "localhost:8080/product/stdProfitPurchase"
#19.5
printf "\n"
curl -XGET "localhost:8080/product/averagePaidPricePurchase"
#5
printf "\n"
curl -XGET "localhost:8080/product/stdPaidPricePurchase"
#3
printf "\n"
