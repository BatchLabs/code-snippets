package com.batchlabs.android.firebasebatchintegration;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

public class BaseActivity extends Activity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        FirebaseBatchIntegration.handleIntent(getApplicationContext(), getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        FirebaseBatchIntegration.handleIntent(getApplicationContext(), newIntent);
    }
}
