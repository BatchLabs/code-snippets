package com.batchlabs.android.atinernet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.batch.android.Batch;
import com.batch.android.BatchPushPayload;

import com.atinternet.tracker.ATInternet;
import com.atinternet.tracker.Tracker;

public class ATInternetBatchIntegration
{
    public static void handleIntent(Intent intent) {
        Bundle payload = intent.getBundleExtra(Batch.Push.PAYLOAD_KEY);

        if (payload != null) {
            String payloadXtor = payload.getString("xtor");
            if (payloadXtor != null) {
                trackOpen(payloadXtor);
            } else {
                try {
                    BatchPushPayload batchPayload = BatchPushPayload.payloadFromReceiverExtras(payload);
                    String deeplink = batchPayload.getDeeplink();
                    if (!TextUtils.isEmpty(deeplink)) {
                        handleDeeplink(deeplink);
                    }
                } catch (BatchPushPayload.ParsingException e) {
                    // Safely ignore it
                }
            }
            
            trackGenericOpen();
        }
    }

    private static void handleDeeplink(String rawDeeplink) {
        Uri deeplink = Uri.parse(rawDeeplink);

        String queryXtor = deeplink.getQueryParameter("xtor");
        if (!TextUtils.isEmpty(queryXtor)) {
            trackOpen(queryXtor);
        } else {
            String fragment = deeplink.getFragment();
            if (fragment != null && fragment.length() > 5 && fragment.startsWith("xtor=")) {
                trackOpen(fragment.substring(5));
            }
        }
    }

    private static void trackGenericOpen() {
        Tracker tracker = ATInternet.getInstance().getDefaultTracker();
        tracker.Screens().add("OpenedBatchPushNotification").sendView();
    }

    private static void trackOpen(String xtor) {
        Tracker tracker = ATInternet.getInstance().getDefaultTracker();
        tracker.Campaigns().add(xtor);
    }
}
