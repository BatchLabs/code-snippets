class AppDelegate: UIResponder, UIApplicationDelegate {

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
      // [...]
      // Insert this BEFORE Batch.start(withAPIKey:)
      ATInternetBatchIntegration.setup()
    }
}