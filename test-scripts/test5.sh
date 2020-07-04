#!/bin/sh

curl localhost:8080/clear
curl localhost:8080/add -d name=wire -d quantity=5 -d paidPrice=2 -d sellingPrice=15
curl localhost:8080/add -d name=wire -d quantity=5 -d paidPrice=3 -d sellingPrice=15
curl localhost:8080/makePurchaseId -d id=1 -d quantity=4
curl localhost:8080/totalProfit
#52
printf "\n"
curl localhost:8080/makePurchaseId -d id=1 -d quantity=1
curl localhost:8080/totalProfit
#65
printf "\n"
curl localhost:8080/averageProfitPerPurchase
#32.5
printf "\n"
curl localhost:8080/stdProfitPurchase
#19.5
printf "\n"
curl localhost:8080/averagePaidPricePurchase
#5
printf "\n"
curl localhost:8080/stdPaidPricePurchase
#3
printf "\n"
