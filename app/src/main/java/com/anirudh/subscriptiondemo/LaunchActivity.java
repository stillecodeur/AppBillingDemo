package com.anirudh.subscriptiondemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.api.services.androidpublisher.model.SubscriptionPurchase;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);


        TextView tvName = (TextView) findViewById(R.id.tv_name);
        TextView tvToken = (TextView) findViewById(R.id.tv_token);
        TextView tvExpiry = (TextView) findViewById(R.id.tv_expiry);


        PurchaseAni purchaseAni = (PurchaseAni) getIntent().getExtras().get("p");

        tvName.setText(purchaseAni.getName());
        tvToken.setText(purchaseAni.getToken());
        tvExpiry.setText(String.valueOf(purchaseAni.getExpiry()));

    }
}
