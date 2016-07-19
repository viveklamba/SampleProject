/**
 * Paypal Button and Instant Payment Notification (IPN) Integration with Java
 * http://codeoftheday.blogspot.com/2013/07/paypal-button-and-instant-payment_6.html
 */
package com.redpine;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Replace this service with database
 *
 * @author ThirupathiReddy V
 *
 */
public class IpnInfoService implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8007026127522317084L;

    IpnInfo ipnInfo;

    Map<String, IpnInfo> map = new HashMap<String, IpnInfo>();

    /**
     * Store Paypal IPN Notification related information for future use
     *
     * @param ipnInfo
     *            {@link IpnInfo}
     * @throws IpnException
     */
    public void log(final IpnInfo ipnInfo) throws IpnException {
        /**
         * Implementation...
         */
        map.put(ipnInfo.getTxnId(), ipnInfo);
        this.ipnInfo = ipnInfo;
    }

    /**
     * Fetch Paypal IPN Notification related information saved earlier
     *
     * @param txnId
     *            Paypal IPN Notification's Transaction ID
     * @return {@link IpnInfo}
     * @throws IpnException
     */
    public IpnInfo getIpnInfo(final String txnId) throws IpnException {
        /**
         * Implementation...
         */
        return map.get(txnId);
    }

}
