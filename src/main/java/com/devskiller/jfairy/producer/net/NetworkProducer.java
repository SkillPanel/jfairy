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
     * Generates a faked URL based on the current IP address.
     * <p>
     * The domain name is derived by mapping the digits of the IP address
     * to characters starting from 'a'.
     *
     * @param isHttps specifies whether to use the {@code https} or {@code http} protocol
     * @return a formatted URL string, e.g., "https://abcdefghij.com"
     */
    public String url(boolean isHttps) {
        String mergedIp = ipAddress().replace(".", "");
        StringBuilder domainBuilder = new StringBuilder(mergedIp.length());

        for (int i = 0; i < mergedIp.length(); i++) {
            char digitChar = mergedIp.charAt(i);
            // Character.digit is safer and more predictable than getNumericValue
            int numericValue = Character.digit(digitChar, 10);

            if (numericValue != -1) {
                domainBuilder.append((char) (numericValue + 'a'));
            }
        }

        String protocol = "http";
        if (isHttps) {
            protocol += "s";
        }
        return protocol + "://" + domainBuilder + ".com";
    }
}
