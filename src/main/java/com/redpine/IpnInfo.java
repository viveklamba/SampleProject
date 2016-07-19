/**
 * Paypal Button and Instant Payment Notification (IPN) Integration with Java
 * http://codeoftheday.blogspot.com/2013/07/paypal-button-and-instant-payment_6.html
 */
package com.redpine;

import java.io.Serializable;

/**
 * Model class to hold Paypal IPN Notification related information
 *
 * User: smhumayun Date: 7/6/13 Time: 6:15 PM
 */
public class IpnInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3679932197001196631L;
    private String itemName;
    private String itemNumber;
    private String paymentStatus;
    private String paymentAmount;
    private String paymentCurrency;
    private String txnId;
    private String receiverEmail;
    private String payerEmail;
    private String response;
    private String requestParams;
    private String error;
    private Long logTime;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    public Long getLogTime() {
        return logTime;
    }

    public void setLogTime(Long logTime) {
        this.logTime = logTime;
    }

    public String getPayerEmail() {
        return payerEmail;
    }

    public void setPayerEmail(String payerEmail) {
        this.payerEmail = payerEmail;
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

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "IpnInfo [itemName=" + itemName + ", itemNumber=" + itemNumber + ", paymentStatus=" + paymentStatus + ", paymentAmount=" + paymentAmount
                + ", paymentCurrency=" + paymentCurrency + ", txnId=" + txnId + ", receiverEmail=" + receiverEmail + ", payerEmail=" + payerEmail
                + ", response=" + response + ", requestParams=" + requestParams + ", error=" + error + ", logTime=" + logTime + "]";
    }

}