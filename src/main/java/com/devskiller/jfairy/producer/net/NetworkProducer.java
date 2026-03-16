package com.devskiller.jfairy.producer.net;

/**
 * TODO: Add emails
 */
public class NetworkProducer {

	private final IPNumberProducer ipNumberProducer;

	public NetworkProducer(IPNumberProducer ipNumberProducer) {
		this.ipNumberProducer = ipNumberProducer;
	}

	public String ipAddress() {
		return ipNumberProducer.generate();
	}

	/**
	 * Add a simple url generator
	 * Example: networkProducer.url(baseProducer.trueOrFalse())
	 *
	 * @param isHttps is https or not
	 * @return A faked url.
	 */
	public String url(boolean isHttps) {
		String mergedIP = ipAddress().replaceAll("\\.", "");
		char[] domainChars = mergedIP.toCharArray();
		for (int i = 0; i < domainChars.length; i++) {
			domainChars[i] = (char) (Character.getNumericValue(domainChars[i]) + 'a');
		}

		String domain = String.valueOf(domainChars);
		String protocol = isHttps ? "https" : "http";
		return protocol + "://" + domain + ".com";
	}
}
