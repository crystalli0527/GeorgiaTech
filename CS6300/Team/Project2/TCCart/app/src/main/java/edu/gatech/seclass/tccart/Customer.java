package edu.gatech.seclass.tccart;


import java.util.Date;

public class Customer {

    //DB table name constants
    public static final String _TABLE_NAME = "customer";
    public static final String _NAME = "name";
    public static final String _ID = "id";
    public static final String _EMAIL = "email";
    public static final String _VIP_DISCOUNT_YEAR = "vip_discount_year"; //we only use the year from here
    public static final String _DISCOUNT = "discount";
    public static final String _CREDIT = "credit";
    public static final String _CREDIT_EXPIRATION_DATE = "credit_exp_date";

    private String name;
    private String email;
    private String hexId;
    private Date vipDiscountYear;
    private double discount;
    private boolean isEmailAlreadyRegistered = false;
    private double credit;
    private Date creditExpiration;

    public Date getCreditExpiration() {
        return creditExpiration;
    }

    public void setCreditExpiration(Date creditExpiration) {
        this.creditExpiration = creditExpiration;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }


    public boolean isEmailAlreadyRegistered() {
        return isEmailAlreadyRegistered;
    }

    public void setIsEmailAlreadyRegistered(boolean isEmailAlreadyRegistered) {
        this.isEmailAlreadyRegistered = isEmailAlreadyRegistered;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getHexId() {
        return hexId;
    }

    //not a good practice but works for this project...
    protected void setHexId(String hexId) {
        this.hexId = hexId;
    }

    public Date getVipDiscountYear() {
        return vipDiscountYear;
    }

    public void setVipDiscountYear(Date vipDiscountYear) {
        this.vipDiscountYear = vipDiscountYear;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
        //id is generated when customer is saved to the DB
    }

}
