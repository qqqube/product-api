# Crud API with Spring Boot using MySQL and JPA

## Database Tables
---
#### product
`int` id [primary key, auto_increment], `varchar(255)` name, `double` paid_price, `int` quantity, `double` selling_price

#### purchase
`varchar(255)` timestamp [primary key], `double` paid_price, `int` productid, `varchar(255)` product_name, `double` profit, `int` quantity

#### returned_product
`varchar(255)` return_timestamp [primary key], `double` paid_price, `int` product_id, `varchar(255)` product_name, `int` quantity, `double` selling_price, `bigint` pr_gap


## Documentation: `/product`
---

`/nameId`: get the name of the product with specified id  [parameters: `Integer` id]

`/quantityId`: get the quantity of the product with specified id [parameters: `Integer` id]

`/sellingPriceId`: get selling price of item with specified id [parameters: `Integer` id]

`/paidPriceId`: get paid price of the product with specified id [parameters: `Integer` id]

`/renameId`: rename the product with the given id; if the product has been purchased, the name is also updated in the purchase table [parameters: `Integer` id, `String` newName]

`/updateQuantityId`: change the quantity of the product with the given id [parameters: `Integer` id, `Integer` quantity]

`/updatePaidPriceId`: update paid price of product specified by id; if the product has been purchased, the paid prices and profits of the corresponding purchases are updated in the purchase table [parameters: `Integer` id, `Double` paidPrice]

`/updateSellingPriceId`: update selling price of product specified by id [parameters: `Integer` id, `Double` sellingPrice]

`/deleteId`: remove product with given id [parameters: `Integer` id]

`/makePurchaseId`: purchase specified amount of the product with the given id [parameters: `Integer` id, `Integer` quantity]

`/deleteName`: remove all products with the given name [parameters: `String` name]

`/updateName`: change the names of all products with the specified name; if some of the products have been purchased, update the purchase table accordingly [parameters: `String` name, `String` newName]

`/updateSellingPriceOffsetName`: change the selling price of all products with the given name by some offset [parameters: `String` name, `Integer` priceOffset]

`/expectedProfitFromProductName`: calculate expected profit from selling all products with specified name [parameters: `String` name]

`/listIdByProfitName`: return list of product-id's sorted by expected profit from selling out (ascending order) [parameters: `String` name]

`/productQuantityName`: return the number of products with the specified name [parameters: `String` name]

`/purchaseNameId`: get product name of purchase specified by productId [parameters: `String` date]

`/purchaseProfit`: get profit of purchase specified by productId [parameters: `String` date]

`/purchaseIndPaidPrice`: get individual paid price of purchase's product specified by productId [parameters: `String` date]

`/purchaseQuantity`: get quantity of purchase's product, specified by productId [parameters: `String` date]

`/returnPurchase`: return a purchase, specified by datetime [parameters: `String` date, `Integer` quantity]

`/profitStart`: returns the total profit made starting from the input date [parameters: `String` date]

`/profitEnd`: takes in a date string and returns the total profit made before the input date [parameters: `String` date]

`/returnPRGap`: get purchase-return gap in days, provided the return's timestamp [`String` date]

`/returnProductID`: get productId, provided the return's timestamp [`String` date]

`/returnProductName`: return product name, provided the return's timestamp [parameters: `String` date]

`/returnQuantity`: return product quantity, provided the return's timestamp [parameters: `String` date]

`/returnSellingPrice`: return selling price, provided the return's timestamp [parameters: `String` date]

`/returnPaidPrice`: return paid price, provided the return's timestamp [parameters: `String` date]

`/processReturn`: process a return; add the product back to the product table with specified selling price [parameters: `String` date, `Double` sellingPrice]

`/removeReturn`: discards a return, specified by datetime [parameters: `String` date]

`/expectedProfitMax`: takes in a quantity and returns maximum profit from selling the specified
number of products [parameters: `Integer` quantity]

`/expectedProfitMin`: takes in a quantity and returns minimum profit from selling the specified number of items [parameters: `Integer` quantity]

`/checkInventoryLowerBound`: takes in a quantity and returns a list of productid's that have at least the specified number of items [parameters: `Integer` quantity]

`/checkInventoryUpperBound`: takes in a quantity and returns a list of productid's where each product corresponding to the product id has a quantity less than the specified number of items [parameters: `Integer` quantity]

`/individualItemFilterLowerBound`: takes in a profit value and returns a list of productid’s such that each product corresponding to the id will yield a profit that is greater than or equal to the specified value (by selling one) [parameters: `Double` profit]

`/individualItemFilterUpperBound`: takes in a profit value, and returns a list of productid’s such that each product corresponding to the id will yield a profit that is less than or equal to the specified value (by selling one) [parameters: `Double` profit]

`/totalProfit`: returns the total profit made from purchases in the purchase table [no parameters]

`/averageProfitPerPurchase`: returns the average profit per purchase [no parameters]

`/stdProfitPurchase`: returns the standard deviation of the profits of the purchases  so far [no parameters]

`/averagePaidPricePurchase`: returns the average paidPrice of all purchases made [no parameters]

`/stdPaidPricePurchase`: returns the standard deviation of the paid prices of all the purchases [no parameters]

`/listIdByProfit`: return list of product-id's sorted by expected profit from selling out (ascending order) [no parameters]

`/allPurchases`: returns Iterable of `Purchase`'s [no parameters]

`/allReturns`: returns Iterable of `ReturnedProduct`'s [no parameters]

`/allProducts`: returns Iterable of `Product`'s [no parameters]

`/add`: add product [parameters: `Integer` id, `String` name, `Integer` quantity (must be at least one), `Double` paidPrice, `Double` sellingPrice]

`/expectedProfit`: calculate profit from selling all products in table [no parameters]

`/clear`: delete all entries in the database; reset the product id sequence to start from 1 [no parameters]

`/averageSellingPriceProduct`: returns the average selling prices of the products [no parameters]

`/stdSellingPriceProduct`: returns the standard deviation of the selling prices of the products [no parameters]

`/averagePaidPriceProduct`: returns the average paid price of the products [no parameters]

`/stdPaidPriceProduct`: returns the standard deviation of the paid prices of the products [no parameters]

`/averageReturnGap`: takes in no arguments, returns mean of return gap

`/stdReturnGap`: takes in no arguments, returns std of return gap

`/averagePaidPriceReturn`: returns mean of paid prices of returns [no parameters]

`/stdPaidPriceReturn`: returns standard deviation of paid prices of returns [no parameters]

`/averageSellingPriceReturn`: returns mean of selling prices of returns [no parameters]

`/stdSellingPriceReturn`: returns standard deviation of selling prices of returns [no parameters]


## Sanity Checks
---
Run simple tests:

`mvn spring-boot:run`

`chmod +x testDriver.sh`

`./testDriver.sh`
