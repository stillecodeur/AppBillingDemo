package billing;

public interface TokenGenerationCallback {
    void onSuccess(String token);
    void onFailure(String msg);
}
