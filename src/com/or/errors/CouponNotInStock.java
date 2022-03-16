package com.or.errors;

import com.or.enums.EntityType;
import com.or.model.Coupon;

public class CouponNotInStock extends ApplicationException {

    public CouponNotInStock(final EntityType entityType, final Coupon coupon) {
        super(entityType + ": " + coupon.getTitle() + " with id number: " + coupon.getId() +"," +  " is currently not in stock");
    }
}
