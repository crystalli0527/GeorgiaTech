package edu.gatech.seclass.tccart;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import edu.gatech.seclass.services.QRCodeService;

public class EnterPurchase extends Activity {

    private EditText edEnterPurchaseAmount;
    private TextView txtEnterPurchaseName;

    private Double amountEntered = 0.0; //this is pre-discount amount
    Customer scannedCustomer = null; // if scannedCustomer != null, we can proceed with a transaction

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_purchase);

        edEnterPurchaseAmount = (EditText)findViewById(R.id.edEnterPurchaseAmount);
        txtEnterPurchaseName = (TextView)findViewById(R.id.txtEnterPurchaseName);
    }


    public void onEnterPurchaseButtonClick(View view) {
        switch (view.getId()) {
            case R.id.btnEnterPurchaseClear:
                btnEditPurchaseClearAction(true);
                break;

            case R.id.btnEnterPurchaseScanQR:
                btnEnterPurchaseScanQRAction();
                break;

            case R.id.btnEnterPurchaseScanProcess:
                btnEnterPurchaseScanProcessAction();
                break;
        }
    }


    private void btnEditPurchaseClearAction(boolean isClearEdAmount) {
        scannedCustomer = null;
        txtEnterPurchaseName.setText("Customer:");
        amountEntered = 0.0;
        if (isClearEdAmount) {
            edEnterPurchaseAmount.setText("");
        }
    }

    private void btnEnterPurchaseScanQRAction() {
        String hexId = QRCodeService.scanQRCode().toUpperCase();
        if (!hexId.equalsIgnoreCase("ERR")) {

            scannedCustomer = TCCartDBHelper.loadCustomerFromDbByHexId(hexId, this);

            if (scannedCustomer != null) { //checking for null anyway: see to-dos in db helper for more info
                txtEnterPurchaseName.setText("Customer: " + scannedCustomer.getName());
            }
            else {
                TCCartHelper.showMsg("ERROR: customer for ID " + hexId + " doesn't exist! IS IT A COUNTERFEIT ID CARD?! :)", this);
                btnEditPurchaseClearAction(false);
            }
        }
        else {
            btnEditPurchaseClearAction(false);
            TCCartHelper.showMsg("Error scanning ID card, please rescan", this);
        }

    }

    private void btnEnterPurchaseScanProcessAction() {
        if (scannedCustomer != null) { //proceed with transaction
            if (!edEnterPurchaseAmount.getText().toString().equalsIgnoreCase("")) {
                amountEntered = Double.parseDouble(edEnterPurchaseAmount.getText().toString());
                if (amountEntered > 0.0) {
                    Transaction transaction = TransactionManager.processTransaction(scannedCustomer, amountEntered, this);
                    //^ transaction will never be null

                    if (transaction.isCreditCardScanFailed()) {
                        TCCartHelper.showMsg("Credit card scan failed, please rescan", this);
                    } else if (transaction.isPaymentProcessingFailed()) {
                        TCCartHelper.showMsg("Payment processing failed, please rescan and resubmit", this);
                    } else if (transaction.getPostDiscountTotal() >= 0.0) {
                        //TCCartHelper.showMsg("Payment processed successfully: $" + transaction.getPostDiscountTotal() +
                        //        TCCartHelper.showDiscounts(transaction), this);
                        //^ moved to processTransaction so that VIP/Credit msgs appear after
                        btnEditPurchaseClearAction(true);
                    } else {
                        //shouldn't happen
                        TCCartHelper.showMsg("Unknown transaction error: contact developers :(", this);
                    }
                } else {
                    TCCartHelper.showMsg("Amount must be greater than 0", this);
                }
            }
            else {
                TCCartHelper.showMsg("Please enter amount", this);
            }
        }
        else {
            //TODO: LOW: add anonymous purchases?
            TCCartHelper.showMsg("Please scan the QR code to proceed", this);
        }

    }


}
