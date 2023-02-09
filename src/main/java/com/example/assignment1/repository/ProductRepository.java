package com.example.assignment1.repository;

import com.example.assignment1.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer>{
    @Query(value="SELECT * FROM product p WHERE p.owner_user_id = ?1 and p.sku=?2",
            nativeQuery = true)
    Product findProductByownerUserIdAndSku(Integer ownerUserId,String sku);

    @Query(value="SELECT * FROM product p WHERE p.id!=?1 and p.owner_user_id = ?2 and p.sku=?3",
            nativeQuery = true)
    Product checkProductSku(Integer id,Integer ownerUserId,String sku);

}
