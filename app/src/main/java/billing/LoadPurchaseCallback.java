package billing;

public interface LoadPurchaseCallback {
    void onLoad(PurchaseReceipt purchaseReceipt);
    void onFailure(String mess);
}
