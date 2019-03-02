package billing;

public interface IPurchaseValidator {

    public interface ValidationCallback {
        void onSuccess(PurchaseReceipt purchaseReceipt);

        void onFailure(String msg);
    }


    void validatePurchase(String authorizeCode, ValidationCallback validationCallback);
}
