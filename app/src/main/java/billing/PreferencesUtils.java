package billing;

import android.content.Context;
import android.content.SharedPreferences;


public abstract class PreferencesUtils {

    public static void setSwatchShape(Context context, String shape) {
        SharedPreferences sharedPreferences = context.
                getSharedPreferences(ConstantUtils.ZCC_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(ConstantUtils.SWATCH_SHAPE, shape).commit();
    }

    public static String getSwatchShape(Context context) {
        SharedPreferences sharedPreferences = context.
                getSharedPreferences(ConstantUtils.ZCC_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ConstantUtils.SWATCH_SHAPE, ConstantUtils.SQUARE);
    }

    public static void setReferenceLocation(Context context, String pos) {
        SharedPreferences sharedPreferences = context.
                getSharedPreferences(ConstantUtils.ZCC_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(ConstantUtils.REFERENCE_LOCATION, pos).commit();
    }

    public static String getReferenceLocation(Context context) {
        SharedPreferences sharedPreferences = context.
                getSharedPreferences(ConstantUtils.ZCC_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ConstantUtils.REFERENCE_LOCATION, ConstantUtils.TL);
    }

    public static void setLanguage(Context context, String language) {
        SharedPreferences sharedPreferences = context.
                getSharedPreferences(ConstantUtils.ZCC_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(ConstantUtils.LANGUAGE, language).commit();
    }

    public static String getLanguage(Context context) {
        SharedPreferences sharedPreferences = context.
                getSharedPreferences(ConstantUtils.ZCC_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ConstantUtils.LANGUAGE, "English");
    }

    public static void setPurchaseJson(Context context, String language) {
        SharedPreferences sharedPreferences = context.
                getSharedPreferences(ConstantUtils.ZCC_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(ConstantUtils.PURCHASE_JSON, language).commit();
    }

    public static String getPurchaseJson(Context context) {
        SharedPreferences sharedPreferences = context.
                getSharedPreferences(ConstantUtils.ZCC_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ConstantUtils.PURCHASE_JSON, "");
    }


    public static void setSubscribed(Context context, boolean value) {
        SharedPreferences sharedPreferences = context.
                getSharedPreferences(ConstantUtils.ZCC_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(ConstantUtils.SUBSCRIBED, value).commit();
    }

    public static boolean getSubscribed(Context context) {
        SharedPreferences sharedPreferences = context.
                getSharedPreferences(ConstantUtils.ZCC_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(ConstantUtils.SUBSCRIBED, false);
    }


    public static void saveRefreshToken(Context context, String value) {
        SharedPreferences sharedPreferences = context.
                getSharedPreferences(ConstantUtils.ZCC_PREFERENCES, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(ConstantUtils.SAVED_REFRESH_TOKEN, value).commit();
    }

    public static String getRefreshToken(Context context) {
        SharedPreferences sharedPreferences = context.
                getSharedPreferences(ConstantUtils.ZCC_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(ConstantUtils.SAVED_REFRESH_TOKEN, "");
    }
}
