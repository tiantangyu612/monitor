package monitor.core.collector.base.transformer;

import java.security.ProtectionDomain;
import java.util.Set;

/**
 * Created by bjlizhitao on 2018/1/11.
 * 明确指定的需要增强的类 Transformer
 */
public interface SpecifiedClassTransformer extends BytecodeTransformer {
    /**
     * 获取指定的类的类名
     *
     * @return
     */
    Set<String> getClassNames();

    /**
     * 字节码增强监控
     *
     * @param loader              the defining loader of the class to be transformed,
     *                            may be <code>null</code> if the bootstrap loader
     * @param className           the name of the class in the internal form of fully
     *                            qualified class and interface names as defined in
     *                            <i>The Java Virtual Machine Specification</i>.
     *                            For example, <code>"java/util/List"</code>.
     * @param classBeingRedefined if this is triggered by a redefine or retransform,
     *                            the class being redefined or retransformed;
     *                            if this is a class load, <code>null</code>
     * @param protectionDomain    the protection domain of the class being defined or redefined
     * @param classfileBuffer     the input byte buffer in class file format - must not be modified
     * @return
     * @throws Exception
     */
    byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                     ProtectionDomain protectionDomain, byte[] classfileBuffer) throws Exception;
}
