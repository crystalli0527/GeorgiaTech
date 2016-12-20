package edu.gatech.seclass.tccart;


import android.content.Context;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.StringTokenizer;

import edu.gatech.seclass.services.EmailService;

public class TCCartHelper {

    public static void showMsg(String str, Context context) {
        CharSequence text = str;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public static String formatDouble(double val){
        DecimalFormat decf = new DecimalFormat("#0.00");
        decf.setRoundingMode(RoundingMode.HALF_UP);
        return decf.format(val);
    }

    public static String idToHexId(Long id) {
        return String.format("%08X", id);
    }

    public static Long hexIdToLongId(String hexId) {
        return Long.parseLong(hexId, 16);
    }

    //used for ? operator to prevent null pointer exceptions
    public static boolean isNull(Object o) {
        if (o == null) {
            return true;
        }
        else {
            return false;
        }
    }

    public static void sendEmailsWithGuaranteedDelivery(String recipient, String subject, String body, Context context) {
        boolean isSuccessfullyDelivered;
        int msgFailCounter = 0;

        do {
            isSuccessfullyDelivered = EmailService.sendEMail(recipient, subject, body);
            msgFailCounter++;
        }
        while (!isSuccessfullyDelivered && msgFailCounter < 10);

        if (msgFailCounter == 10){
            TCCartHelper.showMsg("ERROR: Couldn't send an email; Email server is down", context);
        }
    }

    //2: FIRST LAST
    //5: CC: Everett#Scott#4224876949325382#12312015#000
    public static String[] tokenizeString(String str, int numberOfTokens) {
        if (numberOfTokens == 2 || numberOfTokens == 5) { //support only 2 and 5 tokens
            String[] returnArr = new String[numberOfTokens];
            str = str.replaceAll("#", " ");

            StringTokenizer st = new StringTokenizer(str);
            for (int i = 0; i < numberOfTokens; i++) {
                returnArr[i] = st.nextToken();
            }
            return returnArr;
        }
        return null;
    }

    public static String showDiscounts(Transaction transaction){
        String result = "\nDiscounts applied: \n";
        boolean isDiscountApplied = false;
        if (transaction.getVipDiscountApplied() > 0.0) {
            result = result + "10% VIP (" + TCCartHelper.formatDouble(transaction.getPreDiscountTotal() * transaction.getVipDiscountApplied()) + ");\n";
            isDiscountApplied = true;
        }
        if (transaction.getCreditApplied() > 0.0) {
            result = result + "Credit ($" + TCCartHelper.formatDouble(transaction.getCreditApplied()) + ");";
            isDiscountApplied = true;
        }

        if (!isDiscountApplied) {
            result = ""; //don't print anything if no discounts are applied
        }
        return result;
    }

}
