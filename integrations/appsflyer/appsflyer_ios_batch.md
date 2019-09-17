## Integrate Appsflyer with the Batch.com SDK on iOS

[Full documentation on attribution callbacks](https://support.appsflyer.com/hc/en-us/articles/207032096-Accessing-AppsFlyer-Attribution-Conversion-Data-from-the-SDK-iOS-Deferred-Deeplinking-).

```objc
-(void)onConversionDataReceived:(NSDictionary*) installData {
    // initiate Batch user editor to set new attributes
    BatchUserDataEditor *editor = [BatchUser editor];
    
    [editor setAttribute:[installData objectForKey:@"media_source"] forKey:@"appsflyer_source"];
    [editor setAttribute:[installData objectForKey:@"campaign"] forKey:@"appsflyer_campaign"];

    // send new attributes to Batch servers
    [editor save];
}
```