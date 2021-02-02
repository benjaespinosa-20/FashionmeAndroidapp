package mx.app.fashionme.ui.app.config;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import mx.app.fashionme.ui.utils.Constants;
import timber.log.Timber;

/**
 * BillingAgentConfig
 * Configuración para la facturación
 *
 * @author heriberto martinez elizarraraz
 * @since 21-08-2020
 */
public class BillingAgentConfig implements PurchasesUpdatedListener, BillingClientStateListener,
        SkuDetailsResponseListener, ConsumeResponseListener {

    /**
     * Instancia de activity
     */
    private Application mApp;
    /**
     * Instancia inicializada de BillingClient
     */
    private BillingClient mBillingClient;

    public List<SkuDetails> getmSubscriptionsList() {
        return mSubscriptionsList;
    }

    /**
     * Lista de skus con detalle de subscripciones activas en google play
     */
    private List<SkuDetails> mSubscriptionsList = new ArrayList<>();
    /**
     * Callback de respuesta para el agente de facturación
     */
    private BillingCallback mCallback;
    /**
     * Lista de skus para suscripciones
     */
    private static final List<String> SUBS_SKUS =
            Arrays.asList(Constants.ONE_MONTH_PREMIUM_SKU,
                    Constants.THREE_MONTHS_PREMIUM_SKU);
    /**
     * Lista de skus para productos
     */
    private static final List<String> INAPP_SKUS =
            Arrays.asList(Constants.THREE_MINUTES_CHAT_SKU,
                    Constants.FIVE_MINUTES_CHAT_SKU,
                    Constants.TEN_MINUTES_CHAT_SKU);

    /**
     * Constructor inyectado
     * @param app Instancia de aplication
     */
    @Inject
    public BillingAgentConfig(Application app) {
        this.mApp = app;
    }

    /**
     * Metodo de inicio para establecer configuración de conexión con Google Play
     */
    public void initConfig(BillingCallback callback) {
        Timber.d("Init Billing Agent Config");
        mBillingClient = BillingClient.newBuilder(mApp)
                .setListener(this)
                .enablePendingPurchases() // No se usa para las suscripciones
                .build();
        if (!mBillingClient.isReady()) {
            Timber.d("BillingClient: Start connection...");
            mBillingClient.startConnection(this);
        }
        if (mCallback == null) {
            this.mCallback = callback;
        }
    }

    /**
     * Metodo callback que se ejecuta una vez se terminó la configuración del cliente
     * @param billingResult resultado
     */
    @Override
    public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
        int responseCode = billingResult.getResponseCode();
        String debugMessageResponse = billingResult.getDebugMessage();
        Timber.d("onBillingSetupFinished CODE=" + responseCode + " MESSAGE=" + debugMessageResponse);
        if (responseCode == BillingClient.BillingResponseCode.OK) {
            // El cliente de facturación está listo. Puedes consultar compras aquí.
            querySkuDetails(BillingClient.SkuType.SUBS, SUBS_SKUS);
            querySkuDetails(BillingClient.SkuType.INAPP, INAPP_SKUS);
            queryPurchases(BillingClient.SkuType.INAPP);
            queryPurchases(BillingClient.SkuType.SUBS);
        }
    }

    /**
     * Método callback que se ejecuta si el servicio se desconecta
     */
    @Override
    public void onBillingServiceDisconnected() {
        Timber.d("onBillingServiceDisconnected");
        // TODO: Try connecting again with exponential backoff.
    }

    /**
     * Metodo para obtener las suscripciones disponibles desde la consola de google play
     * Para realizar compras, necesita SkuDetails para el artículo o la suscripción.
     * Esta es una llamada asincrónica que recibirá un resultado en onSkuDetailsResponse
     */
    public void querySkuDetails(String skuType, List<String> skus) {
        Timber.d("querySkuDetails");
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(skus).setType(skuType);

        Timber.i("querySkuDetailsAsync");
        mBillingClient.querySkuDetailsAsync(params.build(), this);
    }

    /**
     * Query Google Play Billing for existing purchases.
     * <p>
     * New purchases will be provided to the PurchasesUpdatedListener.
     * You still need to check the Google Play Billing API to know when purchase tokens are removed.
     */
    public List<Purchase> queryPurchases(String type) {
        List<Purchase> purchases = new ArrayList<>();
        if (!mBillingClient.isReady()) {
            Timber.e( "queryPurchases: BillingClient is not ready");
            return purchases;
        }
        Timber.d("queryPurchases: SUBS");
        Purchase.PurchasesResult result = mBillingClient.queryPurchases(type);
        if (result == null) {
            Timber.i("queryPurchases: null purchase result");
            processPurchases(null);
            return purchases;
        } else {
            if (result.getPurchasesList() == null) {
                Timber.i("queryPurchases: null purchase list");
                processPurchases(null);
                return purchases;
            } else {
                processPurchases(result.getPurchasesList());
                return result.getPurchasesList();
            }
        }
    }

    /**
     * Send purchase SingleLiveEvent and update purchases LiveData.
     * The SingleLiveEvent will trigger network call to verify the subscriptions on the sever.
     * The LiveData will allow Google Play settings UI to update based on the latest purchase data.
     * @param purchaseList lista de compras
     */
    private void processPurchases(List<Purchase> purchaseList) {
        /*
        if (purchaseList != null) {
            Timber.d("processPurchases: " + purchaseList.size() + " purchase(s)");
        } else {
            Timber.d("processPurchases: with no purchases");
        }
        if (isUnchangedPurchaseList(purchaseList)) {
            Timber.d("processPurchases: Purchase list has not changed");
            return;
        }
        if (purchaseList != null) {
            logAcknowledgementStatus(purchaseList);
        }

         */
    }

    /**
     * Método que recibe el resultado de querySkuDetails()
     *
     * Almacena los SkuDetails y los publíca en skusWithSkuDetails. Esto permite que
     * otras partes de la aplicación utilicen SkuDetails para mostrar información de SKU y realizar compras.
     * @param billingResult resultado del servicio de facturación
     * @param skuDetailsList lista de sku con detalles
     */
    @Override
    public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
        if (billingResult == null) {
            Timber.wtf("onSkuDetailsResponse: null BillingResult");
            return;
        }
        int responseCode = billingResult.getResponseCode();
        String debugMessage = billingResult.getDebugMessage();
        Timber.d("onPurchasesUpdated: CODE=" + responseCode + " MESSAGE=" + debugMessage);
        switch (responseCode) {
            case BillingClient.BillingResponseCode.OK:
                Timber.i("onSkuDetailsResponse: CODE=" + responseCode + " MESSAGE=" + debugMessage);
                if (skuDetailsList == null) {
                    Timber.w("onSkuDetailsResponse: null SkuDetails list");
                } else {
                    mSubscriptionsList.addAll(skuDetailsList);
                    Timber.i("onSkuDetailsResponse: count %s", mSubscriptionsList.size());
                }
                break;
            case BillingClient.BillingResponseCode.SERVICE_DISCONNECTED:
            case BillingClient.BillingResponseCode.SERVICE_UNAVAILABLE:
            case BillingClient.BillingResponseCode.BILLING_UNAVAILABLE:
            case BillingClient.BillingResponseCode.ITEM_UNAVAILABLE:
            case BillingClient.BillingResponseCode.DEVELOPER_ERROR:
            case BillingClient.BillingResponseCode.ERROR:
                Timber.e("onSkuDetailsResponse: " + responseCode + " " + debugMessage);
                break;
            case BillingClient.BillingResponseCode.USER_CANCELED:
                Timber.i("onSkuDetailsResponse: " + responseCode + " " + debugMessage);
                break;
            // These response codes are not expected.
            case BillingClient.BillingResponseCode.FEATURE_NOT_SUPPORTED:
            case BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED:
            case BillingClient.BillingResponseCode.ITEM_NOT_OWNED:
            default:
                Timber.wtf("onSkuDetailsResponse: " + responseCode + " " + debugMessage);
        }

    }

    /**
     * Metodo para actualizacion de compras.
     * Lo llama la biblioteca de facturación cuando se detectan nuevas compras.
     *
     * @param billingResult billing result instance
     * @param purchases list of purchase objects
     */
    @Override
    public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
        if (billingResult == null) {
            Timber.wtf("onPurchasesUpdated: null BillingResult");
            return;
        }
        int responseCode = billingResult.getResponseCode();
        String debugMessage = billingResult.getDebugMessage();
        Timber.d("onPurchasesUpdated: " + responseCode + " " + debugMessage);
        switch (responseCode) {
            case BillingClient.BillingResponseCode.OK:
                Timber.i("onPurchasesUpdated BillingResponseCode OK");
                if (purchases == null) {
                    Timber.d("onPurchasesUpdated: null purchase list");
                    processPurchases(null);
                } else {
                    processPurchases(purchases);
                }
                Timber.i("onPurchasesUpdated callback");
                mCallback.onTokenConsumed(purchases);
                break;
            case BillingClient.BillingResponseCode.USER_CANCELED:
                Timber.i("onPurchasesUpdated: User canceled the purchase");
                break;
            case BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED:
                Timber.i("onPurchasesUpdated BillingResponseCode ITEM ALREADY OWNED");
                Timber.i("onPurchasesUpdated: The user already owns this item");
                mCallback.onTokenConsumed(purchases);
                Timber.i("onPurchasesUpdated callback");
                break;
            case BillingClient.BillingResponseCode.DEVELOPER_ERROR:
                Timber.e("onPurchasesUpdated: Developer error means that Google Play " +
                        "does not recognize the configuration. If you are just getting started, " +
                        "make sure you have configured the application correctly in the " +
                        "Google Play Console. The SKU product ID must match and the APK you " +
                        "are using must be signed with release keys."
                );
                break;
        }
    }

    @Override
    public void onConsumeResponse(@NonNull BillingResult billingResult, @NonNull String s) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            // Handle the success of the consume operation.
        }
    }

    /**
     * Registre el número de compras que se reconocen y no se reconocen.
     *
     * Cuando se reciba la compra por primera vez, no se reconocerá.
     * La aplicación envía el token de compra al servidor para su registro.
     * Una vez que el token de compra se registra en una cuenta, la aplicación reconoce el token de compra.
     * La próxima vez que se actualice la lista de compras, contendrá las compras reconocidas.
     *
     * Ref: https://developer.android.com/google/play/billing/billing_library_releases_notes#2_0_acknowledge
     */
    private void logAcknowledgementStatus(List<Purchase> purchaseList) {
        int ack_yes = 0;
        int ack_no = 0;
        for (Purchase purchase : purchaseList) {
            if (purchase.isAcknowledged()) {
                ack_yes++;
            } else {
                ack_no++;
            }
        }
        Timber.d("logAcknowledgementStatus: acknowledged= " + ack_yes + " unacknowledged= " + ack_no);
    }

    /**
     * Metodo que comprueba si las compras han cambiado antes de publicar cambios.
     */
    private boolean isUnchangedPurchaseList(List<Purchase> purchaseList) {
        // TODO: Optimize to avoid updates with identical data.
        return false;
    }

    /**
     * Metodo para lanzar el flujo de facturación
     * @param activity Instancia de la actividad de donde es lanzada
     * @return codigo de respuesta
     */
    public int launchBillingFlow(Activity activity, SkuDetails skuDetail) {
        if (skuDetail != null) {
            BillingFlowParams params = BillingFlowParams.newBuilder()
                    .setSkuDetails(skuDetail)
                    .build();
            String sku = params.getSku();
            String oldSku = params.getOldSku();
            Timber.i("launchBillingFlow: sku: " + sku + ", oldSku: " + oldSku);
            if (!mBillingClient.isReady()) {
                Timber.e("launchBillingFlow: BillingClient is not ready");
            }
            BillingResult billingResult = mBillingClient.launchBillingFlow(activity, params);
            int responseCode = billingResult.getResponseCode();
            String debugMessage = billingResult.getDebugMessage();
            Timber.d("launchBillingFlow: BillingResponse " + responseCode + " " + debugMessage);
            return responseCode;
        } else {
            return -100;
        }
    }

    public void handlePurchases(List<Purchase> purchases) {
        if (purchases != null) {
            Purchase purchase = purchases.get(0);
            if (purchase.getSku().equals(Constants.FIVE_MINUTES_CHAT_SKU) ||
            purchase.getSku().equals(Constants.TEN_MINUTES_CHAT_SKU) || purchase.getSku().equals(Constants.THREE_MINUTES_CHAT_SKU)) {
                ConsumeParams consumeParams =
                        ConsumeParams.newBuilder()
                                .setPurchaseToken(purchase.getPurchaseToken())
                                .build();
                mBillingClient.consumeAsync(consumeParams, this);
            }
        }
    }


    /**
     * Metodo a ejecutar on destroy en actividad que cierra la conexión con el cliente
     */
    public void onDestroy() {
        Timber.d("on Destroy --- End connection billing agent config");
        if (mBillingClient != null && mBillingClient.isReady()) {
            Timber.d("BillingClient can only be used once -- closing connection");
            mBillingClient.endConnection();
            mSubscriptionsList.clear();
        }
    }

    /**
     * BillingCallback
     */
    public interface BillingCallback {

        /**
         * Method for on token consumed
         */
        void onTokenConsumed(List<Purchase> purchases);

    }
}
