package API.crudProduct;

import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Integer>{
    /* Auto implemented by Spring into a Bean called credRepository. */
    /* CRUD stands for Create, Read, Update, Delete */

}
