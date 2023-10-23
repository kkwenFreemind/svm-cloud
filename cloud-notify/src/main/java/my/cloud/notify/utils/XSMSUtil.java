package my.cloud.notify.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author : kevin Chang
 * @since : 2022/2/25
 */
@Slf4j
public class XSMSUtil {

    public boolean send2XSMS(String BNO,String sms_url,String sms_mdn,String sms_uid,String sms_pwd,String sms_call,String message){
        boolean result =false;

        String[] cArray = BNO.split(",");
        String xsms_mdn = "0906180640";
        String xsms_call ="0906180640";
        String reqUrl = sms_url + "?MDN=" + xsms_mdn + "&UID=" + sms_uid + "&UPASS=" + sms_pwd;
        log.info("sms url: {}"+reqUrl);

        String requestBody = "<Request><Subject>事件資訊:" + "</Subject><Retry>Y</Retry>" +
                "<AutoSplit>Y</AutoSplit><Callback>" + xsms_call + "</Callback>" +
                "<Message>" + message + "</Message>" + "<MDNList>";

        for (String xmdn : cArray) {
            requestBody = requestBody + "<MSISDN>" + xmdn + "</MSISDN>";
        }

        requestBody = requestBody + "</MDNList></Request>";

        log.info("XML==>" + requestBody);


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);

        reqUrl = reqUrl + "&Content=" + requestBody;

        HttpEntity<String> request = new HttpEntity<String>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(reqUrl, request, String.class);

        log.info("===>" + response.getStatusCode());
        log.info("===>" + response.getStatusCodeValue());

        String[] xsms_result = response.toString().split(",");

        log.info("==>" + xsms_result[1]);
        String x_code = "";
        String x_reason = "";
        XMLUtil xmlUtil = new XMLUtil();
        Document doc = xmlUtil.convertStringToXMLDocument(xsms_result[1]);

        Node n = doc.getFirstChild();
        for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()) {
            log.info("==>" + d.getNodeName() + "," + d.getTextContent());
            if("Code".equalsIgnoreCase(d.getNodeName())){
                x_code = d.getTextContent();
            }
        }

        if ("0".equals(x_code)) {
            result = true;
        }
        return result;
    }

}
