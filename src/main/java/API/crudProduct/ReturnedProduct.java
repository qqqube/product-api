package API.crudProduct;

import javax.persistence.Id;
import java.util.Date;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class ReturnedProduct {

    @Id
    private String returnTimestamp;
    private String productName;
    private int quantity;
    private double paidPrice;
    private double sellingPrice;
    private int productId;
    private long prGap; /* Time between purchase and return in days. */

    public void setPRGap(long gap) {
        this.prGap = gap;
    }

    public long getprGap() {
        return this.prGap;
    }

    public Integer getProductId() {
        return this.productId;
    }

    public void setProductId(Integer id) {
        this.productId = id;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public double getSellingPrice() {
        return this.sellingPrice;
    }

    public void setReturnTimestamp() {
        LocalDateTime curr = LocalDateTime.now();
        this.returnTimestamp = curr.toString();
    }

    public String getReturnTimestamp() {
        return this.returnTimestamp;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setPaidPrice(Double paidPrice) {
        this.paidPrice = paidPrice;
    }

    public double getPaidPrice() {
        return this.paidPrice;
    }

}
