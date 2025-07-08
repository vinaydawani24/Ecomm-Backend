package com.vinay.ecomm.repoistory;

import com.vinay.ecomm.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product,Long> {

    List<Product> findByCategoryAndPriceLessThan(String detectedCategory,double maxPrice);
}
