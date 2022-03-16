package com.or.errors;

import com.or.enums.EntityType;
import com.or.model.Coupon;

public class CouponAlreadyPurchased extends ApplicationException {

    public CouponAlreadyPurchased(final EntityType entityType, final Coupon coupon, final EntityType entityType2) {
        super(entityType + ": " + coupon.getTitle() + " with id: " + coupon.getId() +
              " has already been purchased by this " + entityType2);
    }
}
