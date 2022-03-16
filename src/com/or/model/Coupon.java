package com.or.model;

import com.or.enums.Categories;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.sql.Date;

@Data
@ToString
@NoArgsConstructor
public class Coupon {

    private Long id;
    private Long companyID;
    private Categories category;
    private String title;
    private String description;
    private Date startDate;
    private Date endDate;
    private Integer amount;
    private Double price;
    private String image;

    public Coupon(Long id, Long companyID, Categories category, String title, String description,
                  Date startDate, Date endDate, Integer amount, Double price, String image) {
        this.id = id;
        this.companyID = companyID;
        this.category = category;
        this.title = title;
        this.description = description;
        this.startDate =  startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.price = price;
        this.image = image;
    }

    public Coupon(Long companyID, Categories category, String title, String description, Date startDate,
                  Date endDate, Integer amount, Double price, String image) {
        this.companyID = companyID;
        this.category = category;
        this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amount = amount;
        this.price = price;
        this.image = image;
    }
}
