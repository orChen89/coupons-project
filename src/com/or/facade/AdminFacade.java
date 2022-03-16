package com.or.facade;

import com.or.constants.Constants;
import com.or.dao.CompanyDAO;
import com.or.dao.CouponDAO;
import com.or.dao.CustomerDAO;
import com.or.enums.EntityType;
import com.or.errors.*;
import com.or.model.Company;
import com.or.model.Coupon;
import com.or.model.Customer;
import com.or.util.InputUserValidation;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AdminFacade extends ClientFacade {

    public static final AdminFacade instance = new AdminFacade();
    private final CompanyDAO companyDao;
    private final CustomerDAO customerDAO;
    private final CouponDAO couponDAO;

    //Creating a private constructor
    private AdminFacade() {

        //Injecting the entities instances with creation of this class
        this.companyDao = CompanyDAO.instance;
        this.customerDAO = CustomerDAO.instance;
        this.couponDAO = CouponDAO.instance;
    }

    //------------------------------------------Administrator Login-----------------------------------------------

    @Override
    public boolean login(final String email, final String password) throws ApplicationException {

        //Checking if email address is valid according to Email REGEX
        if(!InputUserValidation.isEmailValid(email)){
            throw new UserValidationException();
        }

        //Checking if hashed password address is valid according to password REGEX
        if(!InputUserValidation.isPasswordValid(password)){
            throw new UserValidationException();
        }

        //Checking if email and password are matching to admin
        return email.equals(Constants.ADMIN_LOGIN_EMAIL) && password.equals(Constants.ADMIN_LOGIN_PASSWORD);
    }

    //------------------------------------------Creating company---------------------------------------------------

    public Long createCompany(final Company company) throws ApplicationException {

        //Checking if hashed password address is valid according to password REGEX
        if(!InputUserValidation.isPasswordValid(String.valueOf(company.getPassword()))){
            throw new UserValidationException();
        }

        //Checking if email address is valid according to Email REGEX
        if(!InputUserValidation.isEmailValid(company.getEmail())){
            throw new UserValidationException();
        }

        //Checking if company is already exists according to email
        if(companyDao.isExistsByEmail(company.getEmail())){
            throw new EntityExistException(EntityType.COMPANY);
        }

        //Checking if company is already exists according to name
        if(companyDao.isExistsByName(company.getName())){
            throw new EntityExistException(EntityType.COMPANY);
        }
        //Creating the company
        Long newCompanyId = companyDao.create(company);
        System.out.println("The new company: " + company.getName() + " has been created successfully!");

        return newCompanyId;
    }

    //------------------------------------------Updating company---------------------------------------------------

    public void updateCompany(final Company company) throws ApplicationException {

        //Checking if company is not exists
        if(!companyDao.isExistsById(company.getId())) {
            throw new EntityNotExistException(EntityType.COMPANY);
        }

        //Checking if company name is already exist
        if(!company.getName().equals(companyDao.read(company.getId()).getName())){
            throw new CompanyNameException();
        }

        //Updating the company
        companyDao.update(company);
        System.out.println("The updated company has been created successfully!");
    }

    //------------------------------------------Deleting a company---------------------------------------------------

    public void deleteCompany(final Long companyId) throws ApplicationException {

        //Checking if company is not exists
        if (!companyDao.isExistsById(companyId)) {
            throw new EntityNotExistException(EntityType.COMPANY);
        }

        List<Coupon> companyCoupons = getCompany(companyId).getCoupons();

        for (Coupon coupon : companyCoupons) {
            //Deleting a coupon purchase from the database
            couponDAO.deleteCouponPurchaseByCouponId(coupon.getId());
            //Deleting all coupons of the specific company
            couponDAO.delete(coupon.getId());
        }

        //Deleting the specific company
        System.out.println("The selected company: " + companyDao.read(companyId).getName() +  " has been deleted!");
        companyDao.delete(companyId);

    }

    //------------------------------------------Getting all companies----------------------------------------------

    public Set<Company> getAllCompanies() throws ApplicationException {

        //Setting and adding all companies in a list of companies
        Set<Company> companies = new HashSet<>(companyDao.readAll());

        //Checking if companies are not exist
        if(companies == null){
            throw new EntityNotExistException(EntityType.COMPANY);
        }

        for (Company company : companies) {
            //Setting to each company its specific coupons
            company.setCoupons(getCompany(company.getId()).getCoupons());
        }

        return companies;
    }

    //------------------------------------------Getting a company--------------------------------------------------

    public Company getCompany(final Long companyId) throws ApplicationException {

        //Setting a specific company to a variable
        Company company = companyDao.read(companyId);

        //Checking if the company is not exist
        if(company == null){
            throw new EntityNotExistException(EntityType.COMPANY);
        }
        //Getting the specific company
        return new Company(company.getId(),company.getName(),company.getEmail()
                ,company.getPassword(),couponDAO.readCouponsByCompanyId(companyId));
    }

   //------------------------------------------Creating a customer--------------------------------------------------

    public Long createCustomer(final Customer customer) throws ApplicationException {

        //Checking if hashed password address is valid according to password REGEX
        if (!InputUserValidation.isPasswordValid(customer.getPassword())) {
            throw new UserValidationException();
        }

        //Checking if email address is valid according to Email REGEX
        if (!InputUserValidation.isEmailValid(customer.getEmail())) {
            throw new UserValidationException();
        }

        //Checking if customer is already exist
        if(customerDAO.isExistsByEmail(customer.getEmail())){
            throw new EntityExistException(EntityType.CUSTOMER);
        }

        //Setting a specific customer to a variable
        Long newCustomerId = customerDAO.create(customer);
        System.out.println("The new customer: " + customer.getFirstName() + " " + customer.getLastName() +
                " has been created successfully!");

        return newCustomerId;
    }

    //------------------------------------------Updating a customer--------------------------------------------------

    public void updateCustomer(final Customer customer) throws ApplicationException {

        //Checking if customer is not exist
        if(!customerDAO.isExistsById(customer.getId())) {
            throw new EntityNotExistException(EntityType.CUSTOMER);
        }
        //Updating the specific customer
        customerDAO.update(customer);

        System.out.println("The updated customer has been created successfully!");
    }

    //------------------------------------------Deleting a customer--------------------------------------------------

    public void deleteCustomer(final Long customerId) throws ApplicationException {

        //Checking if customer is not exist
        if (!customerDAO.isExistsById(customerId)) {
            throw new EntityNotExistException(EntityType.COMPANY);
        }
        //Setting the specific customer in a variable
        Customer customer = getCustomer(customerId);
        for (Coupon coupon : customer.getCoupons()) {
            //Deleting the purchase of coupons by the specific customer
            couponDAO.deleteCouponPurchase(customerId, coupon.getId());
        }
        //Deleting the specific customer
        customerDAO.delete(customerId);
        System.out.println("Customer: " + customer.getFirstName() + " " + customer.getLastName() + " has been deleted!");
    }

    //------------------------------------------Getting all customers-----------------------------------------------

    public Set<Customer> getAllCustomers() throws ApplicationException {

        //Setting and adding all customers in a list of customers
        Set<Customer> customers = new HashSet<>(customerDAO.readAll());

        //Checking if customers are not exist
        if(customers == null){
            throw new EntityNotExistException(EntityType.CUSTOMER);
        }

        for (Customer customer : customers) {
            //Setting to every customer its coupons
            customer.setCoupons(getCustomer(customer.getId()).getCoupons());
        }

        return customers;
    }

    //------------------------------------------Getting a customer-------------------------------------------------

    public Customer getCustomer(final Long customerId) throws ApplicationException {

        //Setting a specific customer
        Customer customer = customerDAO.read(customerId);

        //Checking if the customer is not exists
        if(customer == null){
            throw new EntityNotExistException(EntityType.CUSTOMER);
        }

        //Setting the specific customer's coupons in a list
        List<Coupon> customerCoupons = CustomerFacade.instance.getAllCoupons(customerId);
        customer.setCoupons(customerCoupons);

        return customer;
    }
}
