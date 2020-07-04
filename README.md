#Crud API with Spring Boot using MySQL and JPA

## Database Tables
---
#### product 
`int` id [primary key, auto_increment], `varchar(255)` name, `double` paid_price, `int` quantity, `double` selling_price

#### purchase
`varchar(255)` timestamp [primary key], `double` paid_price, `int` productid, `varchar(255)` product_name, `double` profit, `int` quantity

#### returned_product
`varchar(255)` return_timestamp [primary key], `double` paid_price, `int` product_id, `varchar(255)` product_name, `int` quantity, `double` selling_price 

## Documentation
---

`/nameId`: get the name of the product with specified id  [parameters: `Integer` product-id]

`/quantityId`: get the quantity of the product with specified id [parameters: `Integer` product-id]

`/sellingPriceId`: get selling price of item with specified id [parameters: `Integer` product-id]

`/paidPriceId`: get paid price of the product with specified id [parameters: `Integer` product-id]

`/renameId`: rename the product with the given id; if the product has been purchased, the name is also updated in the purchase table [parameters: `Integer` product-id, `String` newName]

`/updateQuantityId`: change the quantity of the product with the given id [parameters: `Integer` product-id, `Integer` quantity]

`/updatePaidPriceId`: update paid price of product specified by id; if the product has been purchased, the paid prices and profits of the corresponding purchases are updated in the purchase table [parameters: `Integer` product-id, `Double` paidPrice]

`/updateSellingPriceId`: update selling price of product specified by id [parameters: `Integer` product-id, `Double` sellingPrice]

`/deleteId`: remove product with given id [parameters: `Integer` product-id]

`/makePurchaseId`: purchase specified amount of the product with the given id [parameters: `Integer` product-id, `Integer` quantity]

`/deleteName`: remove all products with the given name [parameters: `String` name]

`/updateName`: change the names of all products with the specified name; if some of the products have been purchased, update the purchase table accordingly [parameters: `String` name, `String` newName]

`/updateSellingPriceOffsetName`: change the selling price of all products with the given name by some offset [parameters: `String` name, `Integer` priceOffset]

`/expectedProfitFromProductName`: calculate profit from selling all products with specified name [parameters: `String` name]

`/productQuantityName`: return the number of products with the specified name [parameters: `String` name]

`/purchaseNameId`: get product name of purchase specified by productId [parameters: `Datetime` date]

`/purchaseProfit`: get profit of purchase specified by productId [parameters: `Datetime` date]

`/purchaseIndPaidPrice`: get individual paid price of purchase's product specified by productId [parameters: `Datetime` date]

`/purchaseQuantity`: get quantity of purchase's product, specified by productId [parameters: `Datetime` date]

`/returnPurchase`: return a purchase, specified by datetime [parameters: `Datetime` date, `Integer` quantity]

`/return ProductID`: get productId, provided the return's timestamp [`String` date]

`/returnProductName`: return product name, provided the return's timestamp [parameters: `String` date]

`/returnQuantity`: return product quantity, provided the return's timestamp [parameters: `String` date]

`/returnSellingPrice`: return selling price, provided the return's timestamp [parameters: `String` date]

`/returnPaidPrice`: return paid price, provided the return's timestamp [parameters: `String` date]

`/processReturn`: process a return; add the product back to the product table with specified selling price [parameters: `String` date, `Double` sellingPrice]

`/removeReturn`: discards a return, specified by datetime [parameters: `String` date]

`/all`: show all products in table [no parameters]

`/add`: add product [parameters: `Integer` id, `String` name, `Integer` quantity (must be at least one), `Double` paidPrice, `Double` sellingPrice]

`/expectedProfit`: calculate profit from selling all products in table [no parameters]

`/clear`: delete all entries in the product and purchase tables; reset the product id sequence to start from 1 [no parameters]

## Sanity Checks
---
Run simple tests:

`chmod +x testDriver.sh`

`./testDriver.sh`