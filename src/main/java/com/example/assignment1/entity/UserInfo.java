package com.example.assignment1.entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*

//import javax.persistence.Entity;
@Data
@Setter
@Getter
@Entity
@Table()
public class UserInfo {
@Id
@GeneratedValue
    private int id;

    @JsonProperty("first_name")
    private String fName;

    @JsonProperty("last_name")
    private String lName;
    @JsonProperty("user_name")
    private String emailID;
    private String password;

}
