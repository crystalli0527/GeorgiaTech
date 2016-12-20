package edu.gatech.seclass.tccart;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import edu.gatech.seclass.services.QRCodeService;

public class ViewPurchaseRewardHistory extends Activity {

    private WebView wvHistory;
    private TextView txtName;

    String mime = "text/html";
    String encoding = "utf-8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_purchase_reward_history);

        wvHistory = (WebView)findViewById(R.id.wvTst);

        txtName = (TextView)findViewById(R.id.txtName);
    }


    public void onPurchRewHistHandleClick(View view) {
        switch (view.getId()) {
            case R.id.btnPurchRewHistScanQr:
                btnEnterPurchaseScanQRAction();
                break;

            case R.id.btnPurchRewHistClear:
                btnPurchRewHistClearAction();
                break;
        }
    }

    private void btnEnterPurchaseScanQRAction() {
        String hexId = QRCodeService.scanQRCode().toUpperCase();
        if (!hexId.equalsIgnoreCase("ERR")) {

            Customer scannedCustomer = TCCartDBHelper.loadCustomerFromDbByHexId(hexId, this);

            if (scannedCustomer != null) { //checking for null anyway: see to-dos in db helper for more info
                txtName.setText("Customer: " + scannedCustomer.getName());
                String html = TCCartDBHelper.loadHtmlTransactionHistoryFromDbByHexId(hexId, this);
                wvHistory.loadDataWithBaseURL(null, html, mime, encoding, null);
            }
            else {
                TCCartHelper.showMsg("ERROR: customer for ID " + hexId + " doesn't exist! IS IT A COUNTERFEIT ID CARD?! :)", this);
                btnPurchRewHistClearAction();
            }
        }
        else {
            btnPurchRewHistClearAction();
            TCCartHelper.showMsg("Error scanning ID card, please rescan", this);
        }
    }


    private void btnPurchRewHistClearAction(){
        txtName.setText("Customer:");
        String html = "";
        wvHistory.loadDataWithBaseURL(null, html, mime, encoding, null);
    }
}
