package com.example.assignment1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
public class Product {

    @Id
    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )
    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;

    @JsonProperty("pName")
    @NotEmpty(message = "ProductName cannot be Null/Empty")
    private String pName;


    @JsonProperty("pDescription")
    @NotEmpty(message = "Description cannot be Null/Empty")
    private String pDescription;

    @JsonProperty("sku")
    @NotEmpty(message = "Sku cannot be Null/Empty")
    private String sku;

    @JsonProperty("pManufacturer")
    @NotEmpty(message = "Manufacturer cannot be Empty/Null")
    private String pManufacturer;

    @JsonProperty("pQuantity")
    @Min(value = 0, message = "Quantity must be greater than 1" )
    @Max(value = 100, message = "Quantity must be less than 100")
    private int pQuantity;

    @JsonProperty(value ="date_added",access = JsonProperty.Access.READ_ONLY)
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date_added")
    private LocalDateTime dateAdded;;;;;;;;;;

    @JsonProperty(value = "date_last_updated",access = JsonProperty.Access.READ_ONLY)
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date_last_updated")
    private LocalDateTime dateLastUpdated;

    @JsonProperty(value="owner_user_id",access = JsonProperty.Access.READ_ONLY)
    @Column(name="owner_user_id")
    private int ownerUserId;


}
