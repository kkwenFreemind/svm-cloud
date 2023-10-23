package my.cloud.notify.utils;

import lombok.extern.slf4j.Slf4j;
import my.cloud.notify.modules.nms.service.NmsNotificationLogService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

/**
 * @author : kevin Chang
 * @since : 2022/2/23
 */
@Slf4j
public class TeamPlusUtil {


    public boolean send2Channels(String Url){

        boolean result = false;
        log.info("teamPlus API Url:"+Url);
        RestTemplate restTemplate = new RestTemplate(new HttpsClientRequestFactory());
        String message = restTemplate.getForObject(Url, String.class);

        log.info("teamPlus return ==>" + message);
        JSONObject jsonObject = new JSONObject(message);

        //{"BatchID":"c20ca6ef-1c6a-4f2f-b6d4-6b47ca013c98","ErrorCode":0,"IsSuccess":true,"Description":"Success"}

        log.info("BatchID=====>" + jsonObject.getString("BatchID"));
        log.info("IsSuccess===>" + jsonObject.getBoolean("IsSuccess"));
        log.info("Description===>" + jsonObject.getString("Description"));

        if (jsonObject.getBoolean("IsSuccess")) {
            result = true;
        }

        return result;
    }
}
