package billing;

public class PurchaseReceipt {


    private String kind;
    private long startTimeMillis;
    private long expiryTimeMillis;
    private boolean autoRenewing;
    private String priceCurrencyCode;
    private long priceAmountMicros;
    private String countryCode;
    private String developerPayload;
    private String paymentState;
    private int cancelReason;
    private long userCancellationTimeMillis;
    private String orderId;
    private String linkedPurchaseToken;
    private int purchaseType;
    private String profileName;
    private String emailAddress;
    private String givenName;
    private String familyName;
    private String profileId;


    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public long getStartTimeMillis() {
        return startTimeMillis;
    }

    public void setStartTimeMillis(long startTimeMillis) {
        this.startTimeMillis = startTimeMillis;
    }

    public long getExpiryTimeMillis() {
        return expiryTimeMillis;
    }

    public void setExpiryTimeMillis(long expiryTimeMillis) {
        this.expiryTimeMillis = expiryTimeMillis;
    }

    public boolean isAutoRenewing() {
        return autoRenewing;
    }

    public void setAutoRenewing(boolean autoRenewing) {
        this.autoRenewing = autoRenewing;
    }

    public String getPriceCurrencyCode() {
        return priceCurrencyCode;
    }

    public void setPriceCurrencyCode(String priceCurrencyCode) {
        this.priceCurrencyCode = priceCurrencyCode;
    }

    public long getPriceAmountMicros() {
        return priceAmountMicros;
    }

    public void setPriceAmountMicros(long priceAmountMicros) {
        this.priceAmountMicros = priceAmountMicros;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getDeveloperPayload() {
        return developerPayload;
    }

    public void setDeveloperPayload(String developerPayload) {
        this.developerPayload = developerPayload;
    }

    public String getPaymentState() {
        return paymentState;
    }

    public void setPaymentState(String paymentState) {
        this.paymentState = paymentState;
    }

    public int getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(int cancelReason) {
        this.cancelReason = cancelReason;
    }

    public long getUserCancellationTimeMillis() {
        return userCancellationTimeMillis;
    }

    public void setUserCancellationTimeMillis(long userCancellationTimeMillis) {
        this.userCancellationTimeMillis = userCancellationTimeMillis;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getLinkedPurchaseToken() {
        return linkedPurchaseToken;
    }

    public void setLinkedPurchaseToken(String linkedPurchaseToken) {
        this.linkedPurchaseToken = linkedPurchaseToken;
    }

    public int getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(int purchaseType) {
        this.purchaseType = purchaseType;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }


}
