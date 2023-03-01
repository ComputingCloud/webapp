package com.example.assignment1.repository;

import java.util.List;

import com.example.assignment1.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    @Query(value="SELECT * FROM IMAGE WHERE product_id =?",
            nativeQuery = true)
    List<Image> findImageByProductId(Integer productId);
}