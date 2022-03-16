package com.or.facade;

import com.or.dao.CompanyDAO;
import com.or.dao.CouponDAO;
import com.or.enums.EntityType;
import com.or.errors.*;
import com.or.model.Company;
import com.or.model.Coupon;
import com.or.model.Customer;
import com.or.util.CouponUtil;
import com.or.util.InputUserValidation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CouponFacade {

    public static final CouponFacade instance = new CouponFacade();
    private final CouponDAO couponDAO;
    private final CustomerFacade customerFacade;
    private final CompanyDAO companyDAO;
    private final CompanyFacade companyFacade;

    private CouponFacade(){
        this.couponDAO = CouponDAO.instance;
        this.customerFacade = CustomerFacade.instance;
        this.companyDAO = CompanyDAO.instance;
        this.companyFacade = CompanyFacade.instance;
    }

    public Long createCoupon(final Coupon coupon) throws ApplicationException {

        if(coupon.getId() != null){
           throw new EntityExistException(EntityType.COUPON);
        }

        if (!InputUserValidation.isDateValid((coupon.getStartDate()))) {
           throw new UserValidationException();
        }

        if (!InputUserValidation.isDateValid((coupon.getEndDate()))) {
           throw new UserValidationException();
        }

        Long newCouponId = couponDAO.create(coupon);
        System.out.println("The new coupon: " + coupon.getTitle() + " has been created successfully!");

        Company company = companyDAO.read(coupon.getCompanyID());
        company.getCoupons().add(coupon);

        return newCouponId;
    }

    public Coupon getCoupon(final Long couponId) throws ApplicationException {

        Coupon coupon = couponDAO.read(couponId);

        if(coupon.getId() == null){
           throw new EntityNotExistException(EntityType.COUPON);
        }

        return coupon;
    }

    public void deleteCoupon(final Long couponId) throws ApplicationException {

        if (getCoupon(couponId).getId() == null) {
            throw new EntityNotExistException(EntityType.COUPON);
        } else {
            couponDAO.delete(couponId);
            System.out.println("The selected coupon has been deleted");
        }
    }

    public void updateCoupon(final Coupon coupon) throws ApplicationException {

        if (coupon.getId() == null) {
             throw new EntityNotExistException(EntityType.COUPON);
        } else {
            couponDAO.update(coupon);
            System.out.println("Coupon: " + coupon.getTitle() + "," + " updated");
        }
    }

    public List<Coupon> getAllCoupons() throws ApplicationException {

        List<Coupon> coupons = new ArrayList<>(couponDAO.readAll());

        if(coupons == null){
            throw new EntityNotExistException(EntityType.COUPON);
        }
        System.out.println(coupons);
        return coupons;
    }

    public void addCouponPurchase(final Long customerID, final Long couponID) throws ApplicationException {

        Coupon currentCoupon = getCoupon(couponID);
        Customer currentCustomer = customerFacade.getCustomer(customerID);
        Long couponIdForPurchase = getCoupon(couponID).getId();
        Long customerIdForPurchase = customerFacade.getCustomer(customerID).getId();
        List<Coupon> currentCustomerCoupons = customerFacade.getCustomer(customerID).getCoupons();
        List<Coupon> updatedCustomerCoupons = new ArrayList<>();

        if (couponIdForPurchase == null) {
            throw new EntityNotExistException(EntityType.COUPON);
        }

        if (customerIdForPurchase == null) {
            throw new EntityNotExistException(EntityType.CUSTOMER);
        }

        if(currentCustomerCoupons != null) {
            for (Coupon coupon : currentCustomerCoupons) {
                if (Objects.equals(coupon.getId(), couponID) && coupon.getTitle().equals(currentCoupon.getTitle())) {
                    throw new CouponAlreadyPurchased(EntityType.COUPON, currentCoupon, EntityType.CUSTOMER);
                }
            }
        }
        if (currentCoupon.getAmount() <= 0){
            throw new CouponNotInStock(EntityType.COUPON, currentCoupon);
        }

        if(CouponUtil.isCouponExpired(currentCoupon.getEndDate())){
            throw new CouponExpirationDateArrived(currentCoupon);
        }

        couponDAO.addCouponPurchase(customerID, couponID);
        CouponUtil.decreaseCouponAmount(currentCoupon);
        updatedCustomerCoupons.add(currentCoupon);
        currentCustomer.setCoupons(updatedCustomerCoupons);
        updateCoupon(currentCoupon);

        System.out.println("The coupon: " + currentCoupon.getTitle() + " with id: " + couponID +
                           " has been purchased by customer: " + currentCustomer.getFirstName() + " " +
                           currentCustomer.getLastName() + " with id: " + customerID);
    }

    public void deleteCouponPurchase(final Long customerID, final Long couponID) throws ApplicationException {

        Coupon currentCoupon = getCoupon(couponID);

        if (getCoupon(couponID).getId() == null) {
            throw new EntityNotExistException(EntityType.COUPON);
        }

        if (customerFacade.getCustomer(customerID).getId() == null) {
            throw new EntityNotExistException(EntityType.CUSTOMER);
        }

        couponDAO.deleteCouponPurchase(customerID, couponID);

        System.out.println("The coupon: " + currentCoupon.getTitle() + " with id: " + couponID +
                " which purchased by customer id: " + customerID + " has been deleted successfully!");
    }
}
