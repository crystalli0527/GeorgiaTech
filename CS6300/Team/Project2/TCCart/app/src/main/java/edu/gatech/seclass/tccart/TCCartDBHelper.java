package edu.gatech.seclass.tccart;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * REFERENCES:
 * http://developer.android.com/training/basics/data-storage/databases.html
 *
 * FOR DIRECT/REMOTE DB ACCESS READ:
 * http://developer.android.com/tools/help/sqlite3.html
 * adb -s emulator-5554 shell
 *
 * USEFUL SQLITE DOCS:
 * https://www.sqlite.org/lang.html
 * https://www.sqlite.org/datatype3.html
 *
 */

public class TCCartDBHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TCCart.db";

    private static final String HTML_HEADER_AND_CSS = "<html><head><style>" +
            "tr:nth-child(even) {background-color: #ebfdff}\n" +
            "th {\n" +
            "    background-color: #303f9f;\n" +
            "    color: white;\n" +
            "    padding: 10px;\n" +
            "}\n" +
            "table {\n" +
            "    border-collapse: collapse;\n" +
            "}\n" +
            "\n" +
            "table, th, td {\n" +
            "    border: 2px solid #303f9f;\n" +
            "}\n" +
            "td { padding: 10px;\n }" +
            "</style></head><body>";


    //DB CREATE TABLE QUERIES
    private static final String SQL_CREATE_CUSTOMER =
            "CREATE TABLE " + Customer._TABLE_NAME + " (" +
                    Customer._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Customer._NAME + " TEXT NOT NULL, " +
                    Customer._EMAIL + " TEXT NOT NULL UNIQUE, " +
                    Customer._VIP_DISCOUNT_YEAR +" TEXT, " +
                    Customer._DISCOUNT + " REAL, " +
                    Customer._CREDIT+ " REAL, " +
                    Customer._CREDIT_EXPIRATION_DATE + " TEXT" +
            ");";

    private static final String SQL_CREATE_TRANSACTION =    //rowid will be the primary key and transaction_id (although we're not using it)
            "CREATE TABLE " + Transaction._TABLE_NAME + " (" +
                    Transaction._USER_ID + " INTEGER NOT NULL, " +
                    Transaction._YEAR + " INTEGER NOT NULL, " +
                    Transaction._DATE + " TEXT NOT NULL, " +
                    Transaction._PRE_DISCOUNT_TOTAL + " REAL NOT NULL, " +
                    Transaction._VIP_DISCOUNT_APPLIED + " REAL, " +
                    Transaction._CREDIT_APPLIED + " REAL, " +
                    Transaction._POST_DISCOUNT_TOTAL + " REAL, " +
                    "FOREIGN KEY(" + Transaction._USER_ID + ") REFERENCES " + Customer._TABLE_NAME + "(" + Customer._ID + ")" +
            ");";

    //single query batch insert didn't work for some reason...
    private static final String SQL_INSERT_SEED_USER1 = "INSERT INTO customer (id, name, email) VALUES (2089222126, \"Ralph Hapschatt\", \"Ralph_Hapschatt@mail.com\");";
    private static final String SQL_INSERT_SEED_USER2 = "INSERT INTO customer (id, name, email) VALUES (3046392239, \"Betty Monroe\", \"Betty_Monroe@mail.com\");";
    private static final String SQL_INSERT_SEED_USER3 = "INSERT INTO customer (id, name, email) VALUES (3440315909, \"Everett Scott\", \"Everett_Scott@mail.com\");";

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CUSTOMER);
        db.execSQL(SQL_CREATE_TRANSACTION);
        db.execSQL(SQL_INSERT_SEED_USER1);
        db.execSQL(SQL_INSERT_SEED_USER2);
        db.execSQL(SQL_INSERT_SEED_USER3);
    }

    private static final String SQL_DELETE_ENTRIES_CUSTOMER =
            "DROP TABLE IF EXISTS " + Customer._TABLE_NAME;

    private static final String SQL_DELETE_ENTRIES_TRANSACTION =
            "DROP TABLE IF EXISTS " + Transaction._TABLE_NAME;

    public TCCartDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES_CUSTOMER);
        db.execSQL(SQL_DELETE_ENTRIES_TRANSACTION);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    /*
     * DB HELPER CODE STARTS HERE
     */
    //drop all tables and recreate them with seed data
    public static void debugTestCleanDB(Context context){
        TCCartDBHelper mDbHelper = new TCCartDBHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.execSQL(SQL_DELETE_ENTRIES_CUSTOMER);
        db.execSQL(SQL_DELETE_ENTRIES_TRANSACTION);

        db.execSQL(SQL_CREATE_CUSTOMER);
        db.execSQL(SQL_CREATE_TRANSACTION);
        db.execSQL(SQL_INSERT_SEED_USER1);
        db.execSQL(SQL_INSERT_SEED_USER2);
        db.execSQL(SQL_INSERT_SEED_USER3);
    }

    public static double calculateVipYearlySpentTotal(String hexId, int year, Context context) {
        TCCartDBHelper mDbHelper = new TCCartDBHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        //order of input args
        String[] argVals = {TCCartHelper.hexIdToLongId(hexId).toString(), ""+year};

        String query = "select ifnull(sum(" + Transaction._POST_DISCOUNT_TOTAL + "),0.0) as yearly_sum from " +
                Transaction._TABLE_NAME + " where user_id = ? and year = ?;";

        Cursor c = db.rawQuery(query, argVals);

        double result = -1; //will return -1 on error, which should never happen

        if (c.getCount() == 1) {
            c.moveToFirst();
            result = c.getDouble(c.getColumnIndex("yearly_sum"));
        }

        c.close();
        return result;
    }

    public static void saveTransactionToDB(Transaction transaction, Context context) {
        //get DB reference
        TCCartDBHelper mDbHelper = new TCCartDBHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Transaction._USER_ID, transaction.getUserId());
        values.put(Transaction._YEAR, transaction.getDate().getYear());
        values.put(Transaction._DATE, transaction.getDate().toString()); //TODO: HIGH: use date format to make the date string look better in transaction history
        values.put(Transaction._PRE_DISCOUNT_TOTAL, transaction.getPreDiscountTotal());
        values.put(Transaction._VIP_DISCOUNT_APPLIED, transaction.getVipDiscountApplied());
        values.put(Transaction._CREDIT_APPLIED, transaction.getCreditApplied());
        values.put(Transaction._POST_DISCOUNT_TOTAL, transaction.getPostDiscountTotal());

        //insert the new row, returning the primary key value of the new row
        //newRowId should be equal to customer ID
        long newRowId;
        newRowId = db.insert(Transaction._TABLE_NAME, null, values);

        if (newRowId <= 0) {
            //shouldn't happen
            TCCartHelper.showMsg("ERROR inserting transaction: please contact the developers", context);
        }
    }

    public static void saveCustomerToDB(Customer customer, Context context) {
        //get DB reference
        TCCartDBHelper mDbHelper = new TCCartDBHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(Customer._NAME, customer.getName());
        values.put(Customer._EMAIL, customer.getEmail());
        values.put(Customer._DISCOUNT, customer.getDiscount());
        values.put(Customer._VIP_DISCOUNT_YEAR, TCCartHelper.isNull(customer.getVipDiscountYear()) ? null : customer.getVipDiscountYear().toString());
        //^ null if date is null, string value otherwise

        //insert the new row, returning the primary key value of the new row
        //newRowId should be equal to customer ID
        long newRowId;
        newRowId = db.insert(Customer._TABLE_NAME, null, values);

        if (newRowId > 0) {
            //update the ID in customer object
            customer.setHexId(TCCartHelper.idToHexId(newRowId));
        }
        else {
            //TODO: LOW: refactor with boolean return value
            customer.setIsEmailAlreadyRegistered(true);
            //^ this is what happens when you write code late at night :(
        }
    }


    public static String selectStarFromTransaction(Context context){
        String result = HTML_HEADER_AND_CSS +
                "<table><tr>" +
                "<th>User ID</th>" +
                "<th>Date</th>" +
                "<th>Pre Discount Tot</th>" +
                "<th>VIP Discount</th>" +
                "<th>Credit</th>" +
                "<th>Post Discount Tot</th>" +
                "</tr>";

        TCCartDBHelper mDbHelper = new TCCartDBHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String query = "select * from " + Transaction._TABLE_NAME + ";";

        Cursor c = db.rawQuery(query, null);

        while (c.moveToNext()){
            String row = "<tr>";

            row = row + "<td>" + TCCartHelper.idToHexId(c.getLong(c.getColumnIndex(Transaction._USER_ID))) + "</td>";
            row = row + "<td>" + c.getString(c.getColumnIndex(Transaction._DATE)) + "</td>";
            row = row + "<td>" + c.getDouble(c.getColumnIndex(Transaction._PRE_DISCOUNT_TOTAL)) + "</td>";
            row = row + "<td>" + c.getDouble(c.getColumnIndex(Transaction._VIP_DISCOUNT_APPLIED)) + "</td>";
            row = row + "<td>" + c.getDouble(c.getColumnIndex(Transaction._CREDIT_APPLIED)) + "</td>";
            row = row + "<td>" + c.getDouble(c.getColumnIndex(Transaction._POST_DISCOUNT_TOTAL)) + "</td>";

            row = row + "</tr>";
            result = result + row;
        }

        c.close();
        result = result + "</table></body></html>";
        return result;
    }


    public static String selectStarFromCustomer(Context context){
        String result = HTML_HEADER_AND_CSS +
                "<table><tr>" +
                "<th>HEX ID</th>" +
                "<th>Name</th>" +
                "<th>Email</th>" +
                "<th>VIP Year</th>" +
                "<th>VIP Discount</th>" +
                "<th>Credit</th>" +
                "<th>Credit EXP Date</th>" +
                "</tr>";

        TCCartDBHelper mDbHelper = new TCCartDBHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String query = "select * from " + Customer._TABLE_NAME + ";";

        Cursor c = db.rawQuery(query, null);

        while (c.moveToNext()){
            String row = "<tr>";

            row = row + "<td>" + TCCartHelper.idToHexId(c.getLong(c.getColumnIndex(Customer._ID))) + "</td>";
            row = row + "<td>" + c.getString(c.getColumnIndex(Customer._NAME)) + "</td>";
            row = row + "<td>" + c.getString(c.getColumnIndex(Customer._EMAIL)) + "</td>";
            DateFormat df = new SimpleDateFormat("yyyy");
            String date = c.getString(c.getColumnIndex(Customer._VIP_DISCOUNT_YEAR));
            if (date != null) {
                row = row + "<td>" + df.format(new Date(date)) + "</td>";
            }
            else {
                row = row + "<td>null</td>";
            }
            row = row + "<td>" + c.getDouble(c.getColumnIndex(Customer._DISCOUNT)) + "</td>";
            row = row + "<td>" + c.getDouble(c.getColumnIndex(Customer._CREDIT)) + "</td>";
            row = row + "<td>" + c.getString(c.getColumnIndex(Customer._CREDIT_EXPIRATION_DATE)) + "</td>";

            row = row + "</tr>";
            result = result + row;
        }

        c.close();
        result = result + "</table></body></html>";
        return result;
    }

    public static String loadHtmlTransactionHistoryFromDbByHexId(String hexId, Context context) {
        String result = HTML_HEADER_AND_CSS +
                "<table><tr>" +
                "<th>Date</th>" +
                "<th>Pre Discount</th>" +
                "<th>VIP Discount</th>" +
                "<th>Credit</th>" +
                "<th>Post Discount</th>" +
                "</tr>";

        TCCartDBHelper mDbHelper = new TCCartDBHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] argVals = {TCCartHelper.hexIdToLongId(hexId).toString()};
        String query = "select * from " + Transaction._TABLE_NAME + " where " + Transaction._USER_ID + " = ?;";

        Cursor c = db.rawQuery(query, argVals);

        while (c.moveToNext()){
            String row = "<tr>";



            DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aa zzz");
            String date = c.getString(c.getColumnIndex(Transaction._DATE));
            row = row + "<td>" + df.format(new Date(date)) + "</td>";

            Double preDiscountTotal = c.getDouble(c.getColumnIndex(Transaction._PRE_DISCOUNT_TOTAL));
            row = row + "<td>$" + TCCartHelper.formatDouble(preDiscountTotal) + "</td>";

            Double vipDiscount = c.getDouble(c.getColumnIndex(Transaction._VIP_DISCOUNT_APPLIED));
            Double vipDiscountPercentage = vipDiscount * 100;
            row = row + "<td>" + vipDiscountPercentage.intValue() + "% ($" + TCCartHelper.formatDouble(preDiscountTotal * vipDiscount) + ")</td>";

            Double credit = c.getDouble(c.getColumnIndex(Transaction._CREDIT_APPLIED));
            row = row + "<td>$" + TCCartHelper.formatDouble(credit) + "</td>";

            Double postDiscount = c.getDouble(c.getColumnIndex(Transaction._POST_DISCOUNT_TOTAL));
            row = row + "<td>$" + TCCartHelper.formatDouble(postDiscount) + "</td>";

            row = row + "</tr>";
            result = result + row;
        }

        c.close();
        result = result + "</table></body></html>";
        return result;
    }

    public static Customer loadCustomerFromDbByHexId(String hexId, Context context) {
        TCCartDBHelper mDbHelper = new TCCartDBHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String name, email, vip_until, creditExpirationDate;
        Double discount, credit;
        Customer customer = null;

        //order of input args
        String[] argVals = {TCCartHelper.hexIdToLongId(hexId).toString()};

        //String query = "select " + Customer._NAME + ", " + Customer._EMAIL + ", " + Customer._VIP_DISCOUNT_YEAR +
        //        ", " + Customer._DISCOUNT + " from " + Customer._TABLE_NAME + " where " + Customer._ID + " = ?;";
        String query = "select * from " + Customer._TABLE_NAME + " where " + Customer._ID + " = ?;";


        Cursor c = db.rawQuery(query, argVals);

        if (c.getCount() == 1) {
            c.moveToFirst();
            name = c.getString(c.getColumnIndex(Customer._NAME));
            email = c.getString(c.getColumnIndex(Customer._EMAIL));
            vip_until = c.getString(c.getColumnIndex(Customer._VIP_DISCOUNT_YEAR));
            discount = c.getDouble(c.getColumnIndex(Customer._DISCOUNT));
            credit = c.getDouble(c.getColumnIndex(Customer._CREDIT));
            creditExpirationDate = c.getString(c.getColumnIndex(Customer._CREDIT_EXPIRATION_DATE));

            customer = new Customer(name, email);
            customer.setDiscount(discount);
            customer.setHexId(hexId);
            customer.setCredit(credit);

            if (vip_until != null && !vip_until.equalsIgnoreCase("")) {
                Date vipUntil_date = new Date(vip_until);
                customer.setVipDiscountYear(vipUntil_date);
            }
            if (creditExpirationDate != null && !creditExpirationDate.equalsIgnoreCase("")) {
                Date creditExpDate = new Date(creditExpirationDate);
                customer.setCreditExpiration(creditExpDate);
            }
        }
        else {
            //TODO: LOW: handle the case where no customers are found even though this is an impossible case since we scan ID cards to load customers?
            //^ will require extra testing with manual customer removal from DB
            //TODO: SUPER LOW: another impossible case is when query returns more than 1 row
            System.out.println("ERROR: TCCartDBHelper.loadCustomerFromDbByHexId(): something is totally wrong with the customer table: c.getCount()=" + c.getCount());
        }

        c.close();
        return customer;
    }

    //updates only name and email;
    public static boolean updateCustomerNameAndEmail(Customer customer, Context context) {
        TCCartDBHelper mDbHelper = new TCCartDBHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(Customer._NAME, customer.getName());
        values.put(Customer._EMAIL, customer.getEmail());

        // Which row to update, based on the ID
        String selection = Customer._ID + " = ?;";
        String[] selectionArgs = { TCCartHelper.hexIdToLongId(customer.getHexId()).toString() };

        int count = db.update(
                Customer._TABLE_NAME,
                values,
                selection,
                selectionArgs);

        if (count == 1) {
            return true;
        }
        else { //couldn't update, most likely user not found; shouldn't happen with normal flow
            return false;
        }
    }

    //updates only _VIP_DISCOUNT_YEAR, _DISCOUNT, _CREDIT, _CREDIT_EXPIRATION_DATE
    public static boolean updateCustomerCreditAndDiscountInfo(Customer customer, Context context) {
        TCCartDBHelper mDbHelper = new TCCartDBHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // New value for one column
        ContentValues values = new ContentValues();
        values.put(Customer._VIP_DISCOUNT_YEAR, TCCartHelper.isNull(customer.getVipDiscountYear()) ? null : customer.getVipDiscountYear().toString());
        values.put(Customer._DISCOUNT, customer.getDiscount());
        values.put(Customer._CREDIT, customer.getCredit());
        values.put(Customer._CREDIT_EXPIRATION_DATE, TCCartHelper.isNull(customer.getCreditExpiration()) ? null : customer.getCreditExpiration().toString());


        // Which row to update, based on the ID
        String selection = Customer._ID + " = ?;";
        String[] selectionArgs = { TCCartHelper.hexIdToLongId(customer.getHexId()).toString() };

        int count = db.update(
                Customer._TABLE_NAME,
                values,
                selection,
                selectionArgs);

        if (count == 1) {
            return true;
        }
        else { //couldn't update, most likely user not found; shouldn't happen with normal flow
            TCCartHelper.showMsg("ERROR updating customer credit/discount info: please contact the developers", context);
            return false;
        }
    }
}