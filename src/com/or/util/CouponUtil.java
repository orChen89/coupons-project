package com.or.util;

import com.or.model.Coupon;

import java.sql.Date;
import java.time.LocalDate;

public class CouponUtil {

    //This method is decreasing a specific coupon amount by 1
    public static void decreaseCouponAmount(final Coupon coupon) {
        int prevAmount = coupon.getAmount();
        coupon.setAmount(prevAmount - 1);
    }

    //This method is setting a specific coupon amount
    public static void setCouponAmount(final Coupon coupon, final int amount) {
        coupon.setAmount(amount);
    }

    //This method is checking if a coupon end date is expired
    public static boolean isCouponExpired(final Date date){
        LocalDate today = LocalDate.now();
        return date.before(Date.valueOf(today));
    }

    //This method is checking if a coupon date is past today
    public static boolean isCouponDateValid(final Date date){
        LocalDate today = LocalDate.now();
        return date.after(Date.valueOf(today));
    }
}
