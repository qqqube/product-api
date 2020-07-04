package API.crudProduct;

import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class DatabaseUpdates {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void resetID() {
        String query1 = "DROP TABLE IF EXISTS hibernate_sequence";
        String query2 = "DROP TABLE IF EXISTS product";
        String query3 = "DROP TABLE IF EXISTS purchase";
        String query4 = "DROP TABLE IF EXISTS returned_product";
        String query5 = "CREATE TABLE product (\n" +
                "\tid INT AUTO_INCREMENT NOT NULL,\n" +
                "\tname VARCHAR(255),\n" +
                "\tpaid_price DOUBLE NOT NULL,\n" +
                "\tquantity INT NOT NULL,\n" +
                "\tselling_price DOUBLE NOT NULL,\n" +
                "\tPRIMARY KEY(id)\n" +
                ")";
        String query6 = "CREATE TABLE purchase (\n" +
                "\ttimestamp VARCHAR(255) NOT NULL,\n" +
                "\tpaid_price DOUBLE NOT NULL,\n" +
                "\tproductid INT NOT NULL,\n" +
                "\tproduct_name VARCHAR(255),\n" +
                "\tprofit DOUBLE NOT NULL,\n" +
                "\tquantity INT NOT NULL,\n" +
                "\tPRIMARY KEY(timestamp)\n" +
                ")";
        String query7 = "CREATE TABLE hibernate_sequence (\n" +
                "\tnext_val BIGINT \n" +
                ")";
        String query8 = "INSERT INTO hibernate_sequence VALUES (1)";
        String query9 = "CREATE TABLE returned_product (\n" +
                "\treturn_timestamp VARCHAR(255) NOT NULL, paid_price DOUBLE NOT NULL,\n" +
                "\tproduct_id INT NOT NULL, product_name VARCHAR(255), quantity INT NOT NULL,\n" +
                "\tselling_price DOUBLE NOT NULL, pr_gap BIGINT NOT NULL, PRIMARY KEY(return_timestamp))";

        entityManager.createNativeQuery(query1).executeUpdate();
        entityManager.createNativeQuery(query2).executeUpdate();
        entityManager.createNativeQuery(query3).executeUpdate();
        entityManager.createNativeQuery(query4).executeUpdate();
        entityManager.createNativeQuery(query5).executeUpdate();
        entityManager.createNativeQuery(query6).executeUpdate();
        entityManager.createNativeQuery(query7).executeUpdate();
        entityManager.createNativeQuery(query8).executeUpdate();
        entityManager.createNativeQuery(query9).executeUpdate();

    }
}
