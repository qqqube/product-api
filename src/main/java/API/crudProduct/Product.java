package API.crudProduct;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity /* This tells Hibernate to make a table out of this class. */
public class Product {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO) /* Provides for the specification of generation strategies for primary keys*/
    private int id;
    private String name;
    private int quantity;
    private double paidPrice;
    private double sellingPrice;

    public int getID() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setQuantity(Integer count) {
        this.quantity = count;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setPaidPrice(Double price) {
        this.paidPrice = price;
    }

    public Double getPaidPrice() {
        return this.paidPrice;
    }

    public void setSellingPrice(Double price) {
        this.sellingPrice = price;
    }

    public Double getSellingPrice() {
        return this.sellingPrice;
    }

}