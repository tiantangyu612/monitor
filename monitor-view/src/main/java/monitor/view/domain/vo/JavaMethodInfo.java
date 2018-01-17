package monitor.view.domain.vo;

/**
 * Created by lizhitao on 2018/1/14.
 * 需要展示的 java method 信息
 */
public class JavaMethodInfo {
    private String time;
    private String clazz;
    private Long errorCount;
    private Long invokedCount;
    private Long invokingCount;
    private Long maxConcurrency;
    private Long maxTime;
    private String method;
    private Long ms0_10;
    private Long ms10_100;
    private Long ms100_1000;
    private Long s1_10;
    private Long s10_n;
    private Long totalTime;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public Long getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Long errorCount) {
        this.errorCount = errorCount;
    }

    public Long getInvokedCount() {
        return invokedCount;
    }

    public void setInvokedCount(Long invokedCount) {
        this.invokedCount = invokedCount;
    }

    public Long getMaxConcurrency() {
        return maxConcurrency;
    }

    public Long getInvokingCount() {
        return invokingCount;
    }

    public void setInvokingCount(Long invokingCount) {
        this.invokingCount = invokingCount;
    }

    public void setMaxConcurrency(Long maxConcurrency) {
        this.maxConcurrency = maxConcurrency;
    }

    public Long getMaxTime() {
        return maxTime;
    }

    public void setMaxTime(Long maxTime) {
        this.maxTime = maxTime;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Long getMs0_10() {
        return ms0_10;
    }

    public void setMs0_10(Long ms0_10) {
        this.ms0_10 = ms0_10;
    }

    public Long getMs10_100() {
        return ms10_100;
    }

    public void setMs10_100(Long ms10_100) {
        this.ms10_100 = ms10_100;
    }

    public Long getMs100_1000() {
        return ms100_1000;
    }

    public void setMs100_1000(Long ms100_1000) {
        this.ms100_1000 = ms100_1000;
    }

    public Long getS1_10() {
        return s1_10;
    }

    public void setS1_10(Long s1_10) {
        this.s1_10 = s1_10;
    }

    public Long getS10_n() {
        return s10_n;
    }

    public void setS10_n(Long s10_n) {
        this.s10_n = s10_n;
    }

    public Long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Long totalTime) {
        this.totalTime = totalTime;
    }

    @Override
    public String toString() {
        return "JavaMethodInfo{" +
                "time='" + time + '\'' +
                ", clazz='" + clazz + '\'' +
                ", errorCount=" + errorCount +
                ", invokedCount=" + invokedCount +
                ", invokingCount=" + invokingCount +
                ", maxConcurrency=" + maxConcurrency +
                ", maxTime=" + maxTime +
                ", method='" + method + '\'' +
                ", ms0_10=" + ms0_10 +
                ", ms10_100=" + ms10_100 +
                ", ms100_1000=" + ms100_1000 +
                ", s1_10=" + s1_10 +
                ", s10_n=" + s10_n +
                ", totalTime=" + totalTime +
                '}';
    }
}
