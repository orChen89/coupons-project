package com.or.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class Company {

    private Long id;
    private String name;
    private String email;
    private String password;
    private List<Coupon> coupons = new ArrayList<>();

    public Company(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
    public Company(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Company(Long id, String name, String email, String password, List<Coupon> coupons) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.coupons = coupons;
    }
}
