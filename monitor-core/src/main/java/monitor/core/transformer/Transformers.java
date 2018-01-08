package monitor.core.transformer;

import monitor.core.MonitorConfig;

import java.lang.instrument.Instrumentation;

/**
 * Created by lizhitao on 2018/1/6.
 * Transformers
 */
public class Transformers {
    /**
     * 初始化 Transformers
     *
     * @param instrumentation
     */
    public static void initTransformers(Instrumentation instrumentation) {
        if (MonitorConfig.isEnableJavaMethodCollect()) {
            instrumentation.addTransformer(new MethodTransformer());
        }
    }
}
