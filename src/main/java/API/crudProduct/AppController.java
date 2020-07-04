package API.crudProduct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.Duration;

@Controller
@RequestMapping(path="/product")
public class AppController {

    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private PurchaseRepository purchaseRepo;
    @Autowired
    private ReturnedProductRepository returnRepo;
    @Autowired
    private DatabaseUpdater db;

    /* Get the name of the product with specified id */
    @RequestMapping(path="/nameId")
    public @ResponseBody String getNameID(@RequestParam Integer id) throws Exception{
        if (this.productRepo.existsById(id) == false) {
            throw new Exception("No product with that id exists.");
        }
        return this.productRepo.findById(id).get().getName();
    }

    /* Get the quantity of the product with specified id */
    @RequestMapping(path="/quantityId")
    public @ResponseBody Integer getQuantityID(@RequestParam Integer id) throws Exception{
        if (this.productRepo.existsById(id) == false) {
            throw new Exception("No product with that id exists.");
        }
        return this.productRepo.findById(id).get().getQuantity();
    }

    /* Get selling price of item with specified id */
    @RequestMapping(path="/sellingPriceId")
    public @ResponseBody Double getSellingPriceID(@RequestParam Integer id) throws Exception{
        if (this.productRepo.existsById(id) == false) {
            throw new Exception("No product with that id exists.");
        }
        return this.productRepo.findById(id).get().getSellingPrice();
    }

    /* Get paid price of the item with specified id */
    @RequestMapping(path="/paidPriceId")
    public @ResponseBody Double getPaidPriceID(@RequestParam Integer id) throws Exception {
        if (this.productRepo.existsById(id) == false) {
            throw new Exception("No product with that id exists.");
        }
        return this.productRepo.findById(id).get().getPaidPrice();
    }

    /* Rename the product with the given id. */
    @RequestMapping(path="/renameId")
    public @ResponseBody void renameID(@RequestParam Integer id, @RequestParam String newName) throws Exception {

        Product prod = this.productRepo.existsById(id) == false ? null : this.productRepo.findById(id).get();
        boolean purchaseExists = false;
        for (Purchase purchase : this.purchaseRepo.findAll()) {
            if (purchase.getProductID() == id) {
                purchaseExists = true;
                purchase.setProductName(newName);
                this.purchaseRepo.save(purchase);
            }
        }
        if (prod == null && purchaseExists == false) {
            throw new Exception("No product with that id exists.");
        }
        if (prod != null) {
            prod.setName(newName);
            this.productRepo.save(prod);
        }
    }

    /* Change the quantity of the product with the given id. */
    @RequestMapping(path="/updateQuantityId")
    public @ResponseBody void updateQuantityID(@RequestParam Integer id, @RequestParam Integer quantity) throws Exception {
        if (this.productRepo.existsById(id) == false) {
            throw new Exception("No product with that id exists.");
        }
        Product prod = this.productRepo.findById(id).get();
        prod.setQuantity(quantity);
        this.productRepo.save(prod);
    }

    /* Update paid price of specific product (specified by id). */
    @RequestMapping(path="/updatePaidPriceId")
    public @ResponseBody void updatePaidPriceID(@RequestParam Integer id, @RequestParam Double paidPrice) throws Exception {

        Product prod = this.productRepo.existsById(id) == false ? null : this.productRepo.findById(id).get();
        boolean purchaseExists = false;
        for (Purchase purchase : this.purchaseRepo.findAll()) {
            if (purchase.getProductID() == id) {
                purchaseExists = true;
                purchase.setPaidPrice(paidPrice);
                purchase.setProfit((prod.getSellingPrice() - paidPrice)*purchase.getQuantity());
                this.purchaseRepo.save(purchase);
            }
        }
        if (prod == null && purchaseExists == false) {
            throw new Exception("No product with that id exists.");
        }
        if (prod != null) {
            prod.setPaidPrice(paidPrice);
            this.productRepo.save(prod);
        }
    }

    /* Update selling price of product specified by id. */
    @RequestMapping(path="/updateSellingPriceId")
    public @ResponseBody void updateSellingPriceID(@RequestParam Integer id, @RequestParam Double sellingPrice) throws Exception {
        if (this.productRepo.existsById(id) == false) {
            throw new Exception("No product with that id exists.");
        }
        Product prod = this.productRepo.findById(id).get();
        prod.setSellingPrice(sellingPrice);
        this.productRepo.save(prod);
    }

    /* Removes the product with the given id. */
    @RequestMapping(path="/deleteId")
    public @ResponseBody void removeProductID(@RequestParam Integer id) throws Exception {
        if (this.productRepo.existsById(id) == false) {
            throw new Exception("No product with that id exists.");
        }
        this.productRepo.delete(this.productRepo.findById(id).get());
    }

    /* Purchase `quantity` amount of the product specified by id */
    @RequestMapping(path="/makePurchaseId")
    public @ResponseBody void makePurchaseId(@RequestParam Integer id, @RequestParam Integer quantity) throws Exception {
        if (this.productRepo.existsById(id) == false) {
            throw new Exception("No product with that id exists.");
        }
        Product prod = this.productRepo.findById(id).get();
        if (prod.getQuantity() < quantity) {
            throw new Exception("Purchase cannot be completed because there aren't enough products.");
        }
        Purchase newPurchase = new Purchase();
        newPurchase.setProductName(prod.getName());
        newPurchase.setProfit(quantity * (prod.getSellingPrice() - prod.getPaidPrice()));
        newPurchase.setDate();
        newPurchase.setProductID(prod.getID());
        newPurchase.setPaidPrice(prod.getPaidPrice());
        newPurchase.setQuantity(quantity);
        this.purchaseRepo.save(newPurchase);

        if (quantity == prod.getQuantity()) {
            this.productRepo.delete(prod);
        } else {
            prod.setQuantity(prod.getQuantity() - quantity);
            this.productRepo.save(prod);
        }

    }

    /* Removes all products with the given name. */
    @RequestMapping(path="/deleteName")
    public @ResponseBody void removeProduct(@RequestParam String name) throws Exception {
        int count = 0;
        Iterable<Product> allProducts = this.productRepo.findAll();
        for (Product prod : allProducts) {
            if (prod.getName().equals(name)) {
                count++;
                this.productRepo.delete(prod);
            }
        }
        if (count == 0) {
            throw new Exception("No product with that name exists.");
        }
    }

    /* Change the names of all products with the specified name. */
    @RequestMapping(path="/updateName")
    public @ResponseBody void updateName(@RequestParam String name, @RequestParam String newName) throws Exception {
        int count = 0;
        Iterable<Product> allProducts = this.productRepo.findAll();
        for (Product prod: allProducts) {
            if (prod.getName().equals(name)) {
                count++;
                prod.setName(newName);
                this.productRepo.save(prod);

                for (Purchase purchase : this.purchaseRepo.findAll()) {
                    if (purchase.getProductID() == prod.getID()) {
                        purchase.setProductName(newName);
                        this.purchaseRepo.save(purchase);
                    }
                }
            }
        }
        if (count == 0) {
            throw new Exception("No product with that name exists.");
        }
    }


    /* Change the selling price of all products with the given name by some offset. */
    @RequestMapping(path="/updateSellingPriceOffsetName")
    public @ResponseBody void updateSellingPriceName(@RequestParam String name, @RequestParam Integer priceOffset) throws Exception {
        int count = 0;
        Iterable<Product> allProducts = this.productRepo.findAll();
        for (Product prod: allProducts) {
            if (prod.getName().equals(name)) {
                count++;
                prod.setSellingPrice(prod.getSellingPrice() + priceOffset);
                productRepo.save(prod);
            }
        }
        if (count == 0) {
            throw new Exception("No product with that name exists.");
        }
    }



    /* Calculate expected profit from selling all products with specified name. */
    @RequestMapping(path="/expectedProfitFromProductName")
    public @ResponseBody Double getProfitFromProduct(@RequestParam String name) throws Exception {
        double profit = 0;
        int count = 0;
        Iterable<Product> allProducts = this.productRepo.findAll();
        for (Product prod : allProducts) {
            if (prod.getName().equals(name)) {
                count++;
                profit += prod.getQuantity() * (prod.getSellingPrice() - prod.getPaidPrice());
            }
        }
        if (count == 0) {
            throw new Exception("No product with that name exists.");
        }
        return profit;
    }

    /* Return list of product-id's sorted by expected profit from selling out (ascending order) */
    @RequestMapping(path="/listIdByProfitName")
    public @ResponseBody List<Integer> getListIdByProfitName(@RequestParam String name) throws Exception {
        int count = 0;
        List<Integer> idLst = new ArrayList<>();
        List<Double> profitLst = new ArrayList<>();
        Iterable<Product> allProducts = this.productRepo.findAll();
        for (Product prod : allProducts) {
            if (prod.getName().equals(name)) {
                count++;
                double currProfit = prod.getQuantity() * (prod.getSellingPrice() - prod.getPaidPrice());
                int index = 0;
                while (index < profitLst.size()) {
                    if (profitLst.get(index) > currProfit) {
                        break;
                    } index ++;
                }
                profitLst.add(index, currProfit);
                idLst.add(index, prod.getID());
            }
        }
        if (count == 0) {
            throw new Exception("No product with that name exists.");
        }
        return idLst;
    }


    /* Return the number of products with the specified product name */
    @RequestMapping(path="/productQuantityName")
    public @ResponseBody Integer getProductQuantityName(@RequestParam String name) throws Exception {
        int total = 0;
        Iterable<Product> allProducts = this.productRepo.findAll();
        for (Product prod: allProducts) {
            if (prod.getName().equals(name)) {
                total += prod.getQuantity();
            }
        }
        if (total == 0) {
            throw new Exception("No product with that name exists.");
        }
        return total;
    }

    /* Get productID of purchase specified by datetime. */
    @RequestMapping(path="/purchaseProductId")
    public @ResponseBody String getPurchaseProductId(@RequestParam String date) throws Exception {
        if (this.purchaseRepo.existsById(date) == false) {
            throw new Exception("No purchase with that date exists.");
        }
        Purchase purchase = this.purchaseRepo.findById(date).get();
        return purchase.getProductName();
    }

    /* Get product name of purchase specified by datetime. */
    @RequestMapping(path="/purchaseNameId")
    public @ResponseBody String getPurchaseName(@RequestParam String date) throws Exception {
        if (this.purchaseRepo.existsById(date) == false) {
            throw new Exception("No purchase with that date exists.");
        }
        Purchase purchase = this.purchaseRepo.findById(date).get();
        return purchase.getProductName();
    }

    /* Get profit of purchase specified by datetime. */
    @RequestMapping(path="/purchaseProfit")
    public @ResponseBody Double getPurchaseProfit(@RequestParam String date) throws Exception {
        if (this.purchaseRepo.existsById(date) == false) {
            throw new Exception("No purchase with that date exists.");
        }
        Purchase purchase = this.purchaseRepo.findById(date).get();
        return purchase.getProfit();
    }

    /* Get individual paid price of purchase's product specified by datetime. */
    @RequestMapping(path="/purchaseIndPaidPrice")
    public @ResponseBody Double getPaidPricePurchase(@RequestParam String date) throws Exception {
        if (this.purchaseRepo.existsById(date) == false) {
            throw new Exception("No purchase with that date exists.");
        }
        Purchase purchase = this.purchaseRepo.findById(date).get();
        return purchase.getPaidPrice();
    }

    /* Get quantity of purchase's product, specified by datetime. */
    @RequestMapping(path="/purchaseQuantity")
    public @ResponseBody Integer getPurchaseQuantity(@RequestParam String date) throws Exception {
        if (this.purchaseRepo.existsById(date) == false) {
            throw new Exception("No purchase with that date exists.");
        }
        Purchase purchase = this.purchaseRepo.findById(date).get();
        return purchase.getQuantity();
    }

    /* Return a purchase, specified by datetime. */
    @RequestMapping(path="/returnPurchase")
    public @ResponseBody void returnPurchase(@RequestParam String date, @RequestParam Integer quantity) throws Exception {
        if (this.purchaseRepo.existsById(date) == false) {
            throw new Exception("No purchase with that date exists.");
        }
        Purchase purchase = this.purchaseRepo.findById(date).get();
        if (purchase.getQuantity() < quantity) {
            throw new Exception("No purchase wtih that quantity exists.");
        }
        ReturnedProduct newReturn = new ReturnedProduct();
        newReturn.setReturnTimestamp();
        newReturn.setProductName(purchase.getProductName());
        newReturn.setQuantity(quantity);
        newReturn.setPaidPrice(purchase.getPaidPrice());
        Double sellingPrice = (purchase.getProfit() / purchase.getQuantity()) + purchase.getPaidPrice();
        newReturn.setSellingPrice(sellingPrice);
        newReturn.setProductId(purchase.getProductID());
        LocalDateTime purchaseDate = LocalDateTime.parse(purchase.getDate());
        LocalDateTime returnDate = LocalDateTime.parse(newReturn.getReturnTimestamp());
        Duration duration = Duration.between(returnDate, purchaseDate);
        newReturn.setPRGap(duration.toDays());
        this.returnRepo.save(newReturn);
        if (quantity == purchase.getQuantity()) {
            this.purchaseRepo.delete(purchase);
        } else {
            purchase.setQuantity(purchase.getQuantity() - quantity);
            purchase.setProfit((sellingPrice - purchase.getPaidPrice())* purchase.getQuantity());
            this.purchaseRepo.save(purchase);
        }
    }

    /* Returns the total profit made starting from the input date */
    @RequestMapping(path="/profitStart")
    public @ResponseBody Double getProfitStart(@RequestParam String date) throws Exception {
        int count = 0;
        double profit = 0;
        LocalDateTime startDate = LocalDateTime.parse(date);
        for (Purchase purchase : this.purchaseRepo.findAll()) {
            LocalDateTime currDate = LocalDateTime.parse(purchase.getDate());
            Duration duration = Duration.between(startDate, currDate);
            boolean toAdd = (duration.toDays() >=0) && (duration.toHours() >=0) &&  (duration.toMillis() >=0);
            if (toAdd) {
                count++;
                profit += purchase.getProfit();
            }
        }
        if (count == 0) {
            throw new Exception("There are no purchases after the input date.");
        }
        return profit;
    }

    /* Returns the total profit made before the input date */
    @RequestMapping(path="/profitEnd")
    public @ResponseBody Double getProfitEnd(@RequestParam String date) throws Exception {
        int count = 0;
        double profit = 0;
        LocalDateTime startDate = LocalDateTime.parse(date);
        for (Purchase purchase : this.purchaseRepo.findAll()) {
            LocalDateTime currDate = LocalDateTime.parse(purchase.getDate());
            Duration duration = Duration.between(currDate, startDate);
            boolean toAdd = (duration.toDays() >=0) && (duration.toHours() >=0) &&  (duration.toMillis() >=0);
            if (toAdd) {
                count++;
                profit += purchase.getProfit();
            }
        }
        if (count == 0) {
            throw new Exception("There are no purchases after the input date.");
        }
        return profit;
    }

    /* Get productID, provided return's PR gap in days */
    @RequestMapping(path="/returnPRGap")
    public @ResponseBody Long returnPRGap(@RequestParam String date) throws Exception {
        if (this.returnRepo.existsById(date) == false) {
            throw new Exception("No return with that date exists.");
        }
        ReturnedProduct returnObj = this.returnRepo.findById(date).get();
        return returnObj.getprGap();
    }


    /* Get productID, provided return's timestamp */
    @RequestMapping(path="/returnProductID")
    public @ResponseBody Integer returnProductID(@RequestParam String date) throws Exception {
        if (this.returnRepo.existsById(date) == false) {
            throw new Exception("No return with that date exists.");
        }
        ReturnedProduct returnObj = this.returnRepo.findById(date).get();
        return returnObj.getProductId();
    }


    /* Get product name, provided return's timestamp */
    @RequestMapping(path="/returnProductName")
    public @ResponseBody String returnProductName(@RequestParam String date) throws Exception {
        if (this.returnRepo.existsById(date) == false) {
            throw new Exception("No return with that date exists.");
        }
        ReturnedProduct returnObj = this.returnRepo.findById(date).get();
        return returnObj.getProductName();
    }

    /* Get product quantity, provided the return's timestamp*/
    @RequestMapping(path="/returnQuantity")
    public @ResponseBody Integer returnQuantity(@RequestParam String date) throws Exception {
        if (this.returnRepo.existsById(date) == false) {
            throw new Exception("No return with that date exists.");
        }
        ReturnedProduct returnObj = this.returnRepo.findById(date).get();
        return returnObj.getQuantity();
    }

    /* Get selling price, provided the return's timestamp */
    @RequestMapping(path="/returnSellingPrice")
    public @ResponseBody Double returnSellingPrice(@RequestParam String date) throws Exception {
        if (this.returnRepo.existsById(date) == false) {
            throw new Exception("No return with that date exists.");
        }
        ReturnedProduct returnObj = this.returnRepo.findById(date).get();
        return returnObj.getSellingPrice();
    }

    /* Get paid price, provided the return's timestamp */
    @RequestMapping(path="/returnPaidPrice")
    public @ResponseBody Double returnPaidPrice(@RequestParam String date) throws Exception {
        if (this.returnRepo.existsById(date) == false) {
            throw new Exception("No return with that date exists.");
        }
        ReturnedProduct returnObj = this.returnRepo.findById(date).get();
        return returnObj.getPaidPrice();
    }

    /* Process a return; add the product back to the product table with specified selling price */
    @RequestMapping(path="/processReturn")
    public @ResponseBody void processReturn(@RequestParam String date, @RequestParam Double sellingPrice) throws Exception {
        if (this.returnRepo.existsById(date) == false) {
            throw new Exception("No return with that date exists.");
        }
        ReturnedProduct returnObj = this.returnRepo.findById(date).get();
        if (this.productRepo.existsById(returnObj.getProductId())) {
            Product prod = this.productRepo.findById(returnObj.getProductId()).get();
            if (prod.getSellingPrice() == sellingPrice) {
                prod.setQuantity(prod.getQuantity() + returnObj.getQuantity());
                this.productRepo.save(prod);
                this.returnRepo.delete(returnObj);
            }

        }
        Product prod = new Product();
        prod.setName(returnObj.getProductName());
        prod.setQuantity(returnObj.getQuantity());
        prod.setPaidPrice(returnObj.getPaidPrice());
        prod.setSellingPrice(sellingPrice);
        this.productRepo.save(prod);
        this.returnRepo.delete(returnObj);

    }

    /* Discards a return, specified by datetime */
    @RequestMapping(path="/removeReturn")
    public @ResponseBody void removeReturn(@RequestParam String date) throws Exception{
        if (this.returnRepo.existsById(date) == false) {
            throw new Exception("No return with that date exists.");
        }
        ReturnedProduct returnObj = this.returnRepo.findById(date).get();
        this.returnRepo.delete(returnObj);
    }


   /* Returns the total number of products in the product table. */
    private int countProducts() {
        int total = 0;
        for (Product product : this.productRepo.findAll()) {
            total += product.getQuantity();
        }
        return total;
   }


    /*Takes in a quantity, returns maximum profit from selling the specified
    number of products. */
    @RequestMapping(path="/expectedProfitMax")
    public @ResponseBody Double getExpectedProfitMax(@RequestParam Integer quantity) throws Exception {

        if (countProducts() < quantity) {
            throw new Exception("There aren't that many products available.");
        }
        if (quantity <= 0) {
            throw new Exception("Invalid quantity.");
        }
        List<Integer> qLst = new ArrayList<>();
        List<Double> profitLst = new ArrayList<>();
        Iterable<Product> allProducts = this.productRepo.findAll();
        for (Product prod : allProducts) {
            double currProfit = prod.getSellingPrice() - prod.getPaidPrice();
            int index = 0;
            while (index < profitLst.size()) {
                if (profitLst.get(index) > currProfit) {
                    break;
                } index ++;
            }
            profitLst.add(index, currProfit);
            qLst.add(index, prod.getQuantity());
        }

        double maxProfit = 0;
        int currIndex = qLst.size() - 1;
        while (quantity > 0) {
            int currQuant = qLst.remove(currIndex);
            double currProfit = profitLst.remove(currIndex);
            if (quantity - currQuant < 0) {
                maxProfit += quantity * currProfit;
                break;
            } else {
                maxProfit += currQuant * currProfit;
                quantity -= currQuant;
            }
            currIndex--;
        }
        return maxProfit;

    }

    /*Takes in a quantity, returns minimum profit from selling the specified
    number of products. */
    @RequestMapping(path="/expectedProfitMin")
    public @ResponseBody Double getExpectedProfitMin(@RequestParam Integer quantity) throws Exception {

        if (countProducts() < quantity) {
            throw new Exception("There aren't that many products available.");
        }
        if (quantity <= 0) {
            throw new Exception("Invalid quantity.");
        }
        List<Integer> qLst = new ArrayList<>();
        List<Double> profitLst = new ArrayList<>();
        Iterable<Product> allProducts = this.productRepo.findAll();
        for (Product prod : allProducts) {
            double currProfit = prod.getSellingPrice() - prod.getPaidPrice();
            int index = 0;
            while (index < profitLst.size()) {
                if (profitLst.get(index) > currProfit) {
                    break;
                } index ++;
            }
            profitLst.add(index, currProfit);
            qLst.add(index, prod.getQuantity());
        }

        double minProfit = 0;
        while (quantity > 0) {
            int currQuant = qLst.remove(0);
            double currProfit = profitLst.remove(0);
            if (quantity - currQuant < 0) {
                minProfit += quantity * currProfit;
                break;
            } else {
                minProfit += currQuant * currProfit;
                quantity -= currQuant;
            }
        }
        return minProfit;

    }

    /*  Takes in a quantity, returns a list of productid’s that have at least the specified number of items */
    @RequestMapping(path="/checkInventoryLowerBound")
    public @ResponseBody List<Integer> checkInventoryLowerBound(@RequestParam Integer quantity) throws Exception {
        if (countProducts() < quantity) {
            throw new Exception("There aren't that many products available.");
        }
        if (quantity <= 0) {
            throw new Exception("Invalid quantity.");
        }
        List<Integer> productIDs = new ArrayList<>();
        for (Product product : this.productRepo.findAll()) {
            if (product.getQuantity() >= quantity) {
                productIDs.add(product.getID());
            }
        }
        return productIDs;
    }
    /*  Takes in a quantity, returns a list of productid’s where each product
    corresponding to the product id has a quantity less than or equal to
     the specified number of items */
    @RequestMapping(path="/checkInventoryUpperBound")
    public @ResponseBody List<Integer> checkInventoryUpperBound(@RequestParam Integer quantity) throws Exception {
        if (countProducts() < quantity) {
            throw new Exception("There aren't that many products available.");
        }
        if (quantity <= 0) {
            throw new Exception("Invalid quantity.");
        }
        List<Integer> productIDs = new ArrayList<>();
        for (Product product : this.productRepo.findAll()) {
            if (product.getQuantity() <= quantity) {
                productIDs.add(product.getID());
            }
        }
        return productIDs;
    }

    /* Takes in a profit value and returns a list of productid’s such that each
    product corresponding to the id will yield a profit that is greater than or equal
    to the specified value (by selling one) */
    @RequestMapping(path="/individualItemFilterLowerBound")
    public @ResponseBody List<Integer> applyIndividualItemFilterLowerBound(@RequestParam Double profit) {
        List<Integer> ids = new ArrayList<>();
        for (Product product : this.productRepo.findAll()) {
            if (product.getSellingPrice() - product.getPaidPrice() >= profit) {
                ids.add(product.getID());
            }
        }
        return ids;
    }

    /* Takes in a profit value and returns a list of productid’s such that each
    product corresponding to the id will yield a profit that is less than or equal
    to the specified value (by selling one) */
    @RequestMapping(path="/individualItemFilterUpperBound")
    public @ResponseBody List<Integer> applyIndividualItemFilterUpperBound(@RequestParam Double profit) {
        List<Integer> ids = new ArrayList<>();
        for (Product product : this.productRepo.findAll()) {
            if (product.getSellingPrice() - product.getPaidPrice() <= profit) {
                ids.add(product.getID());
            }
        }
        return ids;
    }


    /* Takes in no arguments, returns the total profit made from purchases in the purchase table */
    @RequestMapping(path= "/totalProfit")
    public @ResponseBody Double getTotalProfit() {
        double total = 0;
        for (Purchase purchase : this.purchaseRepo.findAll()) {
            total += purchase.getProfit();
        }
        return total;
    }

    /* Takes in no arguments, returns the average profit per purchase */
    @RequestMapping(path="/averageProfitPerPurchase")
    public @ResponseBody Double getAvgProfitPerPurchase() throws Exception {
        if (this.purchaseRepo.count() == 0) {
            throw new Exception("There are no purchases.");
        }
        double total = 0;
        for (Purchase purchase : this.purchaseRepo.findAll()) {
            total += purchase.getProfit();
        }
        return total / this.purchaseRepo.count();
    }

    /* Takes in no arguments, returns the standard deviation of the profits of the purchases  so far */
    @RequestMapping(path="/stdProfitPurchase")
    public @ResponseBody Double getStdProfitPerPurchase() throws Exception {
        if (this.purchaseRepo.count() == 0) {
            throw new Exception("There are no purchases.");
        }
        double total = 0;
        for (Purchase purchase : this.purchaseRepo.findAll()) {
            total += purchase.getProfit();
        }
        double avg = total / this.purchaseRepo.count();
        double sqDiffTotal = 0;
        for (Purchase purchase : this.purchaseRepo.findAll()) {
            sqDiffTotal += (purchase.getProfit() - avg) * (purchase.getProfit() - avg);
        }
        return Math.sqrt(sqDiffTotal / this.purchaseRepo.count());
    }

    /* Takes in no arguments, returns the average paid price of all purchases made */
    @RequestMapping(path="/averagePaidPricePurchase")
    public @ResponseBody Double getAvgPaidPricePurchase() throws Exception {
        if (this.purchaseRepo.count() == 0) {
            throw new Exception("There are no purchases.");
        }
        double total = 0;
        for (Purchase purchase : this.purchaseRepo.findAll()) {
            total += purchase.getPaidPrice() * purchase.getQuantity();
        }
        return total / this.purchaseRepo.count();
    }

    /* Takes in no arguments, returns the standard deviation of paid prices of all purchases made*/
    @RequestMapping(path="/stdPaidPricePurchase")
    public @ResponseBody Double getStdPaidPricePurchase() throws Exception {
        if (this.purchaseRepo.count() == 0) {
            throw new Exception("There are no purchases.");
        }
        double total = 0;
        for (Purchase purchase : this.purchaseRepo.findAll()) {
            total += purchase.getPaidPrice() * purchase.getQuantity();
        }
        double avg = total / this.purchaseRepo.count();
        double sqDiffTotal = 0;
        for (Purchase purchase : this.purchaseRepo.findAll()) {
            double curr = purchase.getPaidPrice() * purchase.getQuantity();
            sqDiffTotal += (curr - avg) * (curr - avg);
        }
        return Math.sqrt(sqDiffTotal / this.purchaseRepo.count());

    }





    /* Return list of product-id's sorted by expected profit from selling out (ascending order) */
    @RequestMapping(path="/listIdByProfit")
    public @ResponseBody List<Integer> getListIdByProfit()  {

        List<Integer> idLst = new ArrayList<>();
        List<Double> profitLst = new ArrayList<>();
        Iterable<Product> allProducts = this.productRepo.findAll();
        for (Product prod : allProducts) {
                double currProfit = prod.getQuantity() * (prod.getSellingPrice() - prod.getPaidPrice());
                int index = 0;
                while (index < profitLst.size()) {
                    if (profitLst.get(index) > currProfit) {
                        break;
                    } index ++;
                }
                profitLst.add(index, currProfit);
                idLst.add(index, prod.getID());
        }

        return idLst;
    }

    /* Return all Purchases in the purchase table */
    @RequestMapping(path="/allPurchases")
    public @ResponseBody Iterable<Purchase> getAllPurchases() {
        return this.purchaseRepo.findAll();
    }

    /* Return all Returns in the returned_product table */
    @RequestMapping(path="/allReturns")
    public @ResponseBody Iterable<ReturnedProduct> getAllReturns() {
        return this.returnRepo.findAll();
    }

    /* Return all products in product table. */
    @RequestMapping(path="/allProducts")
    public @ResponseBody Iterable<Product> getAllProducts() {
        /* Returns a JSON or XML with the products */
        return this.productRepo.findAll();
    }

    /* Add a product to the product table. */
    @RequestMapping(path="/add")
    public @ResponseBody void addNewProduct(@RequestParam String name, @RequestParam Integer quantity,
                                              @RequestParam Double paidPrice, @RequestParam Double sellingPrice) {

        Product prod = new Product();
        prod.setName(name);
        prod.setQuantity(quantity);
        prod.setPaidPrice(paidPrice);
        prod.setSellingPrice(sellingPrice);
        productRepo.save(prod);
    }

    /* Calculate profit from selling all products in the table. */
    @RequestMapping(path="/expectedProfit")
    public @ResponseBody Double getProfit() {
        double profit = 0;
        Iterable<Product> allProducts = this.productRepo.findAll();
        for (Product prod : allProducts) {
            profit += prod.getQuantity() * (prod.getSellingPrice() - prod.getPaidPrice());
        }
        return profit;
    }

    /*  Delete all entries in the product and purchase tables. */
    @RequestMapping(path="/clear")
    public @ResponseBody void clear() {
        this.db.resetID();
    }

    /*  Takes in no arguments, returns the average selling prices of the products for sale */
    @RequestMapping(path="/averageSellingPriceProduct")
    public @ResponseBody Double getAvgSellingPriceProduct() throws Exception {
        if (this.productRepo.count() == 0) {throw new Exception("There are no products.");}
        double total = 0;
        double counter = 0;
        for (Product product : this.productRepo.findAll()) {
            total += product.getSellingPrice() * product.getQuantity();
            counter += product.getQuantity();
        }
        return total / counter;
    }

    /* Takes in no arguments, returns the standard deviation of the selling prices of the products */
    @RequestMapping(path="/stdSellingPriceProduct")
    public @ResponseBody Double getStdSellingPriceProduct() throws Exception {
        if (this.productRepo.count() == 0) {throw new Exception("There are no products.");}
        double total = 0;
        double counter = 0;
        for (Product product : this.productRepo.findAll()) {
            total += product.getSellingPrice() * product.getQuantity();
            counter += product.getQuantity();
        }
        double avg = total / counter;
        double sqSum = 0;
        for (Product product: this.productRepo.findAll()) {
            sqSum += (product.getSellingPrice() - avg) * (product.getSellingPrice() - avg) * product.getQuantity();
        }
        return Math.sqrt(sqSum / counter);
    }

    /*  Takes in no arguments, returns the average paid prices of the products for sale */
    @RequestMapping(path="/averagePaidPriceProduct")
    public @ResponseBody Double getAvgPaidPriceProduct() throws Exception {
        if (this.productRepo.count() == 0) {throw new Exception("There are no products.");}
        double total = 0;
        double counter = 0;
        for (Product product : this.productRepo.findAll()) {
            total += product.getPaidPrice() * product.getQuantity();
            counter += product.getQuantity();
        }
        return total / counter;
    }

    /* Takes in no arguments, returns the standard deviation of the paid prices of the products */
    @RequestMapping(path="/stdPaidPriceProduct")
    public @ResponseBody Double getStdPaidPriceProduct() throws Exception {
        if (this.productRepo.count() == 0) {throw new Exception("There are no products.");}
        double total = 0;
        double counter = 0;
        for (Product product : this.productRepo.findAll()) {
            total += product.getPaidPrice() * product.getQuantity();
            counter += product.getQuantity();
        }
        double avg = total / counter;
        double sqSum = 0;
        for (Product product: this.productRepo.findAll()) {
            sqSum += (product.getPaidPrice() - avg) * (product.getPaidPrice() - avg) * product.getQuantity();
        }
        return Math.sqrt(sqSum / counter);
    }

    /* Takes in no arguments, returns mean of return gap */
    @RequestMapping(path="/averageReturnGap")
    public @ResponseBody Double getAvgReturnGap() throws Exception {
        if (this.returnRepo.count() == 0) {throw new Exception("There are no returns.");}
        double sum = 0;
        for (ReturnedProduct rp : this.returnRepo.findAll()) {
            sum += rp.getprGap();
        }
        return sum / this.returnRepo.count();

    }

    /* Takes in no arguments, returns standard deviation of return gaps */
    @RequestMapping(path="/stdReturnGap")
    public @ResponseBody Double getStdReturngap() throws Exception {
        if (this.returnRepo.count() == 0) {throw new Exception("There are no returns.");}
        double sum = 0;
        for (ReturnedProduct rp : this.returnRepo.findAll()) {
            sum += rp.getprGap();
        }
        double avg = sum / this.returnRepo.count();
        double sqSum = 0;
        for (ReturnedProduct rp : this.returnRepo.findAll()) {
            sqSum += (rp.getprGap() - avg) * (rp.getprGap() - avg);
        }
        return Math.sqrt(sqSum / this.returnRepo.count());
    }

    /* Takes in no arguments, returns mean of paid price of returns */
    @RequestMapping(path="/averagePaidPriceReturn")
    public @ResponseBody Double getAvgPaidPriceReturn() throws Exception {
        if (this.returnRepo.count() == 0) {throw new Exception("There are no returns.");}
        double sum = 0;
        for (ReturnedProduct rp : this.returnRepo.findAll()) {
            sum += rp.getPaidPrice() * rp.getQuantity();
        }
        return sum / this.returnRepo.count();
    }

    /* Takes in no arguments, returns standard deviation of paid prices of returns */
    @RequestMapping(path="/stdPaidPriceReturn")
    public @ResponseBody Double getStdPaidPriceReturn() throws Exception {
        if (this.returnRepo.count() == 0) {throw new Exception("There are no returns.");}
        double sum = 0;
        for (ReturnedProduct rp : this.returnRepo.findAll()) {
            sum += rp.getPaidPrice() * rp.getQuantity();
        }
        double avg = sum / this.returnRepo.count();
        double sqSum = 0;
        for (ReturnedProduct rp : this.returnRepo.findAll()) {
            double curr = rp.getPaidPrice() * rp.getQuantity();
            sqSum += (curr - avg) * (curr- avg);
        }
        return Math.sqrt(sqSum / this.returnRepo.count());
    }

    /* Takes in no arguments, returns mean of selling prices of returns */
    @RequestMapping(path="/averageSellingPriceReturn")
    public @ResponseBody Double getAvgSellingPriceReturn() throws Exception {
        if (this.returnRepo.count() == 0) {throw new Exception("There are no returns.");}
        double sum = 0;
        for (ReturnedProduct rp : this.returnRepo.findAll()) {
            sum += rp.getSellingPrice() * rp.getQuantity();
        }
        return sum / this.returnRepo.count();
    }

    /* Takes in no arguments, returns standard deviation of selling prices of returns */
    @RequestMapping(path="/stdSellingPriceReturn")
    public @ResponseBody Double getStdSellingdPriceReturn() throws Exception {
        if (this.returnRepo.count() == 0) {throw new Exception("There are no returns.");}
        double sum = 0;
        for (ReturnedProduct rp : this.returnRepo.findAll()) {
            sum += rp.getSellingPrice() * rp.getQuantity();
        }
        double avg = sum / this.returnRepo.count();
        double sqSum = 0;
        for (ReturnedProduct rp : this.returnRepo.findAll()) {
            double curr = rp.getSellingPrice() * rp.getQuantity();
            sqSum += (curr - avg) * (curr- avg);
        }
        return Math.sqrt(sqSum / this.returnRepo.count());
    }





}