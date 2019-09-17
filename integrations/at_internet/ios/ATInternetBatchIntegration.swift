//
//  Copyright © 2018 Batch.com. All rights reserved.
//

import Foundation
import Batch
import Tracker

class ATInternetBatchIntegration {
    
    static func setup() {
        NotificationCenter.default.addObserver(ATInternetBatchIntegration.self,
            selector: #selector(batchPushReceived(notification:)),
            name: NSNotification.Name.BatchPushReceived,
            object: nil)
    }
    
    @objc
    static func batchPushReceived(notification: NSNotification) {
        DispatchQueue.main.async {
            // Ignore remote fetch notifications (background refresh)
            if let payload = notification.userInfo,
                UIApplication.shared.applicationState == .active {
                self.trackFromBatchPush(payload: payload)
            }
        }
    }
    
    private static func trackFromBatchPush(payload: [AnyHashable: Any]) {        
        // If the custom payload has a "xtor" entry, it takes precedence
        if let xtor = payload["xtor"] as? String {
            track(xtor: xtor)
        } else if let rawDeeplink = BatchPush.deeplink(fromUserInfo: payload) {
            // Use NSURL to parse the link rather than URLComponents directly
            // as NSURL is more tolerant about the URL format
            if let deeplink = URL(string: rawDeeplink),
                let components = URLComponents(url: deeplink, resolvingAgainstBaseURL: false) {
                
                // First, check if we have a xtor in the query (?xtor=, &xtor=)
                if let xtor = components.queryItems?.first(where: {$0.name == "xtor"})?.value {
                    track(xtor: xtor)
                } else if let fragment = components.fragment {
                    let wantedPrefix = "xtor="
                    if fragment.hasPrefix(wantedPrefix) {
                        let startIndex = fragment.index(fragment.startIndex, offsetBy: wantedPrefix.count)
                        track(xtor: String(fragment[startIndex...]))
                    }
                }
            }
        }
    }

    private static func track(xtor: String) {
        let tracker = ATInternet.sharedInstance.defaultTracker
        _ = tracker.campaigns.add(campaignId: xtor)
        tracker.screens.add("OpenedBatchPushNotification").sendView()
    }
}