package billing;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface SilentSignInCallback {
    void onSuccess(GoogleSignInAccount googleSignInAccount);
    void onFailure();
}
