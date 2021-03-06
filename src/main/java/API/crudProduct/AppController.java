package API.crudProduct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;
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
    @GetMapping(path="/nameId")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getNameID(@RequestParam Integer id) {
        if (this.productRepo.existsById(id) == false) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        return this.productRepo.findById(id).get().getName();
    }

    /* Get the quantity of the product with specified id */
    @GetMapping(path="/quantityId")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getQuantityID(@RequestParam Integer id) {
        if (this.productRepo.existsById(id) == false) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        return this.productRepo.findById(id).get().getQuantity();
    }

    /* Get selling price of item with specified id */
    @GetMapping(path="/sellingPriceId")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getSellingPriceID(@RequestParam Integer id) {
        if (this.productRepo.existsById(id) == false) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        return this.productRepo.findById(id).get().getSellingPrice();
    }

    /* Get paid price of the item with specified id */
    @GetMapping(path="/paidPriceId")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getPaidPriceID(@RequestParam Integer id) {
        if (this.productRepo.existsById(id) == false) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        return this.productRepo.findById(id).get().getPaidPrice();
    }

    /* Rename the product with the given id. */
    @PatchMapping(path="/renameId")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object renameID(@RequestParam Integer id, @RequestParam String newName) {

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
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        if (prod != null) {
            prod.setName(newName);
            this.productRepo.save(prod);
        }
        return "Renamed";
    }

    /* Change the quantity of the product with the given id. */
    @PatchMapping(path="/updateQuantityId")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object updateQuantityID(@RequestParam Integer id, @RequestParam Integer quantity)  {
        if (this.productRepo.existsById(id) == false) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        Product prod = this.productRepo.findById(id).get();
        prod.setQuantity(quantity);
        this.productRepo.save(prod);
        return "Updated";
    }

    /* Update paid price of specific product (specified by id). */
    @PatchMapping(path="/updatePaidPriceId")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object updatePaidPriceID(@RequestParam Integer id, @RequestParam Double paidPrice)  {

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
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        if (prod != null) {
            prod.setPaidPrice(paidPrice);
            this.productRepo.save(prod);
        }
        return "Updated";
    }

    /* Update selling price of product specified by id. */
    @PatchMapping(path="/updateSellingPriceId")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object updateSellingPriceID(@RequestParam Integer id, @RequestParam Double sellingPrice)  {
        if (this.productRepo.existsById(id) == false) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        Product prod = this.productRepo.findById(id).get();
        prod.setSellingPrice(sellingPrice);
        this.productRepo.save(prod);
        return "Updated";
    }

    /* Removes the product with the given id. */
    @DeleteMapping(path="/deleteId")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object removeProductID(@RequestParam Integer id) {
        if (this.productRepo.existsById(id) == false) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        this.productRepo.delete(this.productRepo.findById(id).get());
        return "Removed.";
    }

    /* Purchase `quantity` amount of the product specified by id */
    @PostMapping(path="/makePurchaseId")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object makePurchaseId(@RequestParam Integer id, @RequestParam Integer quantity)  {
        if (this.productRepo.existsById(id) == false) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        Product prod = this.productRepo.findById(id).get();
        if (prod.getQuantity() < quantity) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST);
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
        return "Purchased.";

    }

    /* Removes all products with the given name. */
    @DeleteMapping(path="/deleteName")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object removeProduct(@RequestParam String name)  {
        int count = 0;
        Iterable<Product> allProducts = this.productRepo.findAll();
        for (Product prod : allProducts) {
            if (prod.getName().equals(name)) {
                count++;
                this.productRepo.delete(prod);
            }
        }
        if (count == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        return "Deleted.";
    }

    /* Change the names of all products with the specified name. */
    @PatchMapping(path="/updateName")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object updateName(@RequestParam String name, @RequestParam String newName)  {
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        return "Updated.";
    }


    /* Change the selling price of all products with the given name by some offset. */
    @PatchMapping(path="/updateSellingPriceOffsetName")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object updateSellingPriceName(@RequestParam String name, @RequestParam Integer priceOffset) {
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        return "Updated.";
    }



    /* Calculate expected profit from selling all products with specified name. */
    @GetMapping(path="/expectedProfitFromProductName")
   // @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getProfitFromProduct(@RequestParam String name) {

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
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        return profit;
    }

    /* Return list of product-id's sorted by expected profit from selling out (ascending order) */
    @GetMapping(path="/listIdByProfitName")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getListIdByProfitName(@RequestParam String name) {
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        return idLst;
    }


    /* Return the number of products with the specified product name */
    @GetMapping(path="/productQuantityName")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getProductQuantityName(@RequestParam String name)  {
        int total = 0;
        Iterable<Product> allProducts = this.productRepo.findAll();
        for (Product prod: allProducts) {
            if (prod.getName().equals(name)) {
                total += prod.getQuantity();
            }
        }
        if (total == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        return total;
    }

    /* Get productID of purchase specified by datetime. */
    @GetMapping(path="/purchaseProductId")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getPurchaseProductId(@RequestParam String date) {
        if (this.purchaseRepo.existsById(date) == false) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        Purchase purchase = this.purchaseRepo.findById(date).get();
        return purchase.getProductName();
    }

    /* Get product name of purchase specified by datetime. */
    @GetMapping(path="/purchaseNameId")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getPurchaseName(@RequestParam String date)  {
        if (this.purchaseRepo.existsById(date) == false) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        Purchase purchase = this.purchaseRepo.findById(date).get();
        return purchase.getProductName();
    }

    /* Get profit of purchase specified by datetime. */
    @GetMapping(path="/purchaseProfit")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getPurchaseProfit(@RequestParam String date)  {
        if (this.purchaseRepo.existsById(date) == false) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        Purchase purchase = this.purchaseRepo.findById(date).get();
        return purchase.getProfit();
    }

    /* Get individual paid price of purchase's product specified by datetime. */
    @GetMapping(path="/purchaseIndPaidPrice")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getPaidPricePurchase(@RequestParam String date) {
        if (this.purchaseRepo.existsById(date) == false) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        Purchase purchase = this.purchaseRepo.findById(date).get();
        return purchase.getPaidPrice();
    }

    /* Get quantity of purchase's product, specified by datetime. */
    @GetMapping(path="/purchaseQuantity")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getPurchaseQuantity(@RequestParam String date) {
        if (this.purchaseRepo.existsById(date) == false) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        Purchase purchase = this.purchaseRepo.findById(date).get();
        return purchase.getQuantity();
    }

    /* Return a purchase, specified by datetime. */
    @GetMapping(path="/returnPurchase")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object returnPurchase(@RequestParam String date, @RequestParam Integer quantity)  {
        if (this.purchaseRepo.existsById(date) == false) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        Purchase purchase = this.purchaseRepo.findById(date).get();
        if (purchase.getQuantity() < quantity) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST);
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
        return "Returned.";
    }

    /* Returns the total profit made starting from the input date */
    @GetMapping(path="/profitStart")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getProfitStart(@RequestParam String date)  {
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        return profit;
    }

    /* Returns the total profit made before the input date */
    @GetMapping(path="/profitEnd")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getProfitEnd(@RequestParam String date) {
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        return profit;
    }

    /* Get productID, provided return's PR gap in days */
    @GetMapping(path="/returnPRGap")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object returnPRGap(@RequestParam String date)  {
        if (this.returnRepo.existsById(date) == false) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        ReturnedProduct returnObj = this.returnRepo.findById(date).get();
        return returnObj.getprGap();
    }


    /* Get productID, provided return's timestamp */
    @GetMapping(path="/returnProductID")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object returnProductID(@RequestParam String date) {
        if (this.returnRepo.existsById(date) == false) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        ReturnedProduct returnObj = this.returnRepo.findById(date).get();
        return returnObj.getProductId();
    }


    /* Get product name, provided return's timestamp */
    @GetMapping(path="/returnProductName")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object returnProductName(@RequestParam String date) {
        if (this.returnRepo.existsById(date) == false) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        ReturnedProduct returnObj = this.returnRepo.findById(date).get();
        return returnObj.getProductName();
    }

    /* Get product quantity, provided the return's timestamp*/
    @GetMapping(path="/returnQuantity")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object returnQuantity(@RequestParam String date) {
        if (this.returnRepo.existsById(date) == false) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        ReturnedProduct returnObj = this.returnRepo.findById(date).get();
        return returnObj.getQuantity();
    }

    /* Get selling price, provided the return's timestamp */
    @GetMapping(path="/returnSellingPrice")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object returnSellingPrice(@RequestParam String date) {
        if (this.returnRepo.existsById(date) == false) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        ReturnedProduct returnObj = this.returnRepo.findById(date).get();
        return returnObj.getSellingPrice();
    }

    /* Get paid price, provided the return's timestamp */
    @GetMapping(path="/returnPaidPrice")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object returnPaidPrice(@RequestParam String date) {
        if (this.returnRepo.existsById(date) == false) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        ReturnedProduct returnObj = this.returnRepo.findById(date).get();
        return returnObj.getPaidPrice();
    }

    /* Process a return; add the product back to the product table with specified selling price */
    @RequestMapping(path="/processReturn")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object processReturn(@RequestParam String date, @RequestParam Double sellingPrice)  {
        if (this.returnRepo.existsById(date) == false) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
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
        return "Processed.";

    }

    /* Discards a return, specified by datetime */
    @DeleteMapping(path="/removeReturn")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object removeReturn(@RequestParam String date){
        if (this.returnRepo.existsById(date) == false) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        ReturnedProduct returnObj = this.returnRepo.findById(date).get();
        this.returnRepo.delete(returnObj);
        return "Updated.";
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
    @GetMapping(path="/expectedProfitMax")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getExpectedProfitMax(@RequestParam Integer quantity)  {

        if (countProducts() < quantity) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        if (quantity <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST);
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
    @GetMapping(path="/expectedProfitMin")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getExpectedProfitMin(@RequestParam Integer quantity) {

        if (countProducts() < quantity) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        if (quantity <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST);
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
    @GetMapping(path="/checkInventoryLowerBound")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object checkInventoryLowerBound(@RequestParam Integer quantity)  {
        if (countProducts() < quantity) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        if (quantity <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST);
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
    @GetMapping(path="/checkInventoryUpperBound")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object checkInventoryUpperBound(@RequestParam Integer quantity) {
        if (countProducts() < quantity) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        if (quantity <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST);
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
    @GetMapping(path="/individualItemFilterLowerBound")
    @ResponseStatus(HttpStatus.OK)
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
    @GetMapping(path="/individualItemFilterUpperBound")
    @ResponseStatus(HttpStatus.OK)
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
    @GetMapping(path= "/totalProfit")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Double getTotalProfit() {
        double total = 0;
        for (Purchase purchase : this.purchaseRepo.findAll()) {
            total += purchase.getProfit();
        }
        return total;
    }

    /* Takes in no arguments, returns the average profit per purchase */
    @GetMapping(path="/averageProfitPerPurchase")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getAvgProfitPerPurchase()  {
        if (this.purchaseRepo.count() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        double total = 0;
        for (Purchase purchase : this.purchaseRepo.findAll()) {
            total += purchase.getProfit();
        }
        return total / this.purchaseRepo.count();
    }

    /* Takes in no arguments, returns the standard deviation of the profits of the purchases  so far */
    @GetMapping(path="/stdProfitPurchase")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getStdProfitPerPurchase()  {
        if (this.purchaseRepo.count() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
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
    @GetMapping(path="/averagePaidPricePurchase")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getAvgPaidPricePurchase() {
        if (this.purchaseRepo.count() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        double total = 0;
        for (Purchase purchase : this.purchaseRepo.findAll()) {
            total += purchase.getPaidPrice() * purchase.getQuantity();
        }
        return total / this.purchaseRepo.count();
    }

    /* Takes in no arguments, returns the standard deviation of paid prices of all purchases made*/
    @GetMapping(path="/stdPaidPricePurchase")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getStdPaidPricePurchase() {
        if (this.purchaseRepo.count() == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND);
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
    @GetMapping(path="/listIdByProfit")
    @ResponseStatus(HttpStatus.OK)
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
    @GetMapping(path="/allPurchases")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Iterable<Purchase> getAllPurchases() {
        return this.purchaseRepo.findAll();
    }

    /* Return all Returns in the returned_product table */
    @GetMapping(path="/allReturns")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Iterable<ReturnedProduct> getAllReturns() {
        return this.returnRepo.findAll();
    }

    /* Return all products in product table. */
    @GetMapping(path="/allProducts")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Iterable<Product> getAllProducts() {
        /* Returns a JSON or XML with the products */
        return this.productRepo.findAll();
    }

    /* Add a product to the product table. */
    @PostMapping(path="/add")
    @ResponseStatus(HttpStatus.OK)
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
    @GetMapping(path="/expectedProfit")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Double getProfit() {
        double profit = 0;
        Iterable<Product> allProducts = this.productRepo.findAll();
        for (Product prod : allProducts) {
            profit += prod.getQuantity() * (prod.getSellingPrice() - prod.getPaidPrice());
        }
        return profit;
    }

    /*  Delete all entries in the product and purchase tables. */
    @DeleteMapping(path="/clear")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody void clear() {
        this.db.resetID();
    }

    /*  Takes in no arguments, returns the average selling prices of the products for sale */
    @GetMapping(path="/averageSellingPriceProduct")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getAvgSellingPriceProduct() {
        if (this.productRepo.count() == 0) { return ResponseEntity.status(HttpStatus.NOT_FOUND);}
        double total = 0;
        double counter = 0;
        for (Product product : this.productRepo.findAll()) {
            total += product.getSellingPrice() * product.getQuantity();
            counter += product.getQuantity();
        }
        return total / counter;
    }

    /* Takes in no arguments, returns the standard deviation of the selling prices of the products */
    @GetMapping(path="/stdSellingPriceProduct")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getStdSellingPriceProduct()  {
        if (this.productRepo.count() == 0) { return ResponseEntity.status(HttpStatus.NOT_FOUND);}
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
    @GetMapping(path="/averagePaidPriceProduct")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getAvgPaidPriceProduct()  {
        if (this.productRepo.count() == 0) { return ResponseEntity.status(HttpStatus.NOT_FOUND);}
        double total = 0;
        double counter = 0;
        for (Product product : this.productRepo.findAll()) {
            total += product.getPaidPrice() * product.getQuantity();
            counter += product.getQuantity();
        }
        return total / counter;
    }

    /* Takes in no arguments, returns the standard deviation of the paid prices of the products */
    @GetMapping(path="/stdPaidPriceProduct")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getStdPaidPriceProduct() {
        if (this.productRepo.count() == 0) { return ResponseEntity.status(HttpStatus.NOT_FOUND);}
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
    @GetMapping(path="/averageReturnGap")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getAvgReturnGap() {
        if (this.returnRepo.count() == 0) {return ResponseEntity.status(HttpStatus.NOT_FOUND);}
        double sum = 0;
        for (ReturnedProduct rp : this.returnRepo.findAll()) {
            sum += rp.getprGap();
        }
        return sum / this.returnRepo.count();

    }

    /* Takes in no arguments, returns standard deviation of return gaps */
    @GetMapping(path="/stdReturnGap")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getStdReturngap() {
        if (this.returnRepo.count() == 0) {return ResponseEntity.status(HttpStatus.NOT_FOUND);}
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
    @GetMapping(path="/averagePaidPriceReturn")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getAvgPaidPriceReturn() {
        if (this.returnRepo.count() == 0) {return ResponseEntity.status(HttpStatus.NOT_FOUND);}
        double sum = 0;
        for (ReturnedProduct rp : this.returnRepo.findAll()) {
            sum += rp.getPaidPrice() * rp.getQuantity();
        }
        return sum / this.returnRepo.count();
    }

    /* Takes in no arguments, returns standard deviation of paid prices of returns */
    @GetMapping(path="/stdPaidPriceReturn")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getStdPaidPriceReturn() {
        if (this.returnRepo.count() == 0) {return ResponseEntity.status(HttpStatus.NOT_FOUND);}
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
    @GetMapping(path="/averageSellingPriceReturn")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getAvgSellingPriceReturn() {
        if (this.returnRepo.count() == 0) { return ResponseEntity.status(HttpStatus.NOT_FOUND);}
        double sum = 0;
        for (ReturnedProduct rp : this.returnRepo.findAll()) {
            sum += rp.getSellingPrice() * rp.getQuantity();
        }
        return sum / this.returnRepo.count();
    }

    /* Takes in no arguments, returns standard deviation of selling prices of returns */
    @GetMapping(path="/stdSellingPriceReturn")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Object getStdSellingdPriceReturn() {
        if (this.returnRepo.count() == 0) {return ResponseEntity.status(HttpStatus.NOT_FOUND);}
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