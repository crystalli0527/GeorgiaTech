package edu.gatech.seclass.tccart;


import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import edu.gatech.seclass.services.CreditCardService;
import edu.gatech.seclass.services.PaymentService;

public class TransactionManager {

    //business logic
    public static Transaction processTransaction(Customer customer, Double amount, Context context) {

        Transaction transaction = new Transaction(amount, TCCartHelper.hexIdToLongId(customer.getHexId()));

        String CreditCardString = CreditCardService.readCreditCard();
        if (CreditCardString.equalsIgnoreCase("err")) {
            transaction.setCreditCardScanFailed(true);
            return transaction;
        }

        //vip discount comes before any other discounts
        applyVipDiscount(transaction, customer, context);

        applyCredit(transaction, customer);


        if (transaction.getPostDiscountTotal() == 0.0) { //suppress CC processing if postDiscount is 0
            //process payment
            String[] tokenizedCreditCardInfo = TCCartHelper.tokenizeString(CreditCardString, 5);

            String ccDateStr = tokenizedCreditCardInfo[3];
            DateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
            Date ccDate = null;
            try {
                ccDate = dateFormat.parse(ccDateStr);
            } catch (Exception e) {
                System.out.println("EXCEPTION: " + e.toString());
            }
            //0:FIRST; 1:LAST; 2:CC_NUMBER; 3:DATE_STR; 4:SEC_CODE
            boolean isPaymentProcessedSuccessfully = PaymentService.processPayment(tokenizedCreditCardInfo[0], tokenizedCreditCardInfo[1],
                    tokenizedCreditCardInfo[2], ccDate, tokenizedCreditCardInfo[4], transaction.getPostDiscountTotal());

            if (!isPaymentProcessedSuccessfully) {
                transaction.setPaymentProcessingFailed(true);
                return transaction;
            }
        }
        //payment processed successfully:
        TCCartHelper.sendEmailsWithGuaranteedDelivery(customer.getEmail(), "TCCart receipt",
                "Thank you for your purchase. Your pre discount/credit total is $" + transaction.getPreDiscountTotal() +
                "; Your post discount/credit total is $" + transaction.getPostDiscountTotal() + ";" + TCCartHelper.showDiscounts(transaction), context);


        TCCartHelper.showMsg("Payment processed successfully: $" + TCCartHelper.formatDouble(transaction.getPostDiscountTotal()) +
                TCCartHelper.showDiscounts(transaction), context);
        //for debugging/testing
        System.out.println(">>> TCCart LOG: " + customer.getName() + " | " + TCCartHelper.hexIdToLongId(customer.getHexId()) +
                " | Payment processed successfully: $" + transaction.getPostDiscountTotal() + TCCartHelper.showDiscounts(transaction));

        TCCartDBHelper.saveTransactionToDB(transaction, context);

        calculateNewCredit(transaction, customer, context);

        calculateVipStatus(customer, context);

        TCCartDBHelper.updateCustomerCreditAndDiscountInfo(customer, context);

        return transaction;
    }


    /*
     * Helper methods
     */

    //VIP FIX: this method now is only used to send emails/show toast messages
    private static void calculateVipStatus(Customer customer, Context context) {
        Date today = new Date();
        double yearlySpentTotal = TCCartDBHelper.calculateVipYearlySpentTotal(customer.getHexId(), today.getYear(), context);
        boolean alreadyHasVipStatus = customer.getVipDiscountYear() != null &&
                customer.getVipDiscountYear().getYear() == today.getYear() + 1;

        if (alreadyHasVipStatus){
            System.out.println(">>> TCCart LOG: " + customer.getName() + " | " + TCCartHelper.hexIdToLongId(customer.getHexId()) +
                    " | already has VIP: messages suppressed");
        }

        if (!alreadyHasVipStatus && yearlySpentTotal >= Transaction.TR_CONST_AMOUNT_SPENT_TO_GET_VIP) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, 1);
            customer.setVipDiscountYear(cal.getTime());
            customer.setDiscount(Transaction.TR_CONST_VIP_DISCOUNT_VAL);
            TCCartHelper.sendEmailsWithGuaranteedDelivery(customer.getEmail(), "TCCart VIP status achieved!",
                    "Thank you for your purchases throughout this year. You will receive 10% discount on all purchases during the next year!", context);
            TCCartHelper.showMsg("VIP earned", context);
            //for debugging/testing
            System.out.println(">>> TCCart LOG: " + customer.getName() + " | " + TCCartHelper.hexIdToLongId(customer.getHexId()) +
                    " | VIP earned: totalSpent=" + yearlySpentTotal);
        }
        else if (yearlySpentTotal < 0.0) {
            TCCartHelper.showMsg("ERROR calculating VIP status: please contact the developers", context); //shouldn't happen
        }
    }

    private static void calculateNewCredit(Transaction transaction, Customer customer, Context context){
        if (transaction.getPostDiscountTotal() >= Transaction.TR_CONST_AMOUNT_SPENT_TO_GET_CREDIT) {
            customer.setCredit(Transaction.TR_CONST_CREDIT_VAL);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MONTH, 1);
            customer.setCreditExpiration(cal.getTime());
            TCCartHelper.sendEmailsWithGuaranteedDelivery(customer.getEmail(), "TCCart credit received!!",
                    "Thank you for your purchase of $30 or more. You will receive $3 credit to apply to your purchases for a month from today!", context);
            TCCartHelper.showMsg("$3 credit earned", context);
            //for debugging/testing
            System.out.println(">>> TCCart LOG: " + customer.getName() + " | " + TCCartHelper.hexIdToLongId(customer.getHexId()) +
                    " | $3 credit earned");
        }
    }

    //VIP FIX/HACK: now we check for last year total spent each time
    private static void applyVipDiscount(Transaction transaction, Customer customer, Context context){
        Date today = new Date();
        double lastYearSpentTotal = TCCartDBHelper.calculateVipYearlySpentTotal(customer.getHexId(), today.getYear() - 1, context);
        if (lastYearSpentTotal >= Transaction.TR_CONST_AMOUNT_SPENT_TO_GET_VIP) { //if last year spent total is 300 or more, this year is VIP
            double postVipDiscountTotal = transaction.getPreDiscountTotal() - (transaction.getPreDiscountTotal() * Transaction.TR_CONST_VIP_DISCOUNT_VAL); //apply vip discount
            transaction.setPostDiscountTotal(postVipDiscountTotal);
            transaction.setVipDiscountApplied(Transaction.TR_CONST_VIP_DISCOUNT_VAL);
        }
        else { //no discount
            transaction.setPostDiscountTotal(transaction.getPreDiscountTotal());
            transaction.setVipDiscountApplied(0.0);
        }
    }

    private static void applyCredit(Transaction transaction, Customer customer){
        Date today = new Date();
        if (customer.getCreditExpiration() != null &&
                customer.getCreditExpiration().after(today)) {
            double preCreditTotal;
            if (transaction.getPostDiscountTotal() <= customer.getCredit()){ //if price (after VIP discount) is <= credit
                preCreditTotal = transaction.getPostDiscountTotal();
                transaction.setCreditApplied(preCreditTotal);
                transaction.setPostDiscountTotal(0.0);
                customer.setCredit(customer.getCredit() - preCreditTotal);
                //credit expiration stays the same
            }
            else {
                preCreditTotal = transaction.getPostDiscountTotal();
                transaction.setPostDiscountTotal(preCreditTotal - customer.getCredit());
                transaction.setCreditApplied(customer.getCredit());
                customer.setCredit(0.0);
                customer.setCreditExpiration(null); //remove credit
            }
        }
        else {
            //else no credit to apply
            transaction.setCreditApplied(0.0);
        }
    }

}
