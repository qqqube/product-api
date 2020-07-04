package API.crudProduct;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.time.LocalDateTime;

@Entity
public class Purchase {

    @Id
    private String timestamp;
    private int productID;
    private String productName;
    private double profit;
    private double paidPrice;
    private int quantity;


    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setPaidPrice(Double price) {
        this.paidPrice = price;
    }
    public double getPaidPrice() {
        return this.paidPrice;
    }
    public int getProductID() {
        return this.productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return this.productName;
    }
    public void setProductName(String name) {
        this.productName = name;
    }
    public double getProfit() {
        return this.profit;
    }
    public void setProfit(double profit) {
        this.profit = profit;
    }
    public String getDate() {
        return this.timestamp;
    }
    public void setDate() {
        LocalDateTime curr = LocalDateTime.now();
        this.timestamp = curr.toString();
    }
}
