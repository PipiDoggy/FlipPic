package com.chethan.flippic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.joooonho.SelectableRoundedImageView;

import butterknife.InjectView;

/**
 * Created by chethan on 28/02/15.
 */
public class CoffeeActivity extends MyActivity implements BillingProcessor.IBillingHandler {

    BillingProcessor bp;

    @InjectView(R.id.coffee_title)
    TextView coffeeTitle;

    @InjectView(R.id.SingleCoffee)
    SelectableRoundedImageView singleCoffee;

    @InjectView(R.id.TwoCoffee)
    SelectableRoundedImageView twoCoffee;

    @InjectView(R.id.view)
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_coffee);
        super.onCreate(savedInstanceState);
        coffeeTitle.setTypeface(Utils.getLightTypeface(getApplicationContext()));
        text.setTypeface(Utils.getLatoRegularTypeface(getApplicationContext()));

        bp = new BillingProcessor(this, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApwhS1ebvm5CDVGrLVuE7cUmMvDmDNddHeGzaHDZb8ugUQJccWSjBZ8zlgjTZso23x6lu2X+tyJMJr16a1U1GvnAtPndv3WC94ICyu40DOaI95d17dFPKVe97/mhIYT/eziZwyJmWCqhpM59c1YjGEJfFjrOGymZj52lm+6382MTQeWi6xh2NlEys+FRzVYznEQHJSNlZN8Al6SY2a2vxHmPfdHzTKD38vBYfVfN3ceU6ynsz7bc0/yfI8Ono+MINqgWGrEzrpM31MAbwXwNO5/lQ23obuw1DYvHInzhcjxbq9LPVWS3510ELvdwA0CL7y9xpdVMhsodOGdHOYQXszwIDAQAB", this);
        bp.loadOwnedPurchasesFromGoogle();

        singleCoffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bp.purchase(CoffeeActivity.this,"single_coffee");
            }
        });

        twoCoffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bp.purchase(CoffeeActivity.this,"two_coffee");
            }
        });

    }

    @Override
    public void onProductPurchased(String s, TransactionDetails transactionDetails) {

    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int i, Throwable throwable) {

    }

    @Override
    public void onBillingInitialized() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        if (bp != null)
            bp.release();

        super.onDestroy();
    }

}
