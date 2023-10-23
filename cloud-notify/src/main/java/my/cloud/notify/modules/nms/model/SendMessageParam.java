package my.cloud.notify.modules.nms.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : kevin Chang
 * @since : 2022/3/2
 */

public class SendMessageParam {

    private Integer snsType;
    private String message;
    private String target;

    public SendMessageParam(){}

    public SendMessageParam(int snsType,String message,String target){
        this.snsType = snsType;
        this.message = message;
        this.target = target;
    }

//    @Override
//    public String toString() {
//        return "SendMessageParam{" +
//                "snsType='" + snsType + '\'' +
//                ",message=" + message +
//                '}';
//    }

    public Integer getSnsType() {
        return snsType;
    }

    public void setSnsType(Integer snsType) {
        this.snsType = snsType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
