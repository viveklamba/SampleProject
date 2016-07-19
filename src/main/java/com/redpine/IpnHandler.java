/**
 * Paypal Button and Instant Payment Notification (IPN) Integration with Java
 * http://codeoftheday.blogspot.com/2013/07/paypal-button-and-instant-payment_6.html
 */
package com.redpine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Map;
//import java.util.logging.Level;
//import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class IpnHandler {

    private static final Logger LOGGER = Logger.getLogger(IpnHandler.class);

    private final IpnConfig ipnConfig = new IpnConfig();

    private final IpnInfoService ipnInfoService = new IpnInfoService();

    /**
     * <pre>
     * This method handles the Paypal IPN Notification as follows:
     *      1. Read all posted request parameters
     *      2. Prepare 'notify-validate' command with exactly the same parameters
     *      3. Post above command to Paypal IPN URL {@link IpnConfig#ipnUrl}
     *      4. Read response from Paypal
     *      5. Capture Paypal IPN information
     *      6. Validate captured Paypal IPN Information
     *          6.1. Check that paymentStatus=Completed
     *          6.2. Check that txnId has not been previously processed
     *          6.3. Check that receiverEmail matches with configured {@link IpnConfig#receiverEmail}
     *          6.4. Check that paymentAmount matches with configured {@link IpnConfig#paymentAmount}
     *          6.5. Check that paymentCurrency matches with configured {@link IpnConfig#paymentCurrency}
     *      7. In case of any failed validation checks, throw {@link IpnException}
     *      8. If all is well, return {@link IpnInfo} to the caller for further business logic execution
     * </pre>
     *
     * @param request
     *            {@link HttpServletRequest}
     * @return {@link IpnInfo}
     * @throws IpnException
     */
    public IpnInfo handleIpn(HttpServletRequest request) throws IpnException {
        LOGGER.info("Inside handleIpn");
        final IpnInfo ipnInfo = new IpnInfo();
        try {

            // 1. Read all posted request parameters
            final String requestParams = getAllRequestParams(request);
            LOGGER.info("Request Params : " + requestParams);

            // 2. Prepare 'notify-validate' command with exactly the same parameters
            final Enumeration<String> en = request.getParameterNames();
            final StringBuilder cmd = new StringBuilder("cmd=_notify-validate");

            String paramName;
            String paramValue;

            while (en.hasMoreElements()) {
                paramName = en.nextElement();
                paramValue = request.getParameter(paramName);
                cmd.append("&").append(paramName).append("=").append(URLEncoder.encode(paramValue, "UTF-8"));
               // cmd.append("&").append(paramName).append("=").append(URLEncoder.encode(paramValue));
                System.out.println(paramName + " : "+paramValue);
            }

            // 3. Post above command to Paypal IPN URL {@link IpnConfig#ipnUrl}
            final URL u = new URL(getIpnConfig().getIpnUrl());

            LOGGER.info("Connecting to IpnUrl : " + getIpnConfig().getIpnUrl());

            final HttpsURLConnection uc = (HttpsURLConnection) u.openConnection();
            uc.setDoOutput(true);
            uc.setRequestMethod("POST");
            uc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            uc.setRequestProperty("Host", "https://www.paypal.com/cgi-bin/webscr");
            final PrintWriter pw = new PrintWriter(uc.getOutputStream());
            pw.println(cmd.toString());
            pw.close();

            LOGGER.info("Command " + cmd);
            
            // 4. Read response from Paypal
            final BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));

            final StringBuffer payPalResponseBuilder = new StringBuffer();

            String line = null;

            while ((line = in.readLine()) != null) {
                payPalResponseBuilder.append(line);
            }
            final String payPalResponse = payPalResponseBuilder.toString();

            LOGGER.info("Response from paypal " + payPalResponse);
            in.close();

            // 5. Capture Paypal IPN information
            ipnInfo.setLogTime(System.currentTimeMillis());
            ipnInfo.setItemName(request.getParameter("item_name"));
            ipnInfo.setItemNumber(request.getParameter("item_number"));
            ipnInfo.setPaymentStatus(request.getParameter("payment_status"));
            ipnInfo.setPaymentAmount(request.getParameter("mc_gross"));
            ipnInfo.setPaymentCurrency(request.getParameter("mc_currency"));
            ipnInfo.setTxnId(request.getParameter("txn_id"));
            ipnInfo.setReceiverEmail(request.getParameter("receiver_email"));
            ipnInfo.setPayerEmail(request.getParameter("payer_email"));
            ipnInfo.setResponse(payPalResponse);
            ipnInfo.setRequestParams(requestParams);

           // LOGGER.info("IpnInfo from Paypal" + ipnInfo);

            // 6. Validate captured Paypal IPN Information
            if (payPalResponse.equalsIgnoreCase("VERIFIED")) {

                // 6.1. Check that paymentStatus=Completed
                if (ipnInfo.getPaymentStatus() == null || !ipnInfo.getPaymentStatus().equalsIgnoreCase("COMPLETED")) {
                    ipnInfo.setError("payment_status IS NOT COMPLETED {" + ipnInfo.getPaymentStatus() + "}");
                }

                // 6.2. Check that txnId has not been previously processed
                final IpnInfo oldIpnInfo = getIpnInfoService().getIpnInfo(ipnInfo.getTxnId());
                if (oldIpnInfo != null) {
                    ipnInfo.setError("txn_id is already processed {old ipn_info " + oldIpnInfo);
                }

                // 6.3. Check that receiverEmail matches with configured {@link IpnConfig#receiverEmail}
                if (!ipnInfo.getReceiverEmail().equalsIgnoreCase(getIpnConfig().getReceiverEmail())) {
                    ipnInfo.setError(
                            "receiver_email " + ipnInfo.getReceiverEmail() + " does not match with configured ipn email " + getIpnConfig().getReceiverEmail());
                }

                // 6.4. Check that paymentAmount matches with configured {@link IpnConfig#paymentAmount}
                if (Double.parseDouble(ipnInfo.getPaymentAmount()) != Double.parseDouble(getIpnConfig().getPaymentAmount())) {
                    ipnInfo.setError("payment amount mc_gross " + ipnInfo.getPaymentAmount() + " does not match with configured ipn amount "
                            + getIpnConfig().getPaymentAmount());
                }

                // 6.5. Check that paymentCurrency matches with configured {@link IpnConfig#paymentCurrency}
                if (!ipnInfo.getPaymentCurrency().equalsIgnoreCase(getIpnConfig().getPaymentCurrency())) {
                    ipnInfo.setError("payment currency mc_currency " + ipnInfo.getPaymentCurrency() + " does not match with configured ipn currency "
                            + getIpnConfig().getPaymentCurrency());
                }
            } else {
                ipnInfo.setError("Inavlid response {" + payPalResponse + "} expecting {VERIFIED}");
            }

           // LOGGER.info("ipnInfo = " + ipnInfo);

            getIpnInfoService().log(ipnInfo);

            // 7. In case of any failed validation checks, throw {@link IpnException}
            if (ipnInfo.getError() != null) {
                throw new IpnException(ipnInfo.getError());
            }
        } catch (final Exception e) {

            LOGGER.error("error", e);
            throw new IpnException(e);
        }

        // 8. If all is well, return {@link IpnInfo} to the caller for further business logic execution
        return ipnInfo;
    }

    /**
     * Utility method to extract all request parameters and their values from request object
     *
     * @param request
     *            {@link HttpServletRequest}
     * @return all request parameters in the form: param-name 1 param-value param-name 2 param-value param-value (in case of multiple values)
     */
    private String getAllRequestParams(HttpServletRequest request) {
        final Map<String, String[]> map = request.getParameterMap();
        final StringBuilder sb = new StringBuilder("\nREQUEST PARAMETERS\n");

        for (final String string : map.keySet()) {
            final String pn = string;
            sb.append(pn).append("\n");
            final String[] pvs = map.get(pn);
            for (final String pv : pvs) {
                sb.append("\t").append(pv).append("\n");
            }
        }
      //  LOGGER.info("Request Params : " + sb.toString());
        return sb.toString();
    }

    public IpnConfig getIpnConfig() {
        return ipnConfig;
    }

    public IpnInfoService getIpnInfoService() {
        return ipnInfoService;
    }

}
