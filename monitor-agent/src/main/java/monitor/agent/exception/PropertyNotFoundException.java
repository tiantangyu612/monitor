package monitor.agent.exception;

/**
 * Created by lizhitao on 2018/1/11.
 * 属性不存在异常
 */
public class PropertyNotFoundException extends RuntimeException {
    public PropertyNotFoundException() {
        super();
    }

    public PropertyNotFoundException(String message) {
        super(message);
    }

    public PropertyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PropertyNotFoundException(Throwable cause) {
        super(cause);
    }

    protected PropertyNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
