package my.cloud.backend.common.exception;


import my.cloud.backend.common.api.IErrorCode;

/**
 * 断言處理類，用於抛出各種API異常
 */
public class Asserts {
    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(IErrorCode errorCode) {
        throw new ApiException(errorCode);
    }
}
