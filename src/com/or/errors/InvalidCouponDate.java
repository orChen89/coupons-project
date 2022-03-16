package com.or.errors;

public class InvalidCouponDate extends ApplicationException {

    public InvalidCouponDate() {
        super("The inserted date of current coupon is invalid");
    }
}
