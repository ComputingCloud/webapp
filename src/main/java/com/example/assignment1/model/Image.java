package com.example.assignment1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="image_id",nullable = false)
    @JsonProperty(value="image_id" ,access = JsonProperty.Access.READ_ONLY)
    private Integer imageId;

    @Column(name="product_id",nullable = false)
    @JsonProperty("product_id")
    private Integer productId;

    @JsonProperty("file_name")
    @Column(name="file_name",nullable = false)
    private String fileName;

    @JsonProperty(value ="date_created",access = JsonProperty.Access.READ_ONLY)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date_created")
    private LocalDateTime dateCreated;


    @JsonProperty(value="s3_bucket_path",access = JsonProperty.Access.READ_ONLY)
    @Column(name="s3_bucket_path",nullable = false)
    private String s3BucketPath;



}
