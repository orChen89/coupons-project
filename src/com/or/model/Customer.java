package com.or.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class Customer {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<Coupon> coupons;


    public Customer(Long id, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public Customer(Long id) {
        this.id = id;
    }

    public Customer(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }
}
