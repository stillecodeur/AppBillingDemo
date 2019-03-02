package com.anirudh.subscriptiondemo;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.androidpublisher.AndroidPublisher;
import com.google.api.services.androidpublisher.AndroidPublisherScopes;
import com.google.api.services.androidpublisher.model.ProductPurchase;
import com.google.api.services.androidpublisher.model.SubscriptionPurchase;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import billing.IPurchaseValidator;
import billing.IabBroadcastReceiver;
import billing.IabHelper;
import billing.PreferencesUtils;
import billing.PurchaseLoader;
import billing.PurchaseOperations;
import billing.PurchaseReceipt;

public class MainActivity extends AppCompatActivity implements PurchasesUpdatedListener {

    private Context mContext;
    private TextView tvItem1, tvItem2, tvItem3, tvItem4;
    private Button btnContinue;
    private Button btnRestore;


    private IabBroadcastReceiver broadcastReceiver;
    private IabHelper mHelper;
    private static final String TAG = "Main";
    private String mSelectedSubscriptionPeriod = "";
    private List<String> oldSkus = null;
    static final int RC_REQUEST = 10001;
    private BillingClient mBillingClient;
    private SkuDetails skuDetails;
    private final String SUB_ITEM = "sub_demo_3";
    private IPurchaseValidator purchaseValidator;
    private final int PURCHASE_ADD = 0, PURCHASE_UPDATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mContext = MainActivity.this;
        tvItem1 = (TextView) findViewById(R.id.tv_item_1);
        tvItem2 = (TextView) findViewById(R.id.tv_item_2);
        tvItem3 = (TextView) findViewById(R.id.tv_item_3);
        tvItem4 = (TextView) findViewById(R.id.tv_item_4);
        btnContinue = (Button) findViewById(R.id.btn_continue);
        btnRestore = (Button) findViewById(R.id.btn_restore);


        mProgressDialog = new ProgressDialog(mContext, R.style.ProgressDialogTheme);
        mProgressDialog.getWindow().setGravity(Gravity.CENTER);
        mProgressDialog.setCancelable(false);

        String base64EncodedKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgjqCGHY1zD6EyNpeYrwN1pPl2ZjLV/U48hxTJzh2ufYEIYGmTrMB8yTkiMK2PRZ0yLAwgXt2PUvdHLl4r10zcGVZ00Ud//VaS8alsBGnqTwdLD0UpP0k2UX+mK0LWW/zV1cs4ODc1o5oHWO48kXzHFv3CV8rTE7ukA8eTt6TtTLBrfD4LOVoqEX4IxgxH2MJ+wjgKYdzqbyn0Us3usQh4BiWlYkO20vI6TzmEOrx8MiCMOF6KMUtUAHRdvsBlXl0nQc5CS+yJATV4+xo4MMlBorxAwcr0S8XJJawCrB2bfSdJecYNRODYWsflrBV33xWioGuhf/uZrJKHLC3EL5K/QIDAQAB";

        if (base64EncodedKey.contains("CONSTRUCT_YOUR")) {
            throw new RuntimeException("Please put your app's public key in MainActivity.java. See README.");
        }


        if (getPackageName().startsWith("com.example")) {
            throw new RuntimeException("Please change the sample's package name! See README.");
        }

        mHelper = new IabHelper(mContext, base64EncodedKey);


        Log.d(TAG, "Setup finished.");


//


        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setType(BillingClient.SkuType.SUBS).setSku(skuDetails.getSku()).build();
                mBillingClient.launchBillingFlow(MainActivity.this, billingFlowParams);


            }
        });


        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restorePurchase();
            }
        });


        mBillingClient = BillingClient.newBuilder(MainActivity.this).setListener(this).build();
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    // The billing client is ready. You can query purchases here.
                    Log.d(TAG, "onBillingSetupFinished: ");

                    loadProds();
//                    queryHistory();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });


    }


    private void restorePurchase() {
        mProgressDialog.show();
        mBillingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.SUBS, new PurchaseHistoryResponseListener() {
            @Override
            public void onPurchaseHistoryResponse(int responseCode, List<Purchase> purchasesList) {
                if (responseCode == BillingClient.BillingResponse.OK) {
                    mProgressDialog.dismiss();
                    Purchase purchase = null;

                    for (Purchase purchase1 : purchasesList) {
                        if (purchase1.getSku().equals(SUB_ITEM)) {
                            purchase = purchase1;
                            break;
                        }
                    }


                    validatePurchase(purchase);
                } else {
                    mProgressDialog.dismiss();
                    Toast.makeText(mContext, "You have not subscribed", Toast.LENGTH_SHORT).show();
                    PreferencesUtils.setSubscribed(mContext, false);
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        Task<GoogleSignInAccount> task = null;
        GoogleSignInAccount account = null;

        switch (requestCode) {
            case PURCHASE_ADD:
                task = GoogleSignIn.getSignedInAccountFromIntent(data);
                account = null;
                try {
                    account = task.getResult(ApiException.class);
                    if (account != null) {
                        validatePurchaseAdd(account.getServerAuthCode(), selectedPurchase);
                    }
                } catch (ApiException e) {

                    Toast.makeText(mContext, "SignInFailed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case PURCHASE_UPDATE:
                task = GoogleSignIn.getSignedInAccountFromIntent(data);
                account = null;
                try {
                    account = task.getResult(ApiException.class);
                    if (account != null) {
                        validatePurchaseUpdate(account.getServerAuthCode(), selectedPurchase);
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "SignInFailed" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;

        }


    }

    private Purchase selectedPurchase;

    private void loadProds() {

        oldSkus = new ArrayList<>();
        oldSkus.add(SUB_ITEM);
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(oldSkus).setType(BillingClient.SkuType.SUBS);
        mBillingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
                        Log.d(TAG, "onSkuDetailsResponse: ");
                        if (skuDetailsList != null && skuDetailsList.size() > 0) {
                            tvItem1.setText(skuDetailsList.get(0).getTitle());
                            skuDetails = skuDetailsList.get(0);
                        }
                    }
                });
    }

    private ProgressDialog mProgressDialog;
    private GoogleSignInOptions gso;


    private void validatePurchase(final Purchase purchase) {
//        Scope scope = new Scope("https://www.googleapis.com/auth/androidpublisher");
//        String serverClientId = getString(R.string.client_id);
//        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.api_key))
////                .requestServerAuthCode(serverClientId)
//                .requestScopes(scope)
//                .requestEmail()
//                .build();



        HttpTransport httpTransport = null;

        httpTransport = new ApacheHttpTransport();

        JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        String applicationName = "Susbscription Demo";
        String packageName = "com.anirudh.subscriptiondemo";

        File file = convertP12ToFile();


        final Set<String> scopes = Collections.singleton(AndroidPublisherScopes.ANDROIDPUBLISHER);
        GoogleCredential credential = null;
        try {
            credential = new GoogleCredential.Builder()
                    .setTransport(httpTransport)
                    .setJsonFactory(jsonFactory)
                    .setServiceAccountId("subscriptiondemoservice@subscriptiondemo.iam.gserviceaccount.com").setServiceAccountScopes(scopes)
                    .setServiceAccountPrivateKeyFromP12File(file).build();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        AndroidPublisher pub = new AndroidPublisher.Builder
                (httpTransport, jsonFactory, credential)
                .setApplicationName(applicationName)
                .build();
        AndroidPublisher.Purchases.Subscriptions.Get get = null;
        try {
            get = pub.purchases()
                    .subscriptions().get(packageName, SUB_ITEM, purchase.getPurchaseToken());


            final AndroidPublisher.Purchases.Subscriptions.Get get1 = get;

            new AsyncTask<Void, Void, SubscriptionPurchase>() {


                @Override
                protected void onPreExecute() {
                    mProgressDialog.show();
                }

                @Override
                protected SubscriptionPurchase doInBackground(Void... voids) {
                    try {
                        return get1.execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(SubscriptionPurchase productPurchase) {
                    mProgressDialog.dismiss();
                    Log.d(TAG, "onPostExecute: ");
                    Toast.makeText(mContext, "" + productPurchase.getExpiryTimeMillis(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, LaunchActivity.class);
                    PurchaseAni purchaseAni=new PurchaseAni();
                    purchaseAni.setName(productPurchase.getOrderId());
                    purchaseAni.setToken(productPurchase.getLinkedPurchaseToken());
                    purchaseAni.setExpiry(productPurchase.getExpiryTimeMillis());
                    intent.putExtra("p", purchaseAni);
                    startActivity(intent);
                }
            }.execute();


        } catch (IOException e) {
            e.printStackTrace();
        }


//
//        HttpTransport httpTransport = new NetHttpTransport();
//        JsonFactory jsonFactory = new JacksonFactory();
//
//
//        GoogleCredential googleCredential;
//        try {
//            googleCredential = GoogleCredential.fromStream(new FileInputStream("dsh"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        GoogleApiClient googleApiClient=new GoogleApiClient.Builder(mContext).addApi(Auth.GOOGLE_SIGN_IN_API,gso).addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
//            @Override
//            public void onConnected(@Nullable Bundle bundle) {
//                Log.d(TAG, "onConnected: ");
//            }
//
//            @Override
//            public void onConnectionSuspended(int i) {
//                Log.d(TAG, "onConnectionSuspended: ");
//            }
//        }).build();
//
//
//        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
//        startActivityForResult(intent, 9001);
//
//        googleApiClient.connect();

//        final GoogleSignInClient signInClient = GoogleSignIn.getClient(mContext, gso);
//        Task<GoogleSignInAccount> task = signInClient.silentSignIn();
//
//        task.addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
//            @Override
//            public void onSuccess(GoogleSignInAccount googleSignInAccount) {
//                String serverAuthCode = googleSignInAccount.getServerAuthCode();
//                validatePurchaseUpdate(serverAuthCode, purchase);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                selectedPurchase = purchase;
//                Intent intent = signInClient.getSignInIntent();
//                startActivityForResult(intent, PURCHASE_UPDATE);
//
//            }
//        });
    }


    private void addPurchase(final Purchase purchase) {
        String serverClientId = getString(R.string.android_client_id);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(serverClientId)
                .requestScopes(new Scope("https://www.googleapis.com/auth/androidpublisher"))
                .requestEmail()
                .build();


        final GoogleSignInClient signInClient = GoogleSignIn.getClient(mContext, gso);
        Task<GoogleSignInAccount> task = signInClient.silentSignIn();

        task.addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
            @Override
            public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                String serverAuthCode = googleSignInAccount.getServerAuthCode();
                validatePurchaseAdd(serverAuthCode, purchase);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                selectedPurchase = purchase;
                Intent intent = signInClient.getSignInIntent();
                startActivityForResult(intent, PURCHASE_ADD);

            }
        });
    }


    private void validatePurchaseAdd(String authCode, final Purchase purchase) {
        mProgressDialog.show();
        IPurchaseValidator purchaseValidator = new PurchaseOperations(mContext, purchase, purchase.getSku(), purchase.getPurchaseToken(), mContext.getPackageName());
        purchaseValidator.validatePurchase(authCode, new IPurchaseValidator.ValidationCallback() {
            @Override
            public void onSuccess(PurchaseReceipt purchaseReceipt) {
                mProgressDialog.dismiss();
                if (purchaseReceipt.getExpiryTimeMillis() < System.currentTimeMillis()) {
                    Toast.makeText(mContext, "Subscription expires", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Subscription bought", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void validatePurchaseUpdate(String authCode, final Purchase purchase) {
        mProgressDialog.show();
        IPurchaseValidator purchaseValidator = new PurchaseOperations(mContext, purchase, purchase.getSku(), purchase.getPurchaseToken(), mContext.getPackageName());
        purchaseValidator.validatePurchase(authCode, new IPurchaseValidator.ValidationCallback() {
            @Override
            public void onSuccess(PurchaseReceipt purchaseReceipt) {
                mProgressDialog.dismiss();
                if (purchaseReceipt.getExpiryTimeMillis() < System.currentTimeMillis()) {
                    Toast.makeText(mContext, "Subscription expires", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Subscription restored", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(String msg) {
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

            }
        });
    }


    private File convertP12ToFile() {

        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("subscriptiondemo-e11db8d3411d.p12");
        } catch (IOException e) {
            e.printStackTrace();
        }
        File file = null;
        try {
            file = new File(getCacheDir(), "subscriptiondemo-e11db8d3411d.p12");
            OutputStream output = new FileOutputStream(file);
            try {
                byte[] buffer = new byte[4 * 1024]; // or other buffer size
                int read;

                while ((read = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }

                output.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                output.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
        if (responseCode == BillingClient.BillingResponse.OK) {
            Purchase purchase = purchases.get(0);
            validatePurchase(purchase);
//            addPurchase(purchase);
        } else if (responseCode == BillingClient.BillingResponse.ITEM_ALREADY_OWNED) {
            Toast.makeText(mContext, "Your subscription is active.Please restore it.", Toast.LENGTH_SHORT).show();
        } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {

        }
    }


}



