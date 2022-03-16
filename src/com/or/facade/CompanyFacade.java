package com.or.facade;

import com.or.dao.CompanyDAO;
import com.or.dao.CouponDAO;
import com.or.enums.Categories;
import com.or.enums.EntityType;
import com.or.errors.*;
import com.or.loggin.LoginManager;
import com.or.model.Company;
import com.or.model.Coupon;
import com.or.util.CouponUtil;
import com.or.util.InputUserValidation;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CompanyFacade extends ClientFacade {

    public static final CompanyFacade instance = new CompanyFacade();
    private final CompanyDAO companyDao;
    private final CouponDAO couponDAO;
    LoginManager loginManager = LoginManager.instance;

    //Creating a private constructor
    private CompanyFacade() {
        //Injecting the entities instances with creation of this class
        this.companyDao = CompanyDAO.instance;
        this.couponDAO = CouponDAO.instance;
    }

    //-------------------------------------Company Login-----------------------------------------------------------

    @Override
    public boolean login(final String email, final String password) throws ApplicationException {

        //Checking if email address is valid according to Email REGEX
        if (!InputUserValidation.isEmailValid(email)) {
            throw new UserValidationException();
        }
        //Checking if hashed password is valid according to Password REGEX
        if (!InputUserValidation.isPasswordValid(password)) {
            throw new UserValidationException();
        }
        //Getting and Setting a company in a variable according to email
        Company company = companyDao.readByEmail(email);
        if (company == null) {
            throw new EntityNotExistException(EntityType.COMPANY);
        }

        //Setting company's email to variable
        String companyEmail = company.getEmail();

        //Setting company's password to variable
        String companyPassword = company.getPassword();

        //Setting the injected password to check into hashcode password and placing in a variable
        long hashedPasswordTCheck = password.hashCode();

        //Checking if email and password are matching to the specific company
      return email.equals(companyEmail) && String.valueOf(hashedPasswordTCheck).equals(companyPassword);
    }

    //-------------------------------------Creating a new coupon--------------------------------------------------

    public Long createCoupon(final Coupon coupon) throws ApplicationException {

        //Checking if the specific coupon is already exist
        for (Coupon c : couponDAO.readAll()) {
            if(c.getTitle().equals(coupon.getTitle()) && Objects.equals(c.getCompanyID(), coupon.getCompanyID())) {
                throw new EntityExistException(EntityType.COUPON);
            }
        }

        //Checking if the date format is valid according to Date REGEX
        if (!InputUserValidation.isDateValid((coupon.getStartDate()))) {
            throw new UserValidationException();
        }

        //Checking if the date format is valid according to Date REGEX
        if (!InputUserValidation.isDateValid((coupon.getEndDate()))) {
            throw new UserValidationException();
        }

        //Checking if the end date entered is valid according to today's Date
        if (CouponUtil.isCouponExpired(coupon.getEndDate())) {
            throw new InvalidCouponDate();
        }

        //Creating a coupon on our database
        Long newCouponId = couponDAO.create(coupon);
        System.out.println("The new coupon: " + coupon.getTitle() + " has been created successfully!");

        Company company = companyDao.read(coupon.getCompanyID());
        //Adding new coupon to the company's coupons list
        company.getCoupons().add(coupon);

        return newCouponId;
    }

    //-------------------------------------Updating an existing coupon----------------------------------------------

    public void updateCoupon(final Coupon coupon) throws ApplicationException {

        //Checking if the specific coupon is not exist
        if (coupon == null) {
            throw new EntityNotExistException(EntityType.COUPON);
        }

        //Checking if the date format is valid according to Date REGEX
        if (!InputUserValidation.isDateValid((coupon.getStartDate()))) {
            throw new UserValidationException();
        }

        //Checking if the date format is valid according to Date REGEX
        if (!InputUserValidation.isDateValid((coupon.getEndDate()))) {
            throw new UserValidationException();
        }

        //Checking if the end date entered is valid according to today's Date
        if (CouponUtil.isCouponExpired(coupon.getEndDate())) {
            throw new InvalidCouponDate();
        }

        //Updating the coupon on our database
        couponDAO.update(coupon);
        System.out.println("Coupon: " + coupon.getTitle() + "," + " updated!");
    }

    //-------------------------------------Deleting an existing coupon----------------------------------------------

    public void deleteCoupon(final Long couponId) throws ApplicationException {

        //Checking if the specific coupon is not exist
        if (couponDAO.read(couponId) == null) {
            throw new EntityNotExistException(EntityType.COUPON);
        }

        //Checking if there is a purchase including the current coupon
        if(couponDAO.readCouponPurchaseByCouponId(couponId) == null) {
            //Deleting the coupon on our database
            System.out.println("The selected coupon: " + couponDAO.read(couponId).getTitle() + " has been deleted!");
            couponDAO.delete(couponId);
        }
        //If there is a purchase -
        //Setting a relevant customer Id in a variable
        Long customerId = couponDAO.readCouponPurchaseByCouponId(couponId);
        //Deleting relevant coupon purchase before deleting the specific coupon
        couponDAO.deleteCouponPurchase(customerId, couponId);
        //Deleting the coupon on our database
        System.out.println("The selected coupon: " + couponDAO.read(couponId).getTitle() + " has been deleted!");
        couponDAO.delete(couponId);
    }

    //-------------------------------------Getting an existing coupon-----------------------------------------------

    public Coupon getCoupon(final Long couponId) throws ApplicationException {

    Coupon coupon = couponDAO.read(couponId);
            //Checking if the coupon is not exists
            if (coupon == null) {
        throw new EntityNotExistException(EntityType.COUPON);
    }
            //Getting specific coupon
            return coupon;
    }


    //-------------------------------------Getting all coupons------------------------------------------------------

    public List<Coupon> getAllCoupons(final Long companyId) throws ApplicationException {

        List<Coupon> companyCoupons = new ArrayList<>();

            //Setting the specific company's coupons
            List<Coupon> coupons = couponDAO.readCouponsByCompanyId(companyId);
            //Adding the coupons to a list
            companyCoupons.addAll(coupons);
            //Checking if the coupons are not exists
            if (coupons == null) {
                throw new EntityNotExistException(EntityType.COUPON);
            }

        return companyCoupons;
    }

    //-------------------------------------Getting all coupons by specific category---------------------------------

    public List<Coupon> getCouponsByCategory(final Long companyId, final Categories category) throws ApplicationException {

        List<Coupon> couponsOfCompanyCategories = new ArrayList<>();

        //Setting the specific company's coupons
        List<Coupon> coupons = couponDAO.readCouponsByCompanyId(companyId);
        //Checking if the coupons are not exists
        if (coupons == null) {
            throw new EntityNotExistException(EntityType.COUPON);
        }
        for (Coupon coupon : coupons) {
            if (!coupon.getCategory().equals(category)) {
                System.out.println("This company doesn't holds coupons from this category!");
                break;
            }
            //Adding the specific coupons of the same category to a list
            couponsOfCompanyCategories.add(coupon);
            break;
        }
        return couponsOfCompanyCategories;
    }

    //-------------------------------------Getting all coupons according to price----------------------------------

    public List<Coupon> getCouponsByMaxPrice(final Long companyId, final double maxPrice) throws ApplicationException {

        List<Coupon> couponsByMax = new ArrayList<>();

         List<Coupon> coupons = couponDAO.readCouponsByCompanyId(companyId);
            //Checking if the coupons are not exists
            if(coupons == null) {
                throw new EntityNotExistException(EntityType.COUPON);
            }
            for (Coupon coupon : coupons) {
                if(coupon.getPrice() <= maxPrice) {
                    //Adding the specific coupons according the selected price limit
                    couponsByMax.add(coupon);
                }
            }
            return couponsByMax;
    }

    //-------------------------------------Get a specific company-------------------------------------------------

    public Company getCompany(final Long companyId) throws ApplicationException {

            Company company = companyDao.read(companyId);
            //Checking if the company is not exists
            if (company == null) {
                throw new EntityNotExistException(EntityType.COMPANY);
            }
            //Getting specific company
            return new Company(company.getId(), company.getName(), company.getEmail(),
                               company.getPassword(), couponDAO.readCouponsByCompanyId(companyId));
        }
}
