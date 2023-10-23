package my.cloud.notify.schedule;

import lombok.extern.slf4j.Slf4j;
import my.cloud.notify.modules.nms.model.SendMessageParam;
import my.cloud.notify.modules.nms.service.NmsNotificationLogCategoryService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author : kevin Chang
 * @since : 2022/3/1
 */

@Component
@Slf4j
public class ReceiveMessageListener {

    /**
     * 監聽Queue中是否有資料，若有資料則進行消費。
     *
     * @param user
     */

    @Autowired
    SendAlertScheduleTask sendAlertScheduleTask;

    @Autowired
    NmsNotificationLogCategoryService nmsNotificationLogCategoryService;


    @RabbitListener(queues = {"tpu.queue"})
    public void receive(SendMessageParam sendMessageParam) {

        log.info("receive snsType: {} ", sendMessageParam.getSnsType());

        if(sendMessageParam.getSnsType()==1) {
            sendAlertScheduleTask.Send2TeamPlusTask(sendMessageParam);
        }
        if(sendMessageParam.getSnsType()==3){
            sendAlertScheduleTask.Send2EmailTask(sendMessageParam);
        }
        if(sendMessageParam.getSnsType()==2){
            sendAlertScheduleTask.Send2XSMSTask(sendMessageParam);
        }
    }
}
