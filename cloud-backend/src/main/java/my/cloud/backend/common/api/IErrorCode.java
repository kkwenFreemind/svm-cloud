package my.cloud.backend.common.api;

/**
 * 封装API的錯誤碼
 */
public interface IErrorCode {
    long getCode();

    String getMessage();
}
