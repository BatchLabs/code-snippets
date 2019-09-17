//
//  Copyright © 2019 Batch.com. All rights reserved.
//

import Foundation
import Batch
import FirebaseAnalytics

class FirebaseBatchIntegration {
    static func setup() {
        NotificationCenter.default.addObserver(FirebaseBatchIntegration.self,
                                               selector: #selector(batchPushReceived(notification:)),
                                               name: NSNotification.Name.BatchPushReceived,
                                               object: nil)
    }
    
    fileprivate static func batchPushReceived(notification: NSNotification) {
        DispatchQueue.main.async {
            if let payload = notification.userInfo,
                UIApplication.shared.applicationState == .active {
                self.trackFromBatchPush(payload: payload)
            }
        }
    }
    
    fileprivate static func trackFromBatchPush(payload: [AnyHashable: Any]) {
        // If the custom payload has a "utm_source" entry, it takes precedence
        if let source = payload["utm_source"] as? String {
            self.track(withSource: source)
        } else if let rawDeeplink = BatchPush.deeplink(fromUserInfo: payload) {
            // Use NSURL to parse the link rather than URLComponents directly
            // as NSURL is more tolerant about the URL format
            if let deeplink = URL(string: rawDeeplink),
                let components = URLComponents(url: deeplink, resolvingAgainstBaseURL: false) {
                
                // First, check if we have a xtor in the query (?utm_source=, &utm_source=)
                if let source = components.queryItems?.first(where: {$0.name == "utm_source"})?.value {
                    self.track(withSource: source)
                } else if let fragment = components.fragment {
                    let wantedPrefix = "utm_source="
                    if fragment.hasPrefix(wantedPrefix) {
                        let startIndex = fragment.index(fragment.startIndex, offsetBy: wantedPrefix.count)
                        self.track(withSource: String(fragment[startIndex...]))
                    }
                }
            }
        }
    }
    
    fileprivate static func track(withSource source:String){
        Analytics.logEvent("batch_notification_open",
                           parameters: [AnalyticsParameterSource: source])
    }
}