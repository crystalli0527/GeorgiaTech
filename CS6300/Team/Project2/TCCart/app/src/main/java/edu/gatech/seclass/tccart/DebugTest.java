package edu.gatech.seclass.tccart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

public class DebugTest extends AppCompatActivity {

    private WebView wvDebug;

    String mime = "text/html";
    String encoding = "utf-8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug_test);

        wvDebug = (WebView)findViewById(R.id.wvDebug);
        //wvDebug.getSettings().setUseWideViewPort(true);
    }


    public void debugScreenOnClick(View view){
        switch (view.getId()){
            case R.id.btnDebugCleanDb:
                TCCartDBHelper.debugTestCleanDB(this);
                TCCartHelper.showMsg("DB is now fresh", this);
                String html = "";
                wvDebug.loadDataWithBaseURL(null, html, mime, encoding, null);
                break;

            case R.id.btnDebugCustomer:
                btnDebugCustomerAction();
                break;

            case R.id.btnDebugTransaction:
                btnDebugTransactionAction();
                break;

        }
    }

    private void btnDebugCustomerAction(){
        String html = TCCartDBHelper.selectStarFromCustomer(this);
        wvDebug.loadDataWithBaseURL(null, html, mime, encoding, null);
    }

    private void btnDebugTransactionAction(){
        String html = TCCartDBHelper.selectStarFromTransaction(this);
        wvDebug.loadDataWithBaseURL(null, html, mime, encoding, null);
    }
}
