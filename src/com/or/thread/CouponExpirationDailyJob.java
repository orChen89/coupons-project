package com.or.thread;

import com.or.constants.Constants;
import com.or.dao.CouponDAO;
import com.or.errors.ApplicationException;
import com.or.facade.CompanyFacade;
import com.or.model.Coupon;
import com.or.util.CouponUtil;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CouponExpirationDailyJob implements Runnable {

    private static final CompanyFacade companyFacade = CompanyFacade.instance;
    private static final CouponDAO couponDao = CouponDAO.instance;
    private final DailyJob dailyJob = new DailyJob();


    //------------------------------------------Daily job task config-----------------------------------------------

    @Override
    public void run() {

        System.out.println();
        System.out.println(Constants.ANSI_ORANGE + "Thread: " + dailyJob.getName() + " started to run" + Constants.ANSI_DEFAULT_RESET);
        System.out.println();
        while (!dailyJob.rubJob()) {
            try {
                for (Coupon coupon : couponDao.readAll()) {
                    //Checking for each coupon in the database if expired
                    if (CouponUtil.isCouponExpired(coupon.getEndDate())) {

                        //Deleting the expired coupon from the coupons table in SQL & from purchase table
                        companyFacade.deleteCoupon(coupon.getId());

                        System.out.println("The coupon: " + coupon.getTitle() +
                                " has been deleted due to its expiration date: " + coupon.getEndDate());
                    }
                }
            } catch (ApplicationException e) {
                System.err.println(e);
            }
            try {
                System.out.println(Constants.ANSI_ORANGE + Thread.currentThread() + " went to sleep for 24 hours" +
                        Constants.ANSI_DEFAULT_RESET);
                System.out.println();
                //Setting the sleep period to 24 hours
                Thread.sleep(Constants.DAILY_JOB_SLEEP_PERIOD);
            } catch (InterruptedException ex) {
                System.err.println(ex);
            }
        }
    }
}
