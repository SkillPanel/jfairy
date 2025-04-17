package com.devskiller.jfairy.producer.net;

import javax.inject.Inject;

/**
 * TODO: Add emails
 */
public class NetworkProducer {

	private final IPNumberProducer ipNumberProducer;

	@Inject
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
		String[] domainChars = mergedIP.split("");

		for (int i = 0; i < domainChars.length; i++) {
			int c = Integer.parseInt(domainChars[i]);
			domainChars[i] = ((char) (c + 97)) + "";
		}

		String domain = String.join("", domainChars);
		if (isHttps) {
			return "https://" + domain + ".com";
		} else {
			return "http://" + domain + ".com";
		}
	}
}
