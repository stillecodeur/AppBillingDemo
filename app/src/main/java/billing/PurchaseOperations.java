package billing;

import android.content.Context;

import com.android.billingclient.api.Purchase;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anirudh.subscriptiondemo.R;
import com.google.gson.Gson;


import java.util.HashMap;
import java.util.Map;

public class PurchaseOperations extends PurchaseLoader implements IPurchaseValidator {

    private Context context;
    private RequestQueue requestQueue;
    private String clientId, clientSecret, packageName;
    private String productId, purchaseToken;

    public PurchaseOperations(Context context, Purchase purchase, String productId, String purchaseToken, String packageName) {
        super(purchase, context);
        this.context = context;
        this.purchase = purchase;
        this.productId = productId;
        this.purchaseToken = purchaseToken;
        requestQueue = Volley.newRequestQueue(context);
        this.packageName = packageName;
        clientId = context.getString(R.string.client_id);
        clientSecret = context.getString(R.string.client_secret);
    }


    @Override
    public void validatePurchase(String authorizeCode, final ValidationCallback validationCallback) {
        if (PreferencesUtils.getRefreshToken(context).equals("")) {
            generateAccessTokenWithGoogle(authorizeCode, new TokenGenerationCallback() {
                @Override
                public void onSuccess(String token) {
                    loadPurchaseDetails(token, new LoadPurchaseCallback() {
                        @Override
                        public void onLoad(PurchaseReceipt purchaseReceipt) {
                            validationCallback.onSuccess(purchaseReceipt);
                        }

                        @Override
                        public void onFailure(String msg) {
                            validationCallback.onFailure(msg);
                        }
                    });
                }

                @Override
                public void onFailure(String msg) {
                    validationCallback.onFailure(msg);
                }
            });
        } else {
            String refershToken = PreferencesUtils.getRefreshToken(context);
            generateAccessTokenWithRefreshToken(refershToken, new TokenGenerationCallback() {
                @Override
                public void onSuccess(String token) {
                    loadPurchaseDetails(token, new LoadPurchaseCallback() {
                        @Override
                        public void onLoad(PurchaseReceipt purchaseReceipt) {
                            validationCallback.onSuccess(purchaseReceipt);
                        }

                        @Override
                        public void onFailure(String msg) {
                            validationCallback.onFailure(msg);
                        }
                    });
                }

                @Override
                public void onFailure(String msg) {
                    validationCallback.onFailure(msg);
                }
            });
        }
    }


    @Override
    public void generateAccessTokenWithGoogle(final String authorizeCode, final TokenGenerationCallback tokenGenerationCallback) {
        String url = "https://accounts.google.com/o/oauth2/token";

        requestQueue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                GoogleTokenModel googleTokenModel = gson.fromJson(response, GoogleTokenModel.class);
                String token = googleTokenModel.getTokenType() + " " + googleTokenModel.getAccessToken();
                PreferencesUtils.saveRefreshToken(context, googleTokenModel.getRefreshToken());
                tokenGenerationCallback.onSuccess(token);

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tokenGenerationCallback.onFailure(error.getCause().getMessage());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("grant_type", ConstantUtils.AUTHORIZATION_CODE);
                params.put("client_id", clientId);
                params.put("client_secret", clientSecret);
                params.put("code", authorizeCode);
                return params;
            }
        };


        requestQueue.add(request);
    }

    @Override
    public void generateAccessTokenWithRefreshToken(final String refreshToken, final TokenGenerationCallback tokenGenerationCallback) {
        String url = "https://accounts.google.com/o/oauth2/token";

        StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                GoogleTokenModel googleTokenModel = gson.fromJson(response, GoogleTokenModel.class);
                String token = googleTokenModel.getTokenType() + " " + googleTokenModel.getAccessToken();
                tokenGenerationCallback.onSuccess(token);

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tokenGenerationCallback.onFailure(error.getCause().getMessage());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("grant_type", ConstantUtils.REFRESH_TOKEN);
                params.put("client_id", clientId);
                params.put("client_secret", clientSecret);
                params.put("refresh_token", refreshToken);
                return params;
            }
        };


        requestQueue.add(request);
    }

    @Override
    public void loadPurchaseDetails(final String accessToken, final LoadPurchaseCallback callback) {
        String url = "https://www.googleapis.com/androidpublisher/v3/applications/" + packageName + "/purchases/subscriptions/" + productId + "/tokens/" + purchaseToken;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                PurchaseReceipt purchaseReceipt = gson.fromJson(response, PurchaseReceipt.class);
                callback.onLoad(purchaseReceipt);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse.statusCode==401) {
                    callback.onFailure("Unauthorized");
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", accessToken);
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }
}
