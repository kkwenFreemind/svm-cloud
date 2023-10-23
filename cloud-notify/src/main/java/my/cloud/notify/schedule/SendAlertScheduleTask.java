package my.cloud.notify.schedule;


import lombok.extern.slf4j.Slf4j;
import my.cloud.notify.modules.nms.model.NmsNotificationLog;
import my.cloud.notify.modules.nms.model.SendMessageParam;
import my.cloud.notify.modules.nms.service.NmsNotificationLogService;
import my.cloud.notify.utils.TeamPlusUtil;
import my.cloud.notify.utils.XSMSUtil;
import my.cloud.notify.utils.properties.MailProperties;
import my.cloud.notify.utils.properties.TeamPlusProperties;
import my.cloud.notify.utils.properties.XSMSProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//import javax.mail.*;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.Date;
import java.util.Properties;

/**
 * @author : kevin Chang
 * @since : 2022/2/23
 */

@Slf4j
@Component
public class SendAlertScheduleTask {

    @Autowired
    TeamPlusProperties teamPlusProperties;

    @Autowired
    MailProperties mailProperties;

    @Autowired
    XSMSProperties xsmsProperties;

    @Autowired
    NmsNotificationLogService nmsNotificationLogService;

    public void Send2EmailTask(SendMessageParam sendMessageParam) {

//        SnsCategory snsType = SnsCategory.eMail;
//
//        String host = mailProperties.getHost();
//        Integer port = mailProperties.getPort();
//        String account = mailProperties.getUser_address();
//        String password = mailProperties.getUser_pwd();
//        String display_name = mailProperties.getUser_display_name();
//        String text_content = sendMessageParam.getMessage();
//        String send2Target = sendMessageParam.getTarget();
//
//        Properties properties = new Properties();
//        properties.put("mail.smtp.host", host);
//        properties.put("mail.smtp.port", port);
//        properties.put("mail.smtp.auth", String.valueOf(true));
//        properties.put("mail.smtp.starttls.enable", String.valueOf(true));
//
//        Session session = Session.getInstance(properties, new Authenticator() {
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(account, password);
//            }
//        });
//
//        try {
//
//            Message message = new MimeMessage(session);
//
//            String mineContent = "<html><body><h3>"+text_content+"</h3></body></html>";
//
//            message.setSubject("Notification Message");
//            message.setContent(mineContent, "text/html;charset=UTF-8");
//            message.setFrom(new InternetAddress(account, display_name));
//
//            message.addRecipient(Message.RecipientType.TO, new InternetAddress(send2Target));
//
//            NmsNotificationLog newLog = new NmsNotificationLog();
//            newLog.setStatus(0); //初始狀態：未發送
//            newLog.setContext(text_content);
//            newLog.setName(snsType.name());//teamPlus
//            newLog.setCategoryId(snsType.ordinal());
//            newLog.setSendTo(send2Target);
//            nmsNotificationLogService.create(newLog);
//
//            int sendResult = 2;//預設失敗
//            Transport.send(message);
//
//            nmsNotificationLogService.updateStatus(newLog.getId(), 1);
//
//        } catch (Exception ex) {
//            log.error(ex.getMessage());
//        }

    }

    //@Scheduled(initialDelay = 2000, fixedRate = 3000)
    public void Send2TeamPlusTask(SendMessageParam sendMessageParam) {

        SnsCategory snsType = SnsCategory.teamPlus;
        String url = teamPlusProperties.getUrl();
        String ask = teamPlusProperties.getAsk();
        String ch_sn = sendMessageParam.getTarget();
        String key = teamPlusProperties.getKey();
        Integer type = teamPlusProperties.getType();
        String text_content = sendMessageParam.getMessage(); //該由資料庫取得發送內容

        String teamPlus_url = url + ask + "&ch_sn=" + ch_sn + "&api_key=" +
                key + "&content_type=" + type + "&text_content=" + text_content;

        NmsNotificationLog newLog = new NmsNotificationLog();
        newLog.setStatus(0); //初始狀態：未發送
        newLog.setContext(text_content);
        newLog.setName(snsType.name());//teamPlus
        newLog.setCategoryId(snsType.ordinal());
        newLog.setSendTo("Channel:" + ch_sn);

        nmsNotificationLogService.create(newLog);

        TeamPlusUtil teamPlusUtil = new TeamPlusUtil();

        boolean result = false;
        result = teamPlusUtil.send2Channels(teamPlus_url);

        int sendResult = 2; //預設發送失敗
        if (result) {
            sendResult = 1; //發送成功
        }
        nmsNotificationLogService.updateStatus(newLog.getId(), sendResult);

    }

    //@Scheduled(initialDelay = 2000, fixedRate = 3000)
    public void Send2XSMSTask(SendMessageParam sendMessageParam) {

        SnsCategory snsType = SnsCategory.SMS;

        String send2Target = sendMessageParam.getTarget();
        String text_content = sendMessageParam.getMessage();

        String url = xsmsProperties.getUrl();
        String mdn = xsmsProperties.getMdn();
        String uid = xsmsProperties.getUid();
        String pwd = xsmsProperties.getPwd();
        String call = xsmsProperties.getCall();

        NmsNotificationLog newLog = new NmsNotificationLog();
        newLog.setStatus(0); //初始狀態：未發送
        newLog.setContext(text_content);
        newLog.setName(snsType.name());//SMS
        newLog.setCategoryId(snsType.ordinal());
        newLog.setSendTo(send2Target);
        newLog.setContext(text_content);

        nmsNotificationLogService.create(newLog);

        XSMSUtil xsmsUtil = new XSMSUtil();
        boolean result = xsmsUtil.send2XSMS(send2Target, url, mdn, uid, pwd, call, text_content);

        log.info("sms result: {}",result);

        int sendResult = 2; //預設發送失敗
        if (result) {
            sendResult = 1; //發送成功
        }
        nmsNotificationLogService.updateStatus(newLog.getId(), sendResult);
    }
}
