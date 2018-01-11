package monitor.core.collector.base.transformer;

/**
 * Created by bjlizhitao on 2018/1/11.
 * 需要匹配的 ClassTransformer
 */
public interface MatchedClassTransformer extends BytecodeTransformer {
    /**
     * 获取匹配的类信息
     *
     * @param loader
     * @param className
     * @param classfileBuffer
     * @param classBeingRedefined
     * @return
     */
    MatchedClass getMatchedClass(ClassLoader loader, String className, byte[] classfileBuffer, Class<?> classBeingRedefined);

    /**
     * 字节码增强监控
     *
     * @param matchedClass 匹配的类信息
     * @return
     * @throws Exception
     */
    byte[] transform(MatchedClass matchedClass) throws Exception;
}
