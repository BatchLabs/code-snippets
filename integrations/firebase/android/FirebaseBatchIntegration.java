import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.batch.android.Batch;
import com.batch.android.BatchPushPayload;
import com.google.firebase.analytics.FirebaseAnalytics;


class FirebaseBatchIntegration {
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    static void handleIntent(Context appContext, Intent intent) {
        context = appContext;
        Bundle payload = intent.getBundleExtra(Batch.Push.PAYLOAD_KEY);

        if (payload != null) {
            String source = payload.getString("utm_source");
            if (source != null) {
                track(source);
            } else {
                try {
                    BatchPushPayload batchPayload = BatchPushPayload.payloadFromReceiverExtras(payload);
                    String deeplink = batchPayload.getDeeplink();
                    handleDeeplink(deeplink);
                } catch (BatchPushPayload.ParsingException e) {
                    // Safely ignore it
                }
            }
        }
    }

    private static void handleDeeplink(String rawDeeplink) {
        Uri deeplink = Uri.parse(rawDeeplink);

        String source = deeplink.getQueryParameter("utm_source");
        if (!TextUtils.isEmpty(source)) {
            track(source);
        } else {
            String fragment = deeplink.getFragment();
            if (fragment != null && fragment.length() > 11 && fragment.startsWith("utm_source=")) {
                track(fragment.substring(11));
            }
        }
    }

    private static void track(String source) {
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.SOURCE, source);
        FirebaseAnalytics.getInstance(context).logEvent("batch_notification_open", params);
    }
}