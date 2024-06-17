package org.mcs.mcsproductservice.repository;

import org.mcs.mcsproductservice.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    Product findProductById(Long id);
    Product findProductByName(String name);

}
