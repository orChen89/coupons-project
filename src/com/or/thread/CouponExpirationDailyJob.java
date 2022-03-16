package com.or.thread;

import com.or.constants.Constants;
import com.or.dao.CouponDAO;
import com.or.errors.CrudException;
import com.or.model.Coupon;
import com.or.util.CouponUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CouponExpirationDailyJob implements Runnable {

    private static final CouponDAO couponDAO = CouponDAO.instance;
    private final DailyJob dailyJob = new DailyJob();


    //------------------------------------------Daily job task config-----------------------------------------------

    @Override
    public void run() {

        System.out.println("Thread: " + dailyJob.getName() + " started to run");
        while (!dailyJob.rubJob()) {
            try {
                for (Coupon coupon : couponDAO.readAll()) {
                    //Checking for each coupon in the database if expired
                    if (CouponUtil.isCouponExpired(coupon.getEndDate())) {
                        //Setting a coupon Id in a variable
                        Long couponId = coupon.getId();
                        //Setting a customer Id in a variable
                        Long customerId = couponDAO.readCouponPurchaseByCouponId(coupon.getId());
                        //Deleting the expired coupon from the purchase table in SQL
                        couponDAO.deleteCouponPurchase(customerId, couponId);
                        //Deleting the expired coupon from the coupons table in SQL
                        couponDAO.delete(couponId);
                        System.out.println("The coupon: " + coupon.getTitle() +
                                " has been deleted due to its expiration date" + coupon.getEndDate());
                    }
                }
            } catch (CrudException e) {
                System.err.println(e);
            }
            try {
                System.out.println(Thread.currentThread() + " went to sleep for 24 hours");
                System.out.println();
                //Setting the sleep period to 24 hours
                Thread.sleep(Constants.DAILY_JOB_SLEEP_PERIOD);
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }
    }
}
