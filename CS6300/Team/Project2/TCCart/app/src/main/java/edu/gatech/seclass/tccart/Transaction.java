package edu.gatech.seclass.tccart;


import java.util.Date;

public class Transaction {

    //credit/discount constants
    public static final double TR_CONST_VIP_DISCOUNT_VAL = 0.1;
    public static final double TR_CONST_CREDIT_VAL = 3.0;
    public static final double TR_CONST_AMOUNT_SPENT_TO_GET_CREDIT = 30.0;
    public static final double TR_CONST_AMOUNT_SPENT_TO_GET_VIP = 300.0;

    //DB table name constants
    public static final String _TABLE_NAME = "transaction_t";
    public static final String _DATE = "date";
    public static final String _PRE_DISCOUNT_TOTAL = "pre_discount_total";
    public static final String _POST_DISCOUNT_TOTAL = "post_discount_total";
    public static final String _VIP_DISCOUNT_APPLIED = "vip_discount_applied";
    public static final String _CREDIT_APPLIED = "credit_applied";
    public static final String _USER_ID = "user_id";
    public static final String _YEAR = "year"; //for easier VIP discount calculation in a single query

    //attributes
    private Date date;
    private double preDiscountTotal;
    private double postDiscountTotal;
    private double vipDiscountApplied;
    private double creditApplied;
    private long userId;

    //error flags
    private boolean isCreditCardScanFailed = false;
    private boolean isPaymentProcessingFailed = false;


    public Transaction(double amountScanned, long userId) {
        this.preDiscountTotal = amountScanned;
        this.userId = userId;
        this.date = new Date();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getPreDiscountTotal() {
        return preDiscountTotal;
    }

    public void setPreDiscountTotal(double preDiscountTotal) {
        this.preDiscountTotal = preDiscountTotal;
    }

    public double getPostDiscountTotal() {
        return postDiscountTotal;
    }

    public void setPostDiscountTotal(double postDiscountTotal) {
        this.postDiscountTotal = postDiscountTotal;
    }

    public double getVipDiscountApplied() {
        return vipDiscountApplied;
    }

    public void setVipDiscountApplied(double vipDiscountApplied) {
        this.vipDiscountApplied = vipDiscountApplied;
    }

    public double getCreditApplied() {
        return creditApplied;
    }

    public void setCreditApplied(double creditApplied) {
        this.creditApplied = creditApplied;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isPaymentProcessingFailed() {
        return isPaymentProcessingFailed;
    }

    public void setPaymentProcessingFailed(boolean paymentProcessingFailed) {
        this.isPaymentProcessingFailed = paymentProcessingFailed;
    }

    public boolean isCreditCardScanFailed() {
        return isCreditCardScanFailed;
    }

    public void setCreditCardScanFailed(boolean creditCardScanFailed) {
        this.isCreditCardScanFailed = creditCardScanFailed;
    }

}
