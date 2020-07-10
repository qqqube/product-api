#!/bin/sh

curl -XDELETE localhost:8080/product/clear


curl -XPOST localhost:8080/product/add -d name=wire1 -d quantity=5 -d paidPrice=10 -d sellingPrice=15
curl -XPOST localhost:8080/product/add -d name=wire2 -d quantity=6 -d paidPrice=11 -d sellingPrice=15
curl -XPOST localhost:8080/product/add -d name=wire3 -d quantity=2 -d paidPrice=10 -d sellingPrice=20
curl -XPOST localhost:8080/product/add -d name=wire4 -d quantity=8 -d paidPrice=20 -d sellingPrice=66

curl -XGET "localhost:8080/product/expectedProfitFromProductName?name=wire1"
printf "\n"
#expect 25
curl -XGET "localhost:8080/product/expectedProfit"
printf "\n"
#expect 437

curl -X PATCH "localhost:8080/product/updateName?name=wire1&newName=fancyWire"
curl -XGET "localhost:8080/product/expectedProfitFromProductName?name=fancyWire"
printf "\n"
#expect 25
curl -XGET "localhost:8080/product/expectedProfit"
printf "\n"
#expect 437
