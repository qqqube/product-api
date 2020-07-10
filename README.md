# Crud API with Spring Boot using MySQL and JPA

## Database Tables
---
#### product
`int` id [primary key, auto_increment], `varchar(255)` name, `double` paid_price, `int` quantity, `double` selling_price

#### purchase
`varchar(255)` timestamp [primary key], `double` paid_price, `int` productid, `varchar(255)` product_name, `double` profit, `int` quantity

#### returned_product
`varchar(255)` return_timestamp [primary key], `double` paid_price, `int` product_id, `varchar(255)` product_name, `int` quantity, `double` selling_price, `bigint` pr_gap


## Documentation
---

`/product/nameId`: get the name of the product with specified id  [parameters: `Integer` id]

`/product/quantityId`: get the quantity of the product with specified id [parameters: `Integer` id]

`/product/sellingPriceId`: get selling price of item with specified id [parameters: `Integer` id]

`/product/paidPriceId`: get paid price of the product with specified id [parameters: `Integer` id]

`/product/renameId`: rename the product with the given id; if the product has been purchased, the name is also updated in the purchase table [parameters: `Integer` id, `String` newName]

`/product/updateQuantityId`: change the quantity of the product with the given id [parameters: `Integer` id, `Integer` quantity]

`/product/updatePaidPriceId`: update paid price of product specified by id; if the product has been purchased, the paid prices and profits of the corresponding purchases are updated in the purchase table [parameters: `Integer` id, `Double` paidPrice]

`/product/updateSellingPriceId`: update selling price of product specified by id [parameters: `Integer` id, `Double` sellingPrice]

`/product/deleteId`: remove product with given id [parameters: `Integer` id]

`/product/makePurchaseId`: purchase specified amount of the product with the given id [parameters: `Integer` id, `Integer` quantity]

`/product/deleteName`: remove all products with the given name [parameters: `String` name]

`/product/updateName`: change the names of all products with the specified name; if some of the products have been purchased, update the purchase table accordingly [parameters: `String` name, `String` newName]

`/product/updateSellingPriceOffsetName`: change the selling price of all products with the given name by some offset [parameters: `String` name, `Integer` priceOffset]

`/product/expectedProfitFromProductName`: calculate expected profit from selling all products with specified name [parameters: `String` name]

`/product/listIdByProfitName`: return list of product-id's sorted by expected profit from selling out (ascending order) [parameters: `String` name]

`/product/productQuantityName`: return the number of products with the specified name [parameters: `String` name]

`/product/purchaseNameId`: get product name of purchase specified by productId [parameters: `String` date]

`/product/purchaseProfit`: get profit of purchase specified by productId [parameters: `String` date]

`/product/purchaseIndPaidPrice`: get individual paid price of purchase's product specified by productId [parameters: `String` date]

`/product/purchaseQuantity`: get quantity of purchase's product, specified by productId [parameters: `String` date]

`/product/returnPurchase`: return a purchase, specified by datetime [parameters: `String` date, `Integer` quantity]

`/product/profitStart`: returns the total profit made starting from the input date [parameters: `String` date]

`/product/profitEnd`: takes in a date string and returns the total profit made before the input date [parameters: `String` date]

`/product/returnPRGap`: get purchase-return gap in days, provided the return's timestamp [`String` date]

`/product/returnProductID`: get productId, provided the return's timestamp [`String` date]

`/product/returnProductName`: return product name, provided the return's timestamp [parameters: `String` date]

`/product/returnQuantity`: return product quantity, provided the return's timestamp [parameters: `String` date]

`/product/returnSellingPrice`: return selling price, provided the return's timestamp [parameters: `String` date]

`/product/returnPaidPrice`: return paid price, provided the return's timestamp [parameters: `String` date]

`/product/processReturn`: process a return; add the product back to the product table with specified selling price [parameters: `String` date, `Double` sellingPrice]

`/product/removeReturn`: discards a return, specified by datetime [parameters: `String` date]

`/product/expectedProfitMax`: takes in a quantity and returns maximum profit from selling the specified
number of products [parameters: `Integer` quantity]

`/product/expectedProfitMin`: takes in a quantity and returns minimum profit from selling the specified number of items [parameters: `Integer` quantity]

`/product/checkInventoryLowerBound`: takes in a quantity and returns a list of productid's that have at least the specified number of items [parameters: `Integer` quantity]

`/product/checkInventoryUpperBound`: takes in a quantity and returns a list of productid's where each product corresponding to the product id has a quantity less than the specified number of items [parameters: `Integer` quantity]

`/product/individualItemFilterLowerBound`: takes in a profit value and returns a list of productid’s such that each product corresponding to the id will yield a profit that is greater than or equal to the specified value (by selling one) [parameters: `Double` profit]

`/product/individualItemFilterUpperBound`: takes in a profit value, and returns a list of productid’s such that each product corresponding to the id will yield a profit that is less than or equal to the specified value (by selling one) [parameters: `Double` profit]

`/product/totalProfit`: returns the total profit made from purchases in the purchase table [no parameters]

`/product/averageProfitPerPurchase`: returns the average profit per purchase [no parameters]

`/product/stdProfitPurchase`: returns the standard deviation of the profits of the purchases  so far [no parameters]

`/product/averagePaidPricePurchase`: returns the average paidPrice of all purchases made [no parameters]

`/product/stdPaidPricePurchase`: returns the standard deviation of the paid prices of all the purchases [no parameters]

`/product/listIdByProfit`: return list of product-id's sorted by expected profit from selling out (ascending order) [no parameters]

`/product/allPurchases`: returns Iterable of `Purchase`'s [no parameters]

`/product/allReturns`: returns Iterable of `ReturnedProduct`'s [no parameters]

`/product/allProducts`: returns Iterable of `Product`'s [no parameters]

`/product/add`: add product [parameters: `Integer` id, `String` name, `Integer` quantity (must be at least one), `Double` paidPrice, `Double` sellingPrice]

`/product/expectedProfit`: calculate profit from selling all products in table [no parameters]

`/product/clear`: delete all entries in the database; reset the product id sequence to start from 1 [no parameters]

`/product/averageSellingPriceProduct`: returns the average selling prices of the products [no parameters]

`/product/stdSellingPriceProduct`: returns the standard deviation of the selling prices of the products [no parameters]

`/product/averagePaidPriceProduct`: returns the average paid price of the products [no parameters]

`/product/stdPaidPriceProduct`: returns the standard deviation of the paid prices of the products [no parameters]

`/product/averageReturnGap`: takes in no arguments, returns mean of return gap

`/product/stdReturnGap`: takes in no arguments, returns std of return gap

`/product/averagePaidPriceReturn`: returns mean of paid prices of returns [no parameters]

`/product/stdPaidPriceReturn`: returns standard deviation of paid prices of returns [no parameters]

`/product/averageSellingPriceReturn`: returns mean of selling prices of returns [no parameters]

`/product/stdSellingPriceReturn`: returns standard deviation of selling prices of returns [no parameters]


## Sanity Checks
---
Run simple tests:

`mvn spring-boot:run`

`chmod +x testDriver.sh`

`./testDriver.sh`
