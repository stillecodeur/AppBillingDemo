package billing;

import android.content.Context;

import com.android.billingclient.api.Purchase;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public abstract class PurchaseLoader {

    protected Context context;
    protected Purchase purchase;

    private GoogleSignInOptions gso;


    public PurchaseLoader(Purchase purchase, Context context) {
        this.context = context;
        this.purchase = purchase;
    }




    public abstract void generateAccessTokenWithGoogle(String authorizeCode, TokenGenerationCallback tokenGenerationCallback);

    public abstract void generateAccessTokenWithRefreshToken(String refreshToken, TokenGenerationCallback tokenGenerationCallback);

    public abstract void loadPurchaseDetails(String accessToken, LoadPurchaseCallback callback);
}
