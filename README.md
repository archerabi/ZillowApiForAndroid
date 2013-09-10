ZillowApiForAndroid
===================

Usage
--------------

```sh
final ZillowApi api = new ZillowApi("apikey");
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				List<Metric> metrics = api.getDemographicInformation("NJ", "hoboken");
			}
		}).start();
```