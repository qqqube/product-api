#!/bin/sh

curl localhost:8080/clear


curl localhost:8080/add -d name=wire1 -d quantity=5 -d paidPrice=10 -d sellingPrice=15
curl localhost:8080/add -d name=wire2 -d quantity=6 -d paidPrice=11 -d sellingPrice=15
curl localhost:8080/add -d name=wire3 -d quantity=2 -d paidPrice=10 -d sellingPrice=20
curl localhost:8080/add -d name=wire4 -d quantity=8 -d paidPrice=20 -d sellingPrice=66


curl localhost:8080/expectedProfitFromProductName -d name=wire1
printf "\n"
#expect 25
curl localhost:8080/expectedProfit
printf "\n"
#expect 437

curl localhost:8080/updateName -d name=wire1 -d newName=fancyWire
curl localhost:8080/expectedProfitFromProductName -d name=fancyWire
printf "\n"
#expect 25
curl localhost:8080/expectedProfit
printf "\n"
#expect 437
