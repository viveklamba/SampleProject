/**
 * Paypal Button and Instant Payment Notification (IPN) Integration with Java
 * http://codeoftheday.blogspot.com/2013/07/paypal-button-and-instant-payment_6.html
 */
package com.redpine;

/**
 * This class serves as a model to hold Paypal IPN Integration Configuration
 *
 * User: smhumayun Date: 7/6/13 Time: 6:06 PM
 */
public class IpnConfig {

    private String ipnUrl = "https://www.paypal.com/cgi-bin/webscr";
    private String receiverEmail = "seller@paypalsandbox.com";
    private String paymentAmount = "12.34";
    private String paymentCurrency = "USD";

    public IpnConfig() {
    }

    public IpnConfig(String ipnUrl, String receiverEmail, String paymentAmount, String paymentCurrency) {
        this.ipnUrl = ipnUrl;
        this.receiverEmail = receiverEmail;
        this.paymentAmount = paymentAmount;
        this.paymentCurrency = paymentCurrency;
    }

    public String getIpnUrl() {
        return ipnUrl;
    }

    public void setIpnUrl(String ipnUrl) {
        this.ipnUrl = ipnUrl;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getPaymentCurrency() {
        return paymentCurrency;
    }

    public void setPaymentCurrency(String paymentCurrency) {
        this.paymentCurrency = paymentCurrency;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

}
