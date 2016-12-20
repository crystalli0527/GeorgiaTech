package edu.gatech.seclass.tccart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainMenu extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }


    public void onHandleClick(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.btnViewHistory:
                intent = new Intent(this, ViewPurchaseRewardHistory.class);
                startActivity(intent);
                break;

            case R.id.btnEnterPurchase:
                intent = new Intent(this, EnterPurchase.class);
                startActivity(intent);
                break;

            case R.id.btnEditCustomer:
                intent = new Intent(this, EditCustomer.class);
                startActivity(intent);
                break;

            case R.id.txtMainMenu:
                intent = new Intent(this, DebugTest.class);
                startActivity(intent);
                break;
        }
    }
}
