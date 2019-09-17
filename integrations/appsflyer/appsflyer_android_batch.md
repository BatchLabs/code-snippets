## Integrate Appsflyer with the Batch.com SDK on Android

[Full documentation on attribution callbacks](https://support.appsflyer.com/hc/en-us/articles/207032176-Accessing-AppsFlyer-Attribution-Conversion-Data-from-the-SDK-Android-Deferred-Deeplinking-).

```java
AppsFlyerLib.getInstance().registerConversionListener(this, new AppsFlyerConversionListener() {

    @Override
    public void onInstallConversionDataLoaded(Map<String, String> conversionData) {
        // initiate Batch user editor to set new attributes
        BatchUserDataEditor editor = Batch.User.editor();

        editor.setAttribute("appsflyer_source", conversionData.get("media_source"));
        editor.setAttribute("appsflyer_campaign", conversionData.get("campaign"));

        // send new attributes to Batch servers
        editor.save();
    }

});
```