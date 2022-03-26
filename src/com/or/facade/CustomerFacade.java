package com.or.facade;

import com.or.dao.CouponDAO;
import com.or.dao.CustomerDAO;
import com.or.enums.Categories;
import com.or.enums.EntityType;
import com.or.errors.*;
import com.or.model.Coupon;
import com.or.model.Customer;
import com.or.util.CouponUtil;
import com.or.util.InputUserValidation;
import java.util.*;

public class CustomerFacade extends ClientFacade {

    public static final CustomerFacade instance = new CustomerFacade();
    private final CustomerDAO customerDAO;
    private final CouponDAO couponDAO;

    //Creating a private constructor
    private CustomerFacade() {
        //Injecting the entities instances with creation of this class
        this.customerDAO = CustomerDAO.instance;
        this.couponDAO = CouponDAO.instance;
    }

    //------------------------------------------Customer Login------------------------------------------------------

    @Override
    public boolean login(final String email, final String password) throws ApplicationException {

        //Checking if email address is valid according to Email REGEX
        if (!InputUserValidation.isEmailValid(email)) {
            throw new UserValidationException();
        }

        //Checking if hashed password address is valid according to password REGEX
        if (!InputUserValidation.isPasswordValid(password)) {
            throw new UserValidationException();
        }
        //Getting and Setting a customer in a variable according to email
        Customer customer = customerDAO.readByEmail(email);
        if (customer == null) {
            throw new EntityNotExistException(EntityType.CUSTOMER);
        }

        //Setting customer's email to variable
        String customerEmail = customer.getEmail();

        //Setting customer's password to variable
        String customerPassword = customer.getPassword();

        //Setting the injected password to check into hashcode password and placing in a variable
        long hashedPasswordTCheck = password.hashCode();

        //Checking if email and password are matching to the customer
        return email.equals(customerEmail) && String.valueOf(hashedPasswordTCheck).equals(customerPassword);
    }

    //------------------------------------------Creating new purchase------------------------------------------------

    public Long addCouponPurchase(final Long customerID, final Long couponID) throws ApplicationException {

        //Setting the specific coupon
        Coupon currentCoupon = couponDAO.read(couponID);
        //Setting the customer coupon
        Customer currentCustomer = customerDAO.read(customerID);
        //Getting the specific coupon's Id
        Long couponIdForPurchase = couponDAO.read(couponID).getId();
        //Setting the specific customer's Id
        Long customerIdForPurchase = customerDAO.read(customerID).getId();
        //Setting the customer's coupons list
        List<Coupon> currentCustomerCoupons = getAllCoupons(customerID);

        List<Coupon> updatedCustomerCoupons = new ArrayList<>();

        //Checking if the specific coupon is not exist
        if (couponIdForPurchase == null) {
            throw new EntityNotExistException(EntityType.COUPON);
        }

        //Checking if the specific customer is not exist
        if (customerIdForPurchase == null) {
            throw new EntityNotExistException(EntityType.CUSTOMER);
        }

        //Checking if the customer's coupon list is not empty
        if (currentCustomerCoupons != null) {
            for (Coupon coupon : currentCustomerCoupons) {
                //Checking for each coupon in the list if already exists
                if (Objects.equals(coupon.getId(), couponID) && coupon.getTitle().equals(currentCoupon.getTitle())) {
                    throw new CouponAlreadyPurchased(EntityType.COUPON, currentCoupon, EntityType.CUSTOMER);
                }
            }
        }

        //Checking if the coupon is not in stock
        if (currentCoupon.getAmount() <= 0) {
            throw new CouponNotInStock(EntityType.COUPON, currentCoupon);
        }

        //Checking if the coupon is already expired
        if (CouponUtil.isCouponExpired(currentCoupon.getEndDate())) {
            throw new CouponExpirationDateArrived(currentCoupon);
        }

        //Creating a new coupon purchase
        couponDAO.addCouponPurchase(customerID, couponID);
        //Decreasing coupon amount
        CouponUtil.decreaseCouponAmount(currentCoupon);
        //Adding the updated coupon to a list
        updatedCustomerCoupons.add(currentCoupon);
        //Setting the updated coupons list of the specific customer
        currentCustomer.setCoupons(updatedCustomerCoupons);
        //Updating the coupon with new amount to our database
        couponDAO.update(currentCoupon);

        System.out.println("The coupon: " + "~" + currentCoupon.getTitle() + "~" + " with id: " + couponID + "," +
                " has been purchased by customer: " + "~" + currentCustomer.getFirstName() + " " +
                currentCustomer.getLastName() + "~" + " with id: " + customerID);

        return currentCustomer.getId();
    }

    //--------------------------Getting all coupons of specific customer-------------------------------------------

    public List<Coupon> getAllCoupons(final Long customerId) throws ApplicationException {

        Customer customer = customerDAO.read(customerId);

        //Setting the coupons list according the specific customer
        List<Long> idList = couponDAO.readCouponsByCustomerId(customerId);

        List<Coupon> coupons = new ArrayList<>();

        //Checking if coupons are not exist
        if (coupons == null) {
            throw new EntityNotExistException(EntityType.COUPON);
        }

        for (Long id : idList) {
            coupons.add(couponDAO.read(id));
        }
        customer.setCoupons(coupons);

        return coupons;
    }

    //-----------------------------------Getting all coupons by category-------------------------------------------

    public List<Coupon> getCouponsByCategory(final Long customerId, final Categories category) throws ApplicationException {

        List<Coupon> couponsOfCustomerCategories = new ArrayList<>();

        //Setting the customer's coupons of specific customer
        List<Coupon> coupons = getAllCoupons(customerId);
        //Checking if the coupons are not exists
        if (coupons == null) {
            throw new EntityNotExistException(EntityType.COUPON);
        }
        for (Coupon coupon : coupons) {
            //Checking for each coupon if it is from same category
            if (coupon.getCategory().equals(category)) {
                //Adding the specific coupon to a list
                couponsOfCustomerCategories.add(coupon);
                break;
            }
            System.out.println("This customer doesn't holds coupons from this category!");
            break;
        }
        return couponsOfCustomerCategories;
    }

    //-----------------------------------Getting all coupons according to price------------------------------------

    public List<Coupon> getCouponByMaxPrice(final Long customerId, final double maxPrice) throws ApplicationException {

        List<Coupon> couponsByMax = new ArrayList<>();

        //Setting the customer's coupons of specific customer
        List<Coupon> coupons = getAllCoupons(customerId);
        //Checking if the coupons are not exists
        if (coupons == null) {
            throw new EntityNotExistException(EntityType.COUPON);
        }
        for (Coupon coupon : coupons) {
            if (coupon.getPrice() <= maxPrice) {
                //Adding the specific coupons according the selected price limit
                couponsByMax.add(coupon);
            } else {
                System.out.println("There is no any coupons less or equal the inserted price!");
            }
        }
        return couponsByMax;
    }

    //-----------------------------------Getting a specific existing customer---------------------------------------

    public Customer getCustomer(final Long customerId) throws ApplicationException {

        //Setting a specific customer
        Customer customer = customerDAO.read(customerId);

        //Checking if the customer is not exists
        if (customer == null) {
            throw new EntityNotExistException(EntityType.CUSTOMER);
        }

        List<Coupon> customerCoupons = getAllCoupons(customerId);

        customer.setCoupons(customerCoupons);

        return customer;
    }
}
