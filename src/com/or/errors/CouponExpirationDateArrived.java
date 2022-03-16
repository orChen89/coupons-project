package com.or.errors;

import com.or.model.Coupon;

public class CouponExpirationDateArrived extends ApplicationException {

    public CouponExpirationDateArrived(final Coupon coupon) {
        super("This coupon id: " + coupon.getId() + "," + " is expired!");
    }
}
