package com.example.myapplication;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.batch.android.Batch;
import com.batch.android.BatchNotificationInterceptor;
import com.batch.android.BatchPushPayload;

public class BranchNotificationInterceptor extends BatchNotificationInterceptor {
    @Nullable
    @Override
    public NotificationCompat.Builder getPushNotificationCompatBuilder(@NonNull Context context, @NonNull NotificationCompat.Builder defaultBuilder, @NonNull Bundle pushIntentExtras, int notificationId) {
        String deeplink = null;
        try {
            BatchPushPayload payload = BatchPushPayload.payloadFromReceiverExtras(pushIntentExtras);
            deeplink = payload.getDeeplink();
        } catch (BatchPushPayload.ParsingException e) {
            e.printStackTrace();
        }

        PendingIntent newIntent = null;

        if (!TextUtils.isEmpty(deeplink)) {
            newIntent = Batch.Push.makePendingIntentForDeeplink(context, deeplink, pushIntentExtras);
        } else {
            Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            if (launchIntent != null) {
                appendBranchData(launchIntent, pushIntentExtras);
                newIntent = Batch.Push.makePendingIntent(context, launchIntent, pushIntentExtras);
            }
        }

        if (newIntent != null) {
            defaultBuilder.setContentIntent(newIntent);
        }

        return defaultBuilder;
    }

    private void appendBranchData(Intent i, Bundle extras) {
        String branchExtra = extras.getString("branch");
        if (branchExtra != null) {
            i.putExtra("branch",  branchExtra);

            if ("true".equals(extras.getString("branch_force_new_session"))) {
                i.putExtra("branch_force_new_session", true);
            }
        }
    }
}
